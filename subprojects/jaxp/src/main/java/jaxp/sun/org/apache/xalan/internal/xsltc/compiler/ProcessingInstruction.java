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
 * $Id: ProcessingInstruction.java,v 1.2.4.1 2005/09/12 11:03:05 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import jaxp.sun.org.apache.xml.internal.utils.XML11Char;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class ProcessingInstruction extends Instruction {

    private AttributeValue _name; // name treated as AVT (7.1.3)
    private boolean _isLiteral = false;  // specified name is not AVT

    public void parseContents(Parser parser) {
        final String name  = getAttribute("name");

        if (name.length() > 0) {
            _isLiteral = Util.isLiteral(name);
            if (_isLiteral) {
                if (!XML11Char.isXML11ValidNCName(name)) {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_NCNAME_ERR, name, this);
                    parser.reportError(ERROR, err);
                }
            }
            _name = AttributeValue.create(this, name, parser);
        }
        else
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");

        if (name.equals("xml")) {
            reportError(this, parser, ErrorMsg.ILLEGAL_PI_ERR, "xml");
        }
        parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        _name.typeCheck(stable);
        typeCheckContents(stable);
        return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (!_isLiteral) {
            // if the ncname is an AVT, then the ncname has to be checked at runtime if it is a valid ncname
            LocalVariableGen nameValue =
                    methodGen.addLocalVariable2("nameValue",
            Util.getJCRefType(STRING_SIG),
                                                null);

            // store the name into a variable first so _name.translate only needs to be called once
            _name.translate(classGen, methodGen);
            nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
            il.append(new ALOAD(nameValue.getIndex()));

            // call checkNCName if the name is an AVT
            final int check = cpg.addMethodref(BASIS_LIBRARY_CLASS, "checkNCName",
                                "("
                                +STRING_SIG
                                +")V");
                                il.append(new INVOKESTATIC(check));

            // Save the current handler base on the stack
            il.append(methodGen.loadHandler());
            il.append(DUP);     // first arg to "attributes" call

            // load name value again
            nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
        } else {
            // Save the current handler base on the stack
            il.append(methodGen.loadHandler());
            il.append(DUP);     // first arg to "attributes" call

            // Push attribute name
            _name.translate(classGen, methodGen);// 2nd arg

        }

        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
                                               "stringValueHandler",
                                               STRING_VALUE_HANDLER_SIG)));
        il.append(DUP);
        il.append(methodGen.storeHandler());

        // translate contents with substituted handler
        translateContents(classGen, methodGen);

        // get String out of the handler
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_VALUE_HANDLER,
                                                     "getValueOfPI",
                                                     "()" + STRING_SIG)));
        // call "processingInstruction"
        final int processingInstruction =
            cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
                                      "processingInstruction",
                                      "(" + STRING_SIG + STRING_SIG + ")V");
        il.append(new INVOKEINTERFACE(processingInstruction, 3));
        // Restore old handler base from stack
        il.append(methodGen.storeHandler());
    }
}
