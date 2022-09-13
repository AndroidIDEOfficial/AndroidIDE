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
 * $Id: XslElement.java,v 1.2.4.1 2005/09/12 11:39:55 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
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
 * @author Morten Jorgensen
 */
final class XslElement extends Instruction {

    private String  _prefix;
    private boolean _ignore = false;
    private boolean _isLiteralName = true;
    private AttributeValueTemplate _name;
    private AttributeValueTemplate _namespace;

    /**
     * Displays the contents of the element
     */
    public void display(int indent) {
        indent(indent);
        Util.println("Element " + _name);
        displayContents(indent + IndentIncrement);
    }

    /**
     * This method is now deprecated. The new implemation of this class
     * never declares the default NS.
     */
    public boolean declaresDefaultNS() {
        return false;
    }

    public void parseContents(Parser parser) {
        final SymbolTable stable = parser.getSymbolTable();

        // Handle the 'name' attribute
        String name = getAttribute("name");
        if (name == EMPTYSTRING) {
            ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR,
                                        name, this);
            parser.reportError(WARNING, msg);
            parseChildren(parser);
            _ignore = true;     // Ignore the element if the QName is invalid
            return;
        }

        // Get namespace attribute
        String namespace = getAttribute("namespace");

        // Optimize compilation when name is known at compile time
        _isLiteralName = Util.isLiteral(name);
        if (_isLiteralName) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.ILLEGAL_ELEM_NAME_ERR,
                                            name, this);
                parser.reportError(WARNING, msg);
                parseChildren(parser);
                _ignore = true;         // Ignore the element if the QName is invalid
                return;
            }

            final QName qname = parser.getQNameSafe(name);
            String prefix = qname.getPrefix();
            String local = qname.getLocalPart();

            if (prefix == null) {
                prefix = EMPTYSTRING;
            }

            if (!hasAttribute("namespace")) {
                namespace = lookupNamespace(prefix);
                if (namespace == null) {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.NAMESPACE_UNDEF_ERR,
                                                prefix, this);
                    parser.reportError(WARNING, err);
                    parseChildren(parser);
                    _ignore = true;     // Ignore the element if prefix is undeclared
                    return;
                }
                _prefix = prefix;
                _namespace = new AttributeValueTemplate(namespace, parser, this);
            }
            else {
                if (prefix == EMPTYSTRING) {
                    if (Util.isLiteral(namespace)) {
                        prefix = lookupPrefix(namespace);
                        if (prefix == null) {
                            prefix = stable.generateNamespacePrefix();
                        }
                    }

                    // Prepend prefix to local name
                    final StringBuffer newName = new StringBuffer(prefix);
                    if (prefix != EMPTYSTRING) {
                        newName.append(':');
                    }
                    name = newName.append(local).toString();
                }
                _prefix = prefix;
                _namespace = new AttributeValueTemplate(namespace, parser, this);
            }
        }
        else {
            _namespace = (namespace == EMPTYSTRING) ? null :
                         new AttributeValueTemplate(namespace, parser, this);
        }

        _name = new AttributeValueTemplate(name, parser, this);

        final String useSets = getAttribute("use-attribute-sets");
        if (useSets.length() > 0) {
            if (!Util.isValidQNames(useSets)) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, useSets, this);
                parser.reportError(Constants.ERROR, err);
            }
            setFirstElement(new UseAttributeSets(useSets, parser));
        }

        parseChildren(parser);
    }

    /**
     * Run type check on element name & contents
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!_ignore) {
            _name.typeCheck(stable);
            if (_namespace != null) {
                _namespace.typeCheck(stable);
            }
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    /**
     * This method is called when the name of the element is known at compile time.
     * In this case, there is no need to inspect the element name at runtime to
     * determine if a prefix exists, needs to be generated, etc.
     */
    public void translateLiteral(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (!_ignore) {
            il.append(methodGen.loadHandler());
            _name.translate(classGen, methodGen);
            il.append(DUP2);
            il.append(methodGen.startElement());

            if (_namespace != null) {
                il.append(methodGen.loadHandler());
                il.append(new PUSH(cpg, _prefix));
                _namespace.translate(classGen,methodGen);
                il.append(methodGen.namespace());
            }
        }

        translateContents(classGen, methodGen);

        if (!_ignore) {
            il.append(methodGen.endElement());
        }
    }

    /**
     * At runtime the compilation of xsl:element results in code that: (i)
     * evaluates the avt for the name, (ii) checks for a prefix in the name
     * (iii) generates a new prefix and create a new qname when necessary
     * (iv) calls startElement() on the handler (v) looks up a uri in the XML
     * when the prefix is not known at compile time (vi) calls namespace()
     * on the handler (vii) evaluates the contents (viii) calls endElement().
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        LocalVariableGen local = null;
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        // Optimize translation if element name is a literal
        if (_isLiteralName) {
            translateLiteral(classGen, methodGen);
            return;
        }

        if (!_ignore) {

            // if the qname is an AVT, then the qname has to be checked at runtime if it is a valid qname
            LocalVariableGen nameValue =
                    methodGen.addLocalVariable2("nameValue",
                                                Util.getJCRefType(STRING_SIG),
                                                null);

            // store the name into a variable first so _name.translate only needs to be called once
            _name.translate(classGen, methodGen);
            nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
            il.append(new ALOAD(nameValue.getIndex()));

            // call checkQName if the name is an AVT
            final int check = cpg.addMethodref(BASIS_LIBRARY_CLASS, "checkQName",
                            "("
                            +STRING_SIG
                            +")V");
            il.append(new INVOKESTATIC(check));

            // Push handler for call to endElement()
            il.append(methodGen.loadHandler());

            // load name value again
            nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));

            if (_namespace != null) {
                _namespace.translate(classGen, methodGen);
            }
            else {
                il.append(ACONST_NULL);
            }

            // Push additional arguments
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadCurrentNode());

            // Invoke BasisLibrary.startXslElemCheckQName()
            il.append(new INVOKESTATIC(
            cpg.addMethodref(BASIS_LIBRARY_CLASS, "startXslElement",
                    "(" + STRING_SIG
                    + STRING_SIG
                    + TRANSLET_OUTPUT_SIG
                    + DOM_INTF_SIG + "I)" + STRING_SIG)));


        }

        translateContents(classGen, methodGen);

        if (!_ignore) {
            il.append(methodGen.endElement());
        }
    }

    /**
     * Override this method to make sure that xsl:attributes are not
     * copied to output if this xsl:element is to be ignored
     */
    public void translateContents(ClassGenerator classGen,
                                  MethodGenerator methodGen) {
        final int n = elementCount();
        for (int i = 0; i < n; i++) {
            final SyntaxTreeNode item =
                (SyntaxTreeNode)getContents().elementAt(i);
            if (_ignore && item instanceof XslAttribute) continue;
            item.translate(classGen, methodGen);
        }
    }

}
