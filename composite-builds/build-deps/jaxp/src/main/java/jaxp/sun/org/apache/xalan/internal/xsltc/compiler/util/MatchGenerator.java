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
 * $Id: MatchGenerator.java,v 1.2.4.1 2005/09/05 11:15:21 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.ILOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ISTORE;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.Type;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class MatchGenerator extends MethodGenerator {
    private static int CURRENT_INDEX = 1;

    private int _iteratorIndex = INVALID_INDEX;

    private final Instruction _iloadCurrent;
    private final Instruction _istoreCurrent;
    private Instruction _aloadDom;

    public MatchGenerator(int access_flags, jaxp.sun.org.apache.bcel.internal.generic.Type return_type,
                          Type[] arg_types, String[] arg_names,
                          String method_name, String class_name,
                          InstructionList il, ConstantPoolGen cp) {
        super(access_flags, return_type, arg_types, arg_names, method_name,
              class_name, il, cp);

        _iloadCurrent = new ILOAD(CURRENT_INDEX);
        _istoreCurrent = new ISTORE(CURRENT_INDEX);
    }

    public Instruction loadCurrentNode() {
        return _iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        return _istoreCurrent;
    }

    public int getHandlerIndex() {
        return INVALID_INDEX;           // not available
    }

    /**
     * Get index of the register where the DOM is stored.
     */
    public Instruction loadDOM() {
        return _aloadDom;
    }

    /**
     * Set index where the reference to the DOM is stored.
     */
    public void setDomIndex(int domIndex) {
        _aloadDom = new ALOAD(domIndex);
    }

    /**
     * Get index of the register where the current iterator is stored.
     */
    public int getIteratorIndex() {
        return _iteratorIndex;
    }

    /**
     * Set index of the register where the current iterator is stored.
     */
    public void setIteratorIndex(int iteratorIndex) {
        _iteratorIndex = iteratorIndex;
    }

    public int getLocalIndex(String name) {
        if (name.equals("current")) {
            return CURRENT_INDEX;
        }
        return super.getLocalIndex(name);
    }
}
