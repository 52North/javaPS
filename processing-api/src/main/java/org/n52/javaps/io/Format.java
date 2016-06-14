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
package org.n52.javaps.io;


/**
 * Class representing a WPS format consisting of MIME type, schema and encoding.
 *
 * @author Benjamin Pross
 *
 */
public class Format {

    private String mimeType;
    private String schema;
    private String encoding;
    private boolean defaultFormat;

    public Format(){
       this("","","", false);
    }

    public Format(String mimeType){
        this(mimeType,"","", false);
    }

    public Format(String mimeType, String schema){
        this(mimeType, schema,"", false);
    }

    public Format(String mimeType, boolean defaultFormat){
        this(mimeType,"","", defaultFormat);
    }

    public Format(String mimeType, String schema, boolean defaultFormat){
        this(mimeType, schema,"", defaultFormat);
    }

    public Format(String mimeType, String schema, String encoding, boolean defaultFormat){
        this.mimeType = mimeType;
        this.schema = schema;
        this.encoding = encoding;
        this.defaultFormat = defaultFormat;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isDefaultFormat() {
        return defaultFormat;
    }

    public void setDefaultFormat(boolean defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public boolean hasSchema(){
        return schema != null && !schema.isEmpty();
    }

    public boolean hasEncoding(){
        return encoding != null && !encoding.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Format)){
            return false;
        }
        Format otherFormat = (Format)obj;

        return isEqual(getMimeType(), otherFormat.getMimeType()) &&
                isEqual(getSchema(), otherFormat.getSchema()) &&
                isEqual(getEncoding(), otherFormat.getEncoding());
    }

    private boolean isEqual(String str1, String str2){
        return (str1 == null ? str2 == null : str1.equals(str2));
    }
}
