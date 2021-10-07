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
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensRequest;
import org.eclipse.lemminx.settings.XMLCodeLensSettings;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML Code Lens support.
 *
 */
class XMLCodeLens {

	private final XMLExtensionsRegistry extensionsRegistry;

	private static final Logger LOGGER = Logger.getLogger(XMLCodeLens.class.getName());

	public XMLCodeLens(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<? extends CodeLens> getCodelens(DOMDocument xmlDocument, XMLCodeLensSettings settings, CancelChecker cancelChecker) {
		ICodeLensRequest request = new CodeLensRequest(xmlDocument, settings);
		List<CodeLens> lenses = new ArrayList<>();
		for (ICodeLensParticipant participant : extensionsRegistry.getCodeLensParticipants()) {
			try {
				participant.doCodeLens(request, lenses, cancelChecker);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Error while processing code lens for the participant '" + participant.getClass().getName() + "'.", e);
			}
		}
		return lenses;
	}

}
