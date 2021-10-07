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
package org.eclipse.lemminx.extensions.xsd.participants.diagnostics;

import java.util.List;

import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.extensions.xsd.XSDPlugin;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Validate XSD file with Xerces.
 *
 */
public class XSDDiagnosticsParticipant implements IDiagnosticsParticipant {

	private final XSDPlugin xsdPlugin;

	public XSDDiagnosticsParticipant(XSDPlugin xsdPlugin) {
		this.xsdPlugin = xsdPlugin;
	}
	
	@Override
	public void doDiagnostics(DOMDocument xmlDocument, List<Diagnostic> diagnostics,
			XMLValidationSettings validationSettings, CancelChecker cancelChecker) {
		if (!DOMUtils.isXSD(xmlDocument)) {
			// Don't use the XSD validator, if the XML document is not a XML Schema.
			return;
		}
		// Get entity resolver (XML catalog resolver, XML schema from the file
		// associations settings., ...)
		XMLEntityResolver entityResolver = xmlDocument.getResolverExtensionManager();
		// Process validation
		XSDValidator.doDiagnostics(xmlDocument, entityResolver, diagnostics, validationSettings,
				xsdPlugin.getContentModelManager(), cancelChecker);
	}

}
