/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import java.util.List;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Adds a space at the end of the diagnostic range
 *
 */
public class FixMissingSpaceCodeAction implements ICodeActionParticipant {

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		Range diagnosticRange = diagnostic.getRange();
		try {
			int startOffset = document.offsetAt(diagnosticRange.getStart());
			int endOffset = document.offsetAt(diagnosticRange.getEnd());
			String text = document.getText();
			String value = text.substring(startOffset, endOffset);
			codeActions.add(CodeActionFactory.insert("Add space after '" + value + "'", diagnosticRange.getEnd(), " ",
					document.getTextDocument(), diagnostic));
		} catch (BadLocationException | IndexOutOfBoundsException e) {
			codeActions.add(CodeActionFactory.insert("Add space", diagnosticRange.getEnd(), " ",
					document.getTextDocument(), diagnostic));
		}
	}

}
