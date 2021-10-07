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

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensRequest;
import org.eclipse.lemminx.settings.XMLCodeLensSettings;

/**
 * CodeLens request
 * 
 * @author Angelo ZERR
 *
 */
class CodeLensRequest implements ICodeLensRequest {

	private final DOMDocument document;

	private final XMLCodeLensSettings settings;

	public CodeLensRequest(DOMDocument document, XMLCodeLensSettings settings) {
		this.document = document;
		this.settings = settings;
	}

	@Override
	public DOMDocument getDocument() {
		return document;
	}

	@Override
	public boolean isSupportedByClient(String kind) {
		return settings.isSupportedByClient(kind);
	}

}
