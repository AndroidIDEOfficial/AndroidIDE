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
 * $Id: SyntaxTreeNode.java,v 1.6 2006/06/06 22:34:33 spericas Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import jaxp.sun.org.apache.bcel.internal.generic.BasicType;
import jaxp.sun.org.apache.bcel.internal.generic.CHECKCAST;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.DUP_X1;
import jaxp.sun.org.apache.bcel.internal.generic.GETFIELD;
import jaxp.sun.org.apache.bcel.internal.generic.ICONST;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.NEW;
import jaxp.sun.org.apache.bcel.internal.generic.NEWARRAY;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.DOM;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author G. Todd Miller
 * @author Morten Jorensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author John Howard <JohnH@schemasoft.com>
 */
public abstract class SyntaxTreeNode implements Constants {

    // Reference to the AST parser
    private Parser _parser;

    // AST navigation pointers
    protected SyntaxTreeNode _parent;          // Parent node
    private Stylesheet       _stylesheet;      // Stylesheet ancestor node
    private Template         _template;        // Template ancestor node
    private final Vector _contents = new Vector(2); // Child nodes

    // Element description data
    protected QName _qname;                    // The element QName
    private int _line;                         // Source file line number
    protected AttributesImpl _attributes = null;   // Attributes of this element
    private   Hashtable _prefixMapping = null; // Namespace declarations

    // Sentinel - used to denote unrecognised syntaxt tree nodes.
    protected static final SyntaxTreeNode Dummy = new AbsolutePathPattern(null);

    // These two are used for indenting nodes in the AST (debug output)
    protected static final int IndentIncrement = 4;
    private static final char[] _spaces =
        "                                                       ".toCharArray();

    /**
     * Creates a new SyntaxTreeNode with a 'null' QName and no source file
     * line number reference.
     */
    public SyntaxTreeNode() {
        _line = 0;
        _qname = null;
    }

    /**
     * Creates a new SyntaxTreeNode with a 'null' QName.
     * @param line Source file line number reference
     */
    public SyntaxTreeNode(int line) {
        _line = line;
        _qname = null;
    }

    /**
     * Creates a new SyntaxTreeNode with no source file line number reference.
     * @param uri The element's namespace URI
     * @param prefix The element's namespace prefix
     * @param local The element's local name
     */
    public SyntaxTreeNode(String uri, String prefix, String local) {
        _line = 0;
        setQName(uri, prefix, local);
    }

    /**
     * Set the source file line number for this element
     * @param line The source file line number.
     */
    protected final void setLineNumber(int line) {
        _line = line;
    }

    /**
     * Get the source file line number for this element. If unavailable, lookup
     * in ancestors.
     *
     * @return The source file line number.
     */
    public final int getLineNumber() {
        if (_line > 0) return _line;
        SyntaxTreeNode parent = getParent();
        return (parent != null) ? parent.getLineNumber() : 0;
    }

    /**
     * Set the QName for the syntax tree node.
     * @param qname The QName for the syntax tree node
     */
    protected void setQName(QName qname) {
        _qname = qname;
    }

    /**
     * Set the QName for the SyntaxTreeNode
     * @param uri The element's namespace URI
     * @param prefix The element's namespace prefix
     * @param local The element's local name
     */
    protected void setQName(String uri, String prefix, String localname) {
        _qname = new QName(uri, prefix, localname);
    }

    /**
     * Set the QName for the SyntaxTreeNode
     * @param qname The QName for the syntax tree node
     */
    protected QName getQName() {
        return(_qname);
    }

    /**
     * Set the attributes for this SyntaxTreeNode.
     * @param attributes Attributes for the element. Must be passed in as an
     *                   implementation of org.xml.sax.Attributes.
     */
    protected void setAttributes(AttributesImpl attributes) {
        _attributes = attributes;
    }

