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
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.IDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML definition support.
 *
 */
class XMLDefinition {

	private static final Logger LOGGER = Logger.getLogger(XMLTypeDefinition.class.getName());

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLDefinition(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<? extends LocationLink> findDefinition(DOMDocument document, Position position,
			CancelChecker cancelChecker) {
		IDefinitionRequest request = null;
		try {
			request = new DefinitionRequest(document, position, extensionsRegistry);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Failed creating TypeDefinitionRequest", e);
			return Collections.emptyList();
		}
		// Custom definition
		List<LocationLink> locations = new ArrayList<>();
		for (IDefinitionParticipant participant : extensionsRegistry.getDefinitionParticipants()) {
			try {
				participant.findDefinition(request, locations, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Error while processing definitions for the participant '" + participant.getClass().getName() + "'.", e);
			}
		}
		// Start end tag definition
		findStartEndTagDefinition(request, locations);
		return locations;
	}

	/**
	 * Find start end tag definition.
	 *
	 * @param request   the definition request
	 * @param locations the locations
	 */
	private static void findStartEndTagDefinition(IDefinitionRequest request, List<LocationLink> locations) {
		DOMNode node = request.getNode();
		if (node != null && node.isElement()) {
			// Node is a DOM element
			DOMElement element = (DOMElement) node;
			if (element.hasStartTag() && element.hasEndTag()) {
				// The DOM element has end and start tag
				DOMDocument document = element.getOwnerDocument();
				Range startRange = XMLPositionUtility.selectStartTagName(element);
				Range endRange = XMLPositionUtility.selectEndTagName(element);
				int offset = request.getOffset();
				if (element.isInStartTag(offset)) {
					// Start tag was clicked, jump to the end tag
					locations.add(new LocationLink(document.getDocumentURI(), endRange, endRange, startRange));
				} else if (element.isInEndTag(offset)) {
					// End tag was clicked, jump to the start tag
					locations.add(new LocationLink(document.getDocumentURI(), startRange, startRange, endRange));
				}
			}
		}
	}

}
