/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.reference;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.faroe.ConfigurationError;
import org.n52.iceland.service.ServiceSettings;
import org.n52.javaps.engine.impl.StaticURLOutputReferencer;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Configurable
public class IcelandOutputReferencer extends StaticURLOutputReferencer {

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceUrl) {
        try {
            setBaseURI(new URL(serviceUrl.toURL(), "results").toURI());
        } catch (MalformedURLException | URISyntaxException ex) {
            throw new ConfigurationError(ex);
        }
    }
}
