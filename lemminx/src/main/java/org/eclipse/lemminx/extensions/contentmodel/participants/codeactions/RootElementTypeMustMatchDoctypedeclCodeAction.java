/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

/**
 * Code action for RootElementTypeMustMatchDoctypedecl
 * 
 * This class depends on the diagnostic message (Diagnostic#message) to 
 * determine the correct root name.
 * 
 * This class assumes that the diagnostic message is in this format:
 * Document root element "<current root name>", must match DOCTYPE root "<expected root name>".
 */
public class RootElementTypeMustMatchDoctypedeclCodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		DOMElement root = document.getDocumentElement();
		if (root == null) {
			return;
		}
		Range rootStartRange = XMLPositionUtility.selectStartTagName(root);
		if (!range.equals(rootStartRange)) {
			return;
		}

		String currentRootText = getCurrentRoot(diagnostic.getMessage());
		if (currentRootText == null || !currentRootText.equals(root.getNodeName())) {
			return;
		}

		String doctypeRootText = getDoctypeRoot(diagnostic.getMessage());
		if (doctypeRootText == null) {
			return;
		}

		List<TextEdit> replace= new ArrayList<>();
		addTextEdits(root, doctypeRootText, replace);

		CodeAction action = CodeActionFactory.replace(
				"Replace with '" + doctypeRootText + "'",
				replace, document.getTextDocument(), diagnostic);
		codeActions.add(action);
	}

	private void addTextEdits(DOMElement root, String newText, List<TextEdit> replace) {
		replace.add(new TextEdit(XMLPositionUtility.selectStartTagName(root), newText));

		if (root.isClosed() && !root.isSelfClosed())  {
			replace.add(new TextEdit(XMLPositionUtility.selectEndTagName(root), newText));
		}
	}

	/**
	 * Returns the current root name, extracted from <code>message</code>
	 * 
	 * The provided <code>message</code> must match this format:
	 * ... root element "<current root name>", must match DOCTYPE ...
	 * 
	 * TODO: This code is a workaround until this issue is fixed:
	 * https://github.com/microsoft/language-server-protocol/issues/887
	 * 
	 * @param message the message to extract the root name from
	 * @return the current root name, extracted from <code>message</code>
	 */
	private static String getCurrentRoot(String message) {
		String preText = "root element \"";
		String postText = "\", must match DOCTYPE";
		int preMatch = message.indexOf(preText);
		if (preMatch < 0) {
			return null;
		}
		int postMatch = message.indexOf(postText);
		if (postMatch < 0) {
			return null;
		}

		return message.substring(preMatch + preText.length(), postMatch);
	}

	/**
	 * Returns the doctype root name, extracted from <code>message</code>
	 * 
	 * The provided <code>message</code> must match this format:
	 * ... DOCTYPE root "<root name>".
	 * 
	 * TODO: This code is a workaround until this issue is fixed:
	 * https://github.com/microsoft/language-server-protocol/issues/887
	 * 
	 * @param message the message to extract the root name from
	 * @return the DOCTYPE root name, extracted from <code>message</code>
	 */
	private static String getDoctypeRoot(String message) {
		String preText = "DOCTYPE root \"";
		int preMatch = message.indexOf(preText);
		if (preMatch < 0) {
			return null;
		}
		return message.substring(preMatch + preText.length(), message.length() - 2);
	}

}