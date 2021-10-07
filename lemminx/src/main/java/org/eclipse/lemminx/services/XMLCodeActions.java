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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionContext;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Code action support.
 *
 */
public class XMLCodeActions {

	private static final Logger LOGGER = Logger.getLogger(XMLCompletions.class.getName());

	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLCodeActions(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	public List<CodeAction> doCodeActions(CodeActionContext context, Range range, DOMDocument document,
			SharedSettings sharedSettings) {
		List<CodeAction> codeActions = new ArrayList<>();
		List<Diagnostic> diagnostics = context.getDiagnostics();
		if (diagnostics != null) {
			for (Diagnostic diagnostic : context.getDiagnostics()) {
				for (ICodeActionParticipant codeActionParticipant : extensionsRegistry.getCodeActionsParticipants()) {
					try {
						codeActionParticipant.doCodeAction(diagnostic, range, document, codeActions, sharedSettings,
								extensionsRegistry);
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, "Error while processing code actions for the participant '"
								+ codeActionParticipant.getClass().getName() + "'.", e);
					}
				}
			}
		}
		return codeActions;
	}
}
