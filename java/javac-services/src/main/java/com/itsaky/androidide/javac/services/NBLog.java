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

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jdkx.tools.Diagnostic;
import jdkx.tools.JavaFileObject;
import openjdk.tools.javac.code.Symbol;
import openjdk.tools.javac.util.Context;
import openjdk.tools.javac.util.JCDiagnostic;
import openjdk.tools.javac.util.Log;

/**
 * @author Tomas Zezula
 */
public class NBLog extends Log {

  private static final String ERR_NOT_IN_PROFILE = "not.in.profile"; // NOI18N

  private final Map<URI, Collection<Symbol.ClassSymbol>> notInProfiles = new HashMap<>();

  private JavaFileObject partialReparseFile;
  private final Set<Integer> seenPartialReparsePositions = new HashSet<>();

  protected NBLog(final Context context, final PrintWriter output) {
    super(context, output);
  }

  public static NBLog instance(Context context) {
    final Log log = Log.instance(context);
    if (!(log instanceof NBLog)) {
      throw new InternalError("No NBLog instance!"); // NOI18N
    }
    return (NBLog) log;
  }

  public static void preRegister(Context context, final PrintWriter output) {
    context.put(logKey, (Context.Factory<Log>) c -> new NBLog(c, output));
  }

  public void startPartialReparse(JavaFileObject inFile) {
    partialReparseFile = inFile;
  }

  public void endPartialReparse(JavaFileObject inFile) {
    partialReparseFile = null;
    seenPartialReparsePositions.clear(); // TODO: not tested
  }

  @Override
  protected int getDefaultMaxErrors() {
    return Integer.MAX_VALUE;
  }

  @Override
  protected int getDefaultMaxWarnings() {
    return Integer.MAX_VALUE;
  }

  @Override
  protected boolean shouldReport(JavaFileObject file, int pos) {
    if (partialReparseFile != null) {
      return file == partialReparseFile && seenPartialReparsePositions.add(pos);
    } else {
      return super.shouldReport(file, pos);
    }
  }

  @Override
  public void report(JCDiagnostic diagnostic) {
    // XXX: needs testing!
    if (diagnostic.getKind() == Diagnostic.Kind.ERROR
        && ERR_NOT_IN_PROFILE.equals(diagnostic.getCode())) {
      final JavaFileObject currentFile = currentSourceFile();
      if (currentFile != null) {
        final URI uri = currentFile.toUri();
        Symbol.ClassSymbol type = (Symbol.ClassSymbol) diagnostic.getArgs()[0];
        Collection<Symbol.ClassSymbol> types =
            notInProfiles.computeIfAbsent(uri, k -> new ArrayList<>());
        types.add(type);
      }
    }
    super.report(diagnostic);
  }

  Collection<? extends Symbol.ClassSymbol> removeNotInProfile(final URI uri) {
    return uri == null ? null : notInProfiles.remove(uri);
  }
}
