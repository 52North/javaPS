package org.n52.javaps.io;

import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.Format;
import org.n52.javaps.io.data.IComplexData;

/**
 *
 * @author Christian Autermann
 */
public interface ParserRepository extends FormatRepository {
    Set<IParser> getParsers();

    Optional<IParser> getParser(Format format, Class<? extends IComplexData> binding);
}
