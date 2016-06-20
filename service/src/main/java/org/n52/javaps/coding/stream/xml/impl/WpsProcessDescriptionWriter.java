/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
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
package org.n52.javaps.coding.stream.xml.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.math.BigInteger;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Attributes;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Namespaces;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.QNames;
import org.n52.javaps.description.BoundingBoxDescription;
import org.n52.javaps.description.BoundingBoxInputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.ComplexDescription;
import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.Description;
import org.n52.javaps.description.Format;
import org.n52.javaps.description.GroupInputDescription;
import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.InputOccurence;
import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralDescription;
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessInputDescriptionContainer;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.ProcessOutputDescriptionContainer;
import org.n52.javaps.description.ThrowingProcessInputVisitor;
import org.n52.javaps.description.ThrowingProcessOutputVisitor;
import org.n52.javaps.ogc.ows.OwsAllowedRange;
import org.n52.javaps.ogc.ows.OwsAllowedValues;
import org.n52.javaps.ogc.ows.OwsAny;
import org.n52.javaps.ogc.ows.OwsCRS;
import org.n52.javaps.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;
import org.n52.javaps.ogc.ows.OwsValueDescription;
import org.n52.javaps.ogc.ows.OwsValueReference;
import org.n52.javaps.ogc.ows.OwsValueRestriction;
import org.n52.javaps.ogc.w3c.xlink.XLink.Actuate;
import org.n52.javaps.ogc.w3c.xlink.XLink.Show;

/**
 *
 * @author Christian Autermann
 */
public class WpsProcessDescriptionWriter extends AbstractXmlElementStreamWriter<ProcessDescription> {
    private static final XmlStreamWriterKey KEY = new XmlStreamWriterKey(ProcessDescription.class);
    private static final String UNBOUNDED = "unbounded";
    private final ConcreteOutputWriter concreteOutputWriter = new ConcreteOutputWriter();
    private final ConcreteInputWriter concreteInputWriter = new ConcreteInputWriter();

    @Override
    public void write(ProcessDescription object)
            throws XMLStreamException {
        start(QNames.WPS_PROCESS);
        namespace(Namespaces.WPS_PREFIX, Namespaces.WPS_20);
        namespace(Namespaces.OWS_PREFIX, Namespaces.OWS_20);
        namespace(Namespaces.XLINK_PREFIX, Namespaces.XLINK);

        writeDescriptionElements(object);
        write((ProcessInputDescriptionContainer) object);
        write((ProcessOutputDescriptionContainer) object);
        end(QNames.WPS_PROCESS);
    }

    private void write(ProcessInputDescription input)
            throws XMLStreamException {
        start(QNames.WPS_INPUT);
        InputOccurence occurence = input.getOccurence();
        attr(Attributes.XS_MIN_OCCURS, occurence.getMin().toString());
        attr(Attributes.XS_MAX_OCCURS, occurence.getMax().map(Object::toString).orElse(UNBOUNDED));
        writeDescriptionElements(input);
        input.visit(this.concreteInputWriter);
        end(QNames.WPS_INPUT);
    }

    private void write(ProcessOutputDescription output)
            throws XMLStreamException {
        start(QNames.WPS_OUTPUT);
        writeDescriptionElements(output);
        output.visit(this.concreteOutputWriter);
        end(QNames.WPS_OUTPUT);
    }

    private void write(QName name, OwsLanguageString value)
            throws XMLStreamException {
        if (value != null) {
            start(name);
            if (value.getLang().isPresent()) {
                attr("xml:lang", value.getLang().get());
            }
            chars(value.getValue());
            end(name);
        }
    }

    private void write(QName name, OwsCodeType id)
            throws XMLStreamException {
        if (id != null) {
            start(name);
            if (id.isSetCodeSpace()) {
                attr(Attributes.OWS_CODE_SPACE, id.getCodeSpace().toString());
            }
            chars(id.getValue());
            end(name);
        }
    }

