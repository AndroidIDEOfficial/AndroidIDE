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
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 * @author Tomas Zezula
 */
public final class NBLog extends Log {

    private static final String ERR_NOT_IN_PROFILE = "not.in.profile";  //NOI18N

    private final Map<URI,Collection<Symbol.ClassSymbol>> notInProfiles =
            new HashMap<>();

    private JavaFileObject partialReparseFile;
    private Set<Integer> seenPartialReparsePositions = new HashSet<>();

    private NBLog(
            final Context context,
            final PrintWriter output) {
        super(context, output);
    }

    public static NBLog instance(Context context) {
        final Log log = Log.instance(context);
        if (!(log instanceof NBLog)) {
            throw new InternalError("No NBLog instance!"); //NOI18N
        }
        return (NBLog) log;
    }
    
    public static void preRegister(Context context,
                                   final PrintWriter errWriter,
                                   final PrintWriter warnWriter,
                                   final PrintWriter noticeWriter) {
        context.put(logKey, new Context.Factory<Log>() {
            @Override
            public Log make(Context c) {
                return new NBLog(
                    c,
                    errWriter);
            }
        });
    }

    public static void preRegister(Context context,
                                   final PrintWriter output) {
        context.put(logKey, new Context.Factory<Log>() {
            @Override
            public Log make(Context c) {
                return new NBLog(
                    c,
                    output);
            }
        });
    }

    @Override
    public void report(JCDiagnostic diagnostic) {
        //XXX: needs testing!
        if (diagnostic.getKind() == Diagnostic.Kind.ERROR &&
            ERR_NOT_IN_PROFILE.equals(diagnostic.getCode())) {
            final JavaFileObject currentFile = currentSourceFile();
            if (currentFile != null) {
                final URI uri = currentFile.toUri();
                Symbol.ClassSymbol type = (Symbol.ClassSymbol) diagnostic.getArgs()[0];
                Collection<Symbol.ClassSymbol> types = notInProfiles.get(uri);
                if (types == null) {
                    types = new ArrayList<>();
                    notInProfiles.put(uri,types);
                }
                types.add(type);
            }
        }
        super.report(diagnostic);
    }

    @Override
    protected boolean shouldReport(JavaFileObject file, int pos) {
        if (partialReparseFile != null) {
            return file == partialReparseFile && seenPartialReparsePositions.add(pos);
        } else {
            return super.shouldReport(file, pos);
        }
    }

    Collection<? extends Symbol.ClassSymbol> removeNotInProfile(final URI uri) {
        return uri == null ? null : notInProfiles.remove(uri);
    }

    @Override
    protected int getDefaultMaxWarnings() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getDefaultMaxErrors() {
        return Integer.MAX_VALUE;
    }

    public void startPartialReparse(JavaFileObject inFile) {
        partialReparseFile = inFile;
    }
    
    public void endPartialReparse(JavaFileObject inFile) {
        partialReparseFile = null;
        seenPartialReparsePositions.clear(); //TODO: not tested
    }
}
