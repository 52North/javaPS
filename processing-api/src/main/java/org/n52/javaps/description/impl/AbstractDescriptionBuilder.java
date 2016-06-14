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
package org.n52.javaps.description.impl;

import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.javaps.description.Description;
import org.n52.javaps.description.DescriptionBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public abstract class AbstractDescriptionBuilder<T extends Description, B extends DescriptionBuilder<T, B>>
        implements DescriptionBuilder<T, B> {

    private OwsCodeType id;
    private OwsLanguageString title;
    private OwsLanguageString abstrakt;

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withIdentifier(OwsCodeType id) {
        this.id = Objects.requireNonNull(id);
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withTitle(OwsLanguageString title) {
        this.title = title;
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withAbstract(OwsLanguageString abstrakt) {
        this.abstrakt = abstrakt;
        return (B) this;
    }

    OwsCodeType getId() {
        return id;
    }

    OwsLanguageString getTitle() {
        return title;
    }

    OwsLanguageString getAbstract() {
        return abstrakt;
    }

}
