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

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;

/**
 *
 * @author lahvac
 */
public class NBEnter extends Enter {

    public static void preRegister(Context context) {
        context.put(enterKey, new Context.Factory<Enter>() {
            public Enter make(Context c) {
                return new NBEnter(c);
            }
        });
    }

    private final CancelService cancelService;
    private final Symtab syms;
    private final NBJavaCompiler compiler;

    public NBEnter(Context context) {
        super(context);
        cancelService = CancelService.instance(context);
        syms = Symtab.instance(context);
        JavaCompiler c = JavaCompiler.instance(context);
        compiler = c instanceof NBJavaCompiler ? (NBJavaCompiler) c : null;
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        cancelService.abortIfCanceled();
        super.visitClassDef(tree);
    }

    @Override
    public void visitTopLevel(JCTree.JCCompilationUnit tree) {
        if (TreeInfo.isModuleInfo(tree) && tree.modle == syms.noModule) {
            //workaround: when source level == 8, then visitTopLevel crashes for module-info.java
            return ;
        }
        super.visitTopLevel(tree);
    }

    @Override
    public Env<AttrContext> getEnv(TypeSymbol sym) {
        Env<AttrContext> env = super.getEnv(sym);
        if (compiler != null) {
            compiler.maybeInvokeDesugarCallback(env);
        }
        return env;
    }

}
