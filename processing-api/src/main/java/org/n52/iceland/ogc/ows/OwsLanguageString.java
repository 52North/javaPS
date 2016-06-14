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
package org.n52.iceland.ogc.ows;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsLanguageString {
    private final String lang;
    private final String value;

    public OwsLanguageString(String lang, String value) {
        this.lang = Strings.emptyToNull(lang);
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    public OwsLanguageString(String value) {
        this(null, value);
    }

    public Optional<String> getLang() {
        return Optional.ofNullable(this.lang);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lang, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsLanguageString that = (OwsLanguageString) obj;
        return Objects.equals(this.lang, that.lang) &&
               Objects.equals(this.value, that.value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("lang", this.lang)
                .add("value", this.value)
                .toString();
    }

}