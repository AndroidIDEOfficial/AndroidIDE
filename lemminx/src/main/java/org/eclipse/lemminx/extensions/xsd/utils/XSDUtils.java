/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.xsd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.function.BiConsumer;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.xs.StringList;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.model.FilesChangedTracker;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.URIUtils;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;

/**
 * XSD utilities.
 *
 * @author Angelo ZERR
 *
 */
public class XSDUtils {

	public static final String SCHEMA_LOCATION_ATTR = "schemaLocation";
	public static final String TARGET_NAMESPACE_ATTR = "targetNamespace";
	public static final String NAMESPACE_ATTR = "namespace";

	public static final String XS_IMPORT_TAG = "xs:import";
	public static final String XS_SCHEMA_TAG = "xs:schema";

	/**
	 * Binding type of xs attribute.
	 *
	 */
	public enum BindingType {

		COMPLEX, SIMPLE, COMPLEX_AND_SIMPLE, NONE, REF, ELEMENT;

		public boolean isSimple() {
			return BindingType.COMPLEX_AND_SIMPLE.equals(this) || BindingType.SIMPLE.equals(this);
		}

		public boolean isComplex() {
			return BindingType.COMPLEX_AND_SIMPLE.equals(this) || BindingType.COMPLEX.equals(this);
		}
	}

	/**
	 * Returns the binding type of the origin attribute which bounds an another
	 * target attribute.
	 *
	 * @param originAttr the origin attribute
	 * @return the binding type of the origin attribute which bounds an another
	 *         target attribute.
	 */
	public static BindingType getBindingType(DOMAttr originAttr) {
		if (originAttr != null) {
			String name = originAttr.getName();
			if ("type".equals(name)) {
				if ("attribute".equals(originAttr.getOwnerElement().getLocalName())) {
					// - <xs:attribute type="
					return BindingType.SIMPLE;
				}
				// - <xs:element type="
				return BindingType.COMPLEX_AND_SIMPLE;
			}
			if ("base".equals(name)) {
				// - <xs:restriction base="
				// - <xs:extension base="
				DOMElement element = originAttr.getOwnerElement();
				DOMElement parent = element.getParentElement();
				if (parent != null) {
					if (parent.getLocalName().equals("complexContent") || isXSComplexType(parent)) {
						// parent element is complexContent or complexType -> bounded type is complex
						return BindingType.COMPLEX;
					}
					if (parent.getLocalName().equals("simpleContent") || isXSSimpleType(parent)) {
						// parent element is simpleContent or simpleType -> bounded type is simple
						return BindingType.SIMPLE;
					}
				}
				return BindingType.NONE;
			}
			if ("ref".equals(name)) {
				// - <xs:element ref="
				// - <xs:group ref="
				return BindingType.REF;
			}
			if ("itemType".equals(name)) {
				// - <xs:list itemType="
				return BindingType.COMPLEX_AND_SIMPLE;
			}
			if ("memberTypes".equals(name)) {
				// - <xs:union memberTypes="
				return BindingType.COMPLEX_AND_SIMPLE;
			}
			if ("substitutionGroup".equals(name)) {
				// - <xs:element substitutionGroup
				return BindingType.ELEMENT;
			}
		}
		return BindingType.NONE;
	}

	/**
	 * Collect XSD target attributes declared in the XML Schema according the given
	 * attribute and binding type.
	 *
	 * @param originAttr             the origin attribute.
	 * @param matchAttr              true if the attribute value must match the
	 *                               value of target attribute value and false
	 *                               otherwise.
	 * @param searchInExternalSchema true if search must be done in included XML
	 *                               Schema (xs:include) and false otherwise.
	 * @param collector              collector to collect XSD target attributes.
	 */
	public static void searchXSTargetAttributes(DOMAttr originAttr, BindingType bindingType, boolean matchAttr,
			boolean searchInExternalSchema, BiConsumer<String, DOMAttr> collector) {
		if (bindingType == BindingType.NONE) {
			return;
		}

		DOMDocument document = originAttr.getOwnerDocument();
		DOMElement documentElement = document != null ? document.getDocumentElement() : null;
		if (documentElement == null) {
			return;
		}
		String originAttrValue = originAttr.getValue();
		if (matchAttr && StringUtils.isEmpty(originAttrValue)) {
			return;
		}

		// <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
		// xmlns:tns="http://camel.apache.org/schema/spring"
		// targetNamespace="http://camel.apache.org/schema/spring" version="1.0">
		String targetNamespace = documentElement.getAttribute(TARGET_NAMESPACE_ATTR); // ->
																						// http://camel.apache.org/schema/spring
		String targetNamespacePrefix = documentElement.getPrefix(targetNamespace); // -> tns

		String originName = null;
		if (matchAttr) {
			originName = getOriginName(originAttrValue, targetNamespacePrefix);
		}

		// Loop for element complexType.
		searchXSTargetAttributes(originAttr, bindingType, matchAttr, collector, documentElement, targetNamespacePrefix,
				originName, new HashSet<>(), searchInExternalSchema);
	}

