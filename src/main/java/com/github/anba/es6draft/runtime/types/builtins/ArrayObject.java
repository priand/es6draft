/**
 * Copyright (c) 2012-2015 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.runtime.types.builtins;

import static com.github.anba.es6draft.runtime.AbstractOperations.*;
import static com.github.anba.es6draft.runtime.internal.Errors.newRangeError;
import static com.github.anba.es6draft.runtime.internal.Errors.newTypeError;
import static com.github.anba.es6draft.runtime.types.Undefined.UNDEFINED;

import java.util.ArrayList;
import java.util.List;

import com.github.anba.es6draft.runtime.ExecutionContext;
import com.github.anba.es6draft.runtime.Realm;
import com.github.anba.es6draft.runtime.internal.Messages;
import com.github.anba.es6draft.runtime.types.BuiltinSymbol;
import com.github.anba.es6draft.runtime.types.Constructor;
import com.github.anba.es6draft.runtime.types.Intrinsics;
import com.github.anba.es6draft.runtime.types.Property;
import com.github.anba.es6draft.runtime.types.PropertyDescriptor;
import com.github.anba.es6draft.runtime.types.ScriptObject;
import com.github.anba.es6draft.runtime.types.Type;

/**
 * <h1>9 Ordinary and Exotic Objects Behaviours</h1><br>
 * <h2>9.4 Built-in Exotic Object Internal Methods and Data Fields</h2>
 * <ul>
 * <li>9.4.2 Array Exotic Objects
 * </ul>
 */
public final class ArrayObject extends OrdinaryObject {
    private boolean hasIndexedAccessors = false;
    private boolean lengthWritable = true;
    private long length = 0;

    /**
     * Constructs a new Array object.
     * 
     * @param realm
     *            the realm object
     */
    public ArrayObject(Realm realm) {
        super(realm);
    }

    /**
     * Returns the array's length.
     * 
     * @return the array's length
     */
    @Override
    public long getLength() {
        return length;
    }

    /**
     * Sets the array's length.
     * 
     * @param length
     *            the new array length
     */
    public void setLengthUnchecked(long length) {
        assert this.length <= length && lengthWritable;
        this.length = length;
    }

    /**
     * Returns {@code true} if the array has indexed accessors.
     * 
     * @return {@code true} if the array has indexed accessors
     */
    @Override
    public boolean hasIndexedAccessors() {
        return hasIndexedAccessors;
    }

    /**
     * 9.4.2 Array Exotic Objects
     * <p>
     * Introductory paragraph
     * 
     * @param p
     *            the property key
     * @return {@code true} if the property key is a valid array index
     */
    private static boolean isArrayIndex(long p) {
        return 0 <= p && p < 0xFFFF_FFFFL;
    }

    private boolean isCompatibleLengthProperty(PropertyDescriptor desc, long newLength) {
        if (desc.isEmpty()) {
            return true;
        }
        if (desc.isAccessorDescriptor() || desc.isConfigurable() || desc.isEnumerable()) {
            return false;
        }
        if (desc.isGenericDescriptor()) {
            return true;
        }
        assert desc.isDataDescriptor();
        if (!lengthWritable && (desc.isWritable() || (desc.hasValue() && newLength != length))) {
            return false;
        }
        return true;
    }

    private boolean defineLength(PropertyDescriptor desc, long newLength) {
        assert desc.hasValue() ? newLength >= 0 : newLength < 0;
        boolean succeeded = isCompatibleLengthProperty(desc, newLength);
        if (succeeded) {
            if (newLength >= 0) {
                length = newLength;
            }
            if (desc.hasWritable()) {
                lengthWritable = desc.isWritable();
            }
        }
        return succeeded;
    }

    private boolean defineLength(long newLength) {
        assert newLength >= 0;
        boolean succeeded = (lengthWritable || newLength == length);
        if (succeeded) {
            length = newLength;
        }
        return succeeded;
    }

    @Override
    protected boolean setPropertyValue(ExecutionContext cx, String propertyKey, Object value,
            Property current) {
        if ("length".equals(propertyKey)) {
            return ArraySetLength(cx, this, value);
        }
        return super.setPropertyValue(cx, propertyKey, value, current);
    }

    @Override
    protected boolean has(ExecutionContext cx, String propertyKey) {
        if ("length".equals(propertyKey)) {
            return true;
        }
        return super.has(cx, propertyKey);
    }

    @Override
    protected boolean hasOwnProperty(ExecutionContext cx, String propertyKey) {
        if ("length".equals(propertyKey)) {
            return true;
        }
        return super.hasOwnProperty(cx, propertyKey);
    }

    @Override
    protected Property getProperty(ExecutionContext cx, String propertyKey) {
        if ("length".equals(propertyKey)) {
            return new Property(length, lengthWritable, false, false);
        }
        return super.getProperty(cx, propertyKey);
    }

    @Override
    protected boolean deleteProperty(ExecutionContext cx, String propertyKey) {
        if ("length".equals(propertyKey)) {
            return false;
        }
        return super.deleteProperty(cx, propertyKey);
    }

