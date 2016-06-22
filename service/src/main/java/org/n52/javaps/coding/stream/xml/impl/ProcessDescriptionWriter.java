/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.coding.stream.xml.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.math.BigInteger;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XMLSchema;
import org.n52.javaps.description.BoundingBoxDescription;
import org.n52.javaps.description.BoundingBoxInputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.ComplexDescription;
import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.Description;
import org.n52.javaps.description.Format;
import org.n52.javaps.description.GroupInputDescription;
import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralDescription;
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessInputDescriptionContainer;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.ProcessOutputDescriptionContainer;
import org.n52.javaps.description.ThrowingProcessInputVisitor;
import org.n52.javaps.description.ThrowingProcessOutputVisitor;
import org.n52.javaps.ogc.ows.OwsAllowedValues;
import org.n52.javaps.ogc.ows.OwsAnyValue;
import org.n52.javaps.ogc.ows.OwsCRS;
import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsPossibleValues;
import org.n52.javaps.ogc.ows.OwsRange;
import org.n52.javaps.ogc.ows.OwsValue;
import org.n52.javaps.ogc.ows.OwsValueRestriction;
import org.n52.javaps.ogc.ows.OwsValuesReference;

/**
 *
 * @author Christian Autermann
 */
public class ProcessDescriptionWriter extends AbstractXmlElementStreamWriter<ProcessDescription> {
    private static final String UNBOUNDED = "unbounded";
    private final ConcreteOutputWriter concreteOutputWriter
            = new ConcreteOutputWriter();
    private final ConcreteInputWriter concreteInputWriter
            = new ConcreteInputWriter();

    public ProcessDescriptionWriter() {
        super(ProcessDescription.class);
    }

