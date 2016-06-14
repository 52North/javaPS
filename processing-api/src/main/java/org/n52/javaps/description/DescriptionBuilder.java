/*
 * Copyright (C) 2013-2015 Christian Autermann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.n52.javaps.description;

import java.net.URI;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.iceland.ogc.ows.OwsLanguageString;

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

}
