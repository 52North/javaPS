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
package org.n52.javaps.transactional;

import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.shetland.ogc.wps.ap.ExecutionUnit;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * {@link ApplicationPackage} that cannot be deleted.
 */
public class UndeletableApplicationPackage extends ApplicationPackage {

    private final ApplicationPackage delegate;

    /**
     * Creates a new {@link UndeletableApplicationPackage}.
     *
     * @param delegate The base {@link ApplicationPackage}.
     */
    public UndeletableApplicationPackage(ApplicationPackage delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public ProcessOffering getProcessDescription() {
        return delegate.getProcessDescription();
    }

    @Override
    public void setProcessDescription(ProcessOffering processDescription) {
        delegate.setProcessDescription(processDescription);
    }

    @Override
    public List<ExecutionUnit> getExecutionUnits() {
        return delegate.getExecutionUnits();
    }

    @Override
    public void setExecutionUnits(List<ExecutionUnit> executionUnits) {
        delegate.setExecutionUnits(executionUnits);
    }

    @Override
    public Boolean getImmediateDeployment() {
        return delegate.getImmediateDeployment();
    }

    @Override
    public void setImmediateDeployment(Boolean immediateDeployment) {
        delegate.setImmediateDeployment(immediateDeployment);
    }

    @Override
    public URI getDeploymentProfileName() {
        return delegate.getDeploymentProfileName();
    }

    @Override
    public void setDeploymentProfileName(URI deploymentProfileName) {
        delegate.setDeploymentProfileName(deploymentProfileName);
    }

    @Override
    public String toString() {
        return String.format("UndeletableApplicationPackage{applicationPackage=%s}", delegate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || delegate == o) {
            return true;
        }
        if (!(o instanceof ApplicationPackage)) {
            return false;
        }
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
