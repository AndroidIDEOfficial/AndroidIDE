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
package org.eclipse.lemminx.dom;

/**
 * 
 * The declared "xsi:noNamespaceSchemaLocation"
 */
public class NoNamespaceSchemaLocation {

	private final DOMAttr attr;

	public NoNamespaceSchemaLocation(DOMAttr attr) {
		this.attr = attr;
	}

	public DOMAttr getAttr() {
		return attr;
	}

	/**
	 * Returns the location declared in the attribute value of
	 * "xsi:noNamespaceSchemaLocation"
	 * 
	 * @return the location declared in the attribute value of
	 *         "xsi:noNamespaceSchemaLocation"
	 */
	public String getLocation() {
		return attr.getValue();
	}

}
