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
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.IRenameRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.Position;

class RenameRequest extends AbstractPositionRequest implements IRenameRequest {

	private final String newText;
	
	public RenameRequest(DOMDocument document, Position position, String newText, XMLExtensionsRegistry extensionsRegistry) throws BadLocationException {
		super(document, position, extensionsRegistry);
		this.newText = newText;
	}

	public String getNewText() {
		return newText;
	}
}