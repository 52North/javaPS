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

import java.util.Arrays;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessDescriptionBuilder<T extends ProcessDescription, B extends ProcessDescriptionBuilder<T, B>>
        extends DescriptionBuilder<T, B> {

    B statusSupported(boolean statusSupported);

    B storeSupported(boolean storeSupported);

    B withInput(ProcessInputDescription input);

    default B withInput(ProcessInputDescriptionBuilder<?, ?> input) {
        return withInput(input.build());
    }

    default B withInput(Iterable<ProcessInputDescription> inputs) {
        for (ProcessInputDescription input : inputs) {
            withInput(input);
        }
        return (B) this;
    }

    default B withInput(ProcessInputDescription... inputs) {
        return withInput(Arrays.asList(inputs));
    }

    B withOutput(ProcessOutputDescription output);

    default B withOutput(ProcessOutputDescriptionBuilder<?, ?> output) {
        return withOutput(output.build());
    }

    default B withOutput(Iterable<ProcessOutputDescription> outputs) {
        for (ProcessOutputDescription output : outputs) {
            withOutput(output);
        }
        return (B) this;
    }

    default B withOutput(ProcessOutputDescription... outputs) {
        return withOutput(Arrays.asList(outputs));
    }

    B withVersion(String version);

}