    @Override
    protected List<Object> getOwnPropertyKeys(ExecutionContext cx) {
        int indexedSize = indexedProperties().size();
        int propertiesSize = properties().size();
        int symbolsSize = symbolProperties().size();
        int totalSize = indexedSize + propertiesSize + symbolsSize + 1; // + 1 for length property
        /* step 1 */
        ArrayList<Object> ownKeys = new ArrayList<>(totalSize);
        /* step 2 */
        if (indexedSize != 0) {
            ownKeys.addAll(indexedProperties().keys());
        }
        /* step 3 */
        ownKeys.add("length");
        if (propertiesSize != 0) {
            ownKeys.addAll(properties().keySet());
        }
        /* step 4 */
        if (symbolsSize != 0) {
            ownKeys.addAll(symbolProperties().keySet());
        }
        /* step 5 */
        return ownKeys;
    }

    /**
     * 9.4.2.1 [[DefineOwnProperty]] (P, Desc)
     */
    @Override
    protected boolean defineProperty(ExecutionContext cx, long propertyKey, PropertyDescriptor desc) {
        /* steps 1-2 (not applicable) */
        /* step 3 */
        if (isArrayIndex(propertyKey)) {
            /* steps 3.a-3.c */
            long oldLen = this.length;
            /* steps 3.d-3.e */
            long index = propertyKey;
            /* steps 3.f */
            if (index >= oldLen && !lengthWritable) {
                return false;
            }
            /* steps 3.g-3.h */
            boolean succeeded = ordinaryDefineOwnProperty(cx, propertyKey, desc);
            /* step 3.i */
            if (!succeeded) {
                return false;
            }
            /* step 3.j */
            if (index >= oldLen) {
                this.length = index + 1;
            }
            hasIndexedAccessors |= desc.isAccessorDescriptor();
            /* step 3.k */
            return true;
        }
        /* step 4 */
        return ordinaryDefineOwnProperty(cx, propertyKey, desc);
    }

    /**
     * 9.4.2.1 [[DefineOwnProperty]] (P, Desc)
     */
    @Override
    protected boolean defineProperty(ExecutionContext cx, String propertyKey,
            PropertyDescriptor desc) {
        /* steps 1, 3 (not applicable) */
        /* step 2 */
        if ("length".equals(propertyKey)) {
            return ArraySetLength(cx, this, desc);
        }
        /* step 4 */
        return ordinaryDefineOwnProperty(cx, propertyKey, desc);
    }

    /**
     * 9.4.2.2 ArrayCreate(length, proto) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param length
     *            the array length
     * @return the new array object
     */
    public static ArrayObject ArrayCreate(ExecutionContext cx, long length) {
        return ArrayCreate(cx, length, cx.getIntrinsic(Intrinsics.ArrayPrototype));
    }

    /**
     * 9.4.2.2 ArrayCreate(length, proto) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param length
     *            the array length
     * @param proto
     *            the prototype object
     * @return the new array object
     */
    public static ArrayObject ArrayCreate(ExecutionContext cx, long length, ScriptObject proto) {
        assert proto != null;
        /* steps 1-2 */
        assert length >= 0;
        /* step 3 */
        if (length > 0xFFFF_FFFFL) {
            // enforce array index invariant
            throw newRangeError(cx, Messages.Key.InvalidArrayLength);
        }
        /* step 4 (not applicable) */
        /* steps 5-7, 9 (implicit) */
        ArrayObject array = new ArrayObject(cx.getRealm());
        /* step 8 */
        array.setPrototype(proto);
        /* step 10 */
        array.length = length;
        /* step 11 */
        return array;
    }

    /**
     * Helper method to create dense arrays
     * <p>
     * [Called from generated code]
     * 
     * @param cx
     *            the execution context
     * @param values
     *            the element values
     * @return the new array object
     */
    public static ArrayObject DenseArrayCreate(ExecutionContext cx, Object[] values) {
        ArrayObject array = ArrayCreate(cx, values.length);
        for (int i = 0, len = values.length; i < len; ++i) {
            array.setIndexed(i, values[i]);
        }
        return array;
    }

    /**
     * Helper method to create sparse arrays
     * <p>
     * [Called from generated code]
     * 
     * @param cx
     *            the execution context
     * @param values
     *            the element values
     * @return the new array object
     */
    public static ArrayObject SparseArrayCreate(ExecutionContext cx, Object[] values) {
        ArrayObject array = ArrayCreate(cx, values.length);
        for (int i = 0, len = values.length; i < len; ++i) {
            if (values[i] != null) {
                array.setIndexed(i, values[i]);
            }
        }
        return array;
    }

