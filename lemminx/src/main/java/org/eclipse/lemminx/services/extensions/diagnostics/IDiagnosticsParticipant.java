/**
 *  Copyright (c) 2018 Angelo ZERR.
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
package org.eclipse.lemminx.services.extensions.diagnostics;

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Diagnostics participant API.
 *
 */
public interface IDiagnosticsParticipant {

	/**
	 * Validate the given XML document.
	 * 
	 * @param xmlDocument        XML document to validate.
	 * @param diagnostics        list to populate with errors, warnings, etc
	 * @param validationSettings the validation settings.
	 * @param cancelChecker      used to stop the validation when XML document
	 *                           changed.
	 */
	void doDiagnostics(DOMDocument xmlDocument, List<Diagnostic> diagnostics, XMLValidationSettings validationSettings,
			CancelChecker cancelChecker);

}
