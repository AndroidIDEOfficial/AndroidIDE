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

import openjdk.source.util.TreePath;
import openjdk.tools.javac.api.JavacTrees;
import openjdk.tools.javac.comp.MemberEnter;
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit;
import openjdk.tools.javac.tree.JCTree.JCImport;
import openjdk.tools.javac.tree.JCTree.JCMethodDecl;
import openjdk.tools.javac.tree.JCTree.JCVariableDecl;
import openjdk.tools.javac.util.Context;

/**
 * @author lahvac
 */
public class NBMemberEnter extends MemberEnter {

  private final CancelService cancelService;
  private final JavacTrees trees;
  private final boolean backgroundScan;

  public NBMemberEnter(Context context, boolean backgroundScan) {
    super(context);
    cancelService = CancelService.instance(context);
    trees = JavacTrees.instance(context);
    this.backgroundScan = backgroundScan;
  }

  public static void preRegister(Context context, boolean backgroundScan) {
    context.put(
        MemberEnter.class,
        (Context.Factory<MemberEnter>) c -> new NBMemberEnter(c, backgroundScan));
  }

  @Override
  public void visitTopLevel(JCCompilationUnit tree) {
    cancelService.abortIfCanceled();
    super.visitTopLevel(tree);
  }

  @Override
  public void visitImport(JCImport tree) {
    cancelService.abortIfCanceled();
    super.visitImport(tree);
  }

  @Override
  public void visitMethodDef(JCMethodDecl tree) {
    cancelService.abortIfCanceled();
    super.visitMethodDef(tree);
    if (!backgroundScan && trees instanceof NBJavacTrees && !env.enclClass.defs.contains(tree)) {
      TreePath path = trees.getPath(env.toplevel, env.enclClass);
      if (path != null) {
        ((NBJavacTrees) trees).addPathForElement(tree.sym, new TreePath(path, tree));
      }
    }
  }

  @Override
  public void visitVarDef(JCVariableDecl tree) {
    cancelService.abortIfCanceled();
    super.visitVarDef(tree);
  }
}
