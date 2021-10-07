/**
 * Copyright (c) 2020 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

/**
 * Code action to add the missing xmlns declaration to the root element in an
 * .xml document.
 * 
 * Finds the namespace of the referenced .xsd. Adds the xmlns attribute to the
 * root of .xml, and sets its value to the .xsd namespace.
 */
public class TargetNamespace_2CodeAction implements ICodeActionParticipant {

	private static final Pattern NAMESPACE_EXTRACTOR = Pattern.compile("'([^']+)'\\.");

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {

		String namespace = extractNamespace(diagnostic.getMessage());
		if (StringUtils.isEmpty(namespace)) {
			return;
		}
		DOMNode root = document.getDocumentElement();
		if (root == null) {
			return;
		}
		Position tagEnd = XMLPositionUtility.selectStartTagName(root).getEnd();
		String quote = sharedSettings.getPreferences().getQuotationAsString();
		// @formatter:off
		CodeAction addNamespaceDecl = CodeActionFactory.insert(
				"Declare '" + namespace + "' as the namespace",
				tagEnd,
				" xmlns=" + quote + namespace + quote,
				document.getTextDocument(),
				diagnostic);
		// @formatter:on
		codeActions.add(addNamespaceDecl);
	}

	private static String extractNamespace(String diagnosticMessage) {
		// The error message has this form:
		// TargetNamespace.2: Expecting no namespace, but the schema document has a
		// target namespace of 'http://docbook.org/ns/docbook'.
		Matcher nsMatcher = NAMESPACE_EXTRACTOR.matcher(diagnosticMessage);
		if (nsMatcher.find()) {
			return nsMatcher.group(1);
		}
		return null;
	}

}
