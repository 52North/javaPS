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
package org.n52.javaps.io.literal.xsd;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralLanguageType extends AbstractXSDLiteralType<Locale> {

    private static final long serialVersionUID = 2579631917686921792L;
    private static final Predicate<String> PATTERN = Pattern.compile("[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*").asPredicate();

    @Override
    public String getName() {
        return LANGUAGE;
    }

    @Override
    public Locale parse(OwsCode name, String value) throws OwsExceptionReport {
        if (PATTERN.test(value)) {
            return LocaleHelper.fromString(value);
        } else {
            throw new InvalidParameterValueException(name.getValue(), value);
        }
    }

    @Override
    public Class<Locale> getPayloadType() {
        return Locale.class;
    }

    @Override
    public String generate(OwsCode name, Locale value) throws OwsExceptionReport {
        return LocaleHelper.toString(value);
    }

}
