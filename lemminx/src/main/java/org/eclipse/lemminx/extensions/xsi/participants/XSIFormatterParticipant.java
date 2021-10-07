/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
*  are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.xsi.participants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.extensions.xsi.XSISchemaModel;
import org.eclipse.lemminx.extensions.xsi.settings.XSISchemaLocationSplit;
import org.eclipse.lemminx.services.extensions.format.IFormatterParticipant;
import org.eclipse.lemminx.settings.XMLFormattingOptions;
import org.eclipse.lemminx.utils.XMLBuilder;

/**
 * Formatter participant implementation to format xsi:schemaLocation attribute
 * value. The format of the xsi:schemaLocation attribute value depends on the
 * {@link XSISchemaLocationSplit} setting:
 * 
 * <ul>
 * <li>{@link XSISchemaLocationSplit#none} : don't format the xsi:schemaLocation
 * attribute value.</li>
 * <li>{@link XSISchemaLocationSplit#onElement} : generate a line feed for each
 * namespace declaration:
 * 
 * <pre>
 * &lt;beans
        xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:util="http://www.springframework.org/schema/util"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                            http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/util
                            http://www.springframework.org/schema/util/spring-util.xsd"&gt;
 * </pre>
 * 
 * </li>
 * <li>{@link XSISchemaLocationSplit#onPair} : generate a line feed for each
 * location declaration:
 * 
 * <pre>
 * &lt;beans
        xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:util="http://www.springframework.org/schema/util"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd"&gt;
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author Angelo ZERR
 *
 */
public class XSIFormatterParticipant implements IFormatterParticipant {

	@Override
	public boolean formatAttributeValue(String name, String valueWithoutQuote, Character quote, DOMAttr attr,
			XMLBuilder xml) {
		if (XSISchemaModel.isXSISchemaLocationAttr(name, attr)) {
			// The attribute is a xsi:schemaLocation
			XMLFormattingOptions formattingOptions = xml.getSharedSettings().getFormattingSettings();
			XSISchemaLocationSplit split = XSISchemaLocationSplit.getSplit(formattingOptions);
			if (split == XSISchemaLocationSplit.none) {
				// don't format the xsi:schemaLocation attribute value
				return false;
			}
			int lineFeed = split == XSISchemaLocationSplit.onElement ? 1 : 2;
			if (quote != null) {
				xml.append(quote);
			}
			List<String> locations = getLocations(valueWithoutQuote);
			String indent = "";
			for (int i = 0; i < locations.size(); i++) {
				if (i % lineFeed == 0) {
					if (i == 0) {
						indent = getCurrentLineIndent(xml, formattingOptions);
					} else {
						xml.linefeed();
						xml.append(indent);
					}
				} else {
					xml.appendSpace();
				}
				xml.append(locations.get(i));
			}
			if (quote != null) {
				xml.append(quote);
			}
			return true;
		}
		return false;
	}

	private static List<String> getLocations(String value) {
		List<String> locations = new ArrayList<>();
		int start = -1;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (Character.isWhitespace(c)) {
				if (start != -1) {
					locations.add(value.substring(start, i));
					start = -1;
				}
			} else if (start == -1) {
				start = i;
			}
		}
		if (start != -1) {
			locations.add(value.substring(start, value.length()));
		}
		return locations;
	}

	public String getCurrentLineIndent(XMLBuilder xml, XMLFormattingOptions formattingOptions) {
		boolean insertSpaces = formattingOptions.isInsertSpaces();
		int tabSize = formattingOptions.getTabSize();
		int nbChars = 0;
		for (int i = xml.length() - 1; i >= 0; i--) {
			if (xml.charAt(i) == '\r' || xml.charAt(i) == '\n') {
				break;
			}
			if (!insertSpaces && xml.charAt(i) == '\t') {
				nbChars = nbChars + tabSize;
			} else {
				nbChars++;
			}
		}
		StringBuilder indent = new StringBuilder();
		if (insertSpaces || tabSize <= 0) {
			for (int i = 0; i < nbChars; i++) {
				indent.append(" ");
			}
		} else {
			int nbTabs = nbChars / tabSize;
			nbChars = nbChars % tabSize;
			for (int i = 0; i < nbTabs; i++) {
				indent.append("\t");
			}
			for (int i = 0; i < nbChars; i++) {
				indent.append(" ");
			}
		}
		return indent.toString();
	}

}
