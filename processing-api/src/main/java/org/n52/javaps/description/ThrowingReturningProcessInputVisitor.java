package org.n52.javaps.description;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface ThrowingReturningProcessInputVisitor<T, X extends Exception> {
    T visit(BoundingBoxInputDescription input)
            throws X;

    T visit(ComplexInputDescription input)
            throws X;

    T visit(LiteralInputDescription input)
            throws X;

    T visit(GroupInputDescription input)
            throws X;

}
