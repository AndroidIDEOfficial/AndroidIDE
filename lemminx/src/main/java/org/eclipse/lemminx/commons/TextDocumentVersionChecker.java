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
package org.eclipse.lemminx.commons;

import java.util.concurrent.CancellationException;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * A {@link CancelChecker} implementation to throw a
 * {@link CancellationException} when version of {@link TextDocument} changed.
 * 
 * @author Angelo ZERR
 *
 */
public class TextDocumentVersionChecker implements CancelChecker {

	private final TextDocument textDocument;

	private final int version;

	public TextDocumentVersionChecker(TextDocument textDocument, int version) {
		this.textDocument = textDocument;
		this.version = version;
	}

	@Override
	public void checkCanceled() {
		if (textDocument.getVersion() != version) {
			// the text document version has changed
			throw new CancellationException("Text document version '" + version + "' has changed to version '"
					+ textDocument.getVersion() + ".");
		}
	}

}