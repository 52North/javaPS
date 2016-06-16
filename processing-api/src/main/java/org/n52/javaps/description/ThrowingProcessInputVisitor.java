package org.n52.javaps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ThrowingProcessInputVisitor<X extends Exception> {
    void visit(BoundingBoxInputDescription input)
            throws X;

    void visit(ComplexInputDescription input)
            throws X;

    void visit(LiteralInputDescription input)
            throws X;

    void visit(GroupInputDescription input)
            throws X;

}