    /**
     * Returns a value for an attribute from the source element.
     * @param qname The QName of the attribute to return.
     * @return The value of the attribute of name 'qname'.
     */
    protected String getAttribute(String qname) {
        if (_attributes == null) {
            return EMPTYSTRING;
        }
        final String value = _attributes.getValue(qname);
        return (value == null || value.equals(EMPTYSTRING)) ?
            EMPTYSTRING : value;
    }

    protected String getAttribute(String prefix, String localName) {
        return getAttribute(prefix + ':' + localName);
    }

    protected boolean hasAttribute(String qname) {
        return (_attributes != null && _attributes.getValue(qname) != null);
    }

    protected void addAttribute(String qname, String value) {
        int index = _attributes.getIndex(qname);
        if (index != -1) {
            _attributes.setAttribute(index, "", Util.getLocalName(qname),
                    qname, "CDATA", value);
        }
        else {
            _attributes.addAttribute("", Util.getLocalName(qname), qname,
                    "CDATA", value);
        }
    }

    /**
     * Returns a list of all attributes declared for the element represented by
     * this syntax tree node.
     * @return Attributes for this syntax tree node
     */
    protected Attributes getAttributes() {
        return(_attributes);
    }

    /**
     * Sets the prefix mapping for the namespaces that were declared in this
     * element. This does not include all prefix mappings in scope, so one
     * may have to check ancestor elements to get all mappings that are in
     * in scope. The prefixes must be passed in as a Hashtable that maps
     * namespace prefixes (String objects) to namespace URIs (also String).
     * @param mapping The Hashtable containing the mappings.
     */
    protected void setPrefixMapping(Hashtable mapping) {
        _prefixMapping = mapping;
    }

    /**
     * Returns a Hashtable containing the prefix mappings that were declared
     * for this element. This does not include all prefix mappings in scope,
     * so one may have to check ancestor elements to get all mappings that are
     * in in scope.
     * @return Prefix mappings (for this element only).
     */
    protected Hashtable getPrefixMapping() {
        return _prefixMapping;
    }

    /**
     * Adds a single prefix mapping to this syntax tree node.
     * @param prefix Namespace prefix.
     * @param uri Namespace URI.
     */
    protected void addPrefixMapping(String prefix, String uri) {
        if (_prefixMapping == null)
            _prefixMapping = new Hashtable();
        _prefixMapping.put(prefix, uri);
    }

    /**
     * Returns any namespace URI that is in scope for a given prefix. This
     * method checks namespace mappings for this element, and if necessary
     * for ancestor elements as well (ie. if the prefix maps to an URI in this
     * scope then you'll definately get the URI from this method).
     * @param prefix Namespace prefix.
     * @return Namespace URI.
     */
    protected String lookupNamespace(String prefix) {
        // Initialise the output (default is 'null' for undefined)
        String uri = null;

        // First look up the prefix/uri mapping in our own hashtable...
        if (_prefixMapping != null)
            uri = (String)_prefixMapping.get(prefix);
        // ... but if we can't find it there we ask our parent for the mapping
        if ((uri == null) && (_parent != null)) {
            uri = _parent.lookupNamespace(prefix);
            if ((prefix == Constants.EMPTYSTRING) && (uri == null))
                uri = Constants.EMPTYSTRING;
        }
        // ... and then we return whatever URI we've got.
        return(uri);
    }

