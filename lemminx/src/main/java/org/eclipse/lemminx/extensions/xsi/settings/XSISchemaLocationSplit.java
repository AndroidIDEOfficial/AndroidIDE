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
package org.eclipse.lemminx.extensions.xsi.settings;

import org.eclipse.lemminx.settings.XMLFormattingOptions;

/**
 * The xsi:schemaLocation split settings.
 * 
 * @author Angelo ZERR
 *
 */
public enum XSISchemaLocationSplit {

	onElement, onPair, none;

	private static final String XSI_SCHEMA_LOCATION_SPLIT = "xsiSchemaLocationSplit";

	public static XSISchemaLocationSplit getSplit(XMLFormattingOptions formattingSettings) {
		return getSplit(formattingSettings.getString(XSI_SCHEMA_LOCATION_SPLIT));
	}

	public static void setSplit(XSISchemaLocationSplit split, XMLFormattingOptions formattingSettings) {
		formattingSettings.putString(XSI_SCHEMA_LOCATION_SPLIT, split.name());
	}

	public static XSISchemaLocationSplit getSplit(String value) {
		if (value != null && !value.isEmpty()) {
			try {
				return XSISchemaLocationSplit.valueOf(value);
			} catch (Exception e) {
				// Do nothing
			}
		}
		return XSISchemaLocationSplit.none;
	}
}
