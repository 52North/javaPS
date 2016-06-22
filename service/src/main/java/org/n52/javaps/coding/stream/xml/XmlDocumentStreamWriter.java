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
package org.n52.javaps.coding.stream.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.coding.stream.StreamWriter;
import org.n52.javaps.coding.stream.StreamWriterKey;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlDocumentStreamWriter implements StreamWriter<Object> {
    private static final TransformerFactory TRANSFORMER_FACTORY = createTransformerFactory();
    private static final String INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
    private final XmlElementStreamWriterRepository repository;
    private final ExecutorService executor;

    @Inject
    public XmlDocumentStreamWriter(XmlElementStreamWriterRepository repository) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("xml-transformer-%d").build();
        this.executor = Executors.newCachedThreadPool(threadFactory);
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void write(Object object, OutputStream stream)
            throws OwsExceptionReport {

        try {
            PipedOutputStream pos = new PipedOutputStream();
            Transformer transformer = createTransformer();

            Future<Void> t = executor.submit(() -> {
                try (PipedInputStream pis = new PipedInputStream(pos)) {
                    StreamResult result = new StreamResult(stream);
                    StreamSource source = new StreamSource(pis);

                    transformer.transform(source, result);
                }
                return null; // use a callable to allow exception throwing
            });

            try (XmlStreamWritingContext context = createContext(pos)) {
                context.startDocument();
                context.write(object);
                context.endDocument();
            }

            t.get(); // wait for the transformer to finish

        } catch (TransformerException | XMLStreamException | IOException | InterruptedException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        } catch (ExecutionException ex) {
            throw new NoApplicableCodeException().causedBy(ex.getCause());
        }
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return this.repository.keys();
    }

    private Transformer createTransformer() throws TransformerException {
        Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(INDENT_AMOUNT, "2");
        return transformer;
    }

    private XmlStreamWritingContext createContext(OutputStream pos)
            throws XMLStreamException {
        return new XmlStreamWritingContext(pos, this.repository::get);
    }

    private static TransformerFactory createTransformerFactory() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "false");
        return transformerFactory;
    }
}
