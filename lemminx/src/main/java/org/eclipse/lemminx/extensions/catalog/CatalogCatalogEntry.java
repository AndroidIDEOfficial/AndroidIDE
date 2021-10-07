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

import java.nio.file.Paths;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/**
 * Represents a catalog entry that uses the "catalog" attribute to reference an external document
 */
public class CatalogCatalogEntry extends CatalogEntry {

	public CatalogCatalogEntry(@NonNull String baseURI, DOMElement entryElement) {
		super(baseURI, entryElement);
	}

	@Override
	public DOMRange getLinkRange() {
		DOMAttr catalogAttr = CatalogUtils.getCatalogEntryCatalog(getEntryElement());
		if (catalogAttr == null) {
			return null;
		}
		return catalogAttr.getNodeAttrValue();
	}

	@Override
	public String getResolvedURI() {
		DOMAttr catalogAttr = CatalogUtils.getCatalogEntryCatalog(getEntryElement());
		if (catalogAttr == null) {
			return null;
		}
		String lastSegment = catalogAttr.getValue();
		if (StringUtils.isBlank(lastSegment)) {
			return null;
		}
		return Paths.get(getBaseURI(), lastSegment).toString();
	}

}
