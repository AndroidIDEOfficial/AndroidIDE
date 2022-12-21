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
 * $Id: TestGenerator.java,v 1.2.4.1 2005/09/05 11:36:49 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.ILOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ISTORE;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.Type;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class TestGenerator extends MethodGenerator {
    private static int CONTEXT_NODE_INDEX = 1;
    private static int CURRENT_NODE_INDEX = 4;
    private static int ITERATOR_INDEX = 6;

    private Instruction _aloadDom;
    private final Instruction _iloadCurrent;
    private final Instruction _iloadContext;
    private final Instruction _istoreCurrent;
    private final Instruction _istoreContext;
    private final Instruction _astoreIterator;
    private final Instruction _aloadIterator;

    public TestGenerator(int access_flags, jaxp.sun.org.apache.bcel.internal.generic.Type return_type,
                         Type[] arg_types, String[] arg_names,
                         String method_name, String class_name,
                         InstructionList il, ConstantPoolGen cp) {
        super(access_flags, return_type, arg_types, arg_names, method_name,
              class_name, il, cp);

        _iloadCurrent  = new ILOAD(CURRENT_NODE_INDEX);
        _istoreCurrent = new ISTORE(CURRENT_NODE_INDEX);
        _iloadContext  = new ILOAD(CONTEXT_NODE_INDEX);
        _istoreContext  = new ILOAD(CONTEXT_NODE_INDEX);
        _astoreIterator = new ASTORE(ITERATOR_INDEX);
        _aloadIterator  = new ALOAD(ITERATOR_INDEX);
    }

    public int getHandlerIndex() {
        return INVALID_INDEX;           // not available
    }

    public int getIteratorIndex() {
        return ITERATOR_INDEX;          // not available
    }

    public void setDomIndex(int domIndex) {
        _aloadDom = new ALOAD(domIndex);
    }

    public Instruction loadDOM() {
        return _aloadDom;
    }

    public Instruction loadCurrentNode() {
        return _iloadCurrent;
    }

    /** by default context node is the same as current node. MK437 */
    public Instruction loadContextNode() {
        return _iloadContext;
    }

    public Instruction storeContextNode() {
        return _istoreContext;
    }

    public Instruction storeCurrentNode() {
        return _istoreCurrent;
    }

    public Instruction storeIterator() {
        return _astoreIterator;
    }

    public Instruction loadIterator() {
        return _aloadIterator;
    }

    public int getLocalIndex(String name) {
        if (name.equals("current")) {
            return CURRENT_NODE_INDEX;
        }
        else {
            return super.getLocalIndex(name);
        }
    }
}
