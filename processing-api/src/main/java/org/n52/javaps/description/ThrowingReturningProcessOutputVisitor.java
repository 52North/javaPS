package org.n52.javaps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ThrowingReturningProcessOutputVisitor<T, X extends Exception> {
    T visit(BoundingBoxOutputDescription output)
            throws X;

    T visit(ComplexOutputDescription output)
            throws X;

    T visit(LiteralOutputDescription output)
            throws X;

    T visit(GroupOutputDescription output)
            throws X;

}
