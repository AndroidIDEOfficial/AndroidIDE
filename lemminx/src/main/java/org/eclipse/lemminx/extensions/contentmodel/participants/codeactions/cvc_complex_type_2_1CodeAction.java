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
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Code action to fix cvc-complex-type.2.1 error.
 *
 */
public class cvc_complex_type_2_1CodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		try {
			int offset = document.offsetAt(range.getStart());
			DOMNode node = document.findNodeAt(offset);
			if (node != null && node.isElement()) {
				DOMElement element = (DOMElement) node;
				int startOffset;
				if(element.isSelfClosed()) {
					startOffset = element.getEnd();
				}
				else {
					startOffset = element.getStartTagCloseOffset();
				}
				int endOffset = element.getEnd();
				Range diagnosticRange = XMLPositionUtility.createRange(startOffset, endOffset, document);
				CodeAction removeContentAction = CodeActionFactory.replace("Set element as empty", diagnosticRange,
						"/>", document.getTextDocument(), diagnostic);
				codeActions.add(removeContentAction);
			}

		} catch (BadLocationException e) {
			// Do nothing
		}
	}

}
