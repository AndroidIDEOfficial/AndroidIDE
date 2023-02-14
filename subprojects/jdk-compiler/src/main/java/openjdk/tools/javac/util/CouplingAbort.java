/*
 * Copyright (c) 1999, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package openjdk.tools.javac.util;

import openjdk.source.tree.Tree;
import openjdk.tools.javac.code.Symbol.ClassSymbol;
import jdkx.tools.JavaFileObject;

/**
 *
 * @author jlahoda
 */
public class CouplingAbort extends Abort {

    private JavaFileObject classFile;
    private JavaFileObject sourceFile;
    private Tree t;

    /** Creates a new instance of CouplingAbort */
    public CouplingAbort(ClassSymbol clazz, Tree t) {
        this.classFile = clazz != null ? clazz.classfile : null;
        this.sourceFile = clazz != null ? clazz.sourcefile : null;
        this.t = t;

        wasCouplingError = true;
    }

    public JavaFileObject getClassFile() {
        return classFile;
    }

    public JavaFileObject getSourceFile() {
        return sourceFile;
    }

    public Tree getTree() {
        return t;
    }

    public static boolean wasCouplingError;

}
