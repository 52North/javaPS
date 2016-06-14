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


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public interface LiteralDescriptionBuilder<T extends LiteralDescription, B extends LiteralDescriptionBuilder<T, B>> {

    @SuppressWarnings("unchecked")
    default B withSupportedLiteralDataDomain(Iterable<LiteralDataDomain> domains) {
        for (LiteralDataDomain domain : domains) {
            withDefaultLiteralDataDomain(domain);
        }
        return (B) this;
    }

    B withDefaultLiteralDataDomain(LiteralDataDomain domain);

    default B withDefaultLiteralDataDomain(LiteralDataDomainBuilder<?,?> domain) {
        return withDefaultLiteralDataDomain(domain.build());
    }

    B withSupportedLiteralDataDomain(LiteralDataDomain domain);

    default B withSupportedLiteralDataDomain(LiteralDataDomainBuilder<?,?> domain) {
        return withSupportedLiteralDataDomain(domain.build());
    }

}
