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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import openjdk.tools.javac.code.Type;
import openjdk.tools.javac.comp.Attr;
import openjdk.tools.javac.comp.AttrContext;
import openjdk.tools.javac.comp.Env;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.tree.JCTree.JCBlock;
import openjdk.tools.javac.tree.JCTree.JCCatch;
import openjdk.tools.javac.tree.JCTree.JCClassDecl;
import openjdk.tools.javac.tree.JCTree.JCExpression;
import openjdk.tools.javac.tree.JCTree.JCMethodDecl;
import openjdk.tools.javac.tree.JCTree.JCVariableDecl;
import openjdk.tools.javac.tree.TreeMaker;
import openjdk.tools.javac.util.Context;
import openjdk.tools.javac.util.List;

/**
 * @author lahvac
 */
public class NBAttr extends Attr {

  private final CancelService cancelService;
  private final TreeMaker tm;
  private boolean fullyAttribute;
  private Env<AttrContext> fullyAttributeResult;

  public static void preRegister(Context context) {
    context.put(attrKey, (Context.Factory<Attr>) NBAttr::new);
  }

  public NBAttr(Context context) {
    super(context);
    cancelService = CancelService.instance(context);
    tm = TreeMaker.instance(context);
  }

  @Override
  public void visitCatch(JCCatch that) {
    super.visitBlock(tm.Block(0, List.of(that.param, that.body)));
  }

  public Env<AttrContext> attributeAndCapture(JCTree tree, Env<AttrContext> env, JCTree to) {
    try {
      fullyAttribute = true;

      Env<AttrContext> result =
          tree instanceof JCExpression
              ? attribExprToTree((JCExpression) tree, env, to)
              : attribStatToTree(tree, env, to);

      return fullyAttributeResult != null ? fullyAttributeResult : result;
    } finally {
      fullyAttribute = false;
    }
  }

  protected void breakTreeFound(Env<AttrContext> env) {
    if (fullyAttribute) {
      fullyAttributeResult = env;
    } else {
      try {
        MethodHandles.lookup()
            .findSpecial(
                Attr.class,
                "breakTreeFound",
                MethodType.methodType(void.class, Env.class),
                NBAttr.class)
            .invokeExact(this, env);
      } catch (Throwable ex) {
        sneakyThrows(ex);
      }
    }
  }

  private <T extends Throwable> void sneakyThrows(Throwable t) throws T {
    throw (T) t;
  }

  protected void breakTreeFound(Env<AttrContext> env, Type result) {
    if (fullyAttribute) {
      fullyAttributeResult = env;
    } else {
      try {
        MethodHandles.lookup()
            .findSpecial(
                Attr.class,
                "breakTreeFound",
                MethodType.methodType(void.class, Env.class, Type.class),
                NBAttr.class)
            .invokeExact(this, env, result);
      } catch (Throwable ex) {
        sneakyThrows(ex);
      }
    }
  }

  @Override
  public Type attribType(JCTree tree, Env<AttrContext> env) {
    cancelService.abortIfCanceled();
    return super.attribType(tree, env);
  }

  @Override
  public void visitClassDef(JCClassDecl tree) {
    cancelService.abortIfCanceled();
    super.visitClassDef(tree);
  }

  @Override
  public void visitMethodDef(JCMethodDecl tree) {
    cancelService.abortIfCanceled();
    super.visitMethodDef(tree);
  }

  @Override
  public void visitVarDef(JCVariableDecl tree) {
    // for erroneous "var", make sure the synthetic make.Error() has an invalid/synthetic position:
    tm.at(-1);
    super.visitVarDef(tree);
  }

  @Override
  public void visitBlock(JCBlock tree) {
    cancelService.abortIfCanceled();
    super.visitBlock(tree);
  }
}
