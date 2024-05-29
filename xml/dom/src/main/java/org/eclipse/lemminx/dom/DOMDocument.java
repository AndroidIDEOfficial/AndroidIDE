/**
 *  Copyright (c) 2018-2020 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/Microsoft/vscode-html-languageservice
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.lemminx.dom;

import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.parser.Constants;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NodeList;

/**
 * XML document.
 *
 */
public class DOMDocument extends DOMNode implements Document {

	private SchemaLocation schemaLocation;
	private NoNamespaceSchemaLocation noNamespaceSchemaLocation;

	private boolean referencedExternalGrammarInitialized;
	private boolean referencedSchemaInitialized;
	private final URIResolverExtensionManager resolverExtensionManager;

	private final TextDocument textDocument;
	private boolean hasNamespaces;
	private Map<String, String> externalGrammarLocation;
	private String schemaInstancePrefix;
	private String schemaPrefix;
	private CancelChecker cancelChecker;
	private String externalGrammarFromNamespaceURI;

	public DOMDocument(TextDocument textDocument, URIResolverExtensionManager resolverExtensionManager) {
		super(0, textDocument.getText().length());
		this.textDocument = textDocument;
		this.resolverExtensionManager = resolverExtensionManager;
		resetGrammar();
	}

	public void setCancelChecker(CancelChecker cancelChecker) {
		this.cancelChecker = cancelChecker;
	}

	public CancelChecker getCancelChecker() {
		return cancelChecker;
	}

	public List<DOMNode> getRoots() {
		return super.getChildren();
	}

	public Position positionAt(int offset) throws BadLocationException {
		checkCanceled();
		return textDocument.positionAt(offset);
	}

	public int offsetAt(Position position) throws BadLocationException {
		checkCanceled();
		return textDocument.offsetAt(position);
	}

	public String lineText(int lineNumber) throws BadLocationException {
		checkCanceled();
		return textDocument.lineText(lineNumber);
	}

	public String lineDelimiter(int lineNumber) throws BadLocationException {
		checkCanceled();
		return textDocument.lineDelimiter(lineNumber);
	}

	public LineIndentInfo getLineIndentInfo(int lineNumber) throws BadLocationException {
		String lineText = lineText(lineNumber);
		String lineDelimiter = lineDelimiter(lineNumber);
		String whitespacesIndent = StringUtils.getStartWhitespaces(lineText);
		return new LineIndentInfo(lineDelimiter, whitespacesIndent);
	}

	/**
	 * Returns the element name on the left of the given position and null
	 * otherwise.
	 * 
	 * @param textOffset
	 * @return the element name on the left of the given position and null
	 *         otherwise.
	 */
	public Range getElementNameRangeAt(int textOffset) {
		checkCanceled();
		return textDocument.getWordRangeAt(textOffset, Constants.ELEMENT_NAME_REGEX);
	}

	@Override
	public String getNamespaceURI() {
		DOMElement documentElement = getDocumentElement();
		return documentElement != null ? documentElement.getNamespaceURI() : null;
	}

	/**
	 * Returns the text content of the XML document.
	 * 
	 * @return the text content of the XML document.
	 */
	public String getText() {
		return textDocument.getText();
	}

	public TextDocument getTextDocument() {
		return textDocument;
	}

	/**
	 * Returns true if the document is bound to a grammar and false otherwise.
	 * 
	 * @return true if the document is bound to a grammar and false otherwise.
	 */
	public boolean hasGrammar() {
		return hasDTD() || hasSchemaLocation() || hasNoNamespaceSchemaLocation() || hasExternalGrammar();
	}

	// -------------------------- Grammar with XML Schema

	/**
	 * Returns the declared "xsi:schemaLocation" and null otherwise.
	 * 
	 * @return the declared "xsi:schemaLocation" and null otherwise.
	 */
	public SchemaLocation getSchemaLocation() {
		initializeReferencedSchemaIfNeeded();
		return schemaLocation;
	}