    /**
     * 9.4.2.3 ArraySpeciesCreate(originalArray, length) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param orginalArray
     *            the source array
     * @param length
     *            the array length
     * @return the new array object
     */
    public static ScriptObject ArraySpeciesCreate(ExecutionContext cx, ScriptObject orginalArray,
            long length) {
        /* step 1 */
        assert length >= 0;
        /* step 2 (not applicable) */
        /* step 3 */
        Object c = UNDEFINED;
        /* step 4 */
        if (IsArray(orginalArray)) {
            /* steps 4.a-4.b */
            c = Get(cx, orginalArray, "constructor");
            /* step 4.c */
            if (IsConstructor(c)) {
                Constructor constructor = (Constructor) c;
                /* step 4.c.i */
                Realm thisRealm = cx.getRealm();
                /* step 4.c.ii */
                Realm realmC = constructor.getRealm(cx);
                /* step 4.c.iii */
                if (thisRealm != realmC && constructor == realmC.getIntrinsic(Intrinsics.Array)) {
                    c = UNDEFINED;
                }
            }
            /* step 4.d */
            if (Type.isObject(c)) {
                c = Get(cx, Type.objectValue(c), BuiltinSymbol.species.get());
            }
        }
        /* step 5 */
        if (Type.isUndefinedOrNull(c)) {
            return ArrayCreate(cx, length);
        }
        /* step 6 */
        if (!IsConstructor(c)) {
            throw newTypeError(cx, Messages.Key.NotConstructor);
        }
        /* step 7 */
        return ((Constructor) c).construct(cx, (Constructor) c, length);
    }

    /**
     * 9.4.2.4 ArraySetLength(A, Desc) Abstract Operation
     * 
     * @param cx
     *            the execution context
     * @param array
     *            the array object
     * @param desc
     *            the property descriptor
     * @return {@code true} on success
     */
    public static boolean ArraySetLength(ExecutionContext cx, ArrayObject array,
            PropertyDescriptor desc) {
        /* step 1 */
        if (!desc.hasValue()) {
            return array.defineLength(desc, -1);
        }
        /* step 2 */
        PropertyDescriptor newLenDesc = desc.clone();
        /* step 3 */
        long newLen = ToUint32(cx, desc.getValue());
        /* step 4 */
        if (newLen != ToNumber(cx, desc.getValue())) {
            throw newRangeError(cx, Messages.Key.InvalidArrayLength);
        }
        /* step 5 */
        newLenDesc.setValue(newLen);
        /* steps 6-8 (not applicable) */
        /* step 9 */
        long oldLen = array.length;
        /* step 10 */
        if (newLen >= oldLen) {
            return array.defineLength(newLenDesc, newLen);
        }
        /* step 11 */
        if (!array.lengthWritable) {
            return false;
        }
        /* steps 12-13 */
        boolean newWritable;
        if (!newLenDesc.hasWritable() || newLenDesc.isWritable()) {
            newWritable = true;
        } else {
            newWritable = false;
            newLenDesc.setWritable(true);
        }
        /* steps 14-15 */
        boolean succeeded = array.defineLength(newLenDesc, newLen);
        /* step 16 */
        if (!succeeded) {
            return false;
        }
        /* step 17 */
        long nonDeletableIndex = array.deleteRange(newLen, oldLen);
        /* step 17.d */
        if (nonDeletableIndex >= 0) {
            array.length = nonDeletableIndex + 1;
            if (!newWritable) {
                array.lengthWritable = false;
            }
            return false;
        }
        /* step 18 */
        if (!newWritable) {
            array.lengthWritable = false;
        }
        /* step 19 */
        return true;
    }

    private static boolean ArraySetLength(ExecutionContext cx, ArrayObject array, Object lenValue) {
        /* steps 1-2 (not applicable) */
        /* step 3 */
        long newLen = ToUint32(cx, lenValue);
        /* step 4 */
        if (newLen != ToNumber(cx, lenValue)) {
            throw newRangeError(cx, Messages.Key.InvalidArrayLength);
        }
        /* steps 5-8 (not applicable) */
        /* step 9 */
        long oldLen = array.length;
        /* step 10 */
        if (newLen >= oldLen) {
            return array.defineLength(newLen);
        }
        /* step 11 */
        if (!array.lengthWritable) {
            return false;
        }
        /* steps 12-13 (not applicable) */
        /* steps 14-15 */
        boolean succeeded = array.defineLength(newLen);
        /* step 16 */
        assert succeeded;
        /* step 17 */
        long nonDeletableIndex = array.deleteRange(newLen, oldLen);
        /* step 17.d */
        if (nonDeletableIndex >= 0) {
            array.length = nonDeletableIndex + 1;
            return false;
        }
        /* step 18 (not applicable) */
        /* step 19 */
        return true;
    }

    /**
     * Inserts the object into this array object.
     * 
     * @param index
     *            the destination start index
     * @param source
     *            the source object
     * @param sourceLength
     *            the source object's length
     */
    public void insertFrom(int index, OrdinaryObject source, long sourceLength) {
        assert isExtensible() && lengthWritable && index >= this.length;
        assert source.isDenseArray(sourceLength);
        assert 0 <= sourceLength && sourceLength <= Integer.MAX_VALUE : "length=" + sourceLength;
        assert index + sourceLength <= Integer.MAX_VALUE;
        int len = (int) sourceLength;
        for (int i = 0, j = index; i < len; ++i, ++j) {
            setIndexed(j, source.getIndexed(i));
        }
        this.length = index + len;
    }
}