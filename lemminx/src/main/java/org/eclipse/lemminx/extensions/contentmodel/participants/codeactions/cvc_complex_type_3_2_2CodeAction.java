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
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Code action to fix cvc-complex-type.3.2.2 error.
 *
 */
public class cvc_complex_type_3_2_2CodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		Range diagnosticRange = diagnostic.getRange();
		try {
			int offset = document.offsetAt(diagnosticRange.getEnd());
			DOMAttr attr = document.findAttrAt(offset);
			if (attr != null) {
				// Remove attribute
				int startOffset = attr.getStart();
				int endOffset = attr.getEnd();
				Range attrRange = new Range(document.positionAt(startOffset), document.positionAt(endOffset));
				CodeAction removeAttributeAction = CodeActionFactory.remove("Remove '" + attr.getName() + "' attribute",
						attrRange, document.getTextDocument(), diagnostic);
				codeActions.add(removeAttributeAction);
			}
		} catch (BadLocationException e) {
			// Do nothing
		}
	}

}
