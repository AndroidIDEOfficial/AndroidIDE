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
package org.eclipse.lemminx.services.extensions.format;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.utils.XMLBuilder;

/**
 * XML formatter participant.
 * 
 * @author Angelo ZERR
 *
 */
public interface IFormatterParticipant {

	/**
	 * Format the given attribute value.
	 * 
	 * <p>
	 * The formatter must take care of to generate attribute value with quote.
	 * </p>
	 * 
	 * @param name              the attribute name.
	 * @param valueWithoutQuote the attribute value without quote.
	 * @param quote             the quote and null otherwise.
	 * @param attr              the DOM attribute and null otherwise.
	 * @param xml               the XML builder.
	 * @return true if the given attribute can be formatted and false otherwise.
	 */
	default boolean formatAttributeValue(String name, String valueWithoutQuote, Character quote, DOMAttr attr,
			XMLBuilder xml) {
		return false;
	}
}
