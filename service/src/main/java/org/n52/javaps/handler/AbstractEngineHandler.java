package org.n52.javaps.handler;

import java.util.Objects;

import org.n52.javaps.Engine;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractEngineHandler extends AbstractHandler {

    private final Engine engine;

    public AbstractEngineHandler(Engine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    protected Engine getEngine() {
        return engine;
    }

}
