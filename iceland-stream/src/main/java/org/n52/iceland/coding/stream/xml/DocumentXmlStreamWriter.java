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
package org.n52.iceland.coding.stream.xml;

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
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.n52.iceland.coding.stream.StreamWriter;
import org.n52.iceland.coding.stream.StreamWriterKey;
import org.n52.iceland.util.XmlFactories;
import org.n52.janmayen.function.ThrowingConsumer;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DocumentXmlStreamWriter extends XmlFactories implements StreamWriter<Object> {


    private final ElementXmlStreamWriterRepository repository;
    private final ExecutorService executor;

    @Inject
    public DocumentXmlStreamWriter(ElementXmlStreamWriterRepository repository) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("xml-transformer-%d").build();
        this.executor = Executors.newCachedThreadPool(threadFactory);
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void write(Object object, OutputStream stream)
            throws EncodingException {
        try {
            writeIndenting(stream, (out) -> {
                       try (XmlStreamWritingContext context = createContext(out)) {
                           context.startDocument();
                           context.write(object);
                           context.endDocument();
                       } catch (XMLStreamException ex) {
                           throw new EncodingException(ex);
                       }
                   });
        } catch (TransformerException | IOException | InterruptedException ex) {
            throw new EncodingException(ex);
        }
    }

    private <X extends Exception> void writeIndenting(OutputStream stream, ThrowingConsumer<OutputStream, X> writer)
            throws X, TransformerException, IOException, InterruptedException {
        try {
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);
            Transformer transformer = createIndentingTransformer();

            Future<Void> t = executor.submit(() -> {
                try {
                    StreamResult result = new StreamResult(stream);
                    StreamSource source = new StreamSource(pis);
                    transformer.transform(source, result);
                } finally {
                    pis.close();
                }
                return null; // use a callable to allow exception throwing
            });

            writer.accept(pos);

            t.get(); // wait for the transformer to finish

        } catch (ExecutionException ex) {
            Throwables.propagateIfInstanceOf(ex.getCause(), TransformerException.class);
            Throwables.propagateIfInstanceOf(ex.getCause(), IOException.class);
            throw Throwables.propagate(ex.getCause());
        }
    }



    @Override
    public Set<StreamWriterKey> getKeys() {
        return this.repository.keys();
    }

    private XmlStreamWritingContext createContext(OutputStream pos)
            throws XMLStreamException {
        return new XmlStreamWritingContext(pos, this.repository::get);
    }


}
