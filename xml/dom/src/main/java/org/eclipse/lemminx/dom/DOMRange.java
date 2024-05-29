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
package org.eclipse.lemminx.dom;

/**
 * DOM range
 * 
 * @author Angelo ZERR
 *
 */
public interface DOMRange {

	/**
	 * Returns the start offset of the node.
	 * 
	 * @return the start offset of the node.
	 */
	int getStart();

	/**
	 * Returns the end offset of the node.
	 * 
	 * @return the end offset of the node.
	 */
	int getEnd();

	/**
	 * Returns the owner document.
	 * 
	 * @return the owner document.
	 */
	DOMDocument getOwnerDocument();
}
