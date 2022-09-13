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
 * Copyright 2001-2005 The Apache Software Foundation.
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
 * $Id: Sort.java,v 1.2.4.1 2005/09/12 11:08:12 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.ArrayList;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.classfile.Field;
import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.CHECKCAST;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.ILOAD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.NEW;
import jaxp.sun.org.apache.bcel.internal.generic.NOP;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.bcel.internal.generic.PUTFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordFactGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import jaxp.sun.org.apache.xml.internal.dtm.Axis;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionConstants;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class Sort extends Instruction implements Closure {

    private Expression     _select;
    private AttributeValue _order;
    private AttributeValue _caseOrder;
    private AttributeValue _dataType;
    private String  _lang; // bug! see 26869

    private String         _data = null;


    private String _className = null;
    private ArrayList _closureVars = null;
    private boolean _needsSortRecordFactory = false;

    // -- Begin Closure interface --------------------

    /**
     * Returns true if this closure is compiled in an inner class (i.e.
     * if this is a real closure).
     */
    public boolean inInnerClass() {
        return (_className != null);
    }

    /**
     * Returns a reference to its parent closure or null if outermost.
     */
    public Closure getParentClosure() {
        return null;
    }

    /**
     * Returns the name of the auxiliary class or null if this predicate
     * is compiled inside the Translet.
     */
    public String getInnerClassName() {
        return _className;
    }

    /**
     * Add new variable to the closure.
     */
    public void addVariable(VariableRefBase variableRef) {
        if (_closureVars == null) {
            _closureVars = new ArrayList();
        }

        // Only one reference per variable
        if (!_closureVars.contains(variableRef)) {
            _closureVars.add(variableRef);
            _needsSortRecordFactory = true;
        }
    }

    // -- End Closure interface ----------------------

    private void setInnerClassName(String className) {
        _className = className;
    }

    /**
     * Parse the attributes of the xsl:sort element
     */
    public void parseContents(Parser parser) {

        final SyntaxTreeNode parent = getParent();
        if (!(parent instanceof ApplyTemplates) &&
            !(parent instanceof ForEach)) {
            reportError(this, parser, ErrorMsg.STRAY_SORT_ERR, null);
            return;
        }

        // Parse the select expression (node string value if no expression)
        _select = parser.parseExpression(this, "select", "string(.)");

        // Get the sort order; default is 'ascending'
        String val = getAttribute("order");
        if (val.length() == 0) val = "ascending";
        _order = AttributeValue.create(this, val, parser);

        // Get the sort data type; default is text
        val = getAttribute("data-type");
        if (val.length() == 0) {
            try {
                final Type type = _select.typeCheck(parser.getSymbolTable());
                if (type instanceof IntType)
                    val = "number";
                else
                    val = "text";
            }
            catch (TypeCheckError e) {
                val = "text";
            }
        }
        _dataType = AttributeValue.create(this, val, parser);

         _lang =  getAttribute("lang"); // bug! see 26869
  // val =  getAttribute("lang");
  // _lang = AttributeValue.create(this, val, parser);
        // Get the case order; default is language dependant
    val = getAttribute("case-order");
    _caseOrder = AttributeValue.create(this, val, parser);

    }

    /**
     * Run type checks on the attributes; expression must return a string
     * which we will use as a sort key
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        final Type tselect = _select.typeCheck(stable);

        // If the sort data-type is not set we use the natural data-type
        // of the data we will sort
        if (!(tselect instanceof StringType)) {
            _select = new CastExpr(_select, Type.String);
        }

        _order.typeCheck(stable);
        _caseOrder.typeCheck(stable);
        _dataType.typeCheck(stable);
        return Type.Void;
    }

    /**
     * These two methods are needed in the static methods that compile the
     * overloaded NodeSortRecord.compareType() and NodeSortRecord.sortOrder()
     */
    public void translateSortType(ClassGenerator classGen,
                                  MethodGenerator methodGen) {
        _dataType.translate(classGen, methodGen);
    }

    public void translateSortOrder(ClassGenerator classGen,
                                   MethodGenerator methodGen) {
        _order.translate(classGen, methodGen);
    }

     public void translateCaseOrder(ClassGenerator classGen,
                   MethodGenerator methodGen) {
    _caseOrder.translate(classGen, methodGen);
    }

    public void translateLang(ClassGenerator classGen,
                   MethodGenerator methodGen) {
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();
    il.append(new PUSH(cpg, _lang)); // bug! see 26869
    }

    /**
     * This method compiles code for the select expression for this
     * xsl:sort element. The method is called from the static code-generating
     * methods in this class.
     */
    public void translateSelect(ClassGenerator classGen,
                                MethodGenerator methodGen) {
        _select.translate(classGen,methodGen);
    }

    /**
     * This method should not produce any code
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        // empty
    }

    /**
     * Compiles code that instantiates a SortingIterator object.
     * This object's constructor needs referencdes to the current iterator
     * and a node sort record producing objects as its parameters.
     */
    public static void translateSortIterator(ClassGenerator classGen,
                                      MethodGenerator methodGen,
                                      Expression nodeSet,
                                      Vector sortObjects)
    {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // SortingIterator.SortingIterator(NodeIterator,NodeSortRecordFactory);
        final int init = cpg.addMethodref(SORT_ITERATOR, "<init>",
                                          "("
                                          + NODE_ITERATOR_SIG
                                          + NODE_SORT_FACTORY_SIG
                                          + ")V");

        // Backwards branches are prohibited if an uninitialized object is
        // on the stack by section 4.9.4 of the JVM Specification, 2nd Ed.
        // We don't know whether this code might contain backwards branches
        // so we mustn't create the new object until after we've created
        // the suspect arguments to its constructor.  Instead we calculate
        // the values of the arguments to the constructor first, store them
        // in temporary variables, create the object and reload the
        // arguments from the temporaries to avoid the problem.

        LocalVariableGen nodesTemp =
            methodGen.addLocalVariable("sort_tmp1",
                                       Util.getJCRefType(NODE_ITERATOR_SIG),
                                       null, null);

        LocalVariableGen sortRecordFactoryTemp =
            methodGen.addLocalVariable("sort_tmp2",
                                      Util.getJCRefType(NODE_SORT_FACTORY_SIG),
                                      null, null);

        // Get the current node iterator
        if (nodeSet == null) {  // apply-templates default
            final int children = cpg.addInterfaceMethodref(DOM_INTF,
                                                           "getAxisIterator",
                                                           "(I)"+
                                                           NODE_ITERATOR_SIG);
            il.append(methodGen.loadDOM());
            il.append(new PUSH(cpg, Axis.CHILD));
            il.append(new INVOKEINTERFACE(children, 2));
        }
        else {
            nodeSet.translate(classGen, methodGen);
        }

        nodesTemp.setStart(il.append(new ASTORE(nodesTemp.getIndex())));

        // Compile the code for the NodeSortRecord producing class and pass
        // that as the last argument to the SortingIterator constructor.
        compileSortRecordFactory(sortObjects, classGen, methodGen);
        sortRecordFactoryTemp.setStart(
                il.append(new ASTORE(sortRecordFactoryTemp.getIndex())));

        il.append(new NEW(cpg.addClass(SORT_ITERATOR)));
        il.append(InstructionConstants.DUP);
        nodesTemp.setEnd(il.append(new ALOAD(nodesTemp.getIndex())));
        sortRecordFactoryTemp.setEnd(
                il.append(new ALOAD(sortRecordFactoryTemp.getIndex())));
        il.append(new INVOKESPECIAL(init));
    }


    /**
     * Compiles code that instantiates a NodeSortRecordFactory object which
     * will produce NodeSortRecord objects of a specific type.
     */
    public static void compileSortRecordFactory(Vector sortObjects,
        ClassGenerator classGen, MethodGenerator methodGen)
    {
        String sortRecordClass =
            compileSortRecord(sortObjects, classGen, methodGen);

        boolean needsSortRecordFactory = false;
        final int nsorts = sortObjects.size();
        for (int i = 0; i < nsorts; i++) {
            final Sort sort = (Sort) sortObjects.elementAt(i);
            needsSortRecordFactory |= sort._needsSortRecordFactory;
        }

        String sortRecordFactoryClass = NODE_SORT_FACTORY;
        if (needsSortRecordFactory) {
            sortRecordFactoryClass =
                compileSortRecordFactory(sortObjects, classGen, methodGen,
                    sortRecordClass);
        }

        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // Backwards branches are prohibited if an uninitialized object is
        // on the stack by section 4.9.4 of the JVM Specification, 2nd Ed.
        // We don't know whether this code might contain backwards branches
        // so we mustn't create the new object until after we've created
        // the suspect arguments to its constructor.  Instead we calculate
        // the values of the arguments to the constructor first, store them
        // in temporary variables, create the object and reload the
        // arguments from the temporaries to avoid the problem.

        // Compile code that initializes the static _sortOrder
        LocalVariableGen sortOrderTemp
                 = methodGen.addLocalVariable("sort_order_tmp",
                                      Util.getJCRefType("[" + STRING_SIG),
                                      null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass(STRING)));
        for (int level = 0; level < nsorts; level++) {
            final Sort sort = (Sort)sortObjects.elementAt(level);
            il.append(InstructionConstants.DUP);
            il.append(new PUSH(cpg, level));
            sort.translateSortOrder(classGen, methodGen);
            il.append(InstructionConstants.AASTORE);
        }
        sortOrderTemp.setStart(il.append(new ASTORE(sortOrderTemp.getIndex())));

        LocalVariableGen sortTypeTemp
                 = methodGen.addLocalVariable("sort_type_tmp",
                                      Util.getJCRefType("[" + STRING_SIG),
                                      null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass(STRING)));
        for (int level = 0; level < nsorts; level++) {
            final Sort sort = (Sort)sortObjects.elementAt(level);
            il.append(InstructionConstants.DUP);
            il.append(new PUSH(cpg, level));
            sort.translateSortType(classGen, methodGen);
            il.append(InstructionConstants.AASTORE);
        }
        sortTypeTemp.setStart(il.append(new ASTORE(sortTypeTemp.getIndex())));

        LocalVariableGen sortLangTemp
                 = methodGen.addLocalVariable("sort_lang_tmp",
                                      Util.getJCRefType("[" + STRING_SIG),
                                      null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass(STRING)));
        for (int level = 0; level < nsorts; level++) {
              final Sort sort = (Sort)sortObjects.elementAt(level);
              il.append(InstructionConstants.DUP);
              il.append(new PUSH(cpg, level));
              sort.translateLang(classGen, methodGen);
              il.append(InstructionConstants.AASTORE);
        }
        sortLangTemp.setStart(il.append(new ASTORE(sortLangTemp.getIndex())));

        LocalVariableGen sortCaseOrderTemp
                 = methodGen.addLocalVariable("sort_case_order_tmp",
                                      Util.getJCRefType("[" + STRING_SIG),
                                      null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass(STRING)));
        for (int level = 0; level < nsorts; level++) {
            final Sort sort = (Sort)sortObjects.elementAt(level);
            il.append(InstructionConstants.DUP);
            il.append(new PUSH(cpg, level));
            sort.translateCaseOrder(classGen, methodGen);
            il.append(InstructionConstants.AASTORE);
        }
        sortCaseOrderTemp.setStart(
                il.append(new ASTORE(sortCaseOrderTemp.getIndex())));

        il.append(new NEW(cpg.addClass(sortRecordFactoryClass)));
        il.append(InstructionConstants.DUP);
        il.append(methodGen.loadDOM());
        il.append(new PUSH(cpg, sortRecordClass));
        il.append(classGen.loadTranslet());

        sortOrderTemp.setEnd(il.append(new ALOAD(sortOrderTemp.getIndex())));
        sortTypeTemp.setEnd(il.append(new ALOAD(sortTypeTemp.getIndex())));
        sortLangTemp.setEnd(il.append(new ALOAD(sortLangTemp.getIndex())));
        sortCaseOrderTemp.setEnd(
                il.append(new ALOAD(sortCaseOrderTemp.getIndex())));

        il.append(new INVOKESPECIAL(
            cpg.addMethodref(sortRecordFactoryClass, "<init>",
                "(" + DOM_INTF_SIG
                    + STRING_SIG
                    + TRANSLET_INTF_SIG
                    + "[" + STRING_SIG
                    + "[" + STRING_SIG
                    + "[" + STRING_SIG
                    + "[" + STRING_SIG + ")V")));

        // Initialize closure variables in sortRecordFactory
        final ArrayList dups = new ArrayList();

        for (int j = 0; j < nsorts; j++) {
            final Sort sort = (Sort) sortObjects.get(j);
            final int length = (sort._closureVars == null) ? 0 :
                sort._closureVars.size();

            for (int i = 0; i < length; i++) {
                VariableRefBase varRef = (VariableRefBase) sort._closureVars.get(i);

                // Discard duplicate variable references
                if (dups.contains(varRef)) continue;

                final VariableBase var = varRef.getVariable();

                // Store variable in new closure
                il.append(InstructionConstants.DUP);
                il.append(var.loadInstruction());
                il.append(new PUTFIELD(
                        cpg.addFieldref(sortRecordFactoryClass, var.getEscapedName(),
                            var.getType().toSignature())));
                dups.add(varRef);
            }
        }
    }

    public static String compileSortRecordFactory(Vector sortObjects,
        ClassGenerator classGen, MethodGenerator methodGen,
        String sortRecordClass)
    {
        final XSLTC  xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
        final String className = xsltc.getHelperClassName();

        final NodeSortRecordFactGenerator sortRecordFactory =
            new NodeSortRecordFactGenerator(className,
                                        NODE_SORT_FACTORY,
                                        className + ".java",
                                        ACC_PUBLIC | ACC_SUPER | ACC_FINAL,
                                        new String[] {},
                                        classGen.getStylesheet());

        ConstantPoolGen cpg = sortRecordFactory.getConstantPool();

        // Add a new instance variable for each var in closure
        final int nsorts = sortObjects.size();
        final ArrayList dups = new ArrayList();

        for (int j = 0; j < nsorts; j++) {
            final Sort sort = (Sort) sortObjects.get(j);
            final int length = (sort._closureVars == null) ? 0 :
                sort._closureVars.size();

            for (int i = 0; i < length; i++) {
                final VariableRefBase varRef = (VariableRefBase) sort._closureVars.get(i);

                // Discard duplicate variable references
                if (dups.contains(varRef)) continue;

                final VariableBase var = varRef.getVariable();
                sortRecordFactory.addField(new Field(ACC_PUBLIC,
                                           cpg.addUtf8(var.getEscapedName()),
                                           cpg.addUtf8(var.getType().toSignature()),
                                           null, cpg.getConstantPool()));
                dups.add(varRef);
            }
        }

        // Define a constructor for this class
        final jaxp.sun.org.apache.bcel.internal.generic.Type[] argTypes =
            new jaxp.sun.org.apache.bcel.internal.generic.Type[7];
        argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
        argTypes[1] = Util.getJCRefType(STRING_SIG);
        argTypes[2] = Util.getJCRefType(TRANSLET_INTF_SIG);
        argTypes[3] = Util.getJCRefType("[" + STRING_SIG);
        argTypes[4] = Util.getJCRefType("[" + STRING_SIG);
  argTypes[5] = Util.getJCRefType("[" + STRING_SIG);
  argTypes[6] = Util.getJCRefType("[" + STRING_SIG);

        final String[] argNames = new String[7];
        argNames[0] = DOCUMENT_PNAME;
        argNames[1] = "className";
        argNames[2] = TRANSLET_PNAME;
        argNames[3] = "order";
        argNames[4] = "type";
  argNames[5] = "lang";
  argNames[6] = "case_order";


        InstructionList il = new InstructionList();
        final MethodGenerator constructor =
            new MethodGenerator(ACC_PUBLIC,
                                jaxp.sun.org.apache.bcel.internal.generic.Type.VOID,
                                argTypes, argNames, "<init>",
                                className, il, cpg);

        // Push all parameters onto the stack and called super.<init>()
        il.append(InstructionConstants.ALOAD_0);
        il.append(InstructionConstants.ALOAD_1);
        il.append(InstructionConstants.ALOAD_2);
        il.append(new ALOAD(3));
        il.append(new ALOAD(4));
        il.append(new ALOAD(5));
  il.append(new ALOAD(6));
  il.append(new ALOAD(7));
        il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY,
            "<init>",
            "(" + DOM_INTF_SIG
                + STRING_SIG
                + TRANSLET_INTF_SIG
                + "[" + STRING_SIG
    + "[" + STRING_SIG
    + "[" + STRING_SIG
                + "[" + STRING_SIG + ")V")));
        il.append(InstructionConstants.RETURN);

        // Override the definition of makeNodeSortRecord()
        il = new InstructionList();
        final MethodGenerator makeNodeSortRecord =
            new MethodGenerator(ACC_PUBLIC,
                Util.getJCRefType(NODE_SORT_RECORD_SIG),
                new jaxp.sun.org.apache.bcel.internal.generic.Type[] {
                    jaxp.sun.org.apache.bcel.internal.generic.Type.INT,
                    jaxp.sun.org.apache.bcel.internal.generic.Type.INT },
                new String[] { "node", "last" }, "makeNodeSortRecord",
                className, il, cpg);

        il.append(InstructionConstants.ALOAD_0);
        il.append(InstructionConstants.ILOAD_1);
        il.append(InstructionConstants.ILOAD_2);
        il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY,
            "makeNodeSortRecord", "(II)" + NODE_SORT_RECORD_SIG)));
        il.append(InstructionConstants.DUP);
        il.append(new CHECKCAST(cpg.addClass(sortRecordClass)));

        // Initialize closure in record class
        final int ndups = dups.size();
        for (int i = 0; i < ndups; i++) {
            final VariableRefBase varRef = (VariableRefBase) dups.get(i);
            final VariableBase var = varRef.getVariable();
            final Type varType = var.getType();

            il.append(InstructionConstants.DUP);

            // Get field from factory class
            il.append(InstructionConstants.ALOAD_0);
            il.append(new GETFIELD(
                cpg.addFieldref(className,
                    var.getEscapedName(), varType.toSignature())));

            // Put field in record class
            il.append(new PUTFIELD(
                cpg.addFieldref(sortRecordClass,
                    var.getEscapedName(), varType.toSignature())));
        }
        il.append(InstructionConstants.POP);
        il.append(InstructionConstants.ARETURN);

        constructor.setMaxLocals();
        constructor.setMaxStack();
        sortRecordFactory.addMethod(constructor);
        makeNodeSortRecord.setMaxLocals();
        makeNodeSortRecord.setMaxStack();
        sortRecordFactory.addMethod(makeNodeSortRecord);
        xsltc.dumpClass(sortRecordFactory.getJavaClass());

        return className;
    }

    /**
     * Create a new auxillary class extending NodeSortRecord.
     */
    private static String compileSortRecord(Vector sortObjects,
                                            ClassGenerator classGen,
                                            MethodGenerator methodGen) {
        final XSLTC  xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
        final String className = xsltc.getHelperClassName();

        // This generates a new class for handling this specific sort
        final NodeSortRecordGenerator sortRecord =
            new NodeSortRecordGenerator(className,
                                        NODE_SORT_RECORD,
                                        "sort$0.java",
                                        ACC_PUBLIC | ACC_SUPER | ACC_FINAL,
                                        new String[] {},
                                        classGen.getStylesheet());

        final ConstantPoolGen cpg = sortRecord.getConstantPool();

        // Add a new instance variable for each var in closure
        final int nsorts = sortObjects.size();
        final ArrayList dups = new ArrayList();

        for (int j = 0; j < nsorts; j++) {
            final Sort sort = (Sort) sortObjects.get(j);

            // Set the name of the inner class in this sort object
            sort.setInnerClassName(className);

            final int length = (sort._closureVars == null) ? 0 :
                sort._closureVars.size();
            for (int i = 0; i < length; i++) {
                final VariableRefBase varRef = (VariableRefBase) sort._closureVars.get(i);

                // Discard duplicate variable references
                if (dups.contains(varRef)) continue;

                final VariableBase var = varRef.getVariable();
                sortRecord.addField(new Field(ACC_PUBLIC,
                                    cpg.addUtf8(var.getEscapedName()),
                                    cpg.addUtf8(var.getType().toSignature()),
                                    null, cpg.getConstantPool()));
                dups.add(varRef);
            }
        }

        MethodGenerator init = compileInit(sortObjects, sortRecord,
                                         cpg, className);
        MethodGenerator extract = compileExtract(sortObjects, sortRecord,
                                        cpg, className);
        sortRecord.addMethod(init);
        sortRecord.addMethod(extract);

        xsltc.dumpClass(sortRecord.getJavaClass());
        return className;
    }

    /**
     * Create a constructor for the new class. Updates the reference to the
     * collator in the super calls only when the stylesheet specifies a new
     * language in xsl:sort.
     */
    private static MethodGenerator compileInit(Vector sortObjects,
                                           NodeSortRecordGenerator sortRecord,
                                           ConstantPoolGen cpg,
                                           String className)
    {
        final InstructionList il = new InstructionList();
        final MethodGenerator init =
            new MethodGenerator(ACC_PUBLIC,
                                jaxp.sun.org.apache.bcel.internal.generic.Type.VOID,
                                null, null, "<init>", className,
                                il, cpg);

        // Call the constructor in the NodeSortRecord superclass
        il.append(InstructionConstants.ALOAD_0);
        il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_RECORD,
                                                     "<init>", "()V")));



        il.append(InstructionConstants.RETURN);

        return init;
    }


    /**
     * Compiles a method that overloads NodeSortRecord.extractValueFromDOM()
     */
    private static MethodGenerator compileExtract(Vector sortObjects,
                                         NodeSortRecordGenerator sortRecord,
                                         ConstantPoolGen cpg,
                                         String className) {
        final InstructionList il = new InstructionList();

        // String NodeSortRecord.extractValueFromDOM(dom,node,level);
        final CompareGenerator extractMethod =
            new CompareGenerator(ACC_PUBLIC | ACC_FINAL,
                                 jaxp.sun.org.apache.bcel.internal.generic.Type.STRING,
                                 new jaxp.sun.org.apache.bcel.internal.generic.Type[] {
                                     Util.getJCRefType(DOM_INTF_SIG),
                                     jaxp.sun.org.apache.bcel.internal.generic.Type.INT,
                                     jaxp.sun.org.apache.bcel.internal.generic.Type.INT,
                                     Util.getJCRefType(TRANSLET_SIG),
                                     jaxp.sun.org.apache.bcel.internal.generic.Type.INT
                                 },
                                 new String[] { "dom",
                                                "current",
                                                "level",
                                                "translet",
                                                "last"
                                 },
                                 "extractValueFromDOM", className, il, cpg);

        // Values needed for the switch statement
        final int levels = sortObjects.size();
        final int match[] = new int[levels];
        final InstructionHandle target[] = new InstructionHandle[levels];
        InstructionHandle tblswitch = null;

        // Compile switch statement only if the key has multiple levels
        if (levels > 1) {
            // Put the parameter to the swtich statement on the stack
            il.append(new ILOAD(extractMethod.getLocalIndex("level")));
            // Append the switch statement here later on
            tblswitch = il.append(new NOP());
        }

        // Append all the cases for the switch statment
        for (int level = 0; level < levels; level++) {
            match[level] = level;
            final Sort sort = (Sort)sortObjects.elementAt(level);
            target[level] = il.append(InstructionConstants.NOP);
            sort.translateSelect(sortRecord, extractMethod);
            il.append(InstructionConstants.ARETURN);
        }

        // Compile def. target for switch statement if key has multiple levels
        if (levels > 1) {
            // Append the default target - it will _NEVER_ be reached
            InstructionHandle defaultTarget =
                il.append(new PUSH(cpg, EMPTYSTRING));
            il.insert(tblswitch,new TABLESWITCH(match, target, defaultTarget));
            il.append(InstructionConstants.ARETURN);
        }

        return extractMethod;
    }
}
