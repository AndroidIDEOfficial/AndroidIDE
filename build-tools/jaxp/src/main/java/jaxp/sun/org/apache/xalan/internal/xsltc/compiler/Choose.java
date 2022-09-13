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
 * $Id: Choose.java,v 1.2.4.1 2005/09/01 12:00:14 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.GOTO;
import jaxp.sun.org.apache.bcel.internal.generic.IFEQ;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class Choose extends Instruction {

    /**
     * Display the element contents (a lot of when's and an otherwise)
     */
    public void display(int indent) {
        indent(indent);
        Util.println("Choose");
        indent(indent + IndentIncrement);
        displayContents(indent + IndentIncrement);
    }

    /**
     * Translate this Choose element. Generate a test-chain for the various
     * <xsl:when> elements and default to the <xsl:otherwise> if present.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final Vector whenElements = new Vector();
        Otherwise otherwise = null;
        Enumeration elements = elements();

        // These two are for reporting errors only
        ErrorMsg error = null;
        final int line = getLineNumber();

        // Traverse all child nodes - must be either When or Otherwise
        while (elements.hasMoreElements()) {
            Object element = elements.nextElement();
            // Add a When child element
            if (element instanceof When) {
                whenElements.addElement(element);
            }
            // Add an Otherwise child element
            else if (element instanceof Otherwise) {
                if (otherwise == null) {
                    otherwise = (Otherwise)element;
                }
                else {
                    error = new ErrorMsg(ErrorMsg.MULTIPLE_OTHERWISE_ERR, this);
                    getParser().reportError(ERROR, error);
                }
            }
            else if (element instanceof Text) {
                ((Text)element).ignore();
            }
            // It is an error if we find some other element here
            else {
                error = new ErrorMsg(ErrorMsg.WHEN_ELEMENT_ERR, this);
                getParser().reportError(ERROR, error);
            }
        }

        // Make sure that there is at least one <xsl:when> element
        if (whenElements.size() == 0) {
            error = new ErrorMsg(ErrorMsg.MISSING_WHEN_ERR, this);
            getParser().reportError(ERROR, error);
            return;
        }

        InstructionList il = methodGen.getInstructionList();

        // next element will hold a handle to the beginning of next
        // When/Otherwise if test on current When fails
        BranchHandle nextElement = null;
        Vector exitHandles = new Vector();
        InstructionHandle exit = null;

        Enumeration whens = whenElements.elements();
        while (whens.hasMoreElements()) {
            final When when = (When)whens.nextElement();
            final Expression test = when.getTest();

            InstructionHandle truec = il.getEnd();

            if (nextElement != null)
                nextElement.setTarget(il.append(NOP));
            test.translateDesynthesized(classGen, methodGen);

            if (test instanceof FunctionCall) {
                FunctionCall call = (FunctionCall)test;
                try {
                    Type type = call.typeCheck(getParser().getSymbolTable());
                    if (type != Type.Boolean) {
                        test._falseList.add(il.append(new IFEQ(null)));
                    }
                }
                catch (TypeCheckError e) {
                    // handled later!
                }
            }
            // remember end of condition
            truec = il.getEnd();

            // The When object should be ignored completely in case it tests
            // for the support of a non-available element
            if (!when.ignore()) when.translateContents(classGen, methodGen);

            // goto exit after executing the body of when
            exitHandles.addElement(il.append(new GOTO(null)));
            if (whens.hasMoreElements() || otherwise != null) {
                nextElement = il.append(new GOTO(null));
                test.backPatchFalseList(nextElement);
            }
            else
                test.backPatchFalseList(exit = il.append(NOP));
            test.backPatchTrueList(truec.getNext());
        }

        // Translate any <xsl:otherwise> element
        if (otherwise != null) {
            nextElement.setTarget(il.append(NOP));
            otherwise.translateContents(classGen, methodGen);
            exit = il.append(NOP);
        }

        // now that end is known set targets of exit gotos
        Enumeration exitGotos = exitHandles.elements();
        while (exitGotos.hasMoreElements()) {
            BranchHandle gotoExit = (BranchHandle)exitGotos.nextElement();
            gotoExit.setTarget(exit);
        }
    }
}
