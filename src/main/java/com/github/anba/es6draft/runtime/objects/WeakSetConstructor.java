/**
 * Copyright (c) 2012-2013 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.runtime.objects;

import static com.github.anba.es6draft.runtime.AbstractOperations.Get;
import static com.github.anba.es6draft.runtime.AbstractOperations.IsCallable;
import static com.github.anba.es6draft.runtime.AbstractOperations.OrdinaryCreateFromConstructor;
import static com.github.anba.es6draft.runtime.internal.Errors.throwTypeError;
import static com.github.anba.es6draft.runtime.internal.Properties.createProperties;
import static com.github.anba.es6draft.runtime.objects.iteration.IterationAbstractOperations.GetIterator;
import static com.github.anba.es6draft.runtime.objects.iteration.IterationAbstractOperations.IteratorComplete;
import static com.github.anba.es6draft.runtime.objects.iteration.IterationAbstractOperations.IteratorNext;
import static com.github.anba.es6draft.runtime.objects.iteration.IterationAbstractOperations.IteratorValue;
import static com.github.anba.es6draft.runtime.types.Undefined.UNDEFINED;
import static com.github.anba.es6draft.runtime.types.builtins.OrdinaryFunction.AddRestrictedFunctionProperties;
import static com.github.anba.es6draft.runtime.types.builtins.OrdinaryFunction.OrdinaryConstruct;

import com.github.anba.es6draft.runtime.ExecutionContext;
import com.github.anba.es6draft.runtime.Realm;
import com.github.anba.es6draft.runtime.internal.Initialisable;
import com.github.anba.es6draft.runtime.internal.Messages;
import com.github.anba.es6draft.runtime.internal.ObjectAllocator;
import com.github.anba.es6draft.runtime.internal.Properties.Attributes;
import com.github.anba.es6draft.runtime.internal.Properties.Function;
import com.github.anba.es6draft.runtime.internal.Properties.Prototype;
import com.github.anba.es6draft.runtime.internal.Properties.Value;
import com.github.anba.es6draft.runtime.types.BuiltinSymbol;
import com.github.anba.es6draft.runtime.types.Callable;
import com.github.anba.es6draft.runtime.types.Constructor;
import com.github.anba.es6draft.runtime.types.Intrinsics;
import com.github.anba.es6draft.runtime.types.ScriptObject;
import com.github.anba.es6draft.runtime.types.Type;
import com.github.anba.es6draft.runtime.types.builtins.BuiltinFunction;

/**
 * <h1>15 Standard Built-in ECMAScript Objects</h1><br>
 * <h2>15.17 WeakSet Objects</h2>
 * <ul>
 * <li>15.17.1 The WeakSet Constructor
 * <li>15.17.2 Properties of the WeakSet Constructor
 * </ul>
 */
public class WeakSetConstructor extends BuiltinFunction implements Constructor, Initialisable {
    public WeakSetConstructor(Realm realm) {
        super(realm);
    }

    @Override
    public void initialise(ExecutionContext cx) {
        createProperties(this, cx, Properties.class);
        AddRestrictedFunctionProperties(cx, this);
    }

    /**
     * 15.17.1.1 WeakSet (iterable = undefined)
     */
    @Override
    public Object call(ExecutionContext callerContext, Object thisValue, Object... args) {
        ExecutionContext calleeContext = calleeContext();
        Object iterable = args.length > 0 ? args[0] : UNDEFINED;

        /* steps 1-4 */
        if (!Type.isObject(thisValue)) {
            // FIXME: spec bug ? `WeakSet()` no longer allowed (Bug 1406)
            throw throwTypeError(calleeContext, Messages.Key.NotObjectType);
        }
        if (!(thisValue instanceof WeakSetObject)) {
            throw throwTypeError(calleeContext, Messages.Key.IncompatibleObject);
        }
        WeakSetObject set = (WeakSetObject) thisValue;
        if (set.isInitialised()) {
            throw throwTypeError(calleeContext, Messages.Key.IncompatibleObject);
        }

        /* steps 5-7 */
        Object iter, adder = null;
        if (Type.isUndefinedOrNull(iterable)) {
            iter = UNDEFINED;
        } else {
            iter = GetIterator(calleeContext, iterable);
            adder = Get(calleeContext, set, "add");
            if (!IsCallable(adder)) {
                throw throwTypeError(calleeContext, Messages.Key.NotCallable);
            }
        }

        /* step 8 */
        set.initialise();

        /* step 9 */
        if (Type.isUndefined(iter)) {
            return set;
        }
        /* step 10 */
        assert iter instanceof ScriptObject;
        ScriptObject iterator = (ScriptObject) iter;
        for (;;) {
            ScriptObject next = IteratorNext(calleeContext, iterator);
            boolean done = IteratorComplete(calleeContext, next);
            if (done) {
                return set;
            }
            Object nextValue = IteratorValue(calleeContext, next);
            ((Callable) adder).call(calleeContext, set, nextValue);
        }
    }

    /**
     * 15.17.1.2 new WeakSet (...argumentsList)
     */
    @Override
    public ScriptObject construct(ExecutionContext callerContext, Object... args) {
        return OrdinaryConstruct(callerContext, this, args);
    }

    /**
     * 15.17.2 Properties of the WeakSet Constructor
     */
    public enum Properties {
        ;

        @Prototype
        public static final Intrinsics __proto__ = Intrinsics.FunctionPrototype;

        @Value(name = "length", attributes = @Attributes(writable = false, enumerable = false,
                configurable = false))
        public static final int length = 0;

        @Value(name = "name", attributes = @Attributes(writable = false, enumerable = false,
                configurable = false))
        public static final String name = "WeakSet";

        /**
         * 15.17.2.1 WeakSet.prototype
         */
        @Value(name = "prototype", attributes = @Attributes(writable = false, enumerable = false,
                configurable = false))
        public static final Intrinsics prototype = Intrinsics.WeakSetPrototype;

        /**
         * 15.17.2.2 WeakSet[ @@create ] ( )
         */
        @Function(name = "@@create", symbol = BuiltinSymbol.create, arity = 0,
                attributes = @Attributes(writable = false, enumerable = false, configurable = true))
        public static Object create(ExecutionContext cx, Object thisValue) {
            return OrdinaryCreateFromConstructor(cx, thisValue, Intrinsics.WeakSetPrototype,
                    WeakSetObjectAllocator.INSTANCE);
        }
    }

    private static class WeakSetObjectAllocator implements ObjectAllocator<WeakSetObject> {
        static final ObjectAllocator<WeakSetObject> INSTANCE = new WeakSetObjectAllocator();

        @Override
        public WeakSetObject newInstance(Realm realm) {
            return new WeakSetObject(realm);
        }
    }
}