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
 * $Id: Comment.java,v 1.2.4.1 2005/09/01 12:02:37 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class Comment extends Instruction {

    public void parseContents(Parser parser) {
        parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        typeCheckContents(stable);
        return Type.String;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // Shortcut for literal strings
        Text rawText = null;
        if (elementCount() == 1) {
            Object content = elementAt(0);
            if (content instanceof Text) {
                rawText = (Text) content;
            }
        }

        // If the content is literal text, call comment(char[],int,int) or
        // comment(String), as appropriate.  Otherwise, use a
        // StringValueHandler to gather the textual content of the xsl:comment
        // and call comment(String) with the result.
        if (rawText != null) {
            il.append(methodGen.loadHandler());

            if (rawText.canLoadAsArrayOffsetLength()) {
                rawText.loadAsArrayOffsetLength(classGen, methodGen);
                final int comment =
                        cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
                                                  "comment",
                                                  "([CII)V");
                il.append(new INVOKEINTERFACE(comment, 4));
            } else {
                il.append(new PUSH(cpg, rawText.getText()));
                final int comment =
                        cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
                                                  "comment",
                                                  "(" + STRING_SIG + ")V");
                il.append(new INVOKEINTERFACE(comment, 2));
            }
        } else {
            // Save the current handler base on the stack
            il.append(methodGen.loadHandler());
            il.append(DUP);             // first arg to "comment" call

            // Get the translet's StringValueHandler
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
                                                         "getValue",
                                                         "()" + STRING_SIG)));
            // call "comment"
            final int comment =
                        cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
                                                  "comment",
                                                  "(" + STRING_SIG + ")V");
            il.append(new INVOKEINTERFACE(comment, 2));
            // Restore old handler base from stack
            il.append(methodGen.storeHandler());
        }
    }
}
