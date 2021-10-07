/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.extensions.dtd.contentmodel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.XMLEntityManager.ScannedEntity;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.DTDEntityDecl;
import org.eclipse.lemminx.extensions.contentmodel.model.CMAttributeDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.CMElementDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.FilesChangedTracker;
import org.eclipse.lemminx.extensions.dtd.utils.DTDUtils;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.w3c.dom.Entity;

/**
 * DTD document.
 * 
 * @author Angelo ZERR
 *
 */
public class CMDTDDocument extends XMLDTDLoader implements CMDocument {

	private static final Logger LOGGER = Logger.getLogger(CMDTDDocument.class.getName());

	static class DTDNodeInfo {

		private String comment;

		public DTDNodeInfo() {
			this.comment = null;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
	}

	static class DTDElementInfo extends DTDNodeInfo {

		private final Set<String> hierarchies;
		private final Map<String, DTDNodeInfo> attributes;

		public DTDElementInfo() {
			this.hierarchies = new LinkedHashSet<>();
			this.attributes = new HashMap<>();
		}

		public Set<String> getHierarchies() {
			return hierarchies;
		}

		public Map<String, DTDNodeInfo> getAttributes() {
			return attributes;
		}

		public String getComment(String attrName) {
			DTDNodeInfo attr = attributes.get(attrName);
			return attr != null ? attr.getComment() : null;
		}
	}

	private static class ScannedDTDEntityDecl extends DTDEntityDecl {

		private final String entityName;
		private final String value;

		private final DTDDeclParameter nameParameter;

		private final String publicId;

		private final String systemId;

		public ScannedDTDEntityDecl(String entityName, String value, ScannedEntity scannedEntity) {
			this(entityName, value, null, scannedEntity);
		}

		public ScannedDTDEntityDecl(String name, XMLResourceIdentifier identifier, ScannedEntity scannedEntity) {
			this(name, null, identifier, scannedEntity);
		}

		private ScannedDTDEntityDecl(String entityName, String value, XMLResourceIdentifier identifier,
				ScannedEntity scannedEntity) {
			super(-1, -1);
			this.entityName = entityName;
			this.value = value;
			this.publicId = identifier != null ? identifier.getPublicId() : null;
			this.systemId = identifier != null ? identifier.getLiteralSystemId() : null;
			this.nameParameter = createNameParameter(entityName, scannedEntity);
		}

		@Override
		public DTDDeclParameter getNameParameter() {
			return nameParameter;
		}

		@Override
		public String getName() {
			return getNodeName();
		}

		@Override
		public String getNodeName() {
			return entityName;
		}

		@Override
		public String getNotationName() {
			return value;
		}

		@Override
		public String getSystemId() {
			return systemId;
		}

		@Override
		public String getPublicId() {
			return publicId;
		}

		private static DTDDeclParameter createNameParameter(String name, ScannedEntity scannedEntity) {
			String systemId = scannedEntity.entityLocation.getExpandedSystemId();
			int lineNumber = scannedEntity.lineNumber - 1;
			int endEntityColumnNumber = scannedEntity.columnNumber - 1;
			int startNameColumnNumber = getEntityNameStartColumnNumber(name, scannedEntity);
			return new DTDDeclParameter(null, -1, -1) {

				@Override
				public Range getTargetRange() {
					if (startNameColumnNumber < 0) {
						// It should never occur, but in case computation of start name column cannot be
						// done, we use the end entity column.
						return new Range(new Position(lineNumber, endEntityColumnNumber),
								new Position(lineNumber, endEntityColumnNumber));
					}
					return new Range(new Position(lineNumber, startNameColumnNumber),
							new Position(lineNumber, startNameColumnNumber + name.length()));
				};

				@Override
				public String getTargetURI() {
					return systemId;
				}
			};
		}

