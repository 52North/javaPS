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
package org.n52.javaps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessOutputDescription extends DataDescription {

    default BoundingBoxOutputDescription asBoundingBox() {
        throw new UnsupportedOperationException();
    }

    default ComplexOutputDescription asComplex() {
        throw new UnsupportedOperationException();
    }

    default LiteralOutputDescription asLiteral() {
        throw new UnsupportedOperationException();
    }

    default GroupOutputDescription asGroup() {
        throw new UnsupportedOperationException();
    }

    <T> T visit(ReturningProcessOutputVisitor<T> visitor);

    default void visit(ProcessOutputVisitor visitor) {
        visit(new ReturningProcessOutputVisitor<Void>() {
            @Override
            public Void visit(BoundingBoxOutputDescription output) {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(ComplexOutputDescription output) {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(LiteralOutputDescription output) {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(GroupOutputDescription output) {
                visitor.visit(output);
                return null;
            }
        });
    }

    <T, X extends Exception> T visit(ThrowingReturningProcessOutputVisitor<T, X> visitor) throws X;

    default <X extends Exception> void visit(ThrowingProcessOutputVisitor<X> visitor) throws X {
        visit(new ThrowingReturningProcessOutputVisitor<Void, X>() {
            @Override
            public Void visit(BoundingBoxOutputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(ComplexOutputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(LiteralOutputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(GroupOutputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }
        });
    }
}
