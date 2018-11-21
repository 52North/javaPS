/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.stream;

import javax.xml.namespace.QName;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
// TODO merge into W3CConstants
public interface XLinkConstants {
    String NS_XLINK = "http://www.w3.org/1999/xlink";

    String NS_XLINK_PREFIX = "xlink";

    interface Attr {
        String AN_HREF = "href";

        QName QN_HREF = xlink(AN_HREF);

        String AN_ROLE = "role";

        QName QN_ROLE = xlink(AN_ROLE);

        String AN_ARCROLE = "arcrole";

        QName QN_ARCROLE = xlink(AN_ARCROLE);

        String AN_TITLE = "title";

        QName QN_TITLE = xlink(AN_TITLE);

        String AN_SHOW = "show";

        QName QN_SHOW = xlink(AN_SHOW);

        String AN_ACTUATE = "actuate";

        QName QN_ACTUATE = xlink(AN_ACTUATE);
    }

    static QName xlink(String element) {
        return new QName(NS_XLINK, element, NS_XLINK_PREFIX);
    }

}
