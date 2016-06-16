package org.n52.javaps.description;

import java.util.Arrays;
import java.util.stream.Stream;


public interface ProcessInputDescriptionContainerBuilder<T extends ProcessInputDescriptionContainer, B extends ProcessInputDescriptionContainerBuilder<T, B>> {
    B withInput(ProcessInputDescription input);

    default B withInput(ProcessInputDescriptionBuilder<?, ?> input) {
        return withInput(input.build());
    }

    @SuppressWarnings(value = "unchecked")
    default B withInput(Stream<? extends ProcessInputDescription> input) {
        input.forEach(this::withInput);
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    default B withInput(Iterable<ProcessInputDescription> inputs) {
        for (ProcessInputDescription input : inputs) {
            withInput(input);
        }
        return (B) this;
    }

    default B withInput(ProcessInputDescription... inputs){
        return withInput(Arrays.asList(inputs));
    }

}
