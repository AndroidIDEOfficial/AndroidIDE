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
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: NodeSortRecordGenerator.java,v 1.2.4.1 2005/09/05 11:23:42 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

/**
 *
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class NodeSortRecordGenerator extends ClassGenerator {
    private static final int TRANSLET_INDEX = 4;   // translet
    private final Instruction _aloadTranslet;

    public NodeSortRecordGenerator(String className, String superClassName,
                                   String fileName,
                                   int accessFlags, String[] interfaces,
                                   Stylesheet stylesheet) {
        super(className, superClassName, fileName,
              accessFlags, interfaces, stylesheet);
        _aloadTranslet = new ALOAD(TRANSLET_INDEX);
    }

    /**
     * The index of the translet pointer within the execution of
     * the test method.
     */
    public Instruction loadTranslet() {
        return _aloadTranslet;
    }

    /**
     * Returns <tt>true</tt> since this class is external to the
     * translet.
     */
    public boolean isExternal() {
        return true;
    }

}
