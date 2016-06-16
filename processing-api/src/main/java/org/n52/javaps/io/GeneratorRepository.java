package org.n52.javaps.io;

import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.Format;
import org.n52.javaps.io.data.IComplexData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface GeneratorRepository extends FormatRepository {
    Set<IGenerator> getGenerators();

    Optional<IGenerator> getGenerator(Format format, Class<? extends IComplexData> binding);

}
