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

import openjdk.tools.javac.code.Symtab;
import openjdk.tools.javac.code.Types;
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit;
import openjdk.tools.javac.tree.TreeMaker;
import openjdk.tools.javac.util.Context;
import openjdk.tools.javac.util.Names;

/**
 * @author lahvac
 */
public class NBTreeMaker extends TreeMaker {

  private final Names names;
  private final Types types;
  private final Symtab syms;

  protected NBTreeMaker(JCCompilationUnit toplevel, Names names, Types types, Symtab syms) {
    super(toplevel, names, types, syms);
    this.names = names;
    this.types = types;
    this.syms = syms;
  }

  public static void preRegister(Context context) {
    context.put(treeMakerKey, (Context.Factory<TreeMaker>) NBTreeMaker::new);
  }

  protected NBTreeMaker(Context context) {
    super(context);
    this.names = Names.instance(context);
    this.types = Types.instance(context);
    this.syms = Symtab.instance(context);
  }

  @Override
  public TreeMaker forToplevel(JCCompilationUnit toplevel) {
    return new NBTreeMaker(toplevel, names, types, syms);
  }
}
