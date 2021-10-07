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
package org.eclipse.lemminx.extensions.dtd.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDAttlistDecl;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.DTDElementDecl;
import org.eclipse.lemminx.extensions.contentmodel.model.FilesChangedTracker;
import org.eclipse.lemminx.utils.URIUtils;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DTD utilities.
 * 
 * @author Angelo ZERR
 *
 */
public class DTDUtils {

	/**
	 * Collect <!ELEMENT target name declared in the DTD according the origin name
	 * node.
	 * 
	 * @param originNameNode the origin name node (<ex :<!ATTLIST name).
	 * @param matchName      true if the origin name must match the value of target
	 *                       name and false otherwise.
	 * @param collector      collector to collect DTD <!ELEMENT target name.
	 */
	public static void searchDTDTargetElementDecl(DTDDeclParameter originNameNode, boolean matchName,
			Consumer<DTDDeclParameter> collector) {
		DOMDocumentType docType = originNameNode.getOwnerDocType();
		if (docType.hasChildNodes()) {
			// Loop for each <!ELEMENT.
			NodeList children = docType.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);
				if (node.getNodeType() == DOMNode.DTD_ELEMENT_DECL_NODE) {
					DTDElementDecl targetElementDecl = (DTDElementDecl) node;
					if (isValid(targetElementDecl)) {
						// node is <!ELEMENT which defines a name, collect if if it matches the origin
						// name node
						DTDDeclParameter targetElementName = targetElementDecl.getNameParameter();
						if ((!matchName || (matchName
								&& originNameNode.getParameter().equals(targetElementName.getParameter())))) {
							collector.accept(targetElementName);
						}
					}
				}
			}
		}
	}

	/**
	 * Search origin DTD node (<!ATTRLIST element-name) or (child <!ELEMENT
	 * element-name (child)) from the given target DTD node (<!ELEMENT element-name.
	 * 
	 * @param targetNode the referenced node
	 * @param collector  the collector to collect reference between an origin and
	 *                   target attribute.
	 */
	public static void searchDTDOriginElementDecls(DTDDeclNode targetNode,
			BiConsumer<DTDDeclParameter, DTDDeclParameter> collector, CancelChecker cancelChecker) {
		// Collect all potential target DTD nodes (all <!ELEMENT)
		List<DTDDeclNode> targetNodes = getTargetNodes(targetNode);
		if (targetNodes.isEmpty()) {
			// None referenced nodes, stop the search of references
			return;
		}

		// Loop for each <!ELEMENT and check for each target DTD nodes if it reference
		// it.
		DOMDocumentType docType = targetNode.getOwnerDocType();
		if (docType.hasChildNodes()) {
			// Loop for <!ELEMENT.
			NodeList children = docType.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (cancelChecker != null) {
					cancelChecker.checkCanceled();
				}
				Node origin = children.item(i);
				for (DTDDeclNode target : targetNodes) {
					if (target.isDTDElementDecl()) {
						// target node is <!ELEMENT, check if it is references by the current origin
						// node
						DTDElementDecl targetElement = (DTDElementDecl) target;
						switch (origin.getNodeType()) {
						case DOMNode.DTD_ELEMENT_DECL_NODE:
							// check if the current <!ELEMENT origin defines a child which references the
							// target node <!ELEMENT
							// <!ELEMENT from > --> here target node is 'from'
							// <!ELEMENT note(from)> --> here origin node is 'note'
							// --> here 'note' has a 'from' as child and it should be collected
							DTDElementDecl originElement = (DTDElementDecl) origin;
							originElement.collectParameters(targetElement.getNameParameter(), collector);
							break;
						case DOMNode.DTD_ATT_LIST_NODE:
							String name = targetElement.getName();
							// check if the current <!ATTLIST element-name reference the current <!ELEMENT
							// element-name
							// <!ELEMENT note --> here target node is 'note'
							// <!ATTLIST note ... -> here origin node is 'note'
							// --> here <!ATTLIST defines 'note' as element name, and it should be collected
							DTDAttlistDecl originAttribute = (DTDAttlistDecl) origin;
							if (name.equals(originAttribute.getElementName())) {
								// <!ATTLIST origin has the same name than <!ELEMENT target
								collector.accept(originAttribute.getNameParameter(), targetElement.getNameParameter());
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the referenced attributes list from the given referenced node.
	 * 
	 * @param referencedNode the referenced node.
	 * @return the referenced attributes list from the given referenced node.
	 */
	private static List<DTDDeclNode> getTargetNodes(DTDDeclNode referencedNode) {
		List<DTDDeclNode> referencedNodes = new ArrayList<>();
		switch (referencedNode.getNodeType()) {
		case DOMNode.DTD_ELEMENT_DECL_NODE:
			addTargetNode(referencedNode, referencedNodes);
			break;
		case Node.DOCUMENT_TYPE_NODE:
			DOMDocumentType docType = (DOMDocumentType) referencedNode;
			if (docType.hasChildNodes()) {
				// Loop for element <!ELEMENT.
				NodeList children = docType.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					Node node = children.item(i);
					if (node.getNodeType() == DOMNode.DTD_ELEMENT_DECL_NODE) {
						addTargetNode((DTDElementDecl) node, referencedNodes);
					}
				}
			}
			break;
		}
		return referencedNodes;
	}

	private static void addTargetNode(DTDDeclNode referencedNode, List<DTDDeclNode> referencedNodes) {
		if (referencedNode.isDTDElementDecl()) {
			// Add only <!ELEMENT which defines a name.
			DTDElementDecl elementDecl = (DTDElementDecl) referencedNode;
			if (isValid(elementDecl)) {
				referencedNodes.add(elementDecl);
			}
		}
	}

	/**
	 * Returns true if the given <!ELEMENT defines a name and false otherwise.
	 * 
	 * @param elementDecl the <!ELEMENT
	 * @return true if the given <!ELEMENT defines a name and false otherwise.
	 */
	private static boolean isValid(DTDElementDecl elementDecl) {
		if (elementDecl == null) {
			return false;
		}
		// check <!ELEMENT defines a name
		return elementDecl.getNameParameter() != null;
	}

	public static FilesChangedTracker createFilesChangedTracker(DTDGrammar grammar) {
		FilesChangedTracker tracker = new FilesChangedTracker();
		Set<DTDGrammar> trackedGrammars = new HashSet<>();
		updateTracker(grammar, trackedGrammars, tracker);
		return tracker;
	}

	private static void updateTracker(DTDGrammar grammar, Set<DTDGrammar> trackedGrammars,
			FilesChangedTracker tracker) {
		if (grammar == null || trackedGrammars.contains(grammar)) {
			return;
		}
		trackedGrammars.add(grammar);
		// Track the grammar
		String dtdURI = getDTDURI(grammar);
		if (dtdURI != null && URIUtils.isFileResource(dtdURI)) {
			// The DTD is a file, track when file changed
			tracker.addFileURI(dtdURI);
		}
	}

	private static String getDTDURI(DTDGrammar grammar) {
		return grammar.getGrammarDescription().getExpandedSystemId();
	}
}
