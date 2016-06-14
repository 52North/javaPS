/*
 * Copyright (C) 2013-2015 Christian Autermann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.n52.javaps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessInputDescription extends DataDescription {

    default BoundingBoxInputDescription asBoundingBox() {
        throw new UnsupportedOperationException();
    }

    default ComplexInputDescription asComplex() {
        throw new UnsupportedOperationException();
    }

    default LiteralInputDescription asLiteral() {
        throw new UnsupportedOperationException();
    }

    InputOccurence getOccurence();

    <T> T visit(ReturningVisitor<T> visitor);

    default void visit(Visitor visitor) {
        visit(new ReturningVisitor<Void>() {
            @Override
            public Void visit(BoundingBoxInputDescription input) {
                visitor.visit(input);
                return null;
            }

            @Override
            public Void visit(ComplexInputDescription input) {
                visitor.visit(input);
                return null;
            }

            @Override
            public Void visit(LiteralInputDescription input) {
                visitor.visit(input);
                return null;
            }
        });
    }

    interface Visitor {
        void visit(BoundingBoxInputDescription input);

        void visit(ComplexInputDescription input);

        void visit(LiteralInputDescription input);
    }

    interface ReturningVisitor<T> {
        T visit(BoundingBoxInputDescription input);

        T visit(ComplexInputDescription input);

        T visit(LiteralInputDescription input);
    }

}
