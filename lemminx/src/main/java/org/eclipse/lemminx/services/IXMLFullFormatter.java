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
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.settings.SharedSettings;

/**
 * XML formatter API.
 *
 */
public interface IXMLFullFormatter {

	/**
	 * Format the given text document by the shared settings.
	 * 
	 * @param text           the text.
	 * @param uri            the uri.
	 * @param sharedSettings the shared settings.
	 * 
	 * @return the formatted text document by using the shared settings.
	 */
	String formatFull(String text, String uri, SharedSettings sharedSettings);
}