		/**
		 * Returns the colunm number where entity name starts (<!ENTITY |name )
		 * 
		 * @param entityName    the entity name
		 * @param scannedEntity the scanned entity
		 * @return the colunm number where entity name starts (<!ENTITY |name )
		 */
		private static int getEntityNameStartColumnNumber(String entityName, ScannedEntity scannedEntity) {
			// offset of the end of the entity
			int endEntityIndex = scannedEntity.startPosition + scannedEntity.position;
			// char array of the DTD content.
			char[] ch = scannedEntity.ch;
			int wordIndex = entityName.length(); //
			int startEntityNameIndex = -1;
			// Loop for characters from the end of the entity (>) to search the entity name
			// start offset
			// <!ENTITY name .....> |
			for (int i = endEntityIndex; i >= 0; i--) {
				char c = ch[i];
				// current character matches the entity name
				if (c == entityName.charAt(wordIndex - 1)) {
					wordIndex--;
				} else {
					wordIndex = entityName.length();
				}
				if (wordIndex == 0) {
					startEntityNameIndex = i;
					break;
				}
			}
			if (startEntityNameIndex > -1) {
				return scannedEntity.columnNumber - (endEntityIndex - startEntityNameIndex)
						+ scannedEntity.startPosition - 1;
			}
			return -1;
		}
	}

	private final String uri;

	private Map<String, DTDElementInfo> hierarchiesMap;
	private List<CMElementDeclaration> elements;
	private DTDGrammar grammar;
	private Set<String> hierarchies;
	private FilesChangedTracker tracker;
	private String comment;
	private DTDElementInfo dtdElementInfo;
	private Map<String, DTDNodeInfo> attributes;
	private DTDNodeInfo nodeInfo;

	private final List<Entity> entities;

	public CMDTDDocument() {
		this(null);
	}

	public CMDTDDocument(String uri) {
		this.uri = uri;
		this.entities = new ArrayList<>();
	}

	@Override
	public boolean hasNamespace(String namespaceURI) {
		return false;
	}

	@Override
	public Collection<CMElementDeclaration> getElements() {
		if (elements == null) {
			elements = new ArrayList<>();
			// Xerces returns 0 even if there are no element declarations
			int index = grammar.getFirstElementDeclIndex();
			while (index != -1) {
				CMDTDElementDeclaration elementDecl = new CMDTDElementDeclaration(this, index);
				if (grammar.getElementDecl(index, elementDecl)) {
					// case when there are one or several element declarations
					elements.add(elementDecl);
				}
				index = grammar.getNextElementDeclIndex(index);
			}

		}
		return elements;
	}

	/**
	 * Returns the URI of this document, is none was provided this returns null.
	 */
	public String getURI() {
		return uri;
	}

	@Override
	public CMElementDeclaration findCMElement(DOMElement element, String namespace) {
		List<DOMElement> paths = new ArrayList<>();
		while (element != null && (namespace == null || namespace.equals(element.getNamespaceURI()))) {
			paths.add(0, element);
			element = element.getParentNode() instanceof DOMElement ? (DOMElement) element.getParentNode() : null;
		}
		CMElementDeclaration declaration = null;
		for (int i = 0; i < paths.size(); i++) {
			DOMElement elt = paths.get(i);
			if (i == 0) {
				declaration = findElementDeclaration(elt.getLocalName(), namespace);
			} else {
				declaration = declaration.findCMElement(elt.getLocalName(), namespace);
			}
			if (declaration == null) {
				break;
			}
		}
		return declaration;
	}

	private CMElementDeclaration findElementDeclaration(String tag, String namespace) {
		if (tag == null) {
			return null;
		}
		for (CMElementDeclaration cmElement : getElements()) {
			if (tag.equals(cmElement.getName())) {
				return cmElement;
			}
		}
		return null;
	}

