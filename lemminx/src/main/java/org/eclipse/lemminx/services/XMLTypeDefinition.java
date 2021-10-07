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
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ITypeDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.ITypeDefinitionRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML type definition support.
 *
 */
class XMLTypeDefinition {

	private static final Logger LOGGER = Logger.getLogger(XMLTypeDefinition.class.getName());

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLTypeDefinition(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<? extends LocationLink> findTypeDefinition(DOMDocument document, Position position,
			CancelChecker cancelChecker) {
		ITypeDefinitionRequest request = null;
		try {
			request = new TypeDefinitionRequest(document, position, extensionsRegistry);
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Failed creating TypeDefinitionRequest", e);
			return Collections.emptyList();
		}
		List<LocationLink> locations = new ArrayList<>();
		for (ITypeDefinitionParticipant participant : extensionsRegistry.getTypeDefinitionParticipants()) {
			try {
				participant.findTypeDefinition(request, locations, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Error while processing type definitions for the participant '" + participant.getClass().getName() + "'.", e);
			}
		}
		return locations;
	}

}