	/**
	 * Returns true if XML root element declares a "xsi:schemaLocation" and false
	 * otherwise.
	 * 
	 * @return true if XML root element declares a "xsi:schemaLocation" and false
	 *         otherwise.
	 */
	public boolean hasSchemaLocation() {
		return getSchemaLocation() != null;
	}

	/**
	 * Returns the declared "xsi:noNamespaceSchemaLocation" and null otherwise.
	 * 
	 * @return the declared "xsi:noNamespaceSchemaLocation" and null otherwise.
	 */
	public NoNamespaceSchemaLocation getNoNamespaceSchemaLocation() {
		initializeReferencedSchemaIfNeeded();
		return noNamespaceSchemaLocation;
	}

	/**
	 * Returns true if XML root element declares a "xsi:noNamespaceSchemaLocation"
	 * and false otherwise.
	 * 
	 * @return true if XML root element declares a "xsi:noNamespaceSchemaLocation"
	 *         and false otherwise.
	 */
	public boolean hasNoNamespaceSchemaLocation() {
		return getNoNamespaceSchemaLocation() != null;
	}

	/**
	 * Returns true if document defines namespaces (with xmlns) and false otherwise.
	 * 
	 * @return true if document defines namespaces (with xmlns) and false otherwise.
	 */
	public boolean hasNamespaces() {
		initializeReferencedSchemaIfNeeded();
		return hasNamespaces;
	}

	/**
	 * Returns the (xsi) schema instance prefix and null otherwise.
	 * 
	 * @return the (xsi) schema instance prefix and null otherwise.
	 */
	public String getSchemaInstancePrefix() {
		initializeReferencedSchemaIfNeeded();
		return schemaInstancePrefix;
	}

	/**
	 * Returns true if (xsi) schema instance prefix exists.
	 * 
	 * @return true if (xsi) schema instance prefix exists.
	 */
	public boolean hasSchemaInstancePrefix() {
		initializeReferencedSchemaIfNeeded();
		return schemaInstancePrefix != null;
	}

	/**
	 * Initialize schemaLocation, noNamespaceSchemaLocation and hasNamespaces
	 * information if needed.
	 */
	private void initializeReferencedSchemaIfNeeded() {
		if (referencedSchemaInitialized) {
			return;
		}
		initializeReferencedSchema();
		referencedSchemaInitialized = true;
	}

	/**
	 * Initialize namespaces and schema location declaration .
	 */
	private synchronized void initializeReferencedSchema() {
		if (referencedSchemaInitialized) {
			return;
		}
		// Get root element
		DOMElement documentElement = getDocumentElement();
		if (documentElement == null) {
			return;
		}
		schemaInstancePrefix = null;
		schemaPrefix = null;
		// Search if document element root declares namespace with "xmlns".
		if (documentElement.hasAttributes()) {
			for (DOMAttr attr : documentElement.getAttributeNodes()) {
				String attributeName = attr.getName();
				if (attributeName != null) {
					if (attributeName.equals(DOMAttr.XMLNS_ATTR)
							|| attributeName.startsWith(DOMAttr.XMLNS_NO_DEFAULT_ATTR)) {
						hasNamespaces = true;
						String attributeValue = documentElement.getAttribute(attributeName);
						if (attributeValue != null && attributeValue.startsWith("http://www.w3.org/")) {
							if (attributeValue.endsWith("/XMLSchema-instance")) {
								schemaInstancePrefix = attributeName.equals("xmlns") ? ""
										: getUnprefixedName(attributeName);
							} else if (attributeValue.endsWith("/XMLSchema")) {
								schemaPrefix = attributeName.equals("xmlns") ? "" : getUnprefixedName(attributeName);
							}
						}
					}
				}
			}
			if (schemaInstancePrefix != null) {
				// DOM document can declared xsi:noNamespaceSchemaLocation and
				// xsi:schemaLocation both even it's not valid
				noNamespaceSchemaLocation = createNoNamespaceSchemaLocation(documentElement, schemaInstancePrefix);
				schemaLocation = createSchemaLocation(documentElement, schemaInstancePrefix);
			}
		}
	}

