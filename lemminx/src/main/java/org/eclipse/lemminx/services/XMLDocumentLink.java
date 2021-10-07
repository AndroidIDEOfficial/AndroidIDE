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
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.DocumentLink;

/**
 * XML document link support.
 *
 */
class XMLDocumentLink {

	private final XMLExtensionsRegistry extensionsRegistry;

	private static Logger LOGGER = Logger.getLogger(XMLDocumentLink.class.getName());

	public XMLDocumentLink(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<DocumentLink> findDocumentLinks(DOMDocument document) {
		List<DocumentLink> newLinks = new ArrayList<>();
		for (IDocumentLinkParticipant participant : extensionsRegistry.getDocumentLinkParticipants()) {
			try {
				participant.findDocumentLinks(document, newLinks);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Error while processing document links for the participant '" + participant.getClass().getName() + "'.", e);
			}
		}
		return newLinks;
	}
}