    private void writeKeywords(Set<OwsKeyword> set)
            throws XMLStreamException {
        if (!set.isEmpty()) {
            start(QNames.OWS_KEYWORDS);
            Map<Optional<OwsCodeType>, Set<OwsLanguageString>> keywords = set.stream()
                    .collect(groupingBy(OwsKeyword::getType, mapping(OwsKeyword::getKeyword, toSet())));
            for (Entry<Optional<OwsCodeType>, Set<OwsLanguageString>> entry
                 : keywords.entrySet()) {
                Optional<OwsCodeType> type = entry.getKey();
                for (OwsLanguageString keyword : entry.getValue()) {
                    write(QNames.OWS_KEYWORD, keyword);
                }
                if (type.isPresent()) {
                    write(QNames.OWS_TYPE, type.get());
                }
            }
            end(QNames.OWS_KEYWORDS);
        }
    }

    private void writeDescriptionElements(Description description)
            throws XMLStreamException {
        write(QNames.OWS_TITLE, description.getTitle());
        if (description.getAbstract().isPresent()) {
            write(QNames.OWS_ABSTRACT, description.getAbstract().get());
        }
        writeKeywords(description.getKeywords());
        write(QNames.OWS_IDENTIFIER, description.getId());


        for (OwsMetadata metadata : description.getMetadata()) {
            start(QNames.OWS_METADATA);
            attr(QNames.XLINK_HREF, metadata.getHref().map(URI::toString));
            attr(QNames.XLINK_ROLE, metadata.getRole().map(URI::toString));
            attr(QNames.XLINK_SHOW, metadata.getShow().map(Show::toString));
            attr(QNames.XLINK_TITLE, metadata.getTitle());
            attr(QNames.XLINK_ARCROLE, metadata.getArcrole().map(URI::toString));
            attr(QNames.XLINK_ACTUATE, metadata.getActuate().map(Actuate::toString));
            attr(Attributes.OWS_ABOUT, metadata.getAbout().map(URI::toString));
            end(QNames.OWS_METADATA);
        }
    }

    private void write(Format defaultFormat,
                       Optional<BigInteger> maximumMegabytes,
                       boolean isDefaultFormat)
            throws XMLStreamException {
        start(QNames.WPS_FORMAT);
        Optional<String> encoding = defaultFormat.getEncoding();
        Optional<String> mimeType = defaultFormat.getMimeType();
        Optional<String> schema = defaultFormat.getSchema();

        if (mimeType.isPresent()) {
            attr(Attributes.WPS_MIME_TYPE, mimeType.get());
        }
        if (encoding.isPresent()) {
            attr(Attributes.WPS_ENCODING, encoding.get());
        }
        if (schema.isPresent()) {
            attr(Attributes.WPS_SCHEMA, schema.get());
        }
        if (maximumMegabytes.isPresent()) {
            attr(Attributes.WPS_MAXIMUM_MEGABYTES, maximumMegabytes.get()
                 .toString());
        }
        if (isDefaultFormat) {
            attr(Attributes.WPS_DEFAULT, "true");
        }
        end(QNames.WPS_FORMAT);
    }

    private void write(LiteralDataDomain ldd, boolean defaultDomain)
            throws XMLStreamException {
        start(QNames.WPS_LITERAL_DATA_DOMAIN);

        if (defaultDomain) {
            attr(Attributes.WPS_DEFAULT, "true");
        }

        OwsValueDescription vd = ldd.getValueDescription();

        if (vd.isAny()) {
            write(vd.asAny());
        } else if (vd.isAllowedValues()) {
            write(vd.asAllowedValues());
        } else if (vd.isReference()) {
            write(vd.asReference());
        }

        write(QNames.OWS_DATA_TYPE, ldd.getDataType().orElse(null));
        write(QNames.OWS_UOM, ldd.getUOM().orElse(null));
        if (ldd.getDefaultValue().isPresent()) {
            element(QNames.OWS_DEFAULT_VALUE, ldd.getDefaultValue().get());
        }

        end(QNames.WPS_LITERAL_DATA_DOMAIN);
    }

    private void write(OwsValueReference ref)
            throws XMLStreamException {
        start(QNames.OWS_VALUES_REFERENCE);
        attr(QNames.OWS_REFERENCE, ref.getReference().toString());
        chars(ref.getValue());
        end(QNames.OWS_VALUES_REFERENCE);
    }

    private void write(OwsAny any)
            throws XMLStreamException {
        start(QNames.OWS_ANY_VALUE);
        end(QNames.OWS_ANY_VALUE);
    }

