package org.n52.javaps.coding.stream.xml;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.n52.javaps.coding.stream.xml.impl.XMLConstants;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XMLFactories {

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newFactory();
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newFactory();
    private static final Charset DOCUMENT_ENCODING = StandardCharsets.UTF_8;
    private static final String XML_VERSION = XMLConstants.XML_VERSION;

    static {
        OUTPUT_FACTORY.setProperty("escapeCharacters", false);
    }

    /**
     * @return the event factory
     */
    public static XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the output factory
     */
    public static XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }

    /**
     * @return the input factory
     */
    public static XMLInputFactory inputFactory() {
        return INPUT_FACTORY;
    }


    public static Charset documentEncoding() {
        return DOCUMENT_ENCODING;
    }

    public static String documentVersion() {
        return XML_VERSION;
    }

}
