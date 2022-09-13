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
 * $Id: StringType.java,v 1.2.4.1 2005/09/05 11:35:57 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GOTO;
import jaxp.sun.org.apache.bcel.internal.generic.IFEQ;
import jaxp.sun.org.apache.bcel.internal.generic.IFNONNULL;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public class StringType extends Type {
    protected StringType() {}

    public String toString() {
        return "string";
    }

    public boolean identicalTo(Type other) {
        return this == other;
    }

    public String toSignature() {
        return "Ljava/lang/String;";
    }

    public boolean isSimple() {
        return true;
    }

    public jaxp.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return jaxp.sun.org.apache.bcel.internal.generic.Type.STRING;
    }

    /**
     * Translates a string into an object of internal type <code>type</code>.
     * The translation to int is undefined since strings are always converted
     * to reals in arithmetic expressions.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            Type type) {
        if (type == Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
        }
        else if (type == Real) {
            translateTo(classGen, methodGen, (RealType) type);
        }
        else if (type == Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        }
        else if (type == ObjectString) {
            // NOP -> same representation
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), type.toString());
            classGen.getParser().reportError(Constants.FATAL, err);
        }
    }

    /**
     * Translates a string into a synthesized boolean.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            BooleanType type) {
        final InstructionList il = methodGen.getInstructionList();
        FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
        il.append(ICONST_1);
        final BranchHandle truec = il.append(new GOTO(null));
        falsel.backPatch(il.append(ICONST_0));
        truec.setTarget(il.append(NOP));
    }

    /**
     * Translates a string into a real by calling stringToReal() from the
     * basis library.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            RealType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(BASIS_LIBRARY_CLASS,
                                                    STRING_TO_REAL,
                                                    STRING_TO_REAL_SIG)));
    }

    /**
     * Translates a string into a non-synthesized boolean. It does not push a
     * 0 or a 1 but instead returns branchhandle list to be appended to the
     * false list.
     *
     * @see     Type#translateToDesynthesized
     */
    public FlowList translateToDesynthesized(ClassGenerator classGen,
                                             MethodGenerator methodGen,
                                             BooleanType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_CLASS,
                                                     "length", "()I")));
        return new FlowList(il.append(new IFEQ(null)));
    }

    /**
     * Expects a string on the stack and pushes a boxed string.
     * Strings are already boxed so the translation is just a NOP.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            ReferenceType type) {
        methodGen.getInstructionList().append(NOP);
    }

    /**
     * Translates a internal string into an external (Java) string.
     *
     * @see     Type#translateFrom
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            Class clazz)
    {
        // Is String <: clazz? I.e. clazz in { String, Object }
        if (clazz.isAssignableFrom(java.lang.String.class)) {
            methodGen.getInstructionList().append(NOP);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), clazz.getName());
            classGen.getParser().reportError(FATAL, err);
        }
    }

    /**
     * Translates an external (primitive) Java type into a string.
     *
     * @see     Type#translateFrom
     */
    public void translateFrom(ClassGenerator classGen,
        MethodGenerator methodGen, Class clazz)
    {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (clazz.getName().equals("java.lang.String")) {
            // same internal representation, convert null to ""
            il.append(DUP);
            final BranchHandle ifNonNull = il.append(new IFNONNULL(null));
            il.append(POP);
            il.append(new PUSH(cpg, ""));
            ifNonNull.setTarget(il.append(NOP));
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), clazz.getName());
            classGen.getParser().reportError(FATAL, err);
        }
    }

    /**
     * Translates an object of this type to its boxed representation.
     */
    public void translateBox(ClassGenerator classGen,
                             MethodGenerator methodGen) {
        translateTo(classGen, methodGen, Reference);
    }

    /**
     * Translates an object of this type to its unboxed representation.
     */
    public void translateUnBox(ClassGenerator classGen,
                               MethodGenerator methodGen) {
        methodGen.getInstructionList().append(NOP);
    }

    /**
     * Returns the class name of an internal type's external representation.
     */
    public String getClassName() {
        return(STRING_CLASS);
    }


    public Instruction LOAD(int slot) {
        return new ALOAD(slot);
    }

    public Instruction STORE(int slot) {
        return new ASTORE(slot);
    }
}
