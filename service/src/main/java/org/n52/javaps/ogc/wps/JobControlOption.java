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
package org.n52.javaps.ogc.wps;

import java.util.Objects;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class JobControlOption {

    private static final JobControlOption SYNC_EXECUTE
            = new JobControlOption("sync-execute");
    private static final JobControlOption ASYNC_EXECUTE
            = new JobControlOption("async-execute");
    private static final JobControlOption DISMISS
            = new JobControlOption("dismiss");

    private final String value;

    public JobControlOption(String value) {
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof JobControlOption) {
            final JobControlOption other = (JobControlOption) obj;
            return Objects.equals(this.getValue(), other.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public static JobControlOption sync() {
        return SYNC_EXECUTE;
    }

    public static JobControlOption async() {
        return ASYNC_EXECUTE;
    }

    public static JobControlOption dismiss() {
        return DISMISS;
    }
}
