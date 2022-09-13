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
 * $Id: RealType.java,v 1.2.4.1 2005/09/05 11:28:45 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.CHECKCAST;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.DLOAD;
import jaxp.sun.org.apache.bcel.internal.generic.DSTORE;
import jaxp.sun.org.apache.bcel.internal.generic.GOTO;
import jaxp.sun.org.apache.bcel.internal.generic.IFEQ;
import jaxp.sun.org.apache.bcel.internal.generic.IFNE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionConstants;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.NEW;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class RealType extends NumberType {
    protected RealType() {}

    public String toString() {
        return "real";
    }

    public boolean identicalTo(Type other) {
        return this == other;
    }

    public String toSignature() {
        return "D";
    }

    public jaxp.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return jaxp.sun.org.apache.bcel.internal.generic.Type.DOUBLE;
    }

    /**
     * @see     Type#distanceTo
     */
    public int distanceTo(Type type) {
        if (type == this) {
            return 0;
        }
        else if (type == Int) {
            return 1;
        }
        else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Translates a real into an object of internal type <code>type</code>. The
     * translation to int is undefined since reals are never converted to ints.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            Type type) {
        if (type == String) {
            translateTo(classGen, methodGen, (StringType) type);
        }
        else if (type == Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
        }
        else if (type == Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        }
        else if (type == Int) {
            translateTo(classGen, methodGen, (IntType) type);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), type.toString());
            classGen.getParser().reportError(Constants.FATAL, err);
        }
    }

    /**
     * Expects a real on the stack and pushes its string value by calling
     * <code>Double.toString(double d)</code>.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            StringType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(BASIS_LIBRARY_CLASS,
                                                    "realToString",
                                                    "(D)" + STRING_SIG)));
    }

    /**
     * Expects a real on the stack and pushes a 0 if that number is 0.0 and
     * a 1 otherwise.
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
     * Expects a real on the stack and pushes a truncated integer value
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            IntType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(BASIS_LIBRARY_CLASS,
                                                    "realToInt","(D)I")));
    }

    /**
     * Translates a real into a non-synthesized boolean. It does not push a
     * 0 or a 1 but instead returns branchhandle list to be appended to the
     * false list. A NaN must be converted to "false".
     *
     * @see     Type#translateToDesynthesized
     */
    public FlowList translateToDesynthesized(ClassGenerator classGen,
                                             MethodGenerator methodGen,
                                             BooleanType type) {
        LocalVariableGen local;
        final FlowList flowlist = new FlowList();
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // Store real into a local variable
        il.append(DUP2);
        local = methodGen.addLocalVariable("real_to_boolean_tmp",
                                           jaxp.sun.org.apache.bcel.internal.generic.Type.DOUBLE,
                                           null, null);
        local.setStart(il.append(new DSTORE(local.getIndex())));

        // Compare it to 0.0
        il.append(DCONST_0);
        il.append(DCMPG);
        flowlist.add(il.append(new IFEQ(null)));

        //!!! call isNaN
        // Compare it to itself to see if NaN
        il.append(new DLOAD(local.getIndex()));
        local.setEnd(il.append(new DLOAD(local.getIndex())));
        il.append(DCMPG);
        flowlist.add(il.append(new IFNE(null)));        // NaN != NaN
        return flowlist;
    }

    /**
     * Expects a double on the stack and pushes a boxed double. Boxed
     * double are represented by an instance of <code>java.lang.Double</code>.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            ReferenceType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new NEW(cpg.addClass(DOUBLE_CLASS)));
        il.append(DUP_X2);
        il.append(DUP_X2);
        il.append(POP);
        il.append(new INVOKESPECIAL(cpg.addMethodref(DOUBLE_CLASS,
                                                     "<init>", "(D)V")));
    }

    /**
     * Translates a real into the Java type denoted by <code>clazz</code>.
     * Expects a real on the stack and pushes a number of the appropriate
     * type after coercion.
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            final Class clazz) {
        final InstructionList il = methodGen.getInstructionList();
        if (clazz == Character.TYPE) {
            il.append(D2I);
            il.append(I2C);
        }
        else if (clazz == Byte.TYPE) {
            il.append(D2I);
            il.append(I2B);
        }
        else if (clazz == Short.TYPE) {
            il.append(D2I);
            il.append(I2S);
        }
        else if (clazz == Integer.TYPE) {
            il.append(D2I);
        }
        else if (clazz == Long.TYPE) {
            il.append(D2L);
        }
        else if (clazz == Float.TYPE) {
            il.append(D2F);
        }
        else if (clazz == Double.TYPE) {
            il.append(NOP);
        }
        // Is Double <: clazz? I.e. clazz in { Double, Number, Object }
        else if (clazz.isAssignableFrom(java.lang.Double.class)) {
            translateTo(classGen, methodGen, Reference);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), clazz.getName());
            classGen.getParser().reportError(Constants.FATAL, err);
        }
    }

    /**
     * Translates an external (primitive) Java type into a real. Expects a java
     * object on the stack and pushes a real (i.e., a double).
     */
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen,
                              Class clazz) {
        InstructionList il = methodGen.getInstructionList();

        if (clazz == Character.TYPE || clazz == Byte.TYPE ||
            clazz == Short.TYPE || clazz == Integer.TYPE) {
            il.append(I2D);
        }
        else if (clazz == Long.TYPE) {
            il.append(L2D);
        }
        else if (clazz == Float.TYPE) {
            il.append(F2D);
        }
        else if (clazz == Double.TYPE) {
            il.append(NOP);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), clazz.getName());
            classGen.getParser().reportError(Constants.FATAL, err);
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
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(new CHECKCAST(cpg.addClass(DOUBLE_CLASS)));
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(DOUBLE_CLASS,
                                                     DOUBLE_VALUE,
                                                     DOUBLE_VALUE_SIG)));
    }

    public Instruction ADD() {
        return InstructionConstants.DADD;
    }

    public Instruction SUB() {
        return InstructionConstants.DSUB;
    }

    public Instruction MUL() {
        return InstructionConstants.DMUL;
    }

    public Instruction DIV() {
        return InstructionConstants.DDIV;
    }

    public Instruction REM() {
        return InstructionConstants.DREM;
    }

    public Instruction NEG() {
        return InstructionConstants.DNEG;
    }

    public Instruction LOAD(int slot) {
        return new DLOAD(slot);
    }

    public Instruction STORE(int slot) {
        return new DSTORE(slot);
    }

    public Instruction POP() {
        return POP2;
    }

    public Instruction CMP(boolean less) {
        return less ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
    }

    public Instruction DUP() {
        return DUP2;
    }
}
