/**
 * Copyright (c) 2012-2013 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
package com.github.anba.es6draft.compiler;

import static com.github.anba.es6draft.semantics.StaticSemantics.BoundNames;
import static com.github.anba.es6draft.semantics.StaticSemantics.LexicallyDeclaredNames;
import static com.github.anba.es6draft.semantics.StaticSemantics.LexicallyScopedDeclarations;
import static com.github.anba.es6draft.semantics.StaticSemantics.VarScopedDeclarations;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import com.github.anba.es6draft.ast.Declaration;
import com.github.anba.es6draft.ast.FunctionDeclaration;
import com.github.anba.es6draft.ast.GeneratorDeclaration;
import com.github.anba.es6draft.ast.Script;
import com.github.anba.es6draft.ast.StatementListItem;
import com.github.anba.es6draft.ast.VariableStatement;
import com.github.anba.es6draft.compiler.InstructionVisitor.MethodDesc;
import com.github.anba.es6draft.compiler.InstructionVisitor.MethodType;

/**
 * <h1>10 Executable Code and Execution Contexts</h1><br>
 * <h2>10.5 Declaration Binding Instantiation</h2>
 * <ul>
 * <li>10.5.5 Eval Declaration Instantiation
 * </ul>
 */
class EvalDeclarationInstantiationGenerator extends DeclarationBindingInstantiationGenerator {
    private static class InternMethods {
        static final MethodDesc ScriptRuntime_bindingNotPresentOrThrow = MethodDesc.create(
                MethodType.Static, Types.ScriptRuntime, "bindingNotPresentOrThrow", Type
                        .getMethodType(Type.VOID_TYPE, Types.Realm, Types.EnvironmentRecord,
                                Types.String));
    }

    EvalDeclarationInstantiationGenerator(CodeGenerator codegen) {
        super(codegen);
    }

    private static final int REALM = 0;
    private static final int LEX_ENV = 1;
    private static final int VAR_ENV = 2;
    private static final int DELETABLE_BINDINGS = 3;

    private static final String methodName = "script_evalinit";
    private static final Type methodType = Type.getMethodType(Type.VOID_TYPE, Types.Realm,
            Types.LexicalEnvironment, Types.LexicalEnvironment, Type.BOOLEAN_TYPE);

    void generate(Script evalScript) {
        InstructionVisitor init = codegen.publicStaticMethod(methodName, methodType);
        init.begin();
        generate(evalScript, init);
        init.areturn();
        init.end();
    }

    private void generate(Script evalScript, InstructionVisitor mv) {
        // FIXME: spec incomplete (using modified ES5.1 algorithm for now...)

        int realm = REALM;
        int lexEnv = LEX_ENV;
        int varEnv = VAR_ENV;
        int deletableBindings = DELETABLE_BINDINGS;

        int env = varEnv;
        int envRec = mv.newVariable(Types.EnvironmentRecord);
        mv.load(env, Types.LexicalEnvironment);
        mv.invoke(Methods.LexicalEnvironment_getEnvRec);
        mv.store(envRec, Types.EnvironmentRecord);

        int lexEnvRec = mv.newVariable(Types.EnvironmentRecord);
        mv.load(lexEnv, Types.LexicalEnvironment);
        mv.invoke(Methods.LexicalEnvironment_getEnvRec);
        mv.store(lexEnvRec, Types.EnvironmentRecord);

        // begin-modification
        for (String name : LexicallyDeclaredNames(evalScript)) {
            mv.load(realm, Types.Realm);
            mv.load(lexEnvRec, Types.EnvironmentRecord);
            mv.aconst(name);
            mv.invoke(InternMethods.ScriptRuntime_bindingNotPresentOrThrow);
        }
        // end-modification

        /* step 1-2 (not applicable) */
        /* step 3 */
        boolean strict = evalScript.isStrict();
        /* step 4 (not applicable) */
        /* step 5 */
        List<StatementListItem> varDeclarations = VarScopedDeclarations(evalScript);
        for (StatementListItem item : varDeclarations) {
            if (item instanceof FunctionDeclaration || item instanceof GeneratorDeclaration) {
                Declaration f = (Declaration) item;
                String fn = BoundName(f);

                // stack: [] -> [fo]
                if (f instanceof GeneratorDeclaration) {
                    InstantiateGeneratorObject(realm, env, (GeneratorDeclaration) f, mv);
                } else {
                    InstantiateFunctionObject(realm, env, (FunctionDeclaration) f, mv);
                }

                hasBinding(envRec, fn, mv);

                Label funcAlreadyDeclared = new Label();
                mv.ifne(funcAlreadyDeclared);
                createMutableBinding(envRec, fn, deletableBindings, mv);
                mv.mark(funcAlreadyDeclared);
                // omitted else-case

                setMutableBinding(envRec, fn, strict, mv);
            }
        }
        /* step 6-7 (not applicable) */
        /* step 8 */
        for (StatementListItem d : varDeclarations) {
            if (d instanceof VariableStatement) {
                for (String dn : BoundNames(d)) {
                    hasBinding(envRec, dn, mv);

                    Label varAlreadyDeclared = new Label();
                    mv.ifne(varAlreadyDeclared);
                    createMutableBinding(envRec, dn, deletableBindings, mv);
                    mv.get(Fields.Undefined_UNDEFINED);
                    setMutableBinding(envRec, dn, strict, mv);
                    mv.mark(varAlreadyDeclared);
                }
            }
        }

        // begin-modification
        for (Declaration d : LexicallyScopedDeclarations(evalScript)) {
            for (String dn : BoundNames(d)) {
                if (d.isConstDeclaration()) {
                    createImmutableBinding(lexEnvRec, dn, mv);
                } else {
                    createMutableBinding(lexEnvRec, dn, false, mv);
                }
            }
        }
        // end-modification
    }
}