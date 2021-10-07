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

import java.util.Map;

/**
 * Snippet context used to filter the snippet.
 * 
 * @author Angelo ZERR
 *
 * @param <T> the value type waited by the snippet context.
 */
public interface ISnippetContext<T> {

	/**
	 * Return true if the given value match the snippet context and false otherwise.
	 * 
	 * @param value the value to check.
	 * @return true if the given value match the snippet context and false
	 *         otherwise.
	 */
	boolean isMatch(T value, Map<String, String> model);
}