	@Override
	public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
			throws XNIException {
		super.internalEntityDecl(name, text, nonNormalizedText, augs);
		try {
			entities.add(new ScannedDTDEntityDecl(name, text.toString(), fEntityManager.getCurrentEntity()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while extracting information for the internal entity '" + name + "'", e);
		}
	}

	@Override
	public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
			throws XNIException {
		super.externalEntityDecl(name, identifier, augs);
		try {
			entities.add(new ScannedDTDEntityDecl(name, identifier, fEntityManager.getCurrentEntity()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while extracting information for the external entity '" + name + "'", e);
		}
	}

	@Override
	public void startContentModel(String elementName, Augmentations augs) throws XNIException {
		if (hierarchiesMap == null) {
			hierarchiesMap = new HashMap<>();
		}
		dtdElementInfo = new DTDElementInfo();
		if (comment != null) {
			dtdElementInfo.setComment(comment);
		}
		hierarchiesMap.put(elementName, dtdElementInfo);
		super.startContentModel(elementName, augs);
	}

	@Override
	public void element(String elementName, Augmentations augs) throws XNIException {
		hierarchies = dtdElementInfo.getHierarchies();
		hierarchies.add(elementName);
		super.element(elementName, augs);
	}

	@Override
	public void endContentModel(Augmentations augs) throws XNIException {
		comment = null;
		hierarchies = null;
		super.endContentModel(augs);
	}

	@Override
	public void startAttlist(String elementName, Augmentations augs) throws XNIException {
		attributes = dtdElementInfo.getAttributes();
		super.startAttlist(elementName, augs);
	}

	@Override
	public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration,
			String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
			throws XNIException {
		if (comment != null) {
			nodeInfo = new DTDNodeInfo();
			nodeInfo.setComment(comment);
			attributes.put(attributeName, nodeInfo);
		}
		super.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue,
				nonNormalizedDefaultValue, augs);
	}

	@Override
	public void endAttlist(Augmentations augs) throws XNIException {
		comment = null;
		attributes = null;
		nodeInfo = null;
		super.endAttlist(augs);
	}

	@Override
	public Grammar loadGrammar(XMLInputSource source) throws IOException, XNIException {
		grammar = (DTDGrammar) super.loadGrammar(source);
		this.tracker = DTDUtils.createFilesChangedTracker(grammar);
		return grammar;
	}

	public void loadInternalDTD(String internalSubset, String baseSystemId, String systemId)
			throws XNIException, IOException {
		// Load empty DTD grammar
		XMLInputSource source = new XMLInputSource("", "", "", new StringReader(""), "");
		grammar = (DTDGrammar) loadGrammar(source);
		// To get the DTD scanner to end at the right place we have to fool
		// it into thinking that it reached the end of the internal subset
		// in a real document.
		fDTDScanner.reset();
		StringBuilder buffer = new StringBuilder(internalSubset.length() + 2);
		buffer.append(internalSubset).append("]>");
		XMLInputSource is = new XMLInputSource(null, baseSystemId, null, new StringReader(buffer.toString()), null);
		fEntityManager.startDocumentEntity(is);
		fDTDScanner.scanDTDInternalSubset(true, false, systemId != null);
	}

	@Override
	public void comment(XMLString text, Augmentations augs) throws XNIException {
		if (text != null) {
			comment = text.toString();
		}
		super.comment(text, augs);
	}

	public Map<String, DTDElementInfo> getHierarchiesMap() {
		return hierarchiesMap;
	}

	void collectElementsDeclaration(String elementName, List<CMElementDeclaration> elements) {
		if (hierarchiesMap == null) {
			return;
		}
		DTDElementInfo elementInfo = hierarchiesMap.get(elementName);
		Set<String> children = elementInfo.getHierarchies();
		if (children == null) {
			return;
		}
		children.stream().forEach(name -> {
			CMElementDeclaration element = findElementDeclaration(name, null);
			if (element != null) {
				elements.add(element);
			}
		});
	}

	void collectAttributesDeclaration(CMDTDElementDeclaration elementDecl, List<CMAttributeDeclaration> attributes) {
		int elementDeclIndex = grammar.getElementDeclIndex(elementDecl.name);
		int index = grammar.getFirstAttributeDeclIndex(elementDeclIndex);
		while (index != -1) {
			CMDTDAttributeDeclaration attributeDecl = new CMDTDAttributeDeclaration(elementDecl);
			grammar.getAttributeDecl(index, attributeDecl);
			attributes.add(attributeDecl);
			index = grammar.getNextAttributeDeclIndex(index);
		}
	}

	@Override
	public LocationLink findTypeLocation(DOMNode node) {
		return null;
	}

	@Override
	public boolean isDirty() {
		return tracker != null ? tracker.isDirty() : null;
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}
}