    @Override
    public void write(ProcessDescription object)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS, object, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);

            writeDescriptionElements(object);
            write((ProcessInputDescriptionContainer) object);
            write((ProcessOutputDescriptionContainer) object);
        });
    }

    private void write(ProcessInputDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_INPUT, input, x -> {
            attr(XMLSchema.Attr.AN_MIN_OCCURS, x.getOccurence().getMin().toString());
            attr(XMLSchema.Attr.AN_MAX_OCCURS, x.getOccurence().getMax().map(Object::toString).orElse(UNBOUNDED));
            writeDescriptionElements(x);
            x.visit(this.concreteInputWriter);
        });
    }

    private void write(ProcessOutputDescription output)
            throws XMLStreamException {
        element(WPS.Elem.QN_OUTPUT, output, x -> {
            writeDescriptionElements(x);
            x.visit(this.concreteOutputWriter);
        });
    }

    private void write(QName name, Optional<OwsLanguageString> value)
            throws XMLStreamException {
        if (value.isPresent()) {
            write(name, value.get());
        }
    }

    private void write(QName name, OwsLanguageString value)
            throws XMLStreamException {
        element(name, value, x -> {
            attr("xml:lang", x.getLang());
            chars(x.getValue());
        });
    }

    private void write(QName name, OwsCode id)
            throws XMLStreamException {
        element(name, id, x -> {
            attr(OWS.Attr.AN_CODE_SPACE, x.getCodeSpace().map(URI::toString));
            chars(x.getValue());
        });
    }

    private void writeKeywords(Set<OwsKeyword> set)
            throws XMLStreamException {
        if (!set.isEmpty()) {
            element(OWS.Elem.QN_KEYWORDS, () -> {
                Map<Optional<OwsCode>, Set<OwsLanguageString>> keywords = set.stream()
                        .collect(groupingBy(OwsKeyword::getType, mapping(OwsKeyword::getKeyword, toSet())));
                for (Entry<Optional<OwsCode>, Set<OwsLanguageString>> entry : keywords.entrySet()) {
                    Optional<OwsCode> type = entry.getKey();
                    for (OwsLanguageString keyword : entry.getValue()) {
                        write(OWS.Elem.QN_KEYWORD, keyword);
                    }
                    if (type.isPresent()) {
                        write(OWS.Elem.QN_TYPE, type.get());
                    }
                }
            });

        }
    }

    private void writeDescriptionElements(Description description)
            throws XMLStreamException {
        write(OWS.Elem.QN_TITLE, description.getTitle());
        write(OWS.Elem.QN_ABSTRACT, description.getAbstract());
        writeKeywords(description.getKeywords());
        write(OWS.Elem.QN_IDENTIFIER, description.getId());
        forEach(OWS.Elem.QN_METADATA, description.getMetadata(), metadata -> {
            writeXLinkAttrs(metadata);
            attr(OWS.Attr.AN_ABOUT, metadata.getAbout().map(URI::toString));
        });
    }

    private void write(Format format,
                       Optional<BigInteger> maximumMegabytes,
                       boolean isDefaultFormat)
            throws XMLStreamException {
        element(WPS.Elem.QN_FORMAT, format, x -> {
            attr(WPS.Attr.AN_MIME_TYPE, x.getMimeType());
            attr(WPS.Attr.AN_ENCODING, x.getEncoding());
            attr(WPS.Attr.AN_SCHEMA, x.getSchema());
            attr(WPS.Attr.AN_MAXIMUM_MEGABYTES, maximumMegabytes
                 .map(BigInteger::toString));

            if (isDefaultFormat) {
                attr(WPS.Attr.AN_DEFAULT, "true");
            }
        });
    }

    private void write(LiteralDataDomain literalDataDomain, boolean defaultDomain)
            throws XMLStreamException {
        element(WPS.Elem.QN_LITERAL_DATA_DOMAIN, literalDataDomain, ldd -> {
             if (defaultDomain) {
            attr(WPS.Attr.AN_DEFAULT, "true");
        }

        OwsPossibleValues vd = ldd.getValueDescription();

            if (vd.isAnyValue()) {
                write(vd.asAnyValues());
            } else if (vd.isAllowedValues()) {
                write(vd.asAllowedValues());
            } else if (vd.isValuesReference()) {
                write(vd.asValuesReference());
            }

            write(OWS.Elem.QN_DATA_TYPE, ldd.getDataType().orElse(null));
            write(OWS.Elem.QN_UOM, ldd.getUOM().orElse(null));
            element(OWS.Elem.QN_DEFAULT_VALUE, ldd.getDefaultValue());
        });
    }

    private void write(OwsValuesReference ref)
            throws XMLStreamException {
        element(OWS.Elem.QN_VALUES_REFERENCE, () -> {
            attr(OWS.Attr.QN_REFERENCE, ref.getReference().toString());
            chars(ref.getValue());
        });
    }

    private void write(OwsAnyValue any)
            throws XMLStreamException {
        empty(OWS.Elem.QN_ANY_VALUE);
    }

    private void write(OwsAllowedValues allowedValues)
            throws XMLStreamException {
        element(OWS.Elem.QN_ALLOWED_VALUES, () -> {
            for (OwsValueRestriction restriction : allowedValues) {
                write(restriction);
            }
        });
    }

    private void write(OwsRange range)
            throws XMLStreamException {
        element(OWS.Elem.QN_RANGE, range, x -> {
            attr(OWS.Attr.AN_RANGE_CLOSURE, x.getType());
            element(OWS.Elem.QN_MINIMUM_VALUE, x.getLowerBound().map(OwsValue::getValue));
            element(OWS.Elem.QN_MAXIMUM_VALUE, x.getUpperBound().map(OwsValue::getValue));
            element(OWS.Elem.QN_SPACING, x.getSpacing().map(OwsValue::getValue));
        });
    }

    private void write(OwsValueRestriction restriction)
            throws XMLStreamException {
        if (restriction.isRange()) {
            write(restriction.asRange());
        } else if (restriction.isValue()) {
            element(OWS.Elem.QN_VALUE, restriction.asValue().getValue());
        }
    }

    private void write(BoundingBoxDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_BOUNDING_BOX_DATA, input, x -> {
            element(WPS.Elem.QN_SUPPORTED_CRS, x.getDefaultCRS(), crs -> {
                attr(WPS.Attr.AN_DEFAULT, "true");
                chars(crs.getValue().toString());
            });
            for (OwsCRS crs : x.getSupportedCRS()) {
                element(WPS.Elem.QN_SUPPORTED_CRS, crs.getValue().toString());
            }

        });
    }

    private void write(ComplexDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_COMPLEX_DATA, input, x -> {
            write(x.getDefaultFormat(), x.getMaximumMegabytes(), true);
            for (Format format : x.getSupportedFormats()) {
                write(format, x.getMaximumMegabytes(), false);
            }
        });
    }

    private void write(LiteralDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_LITERAL_DATA, input, x -> {
            write(x.getDefaultLiteralDataDomain(), true);
            for (LiteralDataDomain ldd : x.getSupportedLiteralDataDomains()) {
                write(ldd, false);
            }
        });
    }

    private void write(ProcessInputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessInputDescription description : container.getInputDescriptions()) {
            write(description);
        }
    }

    private void write(ProcessOutputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessOutputDescription description : container.getOutputDescriptions()) {
            write(description);
        }
    }

    private void write(QName name, OwsDomainMetadata dmd)
            throws XMLStreamException {
        element(name, dmd, x -> {
            attr(OWS.Elem.QN_REFERENCE, x.getReference().map(URI::toString));
            chars(x.getValue());
        });
    }

    private class ConcreteInputWriter implements
            ThrowingProcessInputVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxInputDescription input)
                throws XMLStreamException {
            write((BoundingBoxDescription) input);
        }

        @Override
        public void visit(ComplexInputDescription input)
                throws XMLStreamException {
            write((ComplexDescription) input);
        }

        @Override
        public void visit(LiteralInputDescription input)
                throws XMLStreamException {
            write((LiteralDescription) input);
        }

        @Override
        public void visit(GroupInputDescription input)
                throws XMLStreamException {
            write((ProcessInputDescriptionContainer) input);
        }
    }

    private class ConcreteOutputWriter implements
            ThrowingProcessOutputVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxOutputDescription output)
                throws XMLStreamException {
            write((BoundingBoxDescription) output);
        }

        @Override
        public void visit(ComplexOutputDescription output)
                throws XMLStreamException {
            write((ComplexDescription) output);
        }

        @Override
        public void visit(LiteralOutputDescription output)
                throws XMLStreamException {
            write((LiteralDescription) output);
        }

        @Override
        public void visit(GroupOutputDescription output)
                throws XMLStreamException {
            write((ProcessOutputDescriptionContainer) output);
        }
    }
}