	/**
	 * If document has {@code <?xml ... ?>}
	 * 
	 * @return
	 */
	public boolean hasProlog() {
		List<DOMNode> children = getChildren();
		return (children != null && !children.isEmpty() && children.get(0).isProlog());
	}

	/**
	 * If document has {@code <?xml ... ?>}, return this prolog, null otherwise
	 * 
	 * @return prolog DOMNode, if the document has one, null otherwise
	 */
	public DOMNode getProlog() {
		if (hasProlog()) {
			return getChild(0);
		} else {
			return null;
		}
	}

	/**
	 * Returns true, if the given offset is before XML declaration
	 * ({@code <?xml ...?>}), false otherwise.
	 * 
	 * @param offset the offset position in question
	 * @return true, if before xml declaration; false otherwise
	 */
	public boolean isBeforeProlog(int offset) {
		return hasProlog() && offset <= getProlog().getStart();
	}

	private static SchemaLocation createSchemaLocation(DOMNode root, String schemaInstancePrefix) {
		DOMAttr attr = root.getAttributeNode(getPrefixedName(schemaInstancePrefix, "schemaLocation"));
		// Check that the attribute and the attribute value of xsi:schemaLocation is not null
		if (attr == null || attr.getNodeAttrValue() == null) {
			return null;
		}
		return new SchemaLocation(attr);
	}

	private static NoNamespaceSchemaLocation createNoNamespaceSchemaLocation(DOMNode root,
			String schemaInstancePrefix) {
		DOMAttr attr = root.getAttributeNode(getPrefixedName(schemaInstancePrefix, "noNamespaceSchemaLocation"));
		if (attr == null || attr.getValue() == null) {
			return null;
		}
		return new NoNamespaceSchemaLocation(attr);
	}

	// -------------------------- Grammar with DTD

	/**
	 * Returns true if XML document has a DTD declaration and false otherwise.
	 * 
	 * @return true if XML document has a DTD declaration and false otherwise.
	 */
	public boolean hasDTD() {
		return getDoctype() != null;
	}

	// -------------------------- External Grammar (XML file associations, catalog)

	/**
	 * Returns true if the document is bound to an external grammar (XML file
	 * associations, XLM catalog) and false otherwise.
	 * 
	 * @return true if the document is bound to an external grammar (XML file
	 *         associations, XLM catalog) and false otherwise.
	 */
	public boolean hasExternalGrammar() {
		return getExternalGrammarLocation() != null || getExternalGrammarFromNamespaceURI() != null;
	}

	/**
	 * Returns the external grammar location (XSD, DTD from xml file associations)
	 * and null otherwise.
	 * 
	 * @return the external grammar location (XSD, DTD from xml file associations)
	 *         and null otherwise.
	 */
	public Map<String, String> getExternalGrammarLocation() {
		initializeReferencedExternalGrammarIfNeeded();
		return externalGrammarLocation;
	}

	/**
	 * Returns the grammar location found by the namespace URI from the document
	 * root element (ex : found with XML catalog) and null otherwise.
	 * 
	 * @return the grammar location found by the namespace URI from the document
	 *         root element (ex : found with XML catalog) and null otherwise.
	 */
	public String getExternalGrammarFromNamespaceURI() {
		initializeReferencedExternalGrammarIfNeeded();
		return externalGrammarFromNamespaceURI;
	}

	private void initializeReferencedExternalGrammarIfNeeded() {
		if (referencedExternalGrammarInitialized) {
			return;
		}
		intializeExternalGrammar();
	}

