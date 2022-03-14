/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.rest;

import java.net.URI;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.javaps.rest.settings.RestSettingsConstants;
import org.springframework.stereotype.Controller;

@Controller
@Configurable
public class ApiController implements Api {

    private URI apiURI;

    @Setting(RestSettingsConstants.API_URI)
    public void setDescription(URI apiURI) {
        Validation.notNull("apiURI", apiURI);
        this.apiURI = apiURI;
    }

    @Override
    public String api() {
        return "redirect:" + apiURI;
    }
}