    /**
     * Returns any namespace prefix that is mapped to a prefix in the current
     * scope. This method checks namespace mappings for this element, and if
     * necessary for ancestor elements as well (ie. if the URI is declared
     * within the current scope then you'll definately get the prefix from
     * this method). Note that this is a very slow method and consequentially
     * it should only be used strictly when needed.
     * @param uri Namespace URI.
     * @return Namespace prefix.
     */
    protected String lookupPrefix(String uri) {
        // Initialise the output (default is 'null' for undefined)
        String prefix = null;

        // First look up the prefix/uri mapping in our own hashtable...
        if ((_prefixMapping != null) &&
            (_prefixMapping.contains(uri))) {
            Enumeration prefixes = _prefixMapping.keys();
            while (prefixes.hasMoreElements()) {
                prefix = (String)prefixes.nextElement();
                String mapsTo = (String)_prefixMapping.get(prefix);
                if (mapsTo.equals(uri)) return(prefix);
            }
        }
        // ... but if we can't find it there we ask our parent for the mapping
        else if (_parent != null) {
            prefix = _parent.lookupPrefix(uri);
            if ((uri == Constants.EMPTYSTRING) && (prefix == null))
                prefix = Constants.EMPTYSTRING;
        }
        return(prefix);
    }

    /**
     * Set this node's parser. The parser (the XSLT parser) gives this
     * syntax tree node access to the symbol table and XPath parser.
     * @param parser The XSLT parser.
     */
    protected void setParser(Parser parser) {
        _parser = parser;
    }

    /**
     * Returns this node's XSLT parser.
     * @return The XSLT parser.
     */
    public final Parser getParser() {
        return _parser;
    }

    /**
     * Set this syntax tree node's parent node, if unset. For
     * re-parenting just use <code>node._parent = newparent</code>.
     *
     * @param parent The parent node.
     */
    protected void setParent(SyntaxTreeNode parent) {
        if (_parent == null) _parent = parent;
    }

    /**
     * Returns this syntax tree node's parent node.
     * @return The parent syntax tree node.
     */
    protected final SyntaxTreeNode getParent() {
        return _parent;
    }

    /**
     * Returns 'true' if this syntax tree node is the Sentinal node.
     * @return 'true' if this syntax tree node is the Sentinal node.
     */
    protected final boolean isDummy() {
        return this == Dummy;
    }

    /**
     * Get the import precedence of this element. The import precedence equals
     * the import precedence of the stylesheet in which this element occured.
     * @return The import precedence of this syntax tree node.
     */
    protected int getImportPrecedence() {
        Stylesheet stylesheet = getStylesheet();
        if (stylesheet == null) return Integer.MIN_VALUE;
        return stylesheet.getImportPrecedence();
    }

    /**
     * Get the Stylesheet node that represents the <xsl:stylesheet/> element
     * that this node occured under.
     * @return The Stylesheet ancestor node of this node.
     */
    public Stylesheet getStylesheet() {
        if (_stylesheet == null) {
            SyntaxTreeNode parent = this;
            while (parent != null) {
                if (parent instanceof Stylesheet)
                    return((Stylesheet)parent);
                parent = parent.getParent();
            }
            _stylesheet = (Stylesheet)parent;
        }
        return(_stylesheet);
    }

    /**
     * Get the Template node that represents the <xsl:template/> element
     * that this node occured under. Note that this method will return 'null'
     * for nodes that represent top-level elements.
     * @return The Template ancestor node of this node or 'null'.
     */
    protected Template getTemplate() {
        if (_template == null) {
            SyntaxTreeNode parent = this;
            while ((parent != null) && (!(parent instanceof Template)))
                parent = parent.getParent();
            _template = (Template)parent;
        }
        return(_template);
    }

    /**
     * Returns a reference to the XSLTC (XSLT compiler) in use.
     * @return XSLTC - XSLT compiler.
     */
    protected final XSLTC getXSLTC() {
        return _parser.getXSLTC();
    }

    /**
     * Returns the XSLT parser's symbol table.
     * @return Symbol table.
     */
    protected final SymbolTable getSymbolTable() {
        return (_parser == null) ? null : _parser.getSymbolTable();
    }

    /**
     * Parse the contents of this syntax tree nodes (child nodes, XPath
     * expressions, patterns and functions). The default behaviour is to parser
     * the syntax tree node's children (since there are no common expressions,
     * patterns, etc. that can be handled in this base class.
     * @param parser reference to the XSLT parser
     */
    public void parseContents(Parser parser) {
        parseChildren(parser);
    }