	private synchronized void intializeExternalGrammar() {
		if (referencedExternalGrammarInitialized) {
			return;
		}
		try {
			if (resolverExtensionManager != null) {
				// No grammar found with standard mean, check if it some components like XML
				// file associations bind this XML document to a grammar with external schema
				// location.
				try {
					externalGrammarLocation = resolverExtensionManager
							.getExternalGrammarLocation(new URI(getDocumentURI()));
					if (externalGrammarLocation != null) {
						return;
					}
				} catch (URISyntaxException e) {
					// Do nothing
				}

				// No grammar found with standard mean and external schema location, check if
				// it some components like XML
				// Catalog, XSL and XSD resolvers, etc bind this XML document to a grammar.
				// Get root element
				DOMElement documentElement = getDocumentElement();
				if (documentElement == null) {
					return;
				}
				String namespaceURI = documentElement.getNamespaceURI();
				externalGrammarFromNamespaceURI = resolverExtensionManager.resolve(getDocumentURI(), namespaceURI,
						null);
			}
		} finally {
			referencedExternalGrammarInitialized = true;
		}
	}

	private static String getUnprefixedName(String name) {
		int index = name.indexOf(":"); //$NON-NLS-1$
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}

	private static String getPrefixedName(String prefix, String localName) {
		return prefix != null && prefix.length() > 0 ? prefix + ":" + localName : localName; //$NON-NLS-1$
	}

	public DOMElement createElement(int start, int end) {
		return new DOMElement(start, end);
	}

	public DOMCDATASection createCDataSection(int start, int end) {
		return new DOMCDATASection(start, end);
	}

	public DOMProcessingInstruction createProcessingInstruction(int start, int end) {
		return new DOMProcessingInstruction(start, end);
	}

	public DOMComment createComment(int start, int end) {
		return new DOMComment(start, end);
	}

	public DOMText createText(int start, int end) {
		return new DOMText(start, end);
	}

