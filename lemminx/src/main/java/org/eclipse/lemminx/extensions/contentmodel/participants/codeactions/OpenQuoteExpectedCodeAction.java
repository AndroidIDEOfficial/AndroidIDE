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
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

/**
 * OpenQuoteExpectedCodeAction
 */
public class OpenQuoteExpectedCodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		Range diagnosticRange = diagnostic.getRange();
		int offset;
		try {
			offset = document.offsetAt(diagnosticRange.getEnd());
		} catch (BadLocationException e) {
			return;
		}
		DOMAttr attr = document.findAttrAt(offset);
		if(attr == null || !attr.isAttribute()) {
			return;
		}
		String q = sharedSettings.getPreferences().getQuotationAsString();
		Position codeactionPosition;
		Position possibleEndPosition = null;
		String possibleValue = null;
		try {
			codeactionPosition = document.positionAt(attr.getEnd());
			DOMNode next = attr.getNextSibling();
			if(next instanceof DOMAttr) {
				DOMAttr nextAttr = (DOMAttr) next;
				if(!nextAttr.hasDelimiter()) {
					possibleEndPosition = document.positionAt(nextAttr.getEnd());
					possibleValue = nextAttr.getName();
				}
			}
		} catch (BadLocationException e) {
		  return;
		}
		CodeAction removeContentAction;
		if(possibleEndPosition != null && possibleValue != null) {
			removeContentAction = CodeActionFactory.replace("Insert quotations", new Range(codeactionPosition, possibleEndPosition), q + possibleValue + q, document.getTextDocument(), diagnostic);	
		}
		else {
			removeContentAction = CodeActionFactory.insert("Insert quotations", codeactionPosition, q + q, document.getTextDocument(), diagnostic);
		}
		codeActions.add(removeContentAction);
	}

	
}