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

import openjdk.tools.javac.code.Symbol;
import openjdk.tools.javac.code.Symbol.TypeSymbol;
import openjdk.tools.javac.code.Type;
import openjdk.tools.javac.comp.AttrContext;
import openjdk.tools.javac.comp.Env;
import openjdk.tools.javac.comp.Resolve;
import openjdk.tools.javac.util.Context;

/**
 * @author lahvac
 */
public class NBResolve extends Resolve {
  private boolean accessibleOverride;

  public static NBResolve instance(Context context) {
    Resolve instance = context.get(resolveKey);
    if (instance == null) instance = new NBResolve(context);
    return (NBResolve) instance;
  }

  public static void preRegister(Context context) {
    context.put(resolveKey, (Context.Factory<Resolve>) NBResolve::new);
  }

  protected NBResolve(Context ctx) {
    super(ctx);
  }

  public static boolean isStatic(Env<AttrContext> env) {
    return Resolve.isStatic(env);
  }

  public void disableAccessibilityChecks() {
    accessibleOverride = true;
  }

  public void restoreAccessbilityChecks() {
    accessibleOverride = false;
  }

  @Override
  public boolean isAccessible(Env<AttrContext> env, TypeSymbol c, boolean checkInner) {
    if (accessibleOverride) return true;
    return super.isAccessible(env, c, checkInner);
  }

  @Override
  public boolean isAccessible(Env<AttrContext> env, Type site, Symbol sym, boolean checkInner) {
    if (accessibleOverride) return true;
    return super.isAccessible(env, site, sym, checkInner);
  }
}