	public DOMDocumentType createDocumentType(int start, int end) {
		return new DOMDocumentType(start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return DOMNode.DOCUMENT_NODE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return "#document";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getDocumentElement()
	 */
	@Override
	public DOMElement getDocumentElement() {
		List<DOMNode> roots = getRoots();
		if (roots != null) {
			for (DOMNode node : roots) {
				if (node.isElement()) {
					return (DOMElement) node;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getDoctype()
	 */
	@Override
	public DOMDocumentType getDoctype() {
		List<DOMNode> roots = getRoots();
		if (roots != null) {
			for (DOMNode node : roots) {
				if (node.isDoctype()) {
					return (DOMDocumentType) node;
				}
			}
		}
		return null;
	}

	@Override
	public DOMDocument getOwnerDocument() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getDocumentURI()
	 */
	@Override
	public String getDocumentURI() {
		return textDocument.getUri();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#setDocumentURI(java.lang.String)
	 */
	@Override
	public void setDocumentURI(String documentURI) {
		textDocument.setUri(documentURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#adoptNode(org.w3c.dom.Node)
	 */
	@Override
	public DOMNode adoptNode(org.w3c.dom.Node source) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createAttribute(java.lang.String)
	 */
	@Override
	public DOMAttr createAttribute(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createAttributeNS(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public DOMAttr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createCDATASection(java.lang.String)
	 */
	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createComment(java.lang.String)
	 */
	@Override
	public DOMComment createComment(String data) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createDocumentFragment()
	 */
	@Override
	public DocumentFragment createDocumentFragment() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createElement(java.lang.String)
	 */
	@Override
	public DOMElement createElement(String tagName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createElementNS(java.lang.String, java.lang.String)
	 */
	@Override
	public DOMElement createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createEntityReference(java.lang.String)
	 */
	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createProcessingInstruction(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public DOMProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#createTextNode(java.lang.String)
	 */
	@Override
	public DOMText createTextNode(String data) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getDomConfig()
	 */
	@Override
	public DOMConfiguration getDomConfig() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getElementById(java.lang.String)
	 */
	@Override
	public DOMElement getElementById(String elementId) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getElementsByTagName(java.lang.String)
	 */
	@Override
	public NodeList getElementsByTagName(String tagname) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getElementsByTagNameNS(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getImplementation()
	 */
	@Override
	public DOMImplementation getImplementation() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getInputEncoding()
	 */
	@Override
	public String getInputEncoding() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getStrictErrorChecking()
	 */
	@Override
	public boolean getStrictErrorChecking() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getXmlEncoding()
	 */
	@Override
	public String getXmlEncoding() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getXmlStandalone()
	 */
	@Override
	public boolean getXmlStandalone() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#getXmlVersion()
	 */
	@Override
	public String getXmlVersion() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#importNode(org.w3c.dom.Node, boolean)
	 */
	@Override
	public DOMNode importNode(org.w3c.dom.Node importedNode, boolean deep) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#normalizeDocument()
	 */
	@Override
	public void normalizeDocument() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#renameNode(org.w3c.dom.Node, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public DOMNode renameNode(org.w3c.dom.Node n, String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#setStrictErrorChecking(boolean)
	 */
	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#setXmlStandalone(boolean)
	 */
	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Document#setXmlVersion(java.lang.String)
	 */
	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Reset the cached grammar flag.
	 */
	public void resetGrammar() {
		this.referencedExternalGrammarInitialized = false;
		this.referencedSchemaInitialized = false;
	}

	public URIResolverExtensionManager getResolverExtensionManager() {
		return resolverExtensionManager;
	}

	/**
	 * Returns true if the XML document is a DTD and false otherwise.
	 * 
	 * @return true if the XML document is a DTD and false otherwise.
	 */
	public boolean isDTD() {
		String uri = this.getDocumentURI();
		if (DOMUtils.isDTD(uri)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if 'offset' is within an internal DOCTYPE dtd. Else false.
	 *
	 * @param offset
	 * @return
	 */
	public boolean isWithinInternalDTD(int offset) {
		DOMDocumentType doctype = this.getDoctype();
		if (doctype != null && doctype.internalSubset != null) {
			return offset > doctype.internalSubset.start && offset < doctype.internalSubset.end;
		}
		return false;
	}

	public Range getTrimmedRange(Range range) {
		if (range != null) {
			return getTrimmedRange(range.getStart().getColumn(), range.getEnd().getColumn());
		}
		return null;

	}

	public Range getTrimmedRange(int start, int end) {
		String text = getText();
		char c = text.charAt(start);
		while (Character.isWhitespace(c)) {
			start++;
			c = text.charAt(start);
		}
		if (start == end) {
			return null;
		}
		end--;
		c = text.charAt(end);
		while (Character.isWhitespace(c)) {
			end--;
			c = text.charAt(end);
		}
		end++;
		return XMLPositionUtility.createRange(start, end, this);
	}

	/**
	 * Returns the DTD Attribute list for the given element name and empty
	 * otherwise.
	 * 
	 * @param elementName
	 * @return the DTD Attribute list for the given element name and empty
	 *         otherwise.
	 */
	public Collection<DOMNode> findDTDAttrList(String elementName) {
		DOMDocumentType docType = getDoctype();
		if (docType == null || elementName == null) {
			return Collections.emptyList();
		}
		return docType.getChildren().stream().filter(DOMNode::isDTDAttListDecl)
				.filter(n -> elementName.equals(((DTDAttlistDecl) n).getElementName())).collect(Collectors.toList());
	}

	/**
	 * Returns the XML Schema prefix (ex : 'xs' for
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema")
	 * 
	 * @return the XML Schema prefix (ex : 'xs' for
	 *         xmlns:xs="http://www.w3.org/2001/XMLSchema")
	 */
	public String getSchemaPrefix() {
		initializeReferencedSchemaIfNeeded();
		return schemaPrefix;
	}

	private void checkCanceled() {
		if (cancelChecker != null) {
			cancelChecker.checkCanceled();
		}
	}
}
