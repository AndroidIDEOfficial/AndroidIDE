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
 * $Id: Template.java,v 1.2.4.1 2005/09/12 11:30:11 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import jaxp.sun.org.apache.xml.internal.utils.XML11Char;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 */
public final class Template extends TopLevelElement {

    private QName   _name;     // The name of the template (if any)
    private QName   _mode;     // Mode in which this template is instantiated.
    private Pattern _pattern;  // Matching pattern defined for this template.
    private double  _priority; // Matching priority of this template.
    private int     _position; // Position within stylesheet (prio. resolution)
    private boolean _disabled = false;
    private boolean _compiled = false;//make sure it is compiled only once
    private boolean _simplified = false;

    // True if this is a simple named template. A simple named
    // template is a template which only has a name but no match pattern.
    private boolean _isSimpleNamedTemplate = false;

    // The list of parameters in this template. This is only used
    // for simple named templates.
    private Vector  _parameters = new Vector();

    public boolean hasParams() {
        return _parameters.size() > 0;
    }

    public boolean isSimplified() {
        return(_simplified);
    }

    public void setSimplified() {
        _simplified = true;
    }

    public boolean isSimpleNamedTemplate() {
        return _isSimpleNamedTemplate;
    }

    public void addParameter(Param param) {
        _parameters.addElement(param);
    }

    public Vector getParameters() {
        return _parameters;
    }

    public void disable() {
        _disabled = true;
    }

    public boolean disabled() {
        return(_disabled);
    }

    public double getPriority() {
        return _priority;
    }

    public int getPosition() {
        return(_position);
    }

    public boolean isNamed() {
        return _name != null;
    }

    public Pattern getPattern() {
        return _pattern;
    }

    public QName getName() {
        return _name;
    }

    public void setName(QName qname) {
        if (_name == null) _name = qname;
    }

    public QName getModeName() {
        return _mode;
    }

    /**
     * Compare this template to another. First checks priority, then position.
     */
    public int compareTo(Object template) {
        Template other = (Template)template;
        if (_priority > other._priority)
            return 1;
        else if (_priority < other._priority)
            return -1;
        else if (_position > other._position)
            return 1;
        else if (_position < other._position)
            return -1;
        else
            return 0;
    }

    public void display(int indent) {
        Util.println('\n');
        indent(indent);
        if (_name != null) {
            indent(indent);
            Util.println("name = " + _name);
        }
        else if (_pattern != null) {
            indent(indent);
            Util.println("match = " + _pattern.toString());
        }
        if (_mode != null) {
            indent(indent);
            Util.println("mode = " + _mode);
        }
        displayContents(indent + IndentIncrement);
    }

    private boolean resolveNamedTemplates(Template other, Parser parser) {

        if (other == null) return true;

        SymbolTable stable = parser.getSymbolTable();

        final int us = this.getImportPrecedence();
        final int them = other.getImportPrecedence();

        if (us > them) {
            other.disable();
            return true;
        }
        else if (us < them) {
            stable.addTemplate(other);
            this.disable();
            return true;
        }
        else {
            return false;
        }
    }

    private Stylesheet _stylesheet = null;

    public Stylesheet getStylesheet() {
        return _stylesheet;
    }

    public void parseContents(Parser parser) {

        final String name     = getAttribute("name");
        final String mode     = getAttribute("mode");
        final String match    = getAttribute("match");
        final String priority = getAttribute("priority");

        _stylesheet = super.getStylesheet();

        if (name.length() > 0) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, name, this);
                parser.reportError(ERROR, err);
            }
            _name = parser.getQNameIgnoreDefaultNs(name);
        }

        if (mode.length() > 0) {
            if (!XML11Char.isXML11ValidQName(mode)) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, mode, this);
                parser.reportError(ERROR, err);
            }
            _mode = parser.getQNameIgnoreDefaultNs(mode);
        }

        if (match.length() > 0) {
            _pattern = parser.parsePattern(this, "match", null);
        }

        if (priority.length() > 0) {
            _priority = Double.parseDouble(priority);
        }
        else {
            if (_pattern != null)
                _priority = _pattern.getPriority();
            else
                _priority = Double.NaN;
        }

        _position = parser.getTemplateIndex();

        // Add the (named) template to the symbol table
        if (_name != null) {
            Template other = parser.getSymbolTable().addTemplate(this);
            if (!resolveNamedTemplates(other, parser)) {
                ErrorMsg err =
                    new ErrorMsg(ErrorMsg.TEMPLATE_REDEF_ERR, _name, this);
                parser.reportError(ERROR, err);
            }
            // Is this a simple named template?
            if (_pattern == null && _mode == null) {
                _isSimpleNamedTemplate = true;
            }
        }

        if (_parent instanceof Stylesheet) {
            ((Stylesheet)_parent).addTemplate(this);
        }

        parser.setTemplate(this);       // set current template
        parseChildren(parser);
        parser.setTemplate(null);       // clear template
    }

    /**
     * When the parser realises that it is dealign with a simplified stylesheet
     * it will create an empty Stylesheet object with the root element of the
     * stylesheet (a LiteralElement object) as its only child. The Stylesheet
     * object will then create this Template object and invoke this method to
     * force some specific behaviour. What we need to do is:
     *  o) create a pattern matching on the root node
     *  o) add the LRE root node (the only child of the Stylesheet) as our
     *     only child node
     *  o) set the empty Stylesheet as our parent
     *  o) set this template as the Stylesheet's only child
     */
    public void parseSimplified(Stylesheet stylesheet, Parser parser) {

        _stylesheet = stylesheet;
        setParent(stylesheet);

        _name = null;
        _mode = null;
        _priority = Double.NaN;
        _pattern = parser.parsePattern(this, "/");

        final Vector contents = _stylesheet.getContents();
        final SyntaxTreeNode root = (SyntaxTreeNode)contents.elementAt(0);

        if (root instanceof LiteralElement) {
            addElement(root);
            root.setParent(this);
            contents.set(0, this);
            parser.setTemplate(this);
            root.parseContents(parser);
            parser.setTemplate(null);
        }
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (_pattern != null) {
            _pattern.typeCheck(stable);
        }

        return typeCheckContents(stable);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        if (_disabled) return;
        // bug fix #4433133, add a call to named template from applyTemplates
        String className = classGen.getClassName();

        if (_compiled && isNamed()){
            String methodName = Util.escape(_name.toString());
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            il.append(methodGen.loadIterator());
            il.append(methodGen.loadHandler());
            il.append(methodGen.loadCurrentNode());
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(className,
                                                         methodName,
                                                         "("
                                                         + DOM_INTF_SIG
                                                         + NODE_ITERATOR_SIG
                                                         + TRANSLET_OUTPUT_SIG
                                                         + "I)V")));
            return;
        }

        if (_compiled) return;
        _compiled = true;

        // %OPT% Special handling for simple named templates.
        if (_isSimpleNamedTemplate && methodGen instanceof NamedMethodGenerator) {
            int numParams = _parameters.size();
            NamedMethodGenerator namedMethodGen = (NamedMethodGenerator)methodGen;

            // Update load/store instructions to access Params from the stack
            for (int i = 0; i < numParams; i++) {
                Param param = (Param)_parameters.elementAt(i);
                param.setLoadInstruction(namedMethodGen.loadParameter(i));
                param.setStoreInstruction(namedMethodGen.storeParameter(i));
            }
        }

        translateContents(classGen, methodGen);
        il.setPositions(true);
    }

}
