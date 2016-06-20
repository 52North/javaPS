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
package org.n52.javaps.description;

import java.net.URI;
import java.util.Arrays;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T> the builded type
 * @param <B> the builder type
 */
public interface DescriptionBuilder<T extends Description, B extends DescriptionBuilder<T, B>> extends Builder<T, B> {

    B withAbstract(OwsLanguageString abstrakt);

    default B withAbstract(String abstrakt) {
        return withAbstract(abstrakt == null ? null
                            : new OwsLanguageString(abstrakt));
    }

    default B withAbstract(String lang, String abstrakt) {
        return withAbstract(abstrakt == null ? null
                            : new OwsLanguageString(lang, abstrakt));
    }

    B withIdentifier(OwsCodeType id);

    default B withIdentifier(String id) {
        return withIdentifier(new OwsCodeType(id));
    }

    default B withIdentifier(String codespace, String id) {
        return withIdentifier(URI.create(codespace), id);
    }

    default B withIdentifier(URI codespace, String id) {
        return withIdentifier(new OwsCodeType(id, codespace));
    }

    B withTitle(OwsLanguageString title);

    default B withTitle(String title) {
        return withTitle(title == null ? null : new OwsLanguageString(title));
    }

    default B withTitle(String lang, String title) {
        return withTitle(title == null ? null
                         : new OwsLanguageString(lang, title));
    }

    B withKeyword(OwsKeyword keyword);

    @SuppressWarnings("unchecked")
    default B withKeywords(Iterable<OwsKeyword> keywords) {
        keywords.forEach(this::withKeyword);
        return (B ) this;
    }

    @SuppressWarnings("unchecked")
    default B withKeywords(OwsKeyword... keywords) {
        return withKeywords(Arrays.asList(keywords));
    }

    default B withKeyword(String keyword) {
        return withKeyword(new OwsKeyword(keyword));
    }

    B withMetadata(OwsMetadata metadata);

}
