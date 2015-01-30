/**
 * Copyright (c) 2012-2015 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.runtime.types.builtins;

import com.github.anba.es6draft.runtime.ExecutionContext;
import com.github.anba.es6draft.runtime.LexicalEnvironment;
import com.github.anba.es6draft.runtime.Realm;
import com.github.anba.es6draft.runtime.internal.RuntimeInfo;
import com.github.anba.es6draft.runtime.types.Constructor;
import com.github.anba.es6draft.runtime.types.Intrinsics;
import com.github.anba.es6draft.runtime.types.ScriptObject;

/**
 * <h1>9 Ordinary and Exotic Objects Behaviours</h1>
 * <ul>
 * <li>9.2 ECMAScript Function Objects
 * </ul>
 */
public final class OrdinaryConstructorFunction extends OrdinaryFunction implements Constructor {
    /**
     * Constructs a new Constructor Function object.
     * 
     * @param realm
     *            the realm object
     */
    public OrdinaryConstructorFunction(Realm realm) {
        super(realm);
    }

    @Override
    protected OrdinaryConstructorFunction allocateNew() {
        return FunctionAllocate(getRealm().defaultContext(), getPrototype(), isStrict(),
                getFunctionKind(), getConstructorKind());
    }

    /**
     * 9.2.3 [[Construct]] (argumentsList)
     */
    @Override
    public ScriptObject construct(ExecutionContext callerContext, Constructor newTarget,
            Object... argumentsList) {
        try {
            return (ScriptObject) getConstructMethod().invokeExact(this, callerContext, newTarget,
                    argumentsList);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 9.2.3 [[Construct]] (argumentsList)
     */
    @Override
    public Object tailConstruct(ExecutionContext callerContext, Constructor newTarget,
            Object... argumentsList) throws Throwable {
        return getTailConstructMethod().invokeExact(this, callerContext, newTarget, argumentsList);
    }

    /* ***************************************************************************************** */

    /**
     * 9.2.4 FunctionAllocate (functionPrototype, strict) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param functionPrototype
     *            the function prototype
     * @param strict
     *            the strict mode flag
     * @param functionKind
     *            the function kind
     * @param constructorKind
     *            the constructor kind
     * @return the new function object
     */
    public static OrdinaryConstructorFunction FunctionAllocate(ExecutionContext cx,
            ScriptObject functionPrototype, boolean strict, FunctionKind functionKind,
            ConstructorKind constructorKind) {
        assert (functionKind == FunctionKind.Normal || functionKind == FunctionKind.ConstructorMethod);
        Realm realm = cx.getRealm();
        /* steps 1-5 (implicit) */
        /* steps 6-11 */
        OrdinaryConstructorFunction f = new OrdinaryConstructorFunction(realm);
        /* steps 12-16 */
        f.allocate(realm, functionPrototype, strict, functionKind, constructorKind);
        /* step 17 */
        return f;
    }

    /**
     * 9.2.6 FunctionCreate (kind, ParameterList, Body, Scope, Strict) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param kind
     *            the function kind
     * @param function
     *            the function code
     * @param scope
     *            the lexical environment
     * @return the new function object
     */
    public static OrdinaryConstructorFunction ConstructorFunctionCreate(ExecutionContext cx,
            FunctionKind kind, RuntimeInfo.Function function, LexicalEnvironment<?> scope) {
        return ConstructorFunctionCreate(cx, kind, function, scope, null);
    }

    /**
     * 9.2.6 FunctionCreate (kind, ParameterList, Body, Scope, Strict) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param kind
     *            the function kind
     * @param function
     *            the function code
     * @param scope
     *            the lexical environment
     * @param functionPrototype
     *            the function prototype
     * @return the new function object
     */
    public static OrdinaryConstructorFunction ConstructorFunctionCreate(ExecutionContext cx,
            FunctionKind kind, RuntimeInfo.Function function, LexicalEnvironment<?> scope,
            ScriptObject functionPrototype) {
        assert !function.isGenerator() && !function.isAsync();
        OrdinaryObject intrinsicFunctionPrototype = cx.getIntrinsic(Intrinsics.FunctionPrototype);
        /* step 1 */
        if (functionPrototype == null) {
            functionPrototype = intrinsicFunctionPrototype;
        }
        ConstructorKind constructorKind;
        if (functionPrototype == intrinsicFunctionPrototype) {
            constructorKind = ConstructorKind.Base;
        } else {
            constructorKind = ConstructorKind.Derived;
        }
        /* step 2 */
        OrdinaryConstructorFunction f = FunctionAllocate(cx, functionPrototype,
                function.isStrict(), kind, constructorKind);
        /* step 3 */
        FunctionInitialize(f, kind, function.isStrict(), function, scope, cx.getCurrentExecutable());
        return f;
    }
}