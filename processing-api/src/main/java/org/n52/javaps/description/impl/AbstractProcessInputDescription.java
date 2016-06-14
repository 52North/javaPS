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

import org.n52.javaps.description.InputOccurence;
import org.n52.javaps.description.ProcessInputDescription;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessInputDescription extends AbstractDescription
        implements ProcessInputDescription {

    private final InputOccurence occurence;

    protected AbstractProcessInputDescription(AbstractProcessInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.occurence = new InputOccurence(builder.getMinimalOccurence(),
                                            builder.getMaximalOccurence());
    }

    @Override
    public InputOccurence getOccurence() {
        return this.occurence;
    }
}
