package org.n52.javaps.coding.xml;

import javax.xml.stream.XMLStreamReader;

import org.n52.iceland.component.Component;

/**
 *
 * @author Christian Autermann
 */
public interface XmlStreamReader<T> extends Component<T>{
    void setReader(XMLStreamReader reader);
    T read();
}
