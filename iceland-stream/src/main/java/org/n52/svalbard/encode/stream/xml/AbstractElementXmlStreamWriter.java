/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.svalbard.encode.stream.xml;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.util.XmlFactories;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.Actuate;
import org.n52.shetland.w3c.xlink.Link;
import org.n52.shetland.w3c.xlink.Show;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.stream.XLinkConstants;

import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.xml.XmlEscapers;

public abstract class AbstractElementXmlStreamWriter extends XmlFactories implements ElementXmlStreamWriter {

    private static final Escaper ESCAPER = XmlEscapers.xmlContentEscaper();

    private XmlStreamWritingContext context;

    @Override
    public void setContext(XmlStreamWritingContext context) {
        this.context = Objects.requireNonNull(context);
    }

    protected XmlStreamWritingContext context() {
        return this.context;
    }

    protected void attr(QName name,
            String value) throws XMLStreamException {
        context().dispatch(eventFactory().createAttribute(name, value));
    }

    protected void attr(QName name,
            Optional<String> value) throws XMLStreamException {
        if (value.isPresent()) {
            attr(name, value.get());
        }
    }

    protected void attr(String name,
            Optional<String> value) throws XMLStreamException {
        attr(new QName(name), value);
    }

    protected void attr(String name,
            String value) throws XMLStreamException {
        attr(new QName(name), value);
    }

    protected <T> void attr(QName name,
            Collection<? extends T> coll,
            Function<T, String> mapper) throws XMLStreamException {
        if (coll != null && !coll.isEmpty()) {
            attr(name, coll.stream().map(mapper).collect(joining(" ")));
        }
    }

    protected void attr(String namespace,
            String localName,
            String value) throws XMLStreamException {
        attr(new QName(namespace, localName), value);
    }

    protected <T> void attr(String name,
            Collection<? extends T> coll,
            Function<T, String> mapper) throws XMLStreamException {
        attr(new QName(name), coll, mapper);
    }

    protected void namespace(String prefix,
            String namespace) throws XMLStreamException {
        if (context().declareNamespace(prefix, namespace)) {
            context().dispatch(eventFactory().createNamespace(prefix, namespace));
        }
    }

    protected void start(String namespace,
            String localName) throws XMLStreamException {
        start(new QName(namespace, localName));
    }

    protected void start(String namespace,
            String localName,
            String prefix) throws XMLStreamException {
        start(new QName(namespace, localName, prefix));
    }

    protected void start(QName name) throws XMLStreamException {
        context().dispatch(eventFactory().createStartElement(name.getPrefix(), name.getNamespaceURI(), name
                .getLocalPart()));
    }

    protected void empty(QName name) throws XMLStreamException {
        start(name);
        end(name);
    }

    protected void chars(String chars) throws XMLStreamException {
        chars(chars, true);
    }

    protected void chars(String chars,
            boolean escape) throws XMLStreamException {
        context().dispatch(eventFactory().createCharacters(escape ? escaper().escape(chars) : chars));
    }

    protected void end(QName name) throws XMLStreamException {
        context().dispatch(eventFactory().createEndElement(name.getPrefix(), name.getNamespaceURI(), name
                .getLocalPart()));
    }

    protected void write(Reader in) throws XMLStreamException {
        context().write(in);
    }

    protected void write(XMLEventReader reader) throws XMLStreamException {
        context().write(reader);
    }

    protected void flush() throws XMLStreamException {
        context().flush();
    }

    protected void cdata(String value) throws XMLStreamException {
        context().dispatch(eventFactory().createCData(value));
    }

    protected void writeBase64(InputStream data) throws IOException {
        try (CharacterEmittingWriter writer = new CharacterEmittingWriter();
                OutputStream encodingStream = base64().encodingStream(writer)) {
            ByteStreams.copy(data, encodingStream);
        }
    }

