/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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

import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.function.IntSupplier;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Day implements IntSupplier {

    private final int day;

    public Day(int day) {
        this.day = ChronoField.DAY_OF_MONTH.checkValidIntValue(day);
    }

    @Override
    public int getAsInt() {
        return this.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.day);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.getAsInt() == ((Day) obj).getAsInt();
    }

    @Override
    public String toString() {
        return "Day{" + day + '}';
    }

}