    private void write(OwsAllowedValues allowedValues)
            throws XMLStreamException {
        start(QNames.OWS_ALLOWED_VALUES);
        for (OwsValueRestriction restriction : allowedValues) {
            write(restriction);
        }
        end(QNames.OWS_ALLOWED_VALUES);
    }

    private void write(OwsAllowedRange range)
            throws XMLStreamException {
        start(QNames.OWS_RANGE);

        attr(Attributes.OWS_RANGE_CLOSURE, range.getType());

        if (range.getLowerBound().isPresent()) {
            element(QNames.OWS_MINIMUM_VALUE, range.getLowerBound().get());
        }

        if (range.getUpperBound().isPresent()) {
            element(QNames.OWS_MAXIMUM_VALUE, range.getUpperBound().get());
        }

        if (range.getSpacing().isPresent()) {
            element(QNames.OWS_SPACING, range.getSpacing().get());
        }

        end(QNames.OWS_RANGE);
    }

    private void write(OwsValueRestriction restriction)
            throws XMLStreamException {
        if (restriction.isRange()) {
            write(restriction.asRange());
        } else if (restriction.isValue()) {
            element(QNames.OWS_VALUE, restriction.asValue().getValue());
        }
    }

    private void write(BoundingBoxDescription input)
            throws XMLStreamException {
        start(QNames.WPS_BOUNDING_BOX_DATA);

        start(QNames.WPS_SUPPORTED_CRS);
        attr(Attributes.WPS_DEFAULT, "true");
        chars(input.getDefaultCRS().getValue().toString());
        end(QNames.WPS_SUPPORTED_CRS);

        for (OwsCRS crs : input.getSupportedCRS()) {
            element(QNames.WPS_SUPPORTED_CRS, crs.getValue().toString());
        }

        end(QNames.WPS_BOUNDING_BOX_DATA);
    }

    private void write(ComplexDescription input)
            throws XMLStreamException {
        start(QNames.WPS_COMPLEX_DATA);
        write(input.getDefaultFormat(), input.getMaximumMegabytes(), true);
        for (Format format : input.getSupportedFormats()) {
            write(format, input.getMaximumMegabytes(), false);
        }
        end(QNames.WPS_COMPLEX_DATA);
    }

    private void write(LiteralDescription input)
            throws XMLStreamException {
        start(QNames.WPS_LITERAL_DATA);
        LiteralDataDomain dldd = input.getDefaultLiteralDataDomain();

        write(dldd, true);

        for (LiteralDataDomain ldd : input.getSupportedLiteralDataDomains()) {
            write(ldd, false);
        }

        end(QNames.WPS_LITERAL_DATA);
    }

    private void write(ProcessInputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessInputDescription description : container.getInputDescriptions()) {
            write(description);
        }
    }

    private void write(ProcessOutputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessOutputDescription description : container.getOutputDescriptions()) {
            write(description);
        }
    }

    private void write(QName name, OwsDomainMetadata dmd)
            throws XMLStreamException {
        if (dmd != null) {
            start(name);
            if (dmd.getReference().isPresent()) {
                attr(QNames.OWS_REFERENCE, dmd.getReference().get().toString());
            }
            chars(dmd.getValue());
            end(name);
        }
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

    private class ConcreteInputWriter implements ThrowingProcessInputVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxInputDescription input) throws XMLStreamException {
            write((BoundingBoxDescription)input);
        }

        @Override
        public void visit(ComplexInputDescription input) throws XMLStreamException {
            write((ComplexDescription)input);
        }

        @Override
        public void visit(LiteralInputDescription input) throws XMLStreamException {
            write((LiteralDescription)input);
        }

        @Override
        public void visit(GroupInputDescription input) throws XMLStreamException {
            write((ProcessInputDescriptionContainer)input);
        }
    }

    private class ConcreteOutputWriter implements ThrowingProcessOutputVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxOutputDescription output) throws XMLStreamException {
            write((BoundingBoxDescription)output);
        }

        @Override
        public void visit(ComplexOutputDescription output) throws XMLStreamException {
            write((ComplexDescription)output);
        }

        @Override
        public void visit(LiteralOutputDescription output) throws XMLStreamException {
            write((LiteralDescription)output);
        }

        @Override
        public void visit(GroupOutputDescription output) throws XMLStreamException {
            write((ProcessOutputDescriptionContainer)output);
        }
    }
}
