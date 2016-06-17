package org.n52.javaps.coding.stream.xml;

import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.coding.stream.StreamWriter;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlDocumentStreamWriter implements StreamWriter<Object> {
    private final XmlElementStreamWriterRepository repository;

    @Inject
    public XmlDocumentStreamWriter(XmlElementStreamWriterRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void write(Object object, OutputStream stream)
            throws OwsExceptionReport {
        try {
            XmlStreamWritingContext context
                    = new XmlStreamWritingContext(stream, this.repository::get);
            XMLEventFactory eventFactory = context.eventFactory();
            context.dispatch(eventFactory
                    .createStartDocument(XMLConstants.XML_ENCODING,
                                         XMLConstants.XML_VERSION));
            context.write(object);
            context.dispatch(eventFactory.createEndDocument());
            context.finish();
        } catch (XMLStreamException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return this.repository.keys();
    }
}
