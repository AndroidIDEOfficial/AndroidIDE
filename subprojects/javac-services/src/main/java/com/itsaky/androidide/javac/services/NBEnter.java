/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

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
package com.itsaky.androidide.javac.services;

import openjdk.tools.javac.code.Symbol.TypeSymbol;
import openjdk.tools.javac.code.Symtab;
import openjdk.tools.javac.comp.AttrContext;
import openjdk.tools.javac.comp.Enter;
import openjdk.tools.javac.comp.Env;
import openjdk.tools.javac.main.JavaCompiler;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.tree.JCTree.JCClassDecl;
import openjdk.tools.javac.tree.TreeInfo;
import openjdk.tools.javac.util.Context;

/**
 * @author lahvac
 */
public class NBEnter extends Enter {

  private final CancelService cancelService;
  private final Symtab syms;
  private final NBJavaCompiler compiler;

  public static void preRegister(Context context) {
    context.put(enterKey, (Context.Factory<Enter>) NBEnter::new);
  }

  public NBEnter(Context context) {
    super(context);
    cancelService = CancelService.instance(context);
    syms = Symtab.instance(context);
    JavaCompiler c = JavaCompiler.instance(context);
    compiler = c instanceof NBJavaCompiler ? (NBJavaCompiler) c : null;
  }

  @SuppressWarnings("unused")
  public void doUnenter(JCTree.JCCompilationUnit cu, JCTree tree) {
    super.unenter(cu, tree);
  }

  @Override
  public Env<AttrContext> getEnv(TypeSymbol sym) {
    Env<AttrContext> env = super.getEnv(sym);
    if (compiler != null) {
      compiler.maybeInvokeDesugarCallback(env);
    }
    return env;
  }

  @Override
  public void visitTopLevel(JCTree.JCCompilationUnit tree) {
    if (TreeInfo.isModuleInfo(tree) && tree.modle == syms.noModule) {
      // workaround: when source level == 8, then visitTopLevel crashes for module-info.java
      return;
    }
    super.visitTopLevel(tree);
  }

  @Override
  public void visitClassDef(JCClassDecl tree) {
    cancelService.abortIfCanceled();
    super.visitClassDef(tree);
  }
}