    /**
     * Parse all children of this syntax tree node. This method is normally
     * called by the parseContents() method.
     * @param parser reference to the XSLT parser
     */
    protected final void parseChildren(Parser parser) {

        Vector locals = null;   // only create when needed

        final int count = _contents.size();
        for (int i=0; i<count; i++) {
            SyntaxTreeNode child = (SyntaxTreeNode)_contents.elementAt(i);
            parser.getSymbolTable().setCurrentNode(child);
            child.parseContents(parser);
            // if variable or parameter, add it to scope
            final QName varOrParamName = updateScope(parser, child);
            if (varOrParamName != null) {
                if (locals == null) {
                    locals = new Vector(2);
                }
                locals.addElement(varOrParamName);
            }
        }

        parser.getSymbolTable().setCurrentNode(this);

        // after the last element, remove any locals from scope
        if (locals != null) {
            final int nLocals = locals.size();
            for (int i = 0; i < nLocals; i++) {
                parser.removeVariable((QName)locals.elementAt(i));
            }
        }
    }

    /**
     * Add a node to the current scope and return name of a variable or
     * parameter if the node represents a variable or a parameter.
     */
    protected QName updateScope(Parser parser, SyntaxTreeNode node) {
        if (node instanceof Variable) {
            final Variable var = (Variable)node;
            parser.addVariable(var);
            return var.getName();
        }
        else if (node instanceof Param) {
            final Param param = (Param)node;
            parser.addParameter(param);
            return param.getName();
        }
        else {
            return null;
        }
    }

    /**
     * Type check the children of this node. The type check phase may add
     * coercions (CastExpr) to the AST.
     * @param stable The compiler/parser's symbol table
     */
    public abstract Type typeCheck(SymbolTable stable) throws TypeCheckError;

    /**
     * Call typeCheck() on all child syntax tree nodes.
     * @param stable The compiler/parser's symbol table
     */
    protected Type typeCheckContents(SymbolTable stable) throws TypeCheckError {
        final int n = elementCount();
        for (int i = 0; i < n; i++) {
            SyntaxTreeNode item = (SyntaxTreeNode)_contents.elementAt(i);
            item.typeCheck(stable);
        }
        return Type.Void;
    }

    /**
     * Translate this abstract syntax tree node into JVM bytecodes.
     * @param classGen BCEL Java class generator
     * @param methodGen BCEL Java method generator
     */
    public abstract void translate(ClassGenerator classGen,
                                   MethodGenerator methodGen);

    /**
     * Call translate() on all child syntax tree nodes.
     * @param classGen BCEL Java class generator
     * @param methodGen BCEL Java method generator
     */
    protected void translateContents(ClassGenerator classGen,
                                     MethodGenerator methodGen) {
        // Call translate() on all child nodes
        final int n = elementCount();

        for (int i = 0; i < n; i++) {
            methodGen.markChunkStart();
            final SyntaxTreeNode item = (SyntaxTreeNode)_contents.elementAt(i);
            item.translate(classGen, methodGen);
            methodGen.markChunkEnd();
        }

        // After translation, unmap any registers for any variables/parameters
        // that were declared in this scope. Performing this unmapping in the
        // same AST scope as the declaration deals with the problems of
        // references falling out-of-scope inside the for-each element.
        // (the cause of which being 'lazy' register allocation for references)
        for (int i = 0; i < n; i++) {
            if( _contents.elementAt(i) instanceof VariableBase) {
                final VariableBase var = (VariableBase)_contents.elementAt(i);
                var.unmapRegister(methodGen);
            }
        }
    }

