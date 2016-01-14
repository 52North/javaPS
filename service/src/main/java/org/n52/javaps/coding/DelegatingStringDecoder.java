/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.coding;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.decode.DecoderRepository;
import org.n52.iceland.coding.decode.XmlNamespaceDecoderKey;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.NoDecoderForKeyException;
import org.n52.iceland.exception.ows.concrete.UnsupportedDecoderInputException;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.javaps.request.DemoRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DelegatingStringDecoder
        implements Decoder<AbstractServiceCommunicationObject, String> {

    private static final Logger log = LoggerFactory.getLogger(DelegatingStringDecoder.class);
    private DecoderRepository decoderRepository;
    private final Set<DecoderKey> keys;
    private final JAXBContext context;

    public DelegatingStringDecoder(Set<DecoderKey> keys) throws JAXBException {
        this.keys = Objects.requireNonNull(keys);
        this.context = JAXBContext.newInstance(DemoRequest.class);
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

    @Override
    public AbstractServiceCommunicationObject decode(String string)
            throws OwsExceptionReport, UnsupportedDecoderInputException {
        try {
            JAXBElement<Object> xmlObject = asXmlElement(string);
            DecoderKey key = new XmlNamespaceDecoderKey(getDocumentNamespace(string), xmlObject.getDeclaredType());
            Decoder<AbstractServiceCommunicationObject, JAXBElement> delegate = getDelegate(key);

            log.trace("Delegated decoding to {} based on key {}", delegate, key);
            return delegate.decode(xmlObject);
        } catch (JAXBException | IOException | ParserConfigurationException | SAXException ex) {
            throw new NoApplicableCodeException()
                    .withMessage("Error while decoding request string: \n%s", string)
                    .causedBy(ex);
        }
    }

    private JAXBElement asXmlElement(String string) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<Object> elem = unmarshaller.unmarshal(new StreamSource(new StringReader(string)), Object.class);
        return elem;
    }

    private Decoder<AbstractServiceCommunicationObject, JAXBElement> getDelegate(DecoderKey decoderKey)
            throws NoDecoderForKeyException {
        Decoder<AbstractServiceCommunicationObject, JAXBElement> decoder = this.decoderRepository.getDecoder(decoderKey);
        if (decoder == null) {
            throw new NoDecoderForKeyException(decoderKey);
        }
        return decoder;
    }

    // FIXME overkill to parse to document just to get the namespace...
    private String getDocumentNamespace(String string) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.isIgnoringComments();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        InputSource source = new InputSource(new StringReader(string));
        Document document = documentBuilder.parse(source);
        return document.getDocumentElement().getNamespaceURI();
    }

}
