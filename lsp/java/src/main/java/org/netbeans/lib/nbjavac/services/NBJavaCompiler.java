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

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.function.Consumer;

/**
 *
 * @author lahvac
 */
public class NBJavaCompiler extends JavaCompiler {

    public static void preRegister(Context context) {
        context.put(compilerKey, new Context.Factory<JavaCompiler>() {
            public JavaCompiler make(Context c) {
                return new NBJavaCompiler(c);
            }
        });
    }

    private final CancelService cancelService;
    private Consumer<Env<AttrContext>> desugarCallback;

    public NBJavaCompiler(Context context) {
        super(context);
        cancelService = CancelService.instance(context);
    }

    @Override
    public void processAnnotations(List<JCTree.JCCompilationUnit> roots, Collection<String> classnames) {
        if (roots.isEmpty()) {
            super.processAnnotations(roots, classnames);
        } else {
            setOrigin(roots.head.sourcefile.toUri().toString());
            try {
                super.processAnnotations(roots, classnames);
            } finally {
                setOrigin("");
            }
        }
    }

    private void setOrigin(String origin) {
        fileManager.handleOption("apt-origin", Collections.singletonList(origin).iterator());
    }

    public void setDesugarCallback(Consumer<Env<AttrContext>> callback) {
        this.desugarCallback = callback;
    }

    private boolean desugaring;

    @Override
    protected void desugar(Env<AttrContext> env, Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> results) {
        boolean prevDesugaring = desugaring;
        try {
            desugaring = true;
        super.desugar(env, results);
        } finally {
            desugaring = prevDesugaring;
        }
    }

    void maybeInvokeDesugarCallback(Env<AttrContext> env) {
        if (desugaring && desugarCallback != null) {
            desugarCallback.accept(env);
        }
    }

}
