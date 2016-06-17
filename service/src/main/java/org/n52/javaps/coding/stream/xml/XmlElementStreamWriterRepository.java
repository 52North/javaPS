package org.n52.javaps.coding.stream.xml;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.javaps.coding.stream.AbstractSimilarityKeyRepository;
import org.n52.javaps.coding.stream.StreamWriterKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlElementStreamWriterRepository extends AbstractSimilarityKeyRepository<StreamWriterKey, XmlElementStreamWriter>
        implements Constructable {

    private Collection<Provider<XmlElementStreamWriter>> writers;

    public XmlElementStreamWriterRepository(Collection<Provider<XmlElementStreamWriter>> writers) {
        this.writers = writers;
        if (writers != null) {
            init();
        }
    }

    public XmlElementStreamWriterRepository() {
        this(null);
    }

    @Autowired(required = false)
    public void set(Collection<Provider<XmlElementStreamWriter>> writers) {
        this.writers = writers;
    }

    @Override
    public void init() {
        Objects.requireNonNull(this.writers);
        setProducers(this.writers);
    }

    public Optional<XmlElementStreamWriter> get(StreamWriterKey key, XmlStreamWritingContext ctx) {
        Optional<XmlElementStreamWriter> writer = get(key);
        writer.ifPresent(x -> x.setContext(ctx));
        return writer;
    }

    @Override
    public Set<StreamWriterKey> keys() {
        return super.keys();
    }

}
