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
 * $Id: ResultTreeType.java,v 1.2.4.1 2005/09/05 11:30:01 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.CHECKCAST;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.IFEQ;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.NEW;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionConstants;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class ResultTreeType extends Type {
    private final String _methodName;

    protected ResultTreeType() {
        _methodName = null;
    }

    public ResultTreeType(String methodName) {
        _methodName = methodName;
    }

    public String toString() {
        return "result-tree";
    }

    public boolean identicalTo(Type other) {
        return (other instanceof ResultTreeType);
    }

    public String toSignature() {
        return Constants.DOM_INTF_SIG;
    }

    public jaxp.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return Util.getJCRefType(toSignature());
    }

    public String getMethodName() {
        return _methodName;
    }

    public boolean implementedAsMethod() {
        return _methodName != null;
    }

    /**
     * Translates a result tree to object of internal type <code>type</code>.
     * The translation to int is undefined since result trees
     * are always converted to reals in arithmetic expressions.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of the type to translate the result tree to
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            Type type) {
        if (type == String) {
            translateTo(classGen, methodGen, (StringType)type);
        }
        else if (type == Boolean) {
            translateTo(classGen, methodGen, (BooleanType)type);
        }
        else if (type == Real) {
            translateTo(classGen, methodGen, (RealType)type);
        }
        else if (type == NodeSet) {
            translateTo(classGen, methodGen, (NodeSetType)type);
        }
        else if (type == Reference) {
            translateTo(classGen, methodGen, (ReferenceType)type);
        }
        else if (type == Object) {
            translateTo(classGen, methodGen, (ObjectType) type);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), type.toString());
            classGen.getParser().reportError(Constants.FATAL, err);
        }
    }

    /**
     * Expects an result tree on the stack and pushes a boolean.
     * Translates a result tree to a boolean by first converting it to string.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of BooleanType (any)
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            BooleanType type) {
        // A result tree is always 'true' when converted to a boolean value,
        // since the tree always has at least one node (the root).
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        il.append(InstructionConstants.POP);      // don't need the DOM reference
        il.append(InstructionConstants.ICONST_1); // push 'true' on the stack
    }

    /**
     * Expects an result tree on the stack and pushes a string.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of StringType (any)
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            StringType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (_methodName == null) {
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                                  "getStringValue",
                                                  "()"+ Constants.STRING_SIG);
            il.append(new INVOKEINTERFACE(index, 1));
        }
        else {
            final String className = classGen.getClassName();
            final int current = methodGen.getLocalIndex("current");

            // Push required parameters
            il.append(classGen.loadTranslet());
            if (classGen.isExternal()) {
                il.append(new CHECKCAST(cpg.addClass(className)));
            }
            il.append(InstructionConstants.DUP);
            il.append(new GETFIELD(cpg.addFieldref(className, "_dom",
                                                   Constants.DOM_INTF_SIG)));

            // Create a new instance of a StringValueHandler
            int index = cpg.addMethodref(Constants.STRING_VALUE_HANDLER, "<init>", "()V");
            il.append(new NEW(cpg.addClass(Constants.STRING_VALUE_HANDLER)));
            il.append(InstructionConstants.DUP);
            il.append(InstructionConstants.DUP);
            il.append(new INVOKESPECIAL(index));

            // Store new Handler into a local variable
            final LocalVariableGen handler =
                methodGen.addLocalVariable("rt_to_string_handler",
                                           Util.getJCRefType(Constants.STRING_VALUE_HANDLER_SIG),
                                           null, null);
            handler.setStart(il.append(new ASTORE(handler.getIndex())));

            // Call the method that implements this result tree
            index = cpg.addMethodref(className, _methodName,
                                     "("+ Constants.DOM_INTF_SIG+ Constants.TRANSLET_OUTPUT_SIG+")V");
            il.append(new INVOKEVIRTUAL(index));

            // Restore new handler and call getValue()
            handler.setEnd(il.append(new ALOAD(handler.getIndex())));
            index = cpg.addMethodref(Constants.STRING_VALUE_HANDLER,
                                     "getValue",
                                     "()" + Constants.STRING_SIG);
            il.append(new INVOKEVIRTUAL(index));
        }
    }

    /**
     * Expects an result tree on the stack and pushes a real.
     * Translates a result tree into a real by first converting it to string.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of RealType (any)
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            RealType type) {
        translateTo(classGen, methodGen, String);
        String.translateTo(classGen, methodGen, Real);
    }

    /**
     * Expects a result tree on the stack and pushes a boxed result tree.
     * Result trees are already boxed so the translation is just a NOP.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of ReferenceType (any)
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            ReferenceType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (_methodName == null) {
            il.append(InstructionConstants.NOP);
        }
        else {
            LocalVariableGen domBuilder, newDom;
            final String className = classGen.getClassName();
            final int current = methodGen.getLocalIndex("current");

            // Push required parameters
            il.append(classGen.loadTranslet());
            if (classGen.isExternal()) {
                il.append(new CHECKCAST(cpg.addClass(className)));
            }
            il.append(methodGen.loadDOM());

            // Create new instance of DOM class (with RTF_INITIAL_SIZE nodes)
            il.append(methodGen.loadDOM());
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                 "getResultTreeFrag",
                                 "(IZ)" + Constants.DOM_INTF_SIG);
            il.append(new PUSH(cpg, Constants.RTF_INITIAL_SIZE));
            il.append(new PUSH(cpg, false));
            il.append(new INVOKEINTERFACE(index,3));
            il.append(InstructionConstants.DUP);

            // Store new DOM into a local variable
            newDom = methodGen.addLocalVariable("rt_to_reference_dom",
                                                Util.getJCRefType(Constants.DOM_INTF_SIG),
                                                null, null);
            il.append(new CHECKCAST(cpg.addClass(Constants.DOM_INTF_SIG)));
            newDom.setStart(il.append(new ASTORE(newDom.getIndex())));

            // Overwrite old handler with DOM handler
            index = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                 "getOutputDomBuilder",
                                 "()" + Constants.TRANSLET_OUTPUT_SIG);

            il.append(new INVOKEINTERFACE(index,1));
            //index = cpg.addMethodref(DOM_IMPL,
                //                   "getOutputDomBuilder",
                //                   "()" + TRANSLET_OUTPUT_SIG);
            //il.append(new INVOKEVIRTUAL(index));
            il.append(InstructionConstants.DUP);
            il.append(InstructionConstants.DUP);

            // Store DOM handler in a local in order to call endDocument()
            domBuilder =
                methodGen.addLocalVariable("rt_to_reference_handler",
                                           Util.getJCRefType(Constants.TRANSLET_OUTPUT_SIG),
                                           null, null);
            domBuilder.setStart(il.append(new ASTORE(domBuilder.getIndex())));

            // Call startDocument on the new handler
            index = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE,
                                              "startDocument", "()V");
            il.append(new INVOKEINTERFACE(index, 1));

            // Call the method that implements this result tree
            index = cpg.addMethodref(className,
                                     _methodName,
                                     "("
                                     + Constants.DOM_INTF_SIG
                                     + Constants.TRANSLET_OUTPUT_SIG
                                     +")V");
            il.append(new INVOKEVIRTUAL(index));

            // Call endDocument on the DOM handler
            domBuilder.setEnd(il.append(new ALOAD(domBuilder.getIndex())));
            index = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE,
                                              "endDocument", "()V");
            il.append(new INVOKEINTERFACE(index, 1));

            // Push the new DOM on the stack
            newDom.setEnd(il.append(new ALOAD(newDom.getIndex())));
        }
    }

    /**
     * Expects a result tree on the stack and pushes a node-set (iterator).
     * Note that the produced iterator is an iterator for the DOM that
     * contains the result tree, and not the DOM that is currently in use.
     * This conversion here will therefore not directly work with elements
     * such as <xsl:apply-templates> and <xsl:for-each> without the DOM
     * parameter/variable being updates as well.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of NodeSetType (any)
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            NodeSetType type) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // Put an extra copy of the result tree (DOM) on the stack
        il.append(InstructionConstants.DUP);

        // DOM adapters containing a result tree are not initialised with
        // translet-type to DOM-type mapping. This must be done now for
        // XPath expressions and patterns to work for the iterator we create.
        il.append(classGen.loadTranslet()); // get names array
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS,
                                               Constants.NAMES_INDEX,
                                               Constants.NAMES_INDEX_SIG)));
        il.append(classGen.loadTranslet()); // get uris array
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS,
                                               Constants.URIS_INDEX,
                                               Constants.URIS_INDEX_SIG)));
        il.append(classGen.loadTranslet()); // get types array
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS,
                                               Constants.TYPES_INDEX,
                                               Constants.TYPES_INDEX_SIG)));
        il.append(classGen.loadTranslet()); // get namespaces array
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS,
                                               Constants.NAMESPACE_INDEX,
                                               Constants.NAMESPACE_INDEX_SIG)));
        // Pass the type mappings to the DOM adapter
        final int mapping = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                                      "setupMapping",
                                                      "(["+ Constants.STRING_SIG+
                                                      "["+ Constants.STRING_SIG+
                                                      "[I" +
                                                      "["+ Constants.STRING_SIG+")V");
        il.append(new INVOKEINTERFACE(mapping, 5));
        il.append(InstructionConstants.DUP);

        // Create an iterator for the root node of the DOM adapter
        final int iter = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                                   "getIterator",
                                                   "()"+ Constants.NODE_ITERATOR_SIG);
        il.append(new INVOKEINTERFACE(iter, 1));
    }

    /**
     * Subsume result tree into ObjectType.
     *
     * @see     Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            ObjectType type) {
        methodGen.getInstructionList().append(InstructionConstants.NOP);
    }

    /**
     * Translates a result tree into a non-synthesized boolean.
     * It does not push a 0 or a 1 but instead returns branchhandle list
     * to be appended to the false list.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param type An instance of BooleanType (any)
     * @see Type#translateToDesynthesized
     */
    public FlowList translateToDesynthesized(ClassGenerator classGen,
                                             MethodGenerator methodGen,
                                             BooleanType type) {
        final InstructionList il = methodGen.getInstructionList();
        translateTo(classGen, methodGen, Boolean);
        return new FlowList(il.append(new IFEQ(null)));
    }

    /**
     * Translates a result tree to a Java type denoted by <code>clazz</code>.
     * Expects a result tree on the stack and pushes an object
     * of the appropriate type after coercion. Result trees are translated
     * to W3C Node or W3C NodeList and the translation is done
     * via node-set type.
     *
     * @param classGen A BCEL class generator
     * @param methodGen A BCEL method generator
     * @param clazz An reference to the Class to translate to
     * @see Type#translateTo
     */
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen,
                            Class clazz) {
        final String className = clazz.getName();
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (className.equals("org.w3c.dom.Node")) {
            translateTo(classGen, methodGen, NodeSet);
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                                  Constants.MAKE_NODE,
                                                  Constants.MAKE_NODE_SIG2);
            il.append(new INVOKEINTERFACE(index, 2));
        }
        else if (className.equals("org.w3c.dom.NodeList")) {
            translateTo(classGen, methodGen, NodeSet);
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF,
                                                  Constants.MAKE_NODE_LIST,
                                                  Constants.MAKE_NODE_LIST_SIG2);
            il.append(new INVOKEINTERFACE(index, 2));
        }
        else if (className.equals("java.lang.Object")) {
            il.append(InstructionConstants.NOP);
        }
        else if (className.equals("java.lang.String")) {
            translateTo(classGen, methodGen, String);
        }
        else {
            ErrorMsg err = new ErrorMsg(ErrorMsg.DATA_CONVERSION_ERR,
                                        toString(), className);
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
        methodGen.getInstructionList().append(InstructionConstants.NOP);
    }

    /**
     * Returns the class name of an internal type's external representation.
     */
    public String getClassName() {
        return(Constants.DOM_INTF);
    }

    public Instruction LOAD(int slot) {
        return new ALOAD(slot);
    }

    public Instruction STORE(int slot) {
        return new ASTORE(slot);
    }
}
