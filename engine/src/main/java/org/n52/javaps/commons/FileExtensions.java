/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.commons;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.n52.janmayen.http.MediaType;

/**
 *
 * @author tkunicki
 */
public class FileExtensions implements FileExtensionProvider {

    private Map<MediaType, String> extensions;

    @Override
    public String getExtension(MediaType mediaType) {

        if (this.extensions.containsKey(mediaType)) {
            return this.extensions.get(mediaType);
        }
        Optional<String> extension = this.extensions.keySet().stream().filter(x -> x.isCompatible(mediaType)).findAny()
                .map(this.extensions::get);

        if (extension.isPresent()) {
            return extension.get();
        }

        if (MediaType.anyText().isCompatible(mediaType)) {
            return "txt";
        }
        String subtype = mediaType.getSubtype();

        int idx = subtype.lastIndexOf('+');
        if (idx > 0 && idx < subtype.length() - 1) {
            return subtype.substring(idx + 1);
        }

        return subtype;

    }

    public void setExtensionsByString(Map<String, String> extensions) {
        setExtensionsByMediaType(extensions.entrySet().stream().collect(Collectors.toMap(x -> MediaType.parse(x
                .getKey()), Entry::getValue)));
    }

    public void setExtensionsByMediaType(Map<MediaType, String> extensions) {
        this.extensions = Objects.requireNonNull(extensions);
    }
}
