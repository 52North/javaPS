package org.n52.javaps.io.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LiteralTypeRepositoryImpl implements LiteralTypeRepository {

    private static final Logger log = LoggerFactory.getLogger(LiteralTypeRepositoryImpl.class);

    @Override
    public <T> LiteralType<T> getLiteralType(Class<? extends LiteralType<?>> literalType, Class<?> payloadType) {
        /* TODO implement org.n52.javaps.io.literal.LiteralTypeRepositoryImpl.getLiteralType() */
        throw new UnsupportedOperationException("org.n52.javaps.io.literal.LiteralTypeRepositoryImpl.getLiteralType() not yet implemented");
    }

}
