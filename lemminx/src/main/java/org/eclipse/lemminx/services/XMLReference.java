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
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.IReferenceParticipant;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ReferenceContext;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML reference support.
 *
 */
class XMLReference {

	private final XMLExtensionsRegistry extensionsRegistry;

	private static Logger LOGGER = Logger.getLogger(XMLReference.class.getName());

	public XMLReference(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<? extends Location> findReferences(DOMDocument document, Position position, ReferenceContext context,
			CancelChecker cancelChecker) {
		List<Location> locations = new ArrayList<>();
		for (IReferenceParticipant participant : extensionsRegistry.getReferenceParticipants()) {
			try {
				participant.findReference(document, position, context, locations, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Error while processing references for the participant '" + participant.getClass().getName() + "'.", e);
			}
		}
		return locations;
	}

}
