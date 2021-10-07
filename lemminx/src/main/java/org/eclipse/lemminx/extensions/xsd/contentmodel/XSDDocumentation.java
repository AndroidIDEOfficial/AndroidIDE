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
package org.eclipse.lemminx.extensions.xsd.contentmodel;

import java.util.Collections;
import java.util.List;

import org.apache.xerces.xs.XSObjectList;
import org.eclipse.lemminx.settings.SchemaDocumentationType;
import org.eclipse.lemminx.utils.StringUtils;
import org.jsoup.Jsoup;

/**
 * XSD documentation
 * 
 * Represents documentation coming from an XML schema file
 */
public class XSDDocumentation {

	private static final String APPINFO_ELEMENT = "appinfo";
	private static final String DOCUMENTATION_ELEMENT = "documentation";

	private final String prefix;
	private final SchemaDocumentationType strategy;
	private final List<String> documentation;
	private final List<String> appinfo;

	public XSDDocumentation(XSObjectList annotations, SchemaDocumentationType docStrategy, boolean convertToPlainText) {
		this(annotations, null, docStrategy, convertToPlainText);
	}

	public XSDDocumentation(XSObjectList annotations, String value, SchemaDocumentationType docStrategy, boolean convertToPlainText) {
		List<String> documentation = Collections.emptyList();
		List<String> appinfo = Collections.emptyList();
		switch(docStrategy) {
			case all: {
				documentation = XSDAnnotationModel.getDocumentation(annotations, value);
				appinfo = XSDAnnotationModel.getAppInfo(annotations, value);
				break;
			}
			case documentation: {
				documentation = XSDAnnotationModel.getDocumentation(annotations, value);
				break;
			}
			case appinfo: {
				appinfo = XSDAnnotationModel.getAppInfo(annotations, value);
				break;
			}
			case none:{
				break;
			}
		}

		if (convertToPlainText) {
			// convert content to plain text
			
			// if the content contains html tags, converting to plaintext
			// will remove them
			convertToPlainText(documentation);
			convertToPlainText(appinfo);
		}

		this.documentation = documentation;
		this.appinfo = appinfo;
		this.strategy = docStrategy;
		this.prefix = XSDAnnotationModel.getPrefix(annotations, value);
	}
	/**
	 * Returns formatted documentation that displays
	 * contents of the documentation element (if exists) and the appinfo
	 * element (if exists).
	 * 
	 * The returned documentation will return raw html if
	 * <code>html</code> is true. Otherwise, the returned documentation
	 * will be plaintext.
	 * 
	 * @param html if true, the return value will contain raw html
	 * @return formatted documentation that displays
	 * contents of the documentation element (if exists) and the appinfo
	 * element (if exists)
	 */
	public String getFormattedDocumentation(boolean html) {
		StringBuilder result = new StringBuilder();
		boolean prependTitles = prependTitleCheck();
		result.append(getFormatted(prefix, DOCUMENTATION_ELEMENT, documentation, prependTitles, html));
		result.append(getFormatted(prefix, APPINFO_ELEMENT, appinfo, prependTitles, html));
		return result.toString().trim();
	}

	/**
	 * Returns true if documentation title (ie, xs:documentation, xs:appinfo)
	 * should be preprended to the documentation
	 * 
	 * @return true if documentation title (ie, xs:documentation, xs:appinfo)
	 * should be preprended to the documentation
	 */
	private boolean prependTitleCheck() {
		return this.documentation.size() > 0 && this.appinfo.size() > 0
				&& strategy == SchemaDocumentationType.all;
	}

	private static void convertToPlainText(List<String> list) {
		HtmlToPlainText formatter = new HtmlToPlainText();
		for (int i = 0; i < list.size(); i++) {
			String curr = list.get(i);
			list.set(i, formatter.getPlainText(Jsoup.parse(curr)));
		}
	}

	private static String getFormatted(String prefix, String elementName, List<String> content,
			boolean prependTitles, boolean html) {
		StringBuilder result = new StringBuilder();
		if (prependTitles) {
			result.append(applyPrefix(prefix, elementName, html));
		}
		for (String doc: content) {
			if (!StringUtils.isBlank(doc)) {
				if (html) {
					result.append("<p>");
				}
				result.append(doc);
				if (html) {
					result.append("</p>");
				} else {
					result.append(System.lineSeparator());
					result.append(System.lineSeparator());
				}
			}
		}
		return result.toString();
	}

	private static String applyPrefix(String prefix, String documentation, boolean html) {
		StringBuilder result = new StringBuilder();
		if (html) {
			result.append("<p><b>");
		}
		if (!StringUtils.isEmpty(prefix)) {
			result.append(prefix).append(':');
		}
		result.append(documentation);
		if (html) {
			result.append("</b>");
		}
		result.append(":");
		if (html) {
			result.append("</p>");
		} else {
			result.append(System.lineSeparator());
			result.append(System.lineSeparator());
		}
		return result.toString();
	}
}