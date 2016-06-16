package org.n52.javaps.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.Format;
import org.n52.javaps.io.data.IComplexData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface FormatRepository {

    Set<Format> getSupportedFormats();

    default Optional<Format> getDefaultFormat(Class<? extends IComplexData> binding) {
        return getSupportedFormats(binding).stream().min(Comparator.naturalOrder());
    }

    Set<Format> getSupportedFormats(Class<? extends IComplexData> binding);

    default Set<Format> getSupportedFormats(IComplexData binding) {
        if (binding == null) {
            return Collections.emptySet();
        } else {
            return getSupportedFormats(binding.getClass());
        }
    }
}
