package org.n52.javaps.description;

/**
 *
 * @author Christian Autermann
 */
public interface GroupInputDescriptionBuilder<T extends GroupInputDescription, B extends GroupInputDescriptionBuilder<T, B>>
        extends ProcessInputDescriptionBuilder<T, B>, ProcessInputDescriptionContainerBuilder<T, B> {

}
