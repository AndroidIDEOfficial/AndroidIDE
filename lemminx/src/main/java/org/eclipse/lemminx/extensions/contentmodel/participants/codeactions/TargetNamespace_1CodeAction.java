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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * CodeAction to replace an incorrect namespace in an .xml document.
 * 
 * Changes the value of the xmlns attribute of the root element of the .xml
 * document to the declared namespace of the referenced .xsd document.
 */
public class TargetNamespace_1CodeAction implements ICodeActionParticipant {

	private static final Pattern NAMESPACE_EXTRACTOR = Pattern.compile("'([^']+)'\\.");

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {

		String namespace = extractNamespace(diagnostic.getMessage());
		if (StringUtils.isEmpty(namespace)) {
			return;
		}
		String quote = sharedSettings.getPreferences().getQuotationAsString();
		// @formatter:off
		CodeAction replaceNamespace = CodeActionFactory.replace(
				"Replace with '" + namespace + "'",
				diagnostic.getRange(),
				quote + namespace + quote,
				document.getTextDocument(),
				diagnostic);
		// @formatter:on
		codeActions.add(replaceNamespace);
	}

	private static String extractNamespace(String diagnosticMessage) {
		// The error message has this form:
		// TargetNamespace.1: Expecting namespace 'NaN', but the target namespace of the
		// schema document is 'http://two-letter-name'.
		Matcher nsMatcher = NAMESPACE_EXTRACTOR.matcher(diagnosticMessage);
		if (nsMatcher.find()) {
			return nsMatcher.group(1);
		}
		return null;
	}

}
