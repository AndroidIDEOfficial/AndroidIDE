/**
 * Copyright (c) 2020 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.dom;

/**
 * Represents one of the location hints provided in an xsi:schemaLocation
 * attribute.
 */
public class SchemaLocationHint implements DOMRange {

	private final int start, end;

	private final String hint;

	private final SchemaLocation parent;

	/**
	 * Create a new SchemaLocationHint object that represents one of the location
	 * hints given in the parent <code>SchemaLocation</code>.
	 *
	 * @param start  The offset from the beginning of the document where this
	 *               location hint starts
	 * @param end    The offset from the beginning of the document where this
	 *               location hint ends
	 * @param hint   The hint to the location of a schema (A URI that points to a
	 *               schema)
	 * @param parent The <code>SchemaLocation</code> in which this hint was given
	 */
	public SchemaLocationHint(int start, int end, String hint, SchemaLocation parent) {
		this.start = start;
		this.end = end;
		this.hint = hint;
		this.parent = parent;
	}

	/**
	 * Returns the location hint that this SchemaLocationHint represents
	 *
	 * @return The location hint, a URI to a schema, as a String
	 */
	public String getHint() {
		return this.hint;
	}

	/**
	 * Returns the offset from the beginning of the document where this location
	 * hint starts
	 * 
	 * @return The offset from the beginning of the document where this location
	 *         hint starts
	 */
	@Override
	public int getStart() {
		return this.start;
	}

	/**
	 * Returns the offset from the beginning of the document where this location
	 * hint ends
	 * 
	 * @return The offset from the beginning of the document where this location
	 *         hint ends
	 */
	@Override
	public int getEnd() {
		return this.end;
	}

	@Override
	public DOMDocument getOwnerDocument() {
		return parent.getAttr().getOwnerDocument();
	}

}