	private static void searchXSTargetAttributes(DOMAttr originAttr, BindingType bindingType, boolean matchAttr,
			BiConsumer<String, DOMAttr> collector, DOMElement documentElement, String targetNamespacePrefix,
			String originName, Set<String> visitedURIs, boolean searchInExternalSchema) {
		DOMDocument document = documentElement.getOwnerDocument();
		String documentURI = document.getDocumentURI();
		if (visitedURIs.contains(documentURI)) {
			return;
		}
		visitedURIs.add(documentURI);
		Set<String> externalURIS = null;
		NodeList children = documentElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element targetElement = (Element) node;
				if (isBounded(originAttr.getOwnerElement(), bindingType, targetElement)) {
					// node is a xs:complexType, xs:simpleType element, xsl:element, xs:group which
					// matches the binding type of the originAttr
					DOMAttr targetAttr = (DOMAttr) targetElement.getAttributeNode("name");
					if (targetAttr != null && (!matchAttr || Objects.equal(originName, targetAttr.getValue()))) {
						collector.accept(targetNamespacePrefix, targetAttr);
					}
				} else if (isXSInclude(targetElement)) {
					// collect xs:include XML Schema location
					String schemaLocation = targetElement.getAttribute(SCHEMA_LOCATION_ATTR);
					if (schemaLocation != null) {
						if (externalURIS == null) {
							externalURIS = new HashSet<>();
						}
						externalURIS.add(schemaLocation);
					}
				}
			}
		}
		if (searchInExternalSchema && externalURIS != null) {
			// Search in xs:include XML Schema location
			URIResolverExtensionManager resolverExtensionManager = document.getResolverExtensionManager();
			for (String externalURI : externalURIS) {
				String resourceURI = resolverExtensionManager.resolve(documentURI, null, externalURI);
				if (URIUtils.isFileResource(resourceURI)) {
					DOMDocument externalDocument = DOMUtils.loadDocument(resourceURI,
							document.getResolverExtensionManager());
					if (externalDocument != null) {
						searchXSTargetAttributes(originAttr, bindingType, matchAttr, collector,
								externalDocument.getDocumentElement(), targetNamespacePrefix, originName, visitedURIs,
								searchInExternalSchema);
					}
				}
			}
		}
	}

	private static String getOriginName(String originAttrValue, String targetNamespacePrefix) {
		int index = originAttrValue.indexOf(":");
		if (index != -1) {
			String prefix = originAttrValue.substring(0, index);
			if (!Objects.equal(prefix, targetNamespacePrefix)) {
				return null;
			}
			return originAttrValue.substring(index + 1, originAttrValue.length());
		}
		return originAttrValue;
	}

	private static boolean isBounded(Element originElement, BindingType originBinding, Element targetElement) {
		if (isXSComplexType(targetElement)) {
			return originBinding.isComplex();
		} else if (isXSSimpleType(targetElement)) {
			return originBinding.isSimple();
		} else if (originBinding == BindingType.REF) {
			// - xs:element/@name attributes if originAttr is xs:element/@ref
			// - xs:group/@name attributes if originAttr is xs:group/@ref
			return (originElement.getLocalName().equals(targetElement.getLocalName()));
		} else if (originBinding == BindingType.ELEMENT) {
			return isXSElement(targetElement);
		}
		return false;
	}

	/**
	 * Search origin attributes from the given target node..
	 *
	 * @param targetNode the referenced node
	 * @param collector  the collector to collect reference between an origin and
	 *                   target attribute.
	 */
	public static void searchXSOriginAttributes(DOMNode targetNode, BiConsumer<DOMAttr, DOMAttr> collector,
			CancelChecker cancelChecker) {
		// get referenced attribute nodes from the given referenced node
		List<DOMAttr> targetAttrs = getTargetAttrs(targetNode);
		if (targetAttrs.isEmpty()) {
			// None referenced nodes, stop the search of references
			return;
		}

		// Here referencedNodes is filled with a list of attributes
		// xs:complexType/@name,
		// xs:simpleType/@name, xs:element/@name, xs:group/@name

		DOMDocument document = targetNode.getOwnerDocument();
		DOMElement documentElement = document.getDocumentElement();

		// <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
		// xmlns:tns="http://camel.apache.org/schema/spring"
		// targetNamespace="http://camel.apache.org/schema/spring" version="1.0">
		String targetNamespace = documentElement.getAttribute(TARGET_NAMESPACE_ATTR); // ->
																						// http://camel.apache.org/schema/spring
		String targetNamespacePrefix = documentElement.getPrefix(targetNamespace); // -> tns

		// Collect references for each references nodes

		NodeList nodes = documentElement.getChildNodes();
		searchXSOriginAttributes(nodes, targetAttrs, targetNamespacePrefix, collector, cancelChecker);
	}

	/**
	 * Returns the referenced attributes list from the given referenced node.
	 *
	 * @param referencedNode the referenced node.
	 * @return the referenced attributes list from the given referenced node.
	 */
	private static List<DOMAttr> getTargetAttrs(DOMNode referencedNode) {
		if (referencedNode == null) {
			return Collections.emptyList();
		}
		List<DOMAttr> referencedNodes = new ArrayList<>();
		Document document = referencedNode.getOwnerDocument();
		switch (referencedNode.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			// The referenced node is an attribute, add it to search references from it.
		case Node.ELEMENT_NODE:
			// The referenced node is an element, get the attribute name) and add it to
			// search references from it.
			addTargetNode(referencedNode, referencedNodes);
			break;
		case Node.DOCUMENT_NODE:
			// The referenced node is the DOM document, collect all attributes
			// xs:complexType/@name, xs:simpleType/@name, xs:element/@name, xs:group/@name
			// which can be referenced
			Element documentElement = document.getDocumentElement();
			if (documentElement == null) {
				break;
			}
			NodeList nodes = documentElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					DOMElement element = (DOMElement) n;
					if (isXSTargetElement(element)) {
						addTargetNode(element, referencedNodes);
					}
				}
			}
		}
		return referencedNodes;
	}

	/**
	 * Add the given node as reference node if it is applicable.
	 *
	 * @param node        the node to add.
	 * @param targetAttrs the list of referenced nodes.
	 */
	private static void addTargetNode(DOMNode node, List<DOMAttr> targetAttrs) {
		DOMAttr attr = null;
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			attr = (DOMAttr) node;
			break;
		case Node.ELEMENT_NODE:
			attr = ((DOMElement) node).getAttributeNode("name");
			break;
		}
		// Attribute must exists and her value must be not empty.
		if (attr != null && !StringUtils.isEmpty(attr.getValue())) {
			targetAttrs.add(attr);
		}
	}

	private static void searchXSOriginAttributes(NodeList nodes, List<DOMAttr> targetAttrs,
			String targetNamespacePrefix, BiConsumer<DOMAttr, DOMAttr> collector, CancelChecker cancelChecker) {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (cancelChecker != null) {
				cancelChecker.checkCanceled();
			}
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				DOMElement originElement = (DOMElement) node;
				NamedNodeMap originAttributes = originElement.getAttributes();
				if (originAttributes != null) {
					for (int j = 0; j < originAttributes.getLength(); j++) {
						DOMAttr originAttr = (DOMAttr) originAttributes.item(j);
						BindingType originBnding = XSDUtils.getBindingType(originAttr);
						if (originBnding != BindingType.NONE) {
							String originName = getOriginName(originAttr.getValue(), targetNamespacePrefix);
							for (DOMAttr targetAttr : targetAttrs) {
								Element targetElement = targetAttr.getOwnerElement();
								if (isBounded(originAttr.getOwnerElement(), originBnding, targetElement)) {
									// node is a xs:complexType, xs:simpleType element, xsl:element, xs:group which
									// matches the binding type of the originAttr
									if (targetAttr != null && (Objects.equal(originName, targetAttr.getValue()))) {
										collector.accept(originAttr, targetAttr);
									}
								}
							}
						}
					}
				}
			}
			if (node.hasChildNodes()) {
				searchXSOriginAttributes(node.getChildNodes(), targetAttrs, targetNamespacePrefix, collector,
						cancelChecker);
			}
		}

	}

	public static boolean isXSComplexType(Element element) {
		return element != null && "complexType".equals(element.getLocalName());
	}

	public static boolean isXSSimpleType(Element element) {
		return element != null && "simpleType".equals(element.getLocalName());
	}

	public static boolean isXSElement(Element element) {
		return element != null && "element".equals(element.getLocalName());
	}

	public static boolean isXSGroup(Element element) {
		return element != null && "group".equals(element.getLocalName());
	}

	public static boolean isXSInclude(Element element) {
		return element != null && "include".equals(element.getLocalName());
	}

	public static boolean isXSImport(Element element) {
		return element != null && "import".equals(element.getLocalName());
	}

	public static boolean isXSTargetElement(Element element) {
		return isXSComplexType(element) || isXSSimpleType(element) || isXSElement(element) || isXSGroup(element);
	}

	public static boolean isXSAttribute(DOMElement element) {
		return element != null && "attribute".equals(element.getLocalName());
	}

	public static boolean isXSSchema(Element element) {
		return element != null && "schema".equals(element.getLocalName());
	}

	public static FilesChangedTracker createFilesChangedTracker(SchemaGrammar grammar) {
		return createFilesChangedTracker(Collections.singleton(grammar));
	}

	public static FilesChangedTracker createFilesChangedTracker(Set<SchemaGrammar> grammars) {
		FilesChangedTracker tracker = new FilesChangedTracker();
		Set<SchemaGrammar> trackedGrammars = new HashSet<>();
		Set<String> trackedURIs = new HashSet<>();
		for (SchemaGrammar grammar : grammars) {
			updateTracker(grammar, trackedGrammars, trackedURIs, tracker);
		}
		return tracker;
	}

	private static void updateTracker(SchemaGrammar grammar, Set<SchemaGrammar> trackedGrammars,
			Set<String> trackedURIs, FilesChangedTracker tracker) {
		if (grammar == null || trackedGrammars.contains(grammar)) {
			return;
		}
		trackedGrammars.add(grammar);
		// Loop for all XML Schema (root + included) to track it
		StringList locations = grammar.getDocumentLocations();
		for (int i = 0; i < locations.getLength(); i++) {
			String location = locations.item(i);
			if (!trackedURIs.contains(location)) {
				trackedURIs.add(location);
			}
			if (location != null && URIUtils.isFileResource(location)) {
				// The schema is a file, track when file changed
				tracker.addFileURI(location);
			}
		}
		// Track the imported grammars
		Vector<?> importedGrammars = grammar.getImportedGrammars();
		if (importedGrammars != null) {
			for (Object importedGrammar : importedGrammars) {
				updateTracker((SchemaGrammar) importedGrammar, trackedGrammars, trackedURIs, tracker);
			}
		}
	}

	public static DOMAttr getSchemaLocation(DOMElement element) {
		if (!(isXSInclude(element) || isXSImport(element))) {
			return null;
		}
		return element.getAttributeNode(SCHEMA_LOCATION_ATTR);
	}

	/**
	 * Returns the xsd:import/@schemaLocation or xsd:include/@schemaLocation
	 * declared in the given <code>document</code> by the given
	 * <code>grammarURI</code> and null otherwise.
	 * 
	 * @param document   the XSD document
	 * @param grammarURI the grammar URI to retreive.
	 * @return the xsd:import/@schemaLocation or xsd:include/@schemaLocation
	 *         declared in the given <code>document</code> by the given
	 *         <code>grammarURI</code> and null otherwise
	 */
	public static DOMAttr findSchemaLocationAttrByURI(DOMDocument document, String grammarURI) {
		DOMElement documentElement = document.getDocumentElement();
		if (documentElement != null) {
			List<DOMNode> children = documentElement.getChildren();
			for (DOMNode child : children) {
				if (child.isElement()) {
					DOMElement xsdElement = (DOMElement) child;
					if (XSDUtils.isXSInclude(xsdElement) || XSDUtils.isXSImport(xsdElement)) {
						DOMAttr schemaLocationAttr = XSDUtils.getSchemaLocation(xsdElement);
						if (schemaLocationAttr != null) {
							String attrValue = schemaLocationAttr.getValue();
							if (grammarURI.equals(attrValue) || ((grammarURI.endsWith(attrValue) && grammarURI.equals(getResolvedLocation(document.getDocumentURI(), attrValue))))) {
								return schemaLocationAttr;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the expanded system location
	 *
	 * @return the expanded system location
	 */
	private static String getResolvedLocation(String documentURI, String location) {
		if (StringUtils.isBlank(location)) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}
}
