package org.n52.javaps.coding.stream;

import org.n52.iceland.component.Component;
import org.n52.iceland.util.Similar;

// TODO move to iceland
public class ComponentSimilarityComparator<
        K extends Similar<K>,
        C extends Component<K>
    >
        extends ProxySimilarityComparator<C, K> {

    public ComponentSimilarityComparator(K ref) {
        super(ref, Component<K>::getKeys);
    }

}
