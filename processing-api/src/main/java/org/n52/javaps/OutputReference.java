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
package org.n52.javaps;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.util.Chain;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OutputReference implements Iterable<OwsCode> {

    private final JobId jobId;
    private final Chain<OwsCode> outputId;

    public OutputReference(JobId jobId, Chain<OwsCode> outputId) {
        this.jobId = Objects.requireNonNull(jobId, "jobId");
        this.outputId = Objects.requireNonNull(outputId, "outputId");
    }


    public OutputReference(JobId jobId, OwsCode outputId) {
        this.jobId = Objects.requireNonNull(jobId, "jobId");
        this.outputId = new Chain<>(Objects.requireNonNull(outputId, "outputId"));
    }

    public JobId getJobId() {
        return this.jobId;
    }

    public Chain<OwsCode> getOutputId() {
        return this.outputId;
    }

    public OwsCode first() {
        return this.outputId.first();
    }

    public OwsCode last() {
        return this.outputId.last();
    }

    public Optional<OutputReference> tail() {
        return this.outputId.tail().map(chain -> new OutputReference(jobId, chain));
    }

    public OutputReference child(OwsCode t) {
        return new OutputReference(jobId, this.outputId.child(t));
    }

    public OutputReference child(Chain<OwsCode> t) {
        return new OutputReference(jobId, this.outputId.child(t));
    }

    public Optional<OutputReference> parent() {
        return this.outputId.parent().map(chain -> new OutputReference(jobId, chain));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jobId, this.outputId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OutputReference other = (OutputReference) obj;
        return Objects.equals(this.jobId, other.getJobId()) &&
               Objects.equals(this.outputId, other.getOutputId());
    }

    @Override
    public String toString() {
        return "OutputReference{" + "jobId=" + jobId + ", outputId=" + outputId + '}';
    }

    @Override
    public Iterator<OwsCode> iterator() {
        return this.outputId.iterator();
    }

}
