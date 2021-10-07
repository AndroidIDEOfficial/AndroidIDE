/**
 * Copyright (c) 2020 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 *  Contributors:
 *      Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.xsd.participants;

import static org.eclipse.lemminx.utils.XMLPositionUtility.createDocumentLink;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.URI.MalformedURIException;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.DocumentLink;

/**
 * 
 * Implements document links in .xsd files for
 * <ul>
 * <li>xs:include/schemaLocation</li>
 * <li>xs:import/schemaLocation</li>
 * </ul>
 * 
 */
public class XSDDocumentLinkParticipant implements IDocumentLinkParticipant {

	private static final Logger LOGGER = Logger.getLogger(XSDDocumentLinkParticipant.class.getName());

	@Override
	public void findDocumentLinks(DOMDocument document, List<DocumentLink> links) {
		DOMElement root = document.getDocumentElement();
		if (!XSDUtils.isXSSchema(root)) {
			return;
		}
		String xmlSchemaPrefix = root.getPrefix();
		List<DOMNode> children = root.getChildren();
		for (DOMNode child : children) {
			if (child.isElement() && Objects.equals(child.getPrefix(), xmlSchemaPrefix)) {
				DOMElement xsdElement = (DOMElement) child;
				if (XSDUtils.isXSInclude(xsdElement) || XSDUtils.isXSImport(xsdElement)) {
					DOMAttr schemaLocationAttr = XSDUtils.getSchemaLocation(xsdElement);
					if (schemaLocationAttr != null && !StringUtils.isEmpty(schemaLocationAttr.getValue())) {
						String location = getResolvedLocation(document.getDocumentURI(), schemaLocationAttr.getValue());
						DOMRange schemaLocationRange = schemaLocationAttr.getNodeAttrValue();
						try {
							links.add(createDocumentLink(schemaLocationRange, location, true));
						} catch (BadLocationException e) {
							LOGGER.log(Level.SEVERE, "Creation of document link failed", e);
						}
					}
				}
			}
		}
	}

	private static String getResolvedLocation(String documentURI, String location) {
		if (location == null) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}

}
