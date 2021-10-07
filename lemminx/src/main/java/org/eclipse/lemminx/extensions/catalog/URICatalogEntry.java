/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.catalog;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/**
 * Represents a catalog entry that uses the "uri" attribute to reference an external document
 */
public class URICatalogEntry extends CatalogEntry {

	public URICatalogEntry(@NonNull String baseURI, DOMElement entryElement) {
		super(baseURI, entryElement);
	}

	@Override
	public DOMRange getLinkRange() {
		DOMAttr uriAttr = CatalogUtils.getCatalogEntryURI(getEntryElement());
		if (uriAttr == null) {
			return null;
		}
		return uriAttr.getNodeAttrValue();
	}

	@Override
	public String getResolvedURI() {
		DOMAttr uriAttr = CatalogUtils.getCatalogEntryURI(getEntryElement());
		if (uriAttr == null) {
			return null;
		}
		String lastSegment = uriAttr.getValue();
		if (StringUtils.isBlank(lastSegment)) {
			return null;
		}
		try {
			return Paths.get(getBaseURI(), lastSegment).toString();
		} catch (InvalidPathException e) {
			// See issue #977
			return null;
		}
	}

}
