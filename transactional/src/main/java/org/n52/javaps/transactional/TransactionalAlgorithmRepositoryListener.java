/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

/**
 * Listener for {@link ListenableTransactionalAlgorithmRepository transactional algorithm repositories}.
 *
 * @author Christian Autermann
 */
public interface TransactionalAlgorithmRepositoryListener {

    /**
     * Called when a new {@link ApplicationPackage} is registered.
     *
     * @param applicationPackage The newly registered {@link ApplicationPackage}.
     */
    void onRegister(ApplicationPackage applicationPackage);

    /**
     * Called when a {@link ApplicationPackage} is unregistered.
     *
     * @param applicationPackage The unregistered {@link ApplicationPackage}.
     */
    void onUnregister(ApplicationPackage applicationPackage);
}
