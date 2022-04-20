/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.lib.nbjavac.services;

import com.sun.tools.javac.code.ClassFinder;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds.Kind;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.Completer;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticInfo;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticType;
import com.sun.tools.javac.util.JCDiagnostic.SimpleDiagnosticPosition;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Names;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaFileObject;

/**
 *
 * @author lahvac
 */
public class NBClassFinder extends ClassFinder {

    public static void preRegister(Context context) {
        context.put(classFinderKey, new Context.Factory<ClassFinder>() {
            public ClassFinder make(Context c) {
                return new NBClassFinder(c);
            }
        });
    }

    private final Context context;
    private final Names names;
    private final JCDiagnostic.Factory diagFactory;
    private final Log log;

    public NBClassFinder(Context context) {
        super(context);
        this.context = context;
        this.names = Names.instance(context);
        this.diagFactory = JCDiagnostic.Factory.instance(context);
        this.log = Log.instance(context);
    }

    @Override
    protected JavaFileObject preferredFileObject(JavaFileObject a, JavaFileObject b) {
        if (b.getName().toLowerCase().endsWith(".sig")) {
            //do not prefer sources over sig files (unless sources are newer):
            boolean prevPreferSource = preferSource;
            try {
                preferSource = false;
                return super.preferredFileObject(a, b);
            } finally {
                preferSource = prevPreferSource;
            }
        }
        return super.preferredFileObject(a, b);
    }

    private Completer completer;

    @Override
    public Completer getCompleter() {
        if (completer == null) {
            try {
                Class.forName("com.sun.tools.javac.model.LazyTreeLoader");
                //patched nb-javac, handles missing java.lang itself:
                completer = super.getCompleter();
            } catch (ClassNotFoundException e) {
                Completer delegate = super.getCompleter();
                completer = sym -> {
                    delegate.complete(sym);
                    if (sym.kind == Kind.PCK &&
                        sym.flatName() == names.java_lang &&
                        sym.members().isEmpty()) {
                        sym.flags_field |= Flags.EXISTS;
                        try {
                            Class<?> dcfhClass = Class.forName("com.sun.tools.javac.code.DeferredCompletionFailureHandler");
                            Constructor<CompletionFailure> constr = CompletionFailure.class.getDeclaredConstructor(Symbol.class, Supplier.class, dcfhClass);
                            Object dcfh = dcfhClass.getDeclaredMethod("instance", Context.class).invoke(null, context);
                            throw constr.newInstance(sym, (Supplier<JCDiagnostic>) () -> {
                                return diagFactory.create(log.currentSource(), new SimpleDiagnosticPosition(0), DiagnosticInfo.of(DiagnosticType.ERROR, "compiler", "cant.resolve", "package", "java.lang"));
                            }, dcfh);
                        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                            Logger.getLogger(NBClassFinder.class.getName()).log(Level.FINE, null, ex);
                        }
                    }
                };
            }
        }
        return completer;
    }

}