    /**
     * Return true if the node represents a simple RTF.
     *
     * A node is a simple RTF if all children only produce Text value.
     *
     * @param node A node
     * @return true if the node content can be considered as a simple RTF.
     */
    private boolean isSimpleRTF(SyntaxTreeNode node) {

        Vector contents = node.getContents();
        for (int i = 0; i < contents.size(); i++) {
            SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
            if (!isTextElement(item, false))
                return false;
        }

        return true;
    }

     /**
     * Return true if the node represents an adaptive RTF.
     *
     * A node is an adaptive RTF if each children is a Text element
     * or it is <xsl:call-template> or <xsl:apply-templates>.
     *
     * @param node A node
     * @return true if the node content can be considered as an adaptive RTF.
     */
    private boolean isAdaptiveRTF(SyntaxTreeNode node) {

        Vector contents = node.getContents();
        for (int i = 0; i < contents.size(); i++) {
            SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
            if (!isTextElement(item, true))
                return false;
        }

        return true;
    }

    /**
     * Return true if the node only produces Text content.
     *
     * A node is a Text element if it is Text, xsl:value-of, xsl:number,
     * or a combination of these nested in a control instruction (xsl:if or
     * xsl:choose).
     *
     * If the doExtendedCheck flag is true, xsl:call-template and xsl:apply-templates
     * are also considered as Text elements.
     *
     * @param node A node
     * @param doExtendedCheck If this flag is true, <xsl:call-template> and
     * <xsl:apply-templates> are also considered as Text elements.
     *
     * @return true if the node of Text type
     */
    private boolean isTextElement(SyntaxTreeNode node, boolean doExtendedCheck) {
        if (node instanceof ValueOf || node instanceof Number
            || node instanceof Text)
        {
            return true;
        }
        else if (node instanceof If) {
            return doExtendedCheck ? isAdaptiveRTF(node) : isSimpleRTF(node);
        }
        else if (node instanceof Choose) {
            Vector contents = node.getContents();
            for (int i = 0; i < contents.size(); i++) {
                SyntaxTreeNode item = (SyntaxTreeNode)contents.elementAt(i);
                if (item instanceof Text ||
                     ((item instanceof When || item instanceof Otherwise)
                     && ((doExtendedCheck && isAdaptiveRTF(item))
                         || (!doExtendedCheck && isSimpleRTF(item)))))
                    continue;
                else
                    return false;
            }
            return true;
        }
        else if (doExtendedCheck &&
                  (node instanceof CallTemplate
                   || node instanceof ApplyTemplates))
            return true;
        else
            return false;
    }

