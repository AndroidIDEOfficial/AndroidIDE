/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
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

import java.util.List;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Code action to fix cvc-attribute-3 error.
 *
 */
public class src_import_1_2CodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {

		try {
			String prefix = document.getDocumentElement().getPrefix();
			CodeAction namespace = createNamespaceCodeAction(diagnostic, range, document, prefix);
			CodeAction targetNamespace = createTargetNamespaceCodeAction(diagnostic, document, prefix);

			if (namespace != null) {
				codeActions.add(namespace);
			}

			if (targetNamespace != null) {
				codeActions.add(targetNamespace);
			}

		} catch (BadLocationException e) {
			// Do nothing
		}
	}

	private CodeAction createNamespaceCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, String prefix) throws BadLocationException {
		int offset = document.offsetAt(range.getStart());
		DOMNode node = document.findNodeAt(offset);

		if (node == null || !node.isElement()) {
			return null;
		}
		
		String message;
		if (prefix != null) {
			message = "Insert 'namespace' attribute in '" + prefix + ":import' element";
		} else {
			message = "Insert 'namespace' attribute in 'import' element";
		}

		return CodeActionFactory.insert(message, diagnostic.getRange().getEnd(), " namespace=\"\"", document.getTextDocument(), diagnostic);
	}

	private CodeAction createTargetNamespaceCodeAction(Diagnostic diagnostic, DOMDocument document, String prefix) throws BadLocationException {
		DOMElement root = document.getDocumentElement();

		if (!root.getTagName().contains("schema")) {
			return null;
		}
		String message;
		if (prefix != null) {
			message = "Insert 'targetNamespace' attribute in '" + prefix + ":schema' element";
		} else {
			message = "Insert 'targetNamespace' attribute in 'schema' element";
		}
		return CodeActionFactory.insert(message, document.positionAt(root.getStartTagCloseOffset()), " targetNamespace=\"\"", document.getTextDocument(), diagnostic);
	}

}
