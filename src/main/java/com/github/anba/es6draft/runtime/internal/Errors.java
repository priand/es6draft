/**
 * Copyright (c) 2012-2013 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.runtime.internal;

import static com.github.anba.es6draft.runtime.internal.ScriptRuntime._throw;

import java.text.MessageFormat;

import com.github.anba.es6draft.runtime.Realm;
import com.github.anba.es6draft.runtime.objects.NativeError;
import com.github.anba.es6draft.runtime.types.Constructor;
import com.github.anba.es6draft.runtime.types.Scriptable;

/**
 *
 */
public final class Errors {
    private Errors() {
    }

    private static Object newError(Realm realm, NativeError.ErrorType type, Messages.Key key) {
        String message = realm.message(key);
        Scriptable nativeError = realm.getNativeError(type);
        return ((Constructor) nativeError).construct(message);
    }

    private static Object newError(Realm realm, NativeError.ErrorType type, Messages.Key key,
            String... args) {
        MessageFormat format = new MessageFormat(realm.message(key), realm.getLocale());
        String message = format.format(args);
        Scriptable nativeError = realm.getNativeError(type);
        return ((Constructor) nativeError).construct(message);
    }

    public static ScriptException throwInternalError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.InternalError, key));
    }

    public static ScriptException throwInternalError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.InternalError, key, args));
    }

    public static ScriptException throwTypeError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.TypeError, key));
    }

    public static ScriptException throwTypeError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.TypeError, key, args));
    }

    public static ScriptException throwReferenceError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.ReferenceError, key));
    }

    public static ScriptException throwReferenceError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.ReferenceError, key, args));
    }

    public static ScriptException throwSyntaxError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.SyntaxError, key));
    }

    public static ScriptException throwSyntaxError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.SyntaxError, key, args));
    }

    public static ScriptException throwRangeError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.RangeError, key));
    }

    public static ScriptException throwRangeError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.RangeError, key, args));
    }

    public static ScriptException throwURIError(Realm realm, Messages.Key key) {
        return _throw(newError(realm, NativeError.ErrorType.URIError, key));
    }

    public static ScriptException throwURIError(Realm realm, Messages.Key key, String... args) {
        return _throw(newError(realm, NativeError.ErrorType.URIError, key, args));
    }
}