    /**
     * Utility method used by parameters and variables to store result trees
     * @param classGen BCEL Java class generator
     * @param methodGen BCEL Java method generator
     */
    protected void compileResultTree(ClassGenerator classGen,
                                     MethodGenerator methodGen)
    {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();
        final Stylesheet stylesheet = classGen.getStylesheet();

        boolean isSimple = isSimpleRTF(this);
        boolean isAdaptive = false;
        if (!isSimple) {
            isAdaptive = isAdaptiveRTF(this);
        }

        int rtfType = isSimple ? DOM.SIMPLE_RTF
                               : (isAdaptive ? DOM.ADAPTIVE_RTF : DOM.TREE_RTF);

        // Save the current handler base on the stack
        il.append(methodGen.loadHandler());

        final String DOM_CLASS = classGen.getDOMClass();

        // Create new instance of DOM class (with RTF_INITIAL_SIZE nodes)
        //int index = cpg.addMethodref(DOM_IMPL, "<init>", "(I)V");
        //il.append(new NEW(cpg.addClass(DOM_IMPL)));

        il.append(methodGen.loadDOM());
        int index = cpg.addInterfaceMethodref(DOM_INTF,
                                 "getResultTreeFrag",
                                 "(IIZ)" + DOM_INTF_SIG);
        il.append(new PUSH(cpg, RTF_INITIAL_SIZE));
        il.append(new PUSH(cpg, rtfType));
        il.append(new PUSH(cpg, stylesheet.callsNodeset()));
        il.append(new INVOKEINTERFACE(index,4));

        il.append(DUP);

        // Overwrite old handler with DOM handler
        index = cpg.addInterfaceMethodref(DOM_INTF,
                                 "getOutputDomBuilder",
                                 "()" + TRANSLET_OUTPUT_SIG);

        il.append(new INVOKEINTERFACE(index,1));
        il.append(DUP);
        il.append(methodGen.storeHandler());

        // Call startDocument on the new handler
        il.append(methodGen.startDocument());

        // Instantiate result tree fragment
        translateContents(classGen, methodGen);

        // Call endDocument on the new handler
        il.append(methodGen.loadHandler());
        il.append(methodGen.endDocument());

        // Check if we need to wrap the DOMImpl object in a DOMAdapter object.
        // DOMAdapter is not needed if the RTF is a simple RTF and the nodeset()
        // function is not used.
        if (stylesheet.callsNodeset()
            && !DOM_CLASS.equals(DOM_IMPL_CLASS)) {
            // new com.sun.org.apache.xalan.internal.xsltc.dom.DOMAdapter(DOMImpl,String[]);
            index = cpg.addMethodref(DOM_ADAPTER_CLASS,
                                     "<init>",
                                     "("+DOM_INTF_SIG+
                                     "["+STRING_SIG+
                                     "["+STRING_SIG+
                                     "[I"+
                                     "["+STRING_SIG+")V");
            il.append(new NEW(cpg.addClass(DOM_ADAPTER_CLASS)));
            il.append(new DUP_X1());
            il.append(SWAP);

            /*
             * Give the DOM adapter an empty type mapping if the nodeset
             * extension function is never called.
             */
            if (!stylesheet.callsNodeset()) {
                il.append(new ICONST(0));
                il.append(new ANEWARRAY(cpg.addClass(STRING)));
                il.append(DUP);
                il.append(DUP);
                il.append(new ICONST(0));
                il.append(new NEWARRAY(BasicType.INT));
                il.append(SWAP);
                il.append(new INVOKESPECIAL(index));
            }
            else {
                // Push name arrays on the stack
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
                                           NAMES_INDEX,
                                           NAMES_INDEX_SIG)));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
                                           URIS_INDEX,
                                           URIS_INDEX_SIG)));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
                                           TYPES_INDEX,
                                           TYPES_INDEX_SIG)));
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
                                           NAMESPACE_INDEX,
                                           NAMESPACE_INDEX_SIG)));

                // Initialized DOM adapter
                il.append(new INVOKESPECIAL(index));

                // Add DOM adapter to MultiDOM class by calling addDOMAdapter()
                il.append(DUP);
                il.append(methodGen.loadDOM());
                il.append(new CHECKCAST(cpg.addClass(classGen.getDOMClass())));
                il.append(SWAP);
                index = cpg.addMethodref(MULTI_DOM_CLASS,
                                         "addDOMAdapter",
                                         "(" + DOM_ADAPTER_SIG + ")I");
                il.append(new INVOKEVIRTUAL(index));
                il.append(POP);         // ignore mask returned by addDOMAdapter
            }
        }

        // Restore old handler base from stack
        il.append(SWAP);
        il.append(methodGen.storeHandler());
    }

    /**
     * Returns true if this expression/instruction depends on the context. By
     * default, every expression/instruction depends on the context unless it
     * overrides this method. Currently used to determine if result trees are
     * compiled using procedures or little DOMs (result tree fragments).
     * @return 'true' if this node depends on the context.
     */
    protected boolean contextDependent() {
        return true;
    }

    /**
     * Return true if any of the expressions/instructions in the contents of
     * this node is context dependent.
     * @return 'true' if the contents of this node is context dependent.
     */
    protected boolean dependentContents() {
        final int n = elementCount();
        for (int i = 0; i < n; i++) {
            final SyntaxTreeNode item = (SyntaxTreeNode)_contents.elementAt(i);
            if (item.contextDependent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a child node to this syntax tree node.
     * @param element is the new child node.
     */
    protected final void addElement(SyntaxTreeNode element) {
        _contents.addElement(element);
        element.setParent(this);
    }

    /**
     * Inserts the first child node of this syntax tree node. The existing
     * children are shifted back one position.
     * @param element is the new child node.
     */
    protected final void setFirstElement(SyntaxTreeNode element) {
        _contents.insertElementAt(element,0);
        element.setParent(this);
    }

    /**
     * Removed a child node of this syntax tree node.
     * @param element is the child node to remove.
     */
    protected final void removeElement(SyntaxTreeNode element) {
        _contents.remove(element);
        element.setParent(null);
    }

    /**
     * Returns a Vector containing all the child nodes of this node.
     * @return A Vector containing all the child nodes of this node.
     */
    protected final Vector getContents() {
        return _contents;
    }

    /**
     * Tells you if this node has any child nodes.
     * @return 'true' if this node has any children.
     */
    protected final boolean hasContents() {
        return elementCount() > 0;
    }

    /**
     * Returns the number of children this node has.
     * @return Number of child nodes.
     */
    protected final int elementCount() {
        return _contents.size();
    }

    /**
     * Returns an Enumeration of all child nodes of this node.
     * @return An Enumeration of all child nodes of this node.
     */
    protected final Enumeration elements() {
        return _contents.elements();
    }

    /**
     * Returns a child node at a given position.
     * @param pos The child node's position.
     * @return The child node.
     */
    protected final Object elementAt(int pos) {
        return _contents.elementAt(pos);
    }

    /**
     * Returns this element's last child
     * @return The child node.
     */
    protected final SyntaxTreeNode lastChild() {
        if (_contents.size() == 0) return null;
        return (SyntaxTreeNode)_contents.lastElement();
    }

    /**
     * Displays the contents of this syntax tree node (to stdout).
     * This method is intended for debugging _only_, and should be overridden
     * by all syntax tree node implementations.
     * @param indent Indentation level for syntax tree levels.
     */
    public void display(int indent) {
        displayContents(indent);
    }

    /**
     * Displays the contents of this syntax tree node (to stdout).
     * This method is intended for debugging _only_ !!!
     * @param indent Indentation level for syntax tree levels.
     */
    protected void displayContents(int indent) {
        final int n = elementCount();
        for (int i = 0; i < n; i++) {
            SyntaxTreeNode item = (SyntaxTreeNode)_contents.elementAt(i);
            item.display(indent);
        }
    }

    /**
     * Set the indentation level for debug output.
     * @param indent Indentation level for syntax tree levels.
     */
    protected final void indent(int indent) {
        System.out.print(new String(_spaces, 0, indent));
    }

    /**
     * Report an error to the parser.
     * @param element The element in which the error occured (normally 'this'
     * but it could also be an expression/pattern/etc.)
     * @param parser The XSLT parser to report the error to.
     * @param error The error code (from util/ErrorMsg).
     * @param message Any additional error message.
     */
    protected void reportError(SyntaxTreeNode element, Parser parser,
                               String errorCode, String message) {
        final ErrorMsg error = new ErrorMsg(errorCode, message, element);
        parser.reportError(Constants.ERROR, error);
    }

    /**
     * Report a recoverable error to the parser.
     * @param element The element in which the error occured (normally 'this'
     * but it could also be an expression/pattern/etc.)
     * @param parser The XSLT parser to report the error to.
     * @param error The error code (from util/ErrorMsg).
     * @param message Any additional error message.
     */
    protected  void reportWarning(SyntaxTreeNode element, Parser parser,
                                  String errorCode, String message) {
        final ErrorMsg error = new ErrorMsg(errorCode, message, element);
        parser.reportError(Constants.WARNING, error);
    }

}
