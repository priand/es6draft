/**
 * Copyright (c) 2012-2013 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.ast;

/**
 * <h1>12 Statements and Declarations</h1>
 * <ul>
 * <li>12.14 The try Statement
 * </ul>
 */
public class CatchNode extends AstNode implements ScopedNode {
    private BlockScope scope;
    private Binding catchParameter;
    private BlockStatement catchBlock;

    public CatchNode(BlockScope scope, Binding catchParameter, BlockStatement catchBlock) {
        this.scope = scope;
        this.catchParameter = catchParameter;
        this.catchBlock = catchBlock;
    }

    @Override
    public BlockScope getScope() {
        return scope;
    }

    public Binding getCatchParameter() {
        return catchParameter;
    }

    public BlockStatement getCatchBlock() {
        return catchBlock;
    }

    @Override
    public <R, V> R accept(NodeVisitor<R, V> visitor, V value) {
        return visitor.visit(this, value);
    }
}