/**
 * Copyright (c) 2012-2015 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.ast;

import java.util.EnumSet;
import java.util.Set;

import com.github.anba.es6draft.ast.scope.BlockScope;

/**
 * <h1>13 ECMAScript Language: Statements and Declarations</h1><br>
 * <h2>13.6 Iteration Statements</h2>
 * <ul>
 * <li>Extension: 'for-each' statement
 * </ul>
 */
public final class ForEachStatement extends IterationStatement implements ForIterationNode {
    private final BlockScope scope;
    private final Node head;
    private final Expression expression;
    private Statement statement;

    public ForEachStatement(long beginPosition, long endPosition, BlockScope scope,
            EnumSet<Abrupt> abrupt, Set<String> labelSet, Node head, Expression expression,
            Statement statement) {
        super(beginPosition, endPosition, abrupt, labelSet);
        this.scope = scope;
        this.head = head;
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public BlockScope getScope() {
        return scope;
    }

    /**
     * Returns the <tt>for each</tt>-statement's head node. The head node is one of the following
     * types:
     * <ul>
     * <li>{@link VariableStatement}:&emsp;{@code for each (var decl in expr)}
     * <li>{@link LexicalDeclaration}:&emsp;{@code for each (let/const decl in expr)}
     * <li>{@link LeftHandSideExpression}:&emsp;{@code for each (lhs in expr)}
     * </ul>
     * 
     * @return the head node
     */
    @Override
    public Node getHead() {
        return head;
    }

    /**
     * Returns the <tt>for each</tt>-statement's expression node.
     * 
     * @return the expression node
     */
    @Override
    public Expression getExpression() {
        return expression;
    }

    /**
     * Returns the <tt>for each</tt>-statement's statement node.
     * 
     * @return the statement node
     */
    @Override
    public Statement getStatement() {
        return statement;
    }

    /**
     * Sets the <tt>for each</tt>-statement's statement node.
     * 
     * @param statement
     *            the new statement node
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public <R, V> R accept(NodeVisitor<R, V> visitor, V value) {
        return visitor.visit(this, value);
    }

    @Override
    public <V> int accept(IntNodeVisitor<V> visitor, V value) {
        return visitor.visit(this, value);
    }

    @Override
    public <V> void accept(VoidNodeVisitor<V> visitor, V value) {
        visitor.visit(this, value);
    }
}
