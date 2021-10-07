/*******************************************************************************
 * Copyright (c) 2016-2017 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.lemminx.utils;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.overzealous.remark.Options;
import com.overzealous.remark.Options.FencedCodeBlocks;
import com.overzealous.remark.Options.Tables;
import com.overzealous.remark.Remark;

import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

/**
 * Converts HTML content into Markdown equivalent.
 *
 * @author Fred Bricon
 */
public class MarkdownConverter {

	private static final Logger LOGGER = Logger.getLogger(MarkdownConverter.class.getName());

	private static Remark remark;

	private MarkdownConverter(){
		//no public instanciation
	}

	static {
		Options options = new Options();
		options.tables = Tables.CONVERT_TO_CODE_BLOCK;
		options.hardwraps = true;
		options.inlineLinks = true;
		options.autoLinks = true;
		options.reverseHtmlSmartPunctuation = true;
		options.fencedCodeBlocks = FencedCodeBlocks.ENABLED_BACKTICK;
		remark = new Remark(options);
		//Stop remark from stripping file protocol in an href
		try {
			Field cleanerField = Remark.class.getDeclaredField("cleaner");
			cleanerField.setAccessible(true);

			Cleaner c = (Cleaner) cleanerField.get(remark);

			Field safelistField = Cleaner.class.getDeclaredField("safelist");
			safelistField.setAccessible(true);

			Safelist s = (Safelist) safelistField.get(c);

			s.addProtocols("a", "href", "file");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LOGGER.severe("Unable to modify jsoup to include file protocols "+ e.getMessage());
		}
	}

	public static String convert(String html) {
		if(!StringUtils.isTagOutsideOfBackticks(html)) {
			return unescapeXml(html); // is not html so it can be returned as is (aside from unescaping)
		}
		return unescapeJava(remark.convert(html));
	}

}
