package org.n52.javaps.description;

public interface GroupOutputDescriptionBuilder<T extends GroupOutputDescription, B extends GroupOutputDescriptionBuilder<T, B>>
        extends ProcessOutputDescriptionBuilder<T, B>, ProcessOutputDescriptionContainerBuilder<T, B> {

}
