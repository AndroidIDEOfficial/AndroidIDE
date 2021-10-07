/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
* 
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.commons.snippets;

import org.eclipse.lsp4j.Position;

/**
 * Suffix position provider API.
 * 
 * @author Angelo ZERR
 *
 */
public interface ISuffixPositionProvider {

	/**
	 * Returns the suffix position provider of the given <code>sufix</code> and null
	 * otherwise.
	 * 
	 * @param suffix 
	 * @return  the suffix position provider of the given <code>sufix</code> and null
	 * otherwise.
	 */
	Position findSuffixPosition(String suffix);
}
