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
 * $Id: XslAttribute.java,v 1.2.4.1 2005/09/12 11:39:32 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.ASTORE;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

import jaxp.sun.org.apache.xml.internal.serializer.ElemDesc;
import jaxp.sun.org.apache.xml.internal.serializer.SerializationHandler;
import jaxp.sun.org.apache.xml.internal.utils.XML11Char;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author Gunnlaugur Briem <gthb@dimon.is>
 */
final class XslAttribute extends Instruction {

    private String _prefix;
    private AttributeValue _name;       // name treated as AVT (7.1.3)
    private AttributeValueTemplate _namespace = null;
    private boolean _ignore = false;
    private boolean _isLiteral = false;  // specified name is not AVT

    /**
     * Returns the name of the attribute
     */
    public AttributeValue getName() {
        return _name;
    }

    /**
     * Displays the contents of the attribute
     */
    public void display(int indent) {
        indent(indent);
        Util.println("Attribute " + _name);
        displayContents(indent + IndentIncrement);
    }

    /**
     * Parses the attribute's contents. Special care taken for namespaces.
     */
    public void parseContents(Parser parser) {
        boolean generated = false;
        final SymbolTable stable = parser.getSymbolTable();

        String name = getAttribute("name");
        String namespace = getAttribute("namespace");
        QName qname = parser.getQName(name, false);
        final String prefix = qname.getPrefix();

        if (((prefix != null) && (prefix.equals(XMLNS_PREFIX)))||(name.equals(XMLNS_PREFIX))) {
            reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
            return;
        }

        _isLiteral = Util.isLiteral(name);
        if (_isLiteral) {
            if (!XML11Char.isXML11ValidQName(name)) {
                reportError(this, parser, ErrorMsg.ILLEGAL_ATTR_NAME_ERR, name);
                return;
            }
        }

        // Ignore attribute if preceeded by some other type of element
        final SyntaxTreeNode parent = getParent();
        final Vector siblings = parent.getContents();
        for (int i = 0; i < parent.elementCount(); i++) {
            SyntaxTreeNode item = (SyntaxTreeNode)siblings.elementAt(i);
            if (item == this) break;

            // These three objects result in one or more attribute output
            if (item instanceof XslAttribute) continue;
            if (item instanceof UseAttributeSets) continue;
            if (item instanceof LiteralAttribute) continue;
            if (item instanceof Text) continue;

            // These objects _can_ result in one or more attribute
            // The output handler will generate an error if not (at runtime)
            if (item instanceof If) continue;
            if (item instanceof Choose) continue;
            if (item instanceof CopyOf) continue;
            if (item instanceof VariableBase) continue;

            // Report warning but do not ignore attribute
            reportWarning(this, parser, ErrorMsg.STRAY_ATTRIBUTE_ERR, name);
        }

        // Get namespace from namespace attribute?
        if (namespace != null && namespace != EMPTYSTRING) {
            _prefix = lookupPrefix(namespace);
            _namespace = new AttributeValueTemplate(namespace, parser, this);
        }
        // Get namespace from prefix in name attribute?
        else if (prefix != null && prefix != EMPTYSTRING) {
            _prefix = prefix;
            namespace = lookupNamespace(prefix);
            if (namespace != null) {
                _namespace = new AttributeValueTemplate(namespace, parser, this);
            }
        }

        // Common handling for namespaces:
        if (_namespace != null) {
            // Generate prefix if we have none
            if (_prefix == null || _prefix == EMPTYSTRING) {
                if (prefix != null) {
                    _prefix = prefix;
                }
                else {
                    _prefix = stable.generateNamespacePrefix();
                    generated = true;
                }
            }
            else if (prefix != null && !prefix.equals(_prefix)) {
                _prefix = prefix;
            }

            name = _prefix + ":" + qname.getLocalPart();

            /*
             * TODO: The namespace URI must be passed to the parent
             * element but we don't yet know what the actual URI is
             * (as we only know it as an attribute value template).
             */
            if ((parent instanceof LiteralElement) && (!generated)) {
                ((LiteralElement)parent).registerNamespace(_prefix,
                                                           namespace,
                                                           stable, false);
            }
        }

        if (parent instanceof LiteralElement) {
            ((LiteralElement)parent).addAttribute(this);
        }

        _name = AttributeValue.create(this, name, parser);
        parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (!_ignore) {
            _name.typeCheck(stable);
            if (_namespace != null) {
                _namespace.typeCheck(stable);
            }
            typeCheckContents(stable);
        }
        return Type.Void;
    }

    /**
     *
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (_ignore) return;
        _ignore = true;

        // Compile code that emits any needed namespace declaration
        if (_namespace != null) {
            // public void attribute(final String name, final String value)
            il.append(methodGen.loadHandler());
            il.append(new PUSH(cpg,_prefix));
            _namespace.translate(classGen,methodGen);
            il.append(methodGen.namespace());
        }

        if (!_isLiteral) {
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
            final int check = cpg.addMethodref(BASIS_LIBRARY_CLASS, "checkAttribQName",
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

        // Push attribute value - shortcut for literal strings
        if ((elementCount() == 1) && (elementAt(0) instanceof Text)) {
            il.append(new PUSH(cpg, ((Text)elementAt(0)).getText()));
        }
        else {
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
        }

        SyntaxTreeNode parent = getParent();
        if (parent instanceof LiteralElement
            && ((LiteralElement)parent).allAttributesUnique()) {
            int flags = 0;
            ElemDesc elemDesc = ((LiteralElement)parent).getElemDesc();

            // Set the HTML flags
            if (elemDesc != null && _name instanceof SimpleAttributeValue) {
                String attrName = ((SimpleAttributeValue)_name).toString();
                if (elemDesc.isAttrFlagSet(attrName, ElemDesc.ATTREMPTY)) {
                    flags = flags | SerializationHandler.HTML_ATTREMPTY;
                }
                else if (elemDesc.isAttrFlagSet(attrName, ElemDesc.ATTRURL)) {
                    flags = flags | SerializationHandler.HTML_ATTRURL;
                }
            }
            il.append(new PUSH(cpg, flags));
            il.append(methodGen.uniqueAttribute());
        }
        else {
            // call "attribute"
            il.append(methodGen.attribute());
        }

        // Restore old handler base from stack
        il.append(methodGen.storeHandler());



    }

}
