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

import java.util.HashMap;
import java.util.Map;
import jdkx.lang.model.element.Element;
import openjdk.source.tree.VariableTree;
import openjdk.source.util.TreePath;
import openjdk.tools.javac.api.JavacTrees;
import openjdk.tools.javac.code.Flags;
import openjdk.tools.javac.code.Symbol;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.tree.JCTree.JCVariableDecl;
import openjdk.tools.javac.tree.TreeInfo;
import openjdk.tools.javac.tree.TreeMaker;
import openjdk.tools.javac.util.Context;

/**
 * @author lahvac
 */
public class NBJavacTrees extends JavacTrees {

  private final Map<Element, TreePath> element2paths = new HashMap<>();

  public static void preRegister(Context context) {
    context.put(JavacTrees.class, (Context.Factory<JavacTrees>) NBJavacTrees::new);
  }

  protected NBJavacTrees(Context context) {
    super(context);
  }

  @Override
  public TreePath getPath(Element e) {
    TreePath path = super.getPath(e);
    return path != null ? path : element2paths.get(e);
  }

  @Override
  public Symbol getElement(TreePath path) {
    return TreeInfo.symbolFor((JCTree) path.getLeaf());
  }

  @Override
  protected Copier createCopier(TreeMaker maker) {
    return new Copier(maker) {
      @Override
      public JCTree visitVariable(VariableTree node, JCTree p) {
        JCVariableDecl old = (JCVariableDecl) node;
        JCVariableDecl nue = (JCVariableDecl) super.visitVariable(node, p);
        if (old.sym != null) {
          nue.mods.flags |= old.sym.flags_field & Flags.EFFECTIVELY_FINAL;
        }
        return nue;
      }
    };
  }

  void addPathForElement(Element elem, TreePath path) {
    element2paths.put(elem, path);
  }
}
