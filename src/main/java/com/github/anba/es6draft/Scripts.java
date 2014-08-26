/**
 * Copyright (c) 2012-2014 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft;

import static com.github.anba.es6draft.runtime.ExecutionContext.newScriptExecutionContext;

import com.github.anba.es6draft.runtime.ExecutionContext;
import com.github.anba.es6draft.runtime.GlobalEnvironmentRecord;
import com.github.anba.es6draft.runtime.LexicalEnvironment;
import com.github.anba.es6draft.runtime.Realm;
import com.github.anba.es6draft.runtime.internal.RuntimeInfo;

/**
 * <h1>15 ECMAScript Language: Scripts and Modules</h1>
 * <ul>
 * <li>15.1 Scripts
 * </ul>
 */
public final class Scripts {
    private Scripts() {
    }

    /**
     * [15.1.7 Runtime Semantics: Script Evaluation]
     * 
     * @param script
     *            the script object
     * @param realm
     *            the realm instance
     * @param deletableBindings
     *            the deletableBindings flag
     * @return the script evaluation result
     */
    public static Object ScriptEvaluation(Script script, Realm realm, boolean deletableBindings) {
        /* steps 1-2 */
        RuntimeInfo.ScriptBody scriptBody = script.getScriptBody();
        /* step 3 */
        LexicalEnvironment<GlobalEnvironmentRecord> globalEnv = realm.getGlobalEnv();
        /* steps 6-10 (moved) */
        ExecutionContext scriptCxt = newScriptExecutionContext(realm, script);
        ExecutionContext oldScriptContext = realm.getScriptContext();
        try {
            realm.setScriptContext(scriptCxt);
            /* steps 4-5 */
            scriptBody.globalDeclarationInstantiation(scriptCxt, globalEnv, globalEnv,
                    deletableBindings);
            /* steps 11-16 */
            Object result = script.evaluate(scriptCxt);
            /* step 17 */
            return result;
        } finally {
            realm.setScriptContext(oldScriptContext);
        }
    }
}
