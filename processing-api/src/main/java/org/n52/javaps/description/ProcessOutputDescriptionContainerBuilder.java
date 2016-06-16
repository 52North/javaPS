package org.n52.javaps.description;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Christian Autermann
 */
public interface ProcessOutputDescriptionContainerBuilder<T extends ProcessOutputDescriptionContainer, B extends ProcessOutputDescriptionContainerBuilder<T, B>>  {
    B withOutput(ProcessOutputDescription output);

    default B withOutput(ProcessOutputDescriptionBuilder<?, ?> output) {
        return withOutput(output.build());
    }

    @SuppressWarnings("unchecked")
     default B withOutput(Stream<ProcessOutputDescription> outputs) {
        outputs.forEach(this::withOutput);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    default B withOutput(Iterable<ProcessOutputDescription> outputs) {
        for (ProcessOutputDescription output : outputs) {
            withOutput(output);
        }
        return (B) this;
    }

    default B withOutput(ProcessOutputDescription... outputs) {
        return withOutput(Arrays.asList(outputs));
    }

}
