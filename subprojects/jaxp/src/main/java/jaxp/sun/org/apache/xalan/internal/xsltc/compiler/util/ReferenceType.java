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
 * $Id: ReferenceType.java,v 1.2.4.1 2005/09/05 11:29:12 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.IFEQ;
import jaxp.sun.org.apache.bcel.internal.generic.ILOAD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
import jaxp.sun.org.apache.xml.internal.dtm.DTM;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Erwin Bolwidt <ejb@klomp.org>
 */
public final class ReferenceType extends Type {
  protected ReferenceType() {}

  /**
   * Translates reference into object of internal type <code>type</code>.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
    final int current = methodGen.getLocalIndex("current");
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();

    // If no current, conversion is a top-level
    if (current < 0) {
      il.append(new PUSH(cpg, DTM.ROOT_NODE)); // push root node
    } else {
      il.append(new ILOAD(current));
    }
    il.append(methodGen.loadDOM());
    final int stringF =
        cpg.addMethodref(
            BASIS_LIBRARY_CLASS,
            "stringF",
            "(" + OBJECT_SIG + NODE_SIG + DOM_INTF_SIG + ")" + STRING_SIG);
    il.append(new INVOKESTATIC(stringF));
  }  public String toString() {
    return "reference";
  }

  /**
   * Translates a reference into an object of internal type <code>type</code>.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();

    il.append(methodGen.loadDOM());
    int index =
        cpg.addMethodref(BASIS_LIBRARY_CLASS, "numberF", "(" + OBJECT_SIG + DOM_INTF_SIG + ")D");
    il.append(new INVOKESTATIC(index));
  }  public boolean identicalTo(Type other) {
    return this == other;
  }

  /**
   * Casts a reference into a NodeIterator.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();
    int index =
        cpg.addMethodref(
            BASIS_LIBRARY_CLASS, "referenceToNodeSet", "(" + OBJECT_SIG + ")" + NODE_ITERATOR_SIG);
    il.append(new INVOKESTATIC(index));

    // Reset this iterator
    index = cpg.addInterfaceMethodref(NODE_ITERATOR, RESET, RESET_SIG);
    il.append(new INVOKEINTERFACE(index, 1));
  }  public String toSignature() {
    return "Ljava/lang/Object;";
  }

  /**
   * Casts a reference into a Node.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type) {
    translateTo(classGen, methodGen, NodeSet);
    NodeSet.translateTo(classGen, methodGen, type);
  }  public jaxp.sun.org.apache.bcel.internal.generic.Type toJCType() {
    return jaxp.sun.org.apache.bcel.internal.generic.Type.OBJECT;
  }

  /**
   * Casts a reference into a ResultTree.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ResultTreeType type) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();
    int index =
        cpg.addMethodref(
            BASIS_LIBRARY_CLASS, "referenceToResultTree", "(" + OBJECT_SIG + ")" + DOM_INTF_SIG);
    il.append(new INVOKESTATIC(index));
  }  /**
   * Translates a reference to an object of internal type <code>type</code>. The translation to int
   * is undefined since references are always converted to reals in arithmetic expressions.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
    if (type == String) {
      translateTo(classGen, methodGen, (StringType) type);
    } else if (type == Real) {
      translateTo(classGen, methodGen, (RealType) type);
    } else if (type == Boolean) {
      translateTo(classGen, methodGen, (BooleanType) type);
    } else if (type == NodeSet) {
      translateTo(classGen, methodGen, (NodeSetType) type);
    } else if (type == Node) {
      translateTo(classGen, methodGen, (NodeType) type);
    } else if (type == ResultTree) {
      translateTo(classGen, methodGen, (ResultTreeType) type);
    } else if (type == Object) {
      translateTo(classGen, methodGen, (ObjectType) type);
    } else if (type == Reference) {
    } else {
      ErrorMsg err = new ErrorMsg(ErrorMsg.INTERNAL_ERR, type.toString());
      classGen.getParser().reportError(Constants.FATAL, err);
    }
  }

  /**
   * Subsume reference into ObjectType.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
    methodGen.getInstructionList().append(NOP);
  }



  /**
   * Translates a reference to an object of internal type <code>type</code>.
   *
   * @see Type#translateTo
   */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();

