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
 * $Id: DocumentCall.java,v 1.2.4.1 2005/09/01 14:10:13 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Morten Jorgensen
 */
final class DocumentCall extends FunctionCall {

    private Expression _arg1 = null;
    private Expression _arg2 = null;
    private Type _arg1Type;

    /**
     * Default function call constructor
     */
    public DocumentCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    /**
     * Type checks the arguments passed to the document() function. The first
     * argument can be any type (we must cast it to a string) and contains the
     * URI of the document
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        // At least one argument - two at most
        final int ac = argumentCount();
        if ((ac < 1) || (ac > 2)) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }
        if (getStylesheet() == null) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }

        // Parse the first argument
        _arg1 = argument(0);

        if (_arg1 == null) {// should not happened
            ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
            throw new TypeCheckError(msg);
        }

        _arg1Type = _arg1.typeCheck(stable);
        if ((_arg1Type != Type.NodeSet) && (_arg1Type != Type.String)) {
            _arg1 = new CastExpr(_arg1, Type.String);
        }

        // Parse the second argument
        if (ac == 2) {
            _arg2 = argument(1);

            if (_arg2 == null) {// should not happened
                ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
                throw new TypeCheckError(msg);
            }

            final Type arg2Type = _arg2.typeCheck(stable);

            if (arg2Type.identicalTo(Type.Node)) {
                _arg2 = new CastExpr(_arg2, Type.NodeSet);
            } else if (arg2Type.identicalTo(Type.NodeSet)) {
                // falls through
            } else {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.DOCUMENT_ARG_ERR, this);
                throw new TypeCheckError(msg);
            }
        }

        return _type = Type.NodeSet;
    }

    /**
     * Translates the document() function call to a call to LoadDocument()'s
     * static method document().
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        final int ac = argumentCount();

        final int domField = cpg.addFieldref(classGen.getClassName(),
                                             DOM_FIELD,
                                             DOM_INTF_SIG);

        String docParamList = null;
        if (ac == 1) {
           // documentF(Object,String,AbstractTranslet,DOM)
           docParamList = "("+OBJECT_SIG+STRING_SIG+TRANSLET_SIG+DOM_INTF_SIG
                         +")"+NODE_ITERATOR_SIG;
        } else { //ac == 2; ac < 1 or as >2  was tested in typeChec()
           // documentF(Object,DTMAxisIterator,String,AbstractTranslet,DOM)
           docParamList = "("+OBJECT_SIG+NODE_ITERATOR_SIG+STRING_SIG
                         +TRANSLET_SIG+DOM_INTF_SIG+")"+NODE_ITERATOR_SIG;
        }
        final int docIdx = cpg.addMethodref(LOAD_DOCUMENT_CLASS, "documentF",
                                            docParamList);


        // The URI can be either a node-set or something else cast to a string
        _arg1.translate(classGen, methodGen);
        if (_arg1Type == Type.NodeSet) {
            _arg1.startIterator(classGen, methodGen);
        }

        if (ac == 2) {
            //_arg2 == null was tested in typeChec()
            _arg2.translate(classGen, methodGen);
            _arg2.startIterator(classGen, methodGen);
        }

        // Feck the rest of the parameters on the stack
        il.append(new PUSH(cpg, getStylesheet().getSystemId()));
        il.append(classGen.loadTranslet());
        il.append(DUP);
        il.append(new GETFIELD(domField));
        il.append(new INVOKESTATIC(docIdx));
    }

}
