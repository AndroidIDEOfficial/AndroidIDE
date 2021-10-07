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
package org.eclipse.lemminx.utils;

import java.util.List;

import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Range;

/**
 * Factory to create LSP4J {@link MarkupContent}
 * 
 * @author Angelo ZERR
 *
 */
public class MarkupContentFactory {

	public static final String MARKDOWN_SEPARATOR = "___";

	/**
	 * Create the markup content according the given markup kind and the capability
	 * of the client.
	 * 
	 * @param value         the documentation value
	 * @param preferredKind the preferred markup kind
	 * @return the markup content according the given markup kind and the capability
	 *         of the client.
	 */
	public static MarkupContent createMarkupContent(String value, String preferredKind,
			ISharedSettingsRequest support) {
		if (value == null) {
			return null;
		}
		MarkupContent content = new MarkupContent();
		if (MarkupKind.MARKDOWN.equals(preferredKind) && support.canSupportMarkupKind(preferredKind)) {
			String markdown = MarkdownConverter.convert(value);
			content.setValue(markdown);
			content.setKind(MarkupKind.MARKDOWN);
		} else {
			content.setValue(value);
			content.setKind(MarkupKind.PLAINTEXT);
		}
		return content;
	}

	/**
	 * Create the hover from the given markup content list.
	 * 
	 * @param values the list of documentation values
	 * @return the hover from the given markup content list.
	 */
	public static Hover createHover(List<MarkupContent> values) {
		return createHover(values, null);
	}

	/**
	 * Create the hover from the given markup content list and range.
	 * 
	 * @param values       the list of documentation values
	 * @param defaultRange the default range.
	 * @return the hover from the given markup content list and range.
	 */
	public static Hover createHover(List<MarkupContent> values, Range defaultRange) {
		if (values.isEmpty()) {
			return null;
		}
		if (values.size() == 1) {
			return new Hover(values.get(0), defaultRange);
		}
		// Markup kind
		boolean hasMarkdown = values.stream() //
				.anyMatch(contents -> MarkupKind.MARKDOWN.equals(contents.getKind()));
		String markupKind = hasMarkdown ? MarkupKind.MARKDOWN : MarkupKind.PLAINTEXT;
		// Contents
		String content = createContent(values, markupKind);
		// Range
		Range range = defaultRange;
		return new Hover(new MarkupContent(markupKind, content), range);
	}

	/**
	 * Create the content.
	 * 
	 * @param values     the list of documentation values
	 * @param markupKind the markup kind.
	 * @return the content.
	 */
	private static String createContent(List<MarkupContent> values, String markupKind) {
		StringBuilder content = new StringBuilder();
		for (MarkupContent value : values) {
			if (!StringUtils.isEmpty(value.getValue())) {
				if (content.length() > 0) {
					if (markupKind.equals(MarkupKind.MARKDOWN)) {
						content.append(System.lineSeparator());
						content.append(System.lineSeparator());
						content.append(MARKDOWN_SEPARATOR);
					}
					content.append(System.lineSeparator());
					content.append(System.lineSeparator());
				}
				content.append(value.getValue());
			}
		}
		return content.toString();
	}

}
