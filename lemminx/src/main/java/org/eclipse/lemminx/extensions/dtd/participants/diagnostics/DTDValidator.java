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
package org.eclipse.lemminx.extensions.dtd.participants.diagnostics;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics.LSPErrorReporterForXML;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * DTD validator
 *
 */
public class DTDValidator {

	public static void doDiagnostics(DOMDocument document, XMLEntityResolver entityResolver,
			List<Diagnostic> diagnostics, ContentModelManager contentModelManager, CancelChecker monitor) {
		try {
			XMLDTDLoader loader = new XMLDTDLoader();
			loader.setProperty("http://apache.org/xml/properties/internal/error-reporter",
					new LSPErrorReporterForXML(document, diagnostics, contentModelManager, false, new HashMap<>()));

			if (entityResolver != null) {
				loader.setEntityResolver(entityResolver);
			}

			String content = document.getText();
			String uri = document.getDocumentURI();
			InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
			XMLInputSource source = new XMLInputSource(null, uri, uri, inputStream, null);
			loader.loadGrammar(source);
		} catch (Exception e) {

		}
	}
}
