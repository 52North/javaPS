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
package org.n52.javaps.io.literal.xsd;

import java.math.BigInteger;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralNegativeIntegerType extends AbstractLiteralBigIntegerType {

    private static final long serialVersionUID = -7090104568566863296L;

    @Override
    public String getName() {
        return NEGATIVE_INTEGER;
    }

    @Override
    protected BigInteger min() {
        return null;
    }

    @Override
    protected BigInteger max() {
        return BigInteger.valueOf(-1);
    }

}