    /**
     * Write {@link TimeInstant} to stream
     *
     * @param time
     *            {@link TimeInstant} to write to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimeInstant time) throws XMLStreamException {
        time(time.getTimePosition());
    }

    /**
     * Write {@link TimePosition} as ISO 8601 to stream
     *
     * @param time
     *            {@link TimePosition} to write as ISO 8601 to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimePosition time) throws XMLStreamException {
        chars(DateTimeHelper.formatDateTime2IsoString(time.getTime()));
    }

    /**
     * Write {@link SchemaLocation}s as xsi:schemaLocations attribute to stream
     *
     * @param schemaLocations
     *            {@link SchemaLocation}s to write
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void schemaLocation(Set<SchemaLocation> schemaLocations) throws XMLStreamException {
        String merged = mergeSchemaLocationsToString(schemaLocations);
        if (!Strings.isNullOrEmpty(merged)) {
            namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
            attr(W3CConstants.QN_SCHEMA_LOCATION_PREFIXED, merged);
        }
    }

    protected void writeXLinkAttrs(Link link) throws XMLStreamException {
        namespace(XLinkConstants.NS_XLINK_PREFIX, XLinkConstants.NS_XLINK);
        attr(XLinkConstants.Attr.QN_HREF, link.getHref().map(URI::toString));
        attr(XLinkConstants.Attr.QN_ROLE, link.getRole().map(URI::toString));
        attr(XLinkConstants.Attr.QN_SHOW, link.getShow().map(Show::toString));
        attr(XLinkConstants.Attr.QN_TITLE, link.getTitle());
        attr(XLinkConstants.Attr.QN_ARCROLE, link.getArcrole().map(URI::toString));
        attr(XLinkConstants.Attr.QN_ACTUATE, link.getActuate().map(Actuate::toString));
    }

    protected void writeLink(QName name,
            Link link) throws XMLStreamException {
        element(name, link, this::writeXLinkAttrs);
    }

    protected void element(QName name,
            String value) throws XMLStreamException {
        start(name);
        chars(value);
        end(name);
    }

    protected void element(QName name,
            Optional<String> value) throws XMLStreamException {
        if (value.isPresent()) {
            element(name, value.get());
        }
    }

    protected void element(QName name,
            OffsetDateTime time) throws XMLStreamException {
        element(name, format(time));
    }

    protected <T> void element(QName name,
            Optional<? extends T> elem,
            ElementWriter<? super T> writer) throws XMLStreamException {
        if (elem.isPresent()) {
            start(name);
            writer.write(elem.get());
            end(name);
        }
    }

    protected <T> void element(QName name,
            T elem,
            ElementWriter<? super T> writer) throws XMLStreamException {
        if (elem != null) {
            start(name);
            writer.write(elem);
            end(name);
        }
    }

    protected <T> void element(QName name,
            ContentWriter writer) throws XMLStreamException {
        start(name);
        writer.write();
        end(name);
    }

    protected <T> void delegate(T object) throws XMLStreamException, EncodingException {
        context().write(object);
    }

    protected String format(OffsetDateTime time) {
        return DateTimeFormatter.ISO_DATE_TIME.format(time);
    }

    protected <T> void forEach(QName name,
            Iterable<? extends T> elements,
            ElementWriter<? super T> writer) throws XMLStreamException {
        if (elements != null) {
            for (T elem : elements) {
                start(name);
                writer.write(elem);
                end(name);
            }
        }
    }

    protected void writeXML(String xml) throws XMLStreamException {
        try (StringReader reader = new StringReader(xml)) {
            write(reader);
        }
    }

    protected static BaseEncoding base64() {
        return BaseEncoding.base64().withSeparator("\n", 80);
    }

    private static String mergeSchemaLocationsToString(Iterable<SchemaLocation> schemaLocations) {
        if (schemaLocations == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator<SchemaLocation> it = schemaLocations.iterator();
        if (it.hasNext()) {
            builder.append(it.next().getSchemaLocationString());
            while (it.hasNext()) {
                builder.append(" ").append(it.next().getSchemaLocationString());
            }
        }
        return builder.toString();
    }

    /**
     * @return the xmlContentEscaper
     */
    protected static Escaper escaper() {
        return ESCAPER;
    }

    @FunctionalInterface
    protected interface ElementWriter<T> {
        void write(T elem) throws XMLStreamException;
    }

    @FunctionalInterface
    protected interface ContentWriter {
        void write() throws XMLStreamException;
    }

    private class CharacterEmittingWriter extends Writer {
        private boolean closed;

        @Override
        public void write(char[] cbuf,
                int off,
                int len) throws IOException {
            checkNotClosed();
            write(new String(cbuf, off, len));
        }

        @Override
        public void write(String str) throws IOException {
            checkNotClosed();
            try {
                AbstractElementXmlStreamWriter.this.chars(str);
            } catch (XMLStreamException ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public void write(String str,
                int off,
                int len) throws IOException {
            checkNotClosed();
            write(str.substring(off, len));
        }

        @Override
        public void flush() throws IOException {
            checkNotClosed();
            try {
                AbstractElementXmlStreamWriter.this.flush();
            } catch (XMLStreamException ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public void close() {
            this.closed = true;
        }

        private void checkNotClosed() throws IOException {
            if (closed) {
                throw new IOException("already closed");
            }
        }
    }

}