    int index = cpg.addMethodref(BASIS_LIBRARY_CLASS, "booleanF", "(" + OBJECT_SIG + ")Z");
    il.append(new INVOKESTATIC(index));
  }









  /** Translates a reference into the Java type denoted by <code>clazz</code>. */
  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();

    int referenceToLong =
        cpg.addMethodref(BASIS_LIBRARY_CLASS, "referenceToLong", "(" + OBJECT_SIG + ")J");
    int referenceToDouble =
        cpg.addMethodref(BASIS_LIBRARY_CLASS, "referenceToDouble", "(" + OBJECT_SIG + ")D");
    int referenceToBoolean =
        cpg.addMethodref(BASIS_LIBRARY_CLASS, "referenceToBoolean", "(" + OBJECT_SIG + ")Z");

    if (clazz.getName().equals("java.lang.Object")) {
      il.append(NOP);
    } else if (clazz == Double.TYPE) {
      il.append(new INVOKESTATIC(referenceToDouble));
    } else if (clazz.getName().equals("java.lang.Double")) {
      il.append(new INVOKESTATIC(referenceToDouble));
      Real.translateTo(classGen, methodGen, Reference);
    } else if (clazz == Float.TYPE) {
      il.append(new INVOKESTATIC(referenceToDouble));
      il.append(D2F);
    } else if (clazz.getName().equals("java.lang.String")) {
      int index =
          cpg.addMethodref(
              BASIS_LIBRARY_CLASS,
              "referenceToString",
              "(" + OBJECT_SIG + DOM_INTF_SIG + ")" + "Ljava/lang/String;");
      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    } else if (clazz.getName().equals("org.w3c.dom.Node")) {
      int index =
          cpg.addMethodref(
              BASIS_LIBRARY_CLASS,
              "referenceToNode",
              "(" + OBJECT_SIG + DOM_INTF_SIG + ")" + "Lorg/w3c/dom/Node;");
      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    } else if (clazz.getName().equals("org.w3c.dom.NodeList")) {
      int index =
          cpg.addMethodref(
              BASIS_LIBRARY_CLASS,
              "referenceToNodeList",
              "(" + OBJECT_SIG + DOM_INTF_SIG + ")" + "Lorg/w3c/dom/NodeList;");
      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    } else if (clazz.getName().equals("jaxp.sun.org.apache.xalan.internal.xsltc.DOM")) {
      translateTo(classGen, methodGen, ResultTree);
    } else if (clazz == Long.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
    } else if (clazz == Integer.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
    } else if (clazz == Short.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2S);
    } else if (clazz == Byte.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2B);
    } else if (clazz == Character.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2C);
    } else if (clazz == java.lang.Boolean.TYPE) {
      il.append(new INVOKESTATIC(referenceToBoolean));
    } else if (clazz.getName().equals("java.lang.Boolean")) {
      il.append(new INVOKESTATIC(referenceToBoolean));
      translateTo(classGen, methodGen, Reference);
    } else {
      ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR, toString(), clazz.getName());
      classGen.getParser().reportError(Constants.FATAL, err);
    }
  }

  /**
   * Translates an external Java type into a reference. Only conversion allowed is from
   * java.lang.Object.
   */
  public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
    if (clazz.getName().equals("java.lang.Object")) {
      methodGen.getInstructionList().append(NOP);
    } else {
      ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR, toString(), clazz.getName());
      classGen.getParser().reportError(Constants.FATAL, err);
    }
  }

  /**
   * Expects a reference on the stack and translates it to a non-synthesized boolean. It does not
   * push a 0 or a 1 but instead returns branchhandle list to be appended to the false list.
   *
   * @see Type#translateToDesynthesized
   */
  public FlowList translateToDesynthesized(
      ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
    InstructionList il = methodGen.getInstructionList();
    translateTo(classGen, methodGen, type);
    return new FlowList(il.append(new IFEQ(null)));
  }

  /** Translates an object of this type to its boxed representation. */
  public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {}

  /** Translates an object of this type to its unboxed representation. */
  public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {}

  public Instruction LOAD(int slot) {
    return new ALOAD(slot);
  }

  public Instruction STORE(int slot) {
    return new ASTORE(slot);
  }
}
