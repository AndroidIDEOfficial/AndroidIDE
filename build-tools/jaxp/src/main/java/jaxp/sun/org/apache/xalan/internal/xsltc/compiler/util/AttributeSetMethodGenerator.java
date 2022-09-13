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
 * $Id: AttributeSetMethodGenerator.java,v 1.5 2005/09/28 13:48:24 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.Constants;
import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.Type;

/**
 * @author Santiago Pericas-Geertsen
 */
public final class AttributeSetMethodGenerator extends MethodGenerator {

    protected static final int CURRENT_INDEX  = 4;
    private static final int PARAM_START_INDEX = 5;

    private static final String[] argNames = new String[4];
    private static final jaxp.sun.org.apache.bcel.internal.generic.Type[] argTypes =
        new jaxp.sun.org.apache.bcel.internal.generic.Type[4];

    static {
        argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
        argTypes[1] = Util.getJCRefType(NODE_ITERATOR_SIG);
        argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
        argTypes[3] = jaxp.sun.org.apache.bcel.internal.generic.Type.INT;
        argNames[0] = DOCUMENT_PNAME;
        argNames[1] = ITERATOR_PNAME;
        argNames[2] = TRANSLET_OUTPUT_PNAME;
        argNames[3] = NODE_PNAME;
    }

   public AttributeSetMethodGenerator(String methodName, ClassGenerator classGen) {
        super(Constants.ACC_PRIVATE,
              Type.VOID,
              argTypes, argNames, methodName,
              classGen.getClassName(),
              new InstructionList(),
              classGen.getConstantPool());
   }

    public int getLocalIndex(String name) {
        if (name.equals("current")) {
            return CURRENT_INDEX;
        }
        return super.getLocalIndex(name);
    }

    public Instruction loadParameter(int index) {
        return new ALOAD(index + PARAM_START_INDEX);
    }

    public Instruction storeParameter(int index) {
        return new ASTORE(index + PARAM_START_INDEX);
    }
}
