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

import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMRange;

/**
 * Represents a catalog entry that references an external document
 */
public abstract class CatalogEntry {

	private final String baseURI;
	private final DOMElement entryElement;

	/**
	 *
	 * @param baseURI      <catalog>'s xml:base + <group>'s xml:base (if in a
	 *                     <group>)
	 * @param entryElement the element that corresponds with this catalog entry
	 */
	protected CatalogEntry(String baseURI, DOMElement entryElement) {
		this.baseURI = baseURI;
		this.entryElement = entryElement;
	}

	/**
	 * Returns the base URI for this catalog entry
	 *
	 * @return the base URI for this catalog entry
	 */
	public String getBaseURI() {
		return baseURI;
	}

	/**
	 * Returns the element that corresponds with this catalog entry
	 *
	 * @return the element that corresponds with this catalog entry
	 */
	protected DOMElement getEntryElement() {
		return entryElement;
	}

	/**
	 * Returns the range in the document where the link to an external document is,
	 * or null if this catalog entry does not refer to an external document
	 *
	 * @return the range in the document where the link to an external document is
	 */
	public abstract DOMRange getLinkRange();

	/**
	 * Returns the URI for the document that this catalog entry references, or null
	 * if this catalog entry does not refer to an external document
	 *
	 * @return the URI for the document that this catalog entry references
	 */
	public abstract String getResolvedURI();
}
