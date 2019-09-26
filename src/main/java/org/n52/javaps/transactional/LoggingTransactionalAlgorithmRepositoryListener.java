/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.transactional;

import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@linkplain TransactionalAlgorithmRepositoryListener Listener} implementation that logs the addition and removal of
 * {@linkplain ApplicationPackage application packages}.
 *
 * @author Christian Autermann
 */
@Component
public class LoggingTransactionalAlgorithmRepositoryListener implements TransactionalAlgorithmRepositoryListener {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingTransactionalAlgorithmRepositoryListener.class);

    @Override
    public void onRegister(ApplicationPackage applicationPackage) {
        ProcessDescription id = applicationPackage.getProcessDescription().getProcessDescription();
        LOG.info("registered application package: {}", id.getId());
    }

    @Override
    public void onUnregister(ApplicationPackage applicationPackage) {
        ProcessDescription id = applicationPackage.getProcessDescription().getProcessDescription();
        LOG.info("unregistered application package: {}", id.getId());
    }
}
