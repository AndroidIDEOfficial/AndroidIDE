/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.commons;

import java.util.Collection;

/**
 * Snippet syntax utilities.
 * 
 * @see https://github.com/Microsoft/language-server-protocol/blob/master/snippetSyntax.md
 */
public class SnippetsBuilder {

	private SnippetsBuilder() {

	}

	public static void tabstops(int index, StringBuilder snippets) {
		snippets.append("$");
		snippets.append(index);
	}

	public static String tabstops(int index) {
		StringBuilder snippets = new StringBuilder();
		snippets.append("$");
		snippets.append(index);
		return snippets.toString();
	}

	public static void placeholders(int index, String text, StringBuilder snippets) {
		snippets.append("${");
		snippets.append(index);
		snippets.append(":");
		snippets.append(text);
		snippets.append("}");
	}

	/**
	 * Returns the LSP choices snippets content.
	 * 
	 * @param index the snippet index.
	 * @param values the values for the choice.
	 * @return the LSP choices snippets content.
	 * 
	 * @see https://github.com/Microsoft/language-server-protocol/blob/master/snippetSyntax.md#choice
	 */
	public static String choice(int index, Collection<String> values) {
		StringBuilder snippets = new StringBuilder();
		choice(index, values, snippets);
		return snippets.toString();
	}

	/**
	 * Add LSP choices snippets in the given snippets content.
	 * 
	 * @param index the snippet index.
	 * @param values the values for the choice.
	 * @return
	 * 
	 * @see https://github.com/Microsoft/language-server-protocol/blob/master/snippetSyntax.md#choice
	 */
	public static void choice(int index, Collection<String> values, StringBuilder snippets) {
		snippets.append("${");
		snippets.append(index);
		snippets.append("|");
		int i = 0;
		for (String value : values) {
			if (i > 0) {
				snippets.append(",");
			}
			snippets.append(value);
			i++;
		}
		snippets.append("|");
		snippets.append("}");
	}

}
