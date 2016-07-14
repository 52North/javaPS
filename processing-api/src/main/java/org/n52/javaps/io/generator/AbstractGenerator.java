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
package org.n52.javaps.io.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64InputStream;
import org.n52.javaps.commons.WPSConfig;
import org.n52.javaps.io.AbstractIOHandler;
import org.n52.javaps.io.IGenerator;
import org.n52.javaps.io.data.IData;

/**
 * @author Matthias Mueller, TU Dresden
 *
 */
public abstract class AbstractGenerator extends AbstractIOHandler implements IGenerator {

    /**
     * A list of files that shall be deleted by destructor. Convenience
     * mechanism to delete temporary files that had to be written during the
     * generation procedure.
     */
    protected List<File> finalizeFiles;

    public AbstractGenerator() {
        super();
    }

    public void init(WPSConfig wpsConfig) {

        finalizeFiles = new ArrayList<File>();

    }

    public InputStream generateBase64Stream(IData data,
            String mimeType,
            String schema) throws IOException {
        return new Base64InputStream(generateStream(data, mimeType, schema), true);
    }

    /**
     * Destructor deletes generated temporary files.
     */
    @Override
    protected void finalize() throws Throwable {

        for (File currentFile : finalizeFiles) {
            currentFile.delete();
        }

        super.finalize();
    }

}
