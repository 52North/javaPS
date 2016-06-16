package org.n52.javaps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ThrowingProcessOutputVisitor<X extends Exception> {
    void visit(BoundingBoxOutputDescription output)
            throws X;

    void visit(ComplexOutputDescription output)
            throws X;

    void visit(LiteralOutputDescription output)
            throws X;

    void visit(GroupOutputDescription output)
            throws X;

}
