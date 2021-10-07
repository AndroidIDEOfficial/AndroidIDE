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
package org.eclipse.lemminx.client;

/**
 * Features that are associated with some limit
 * 
 * For example, document symbols have a maximum limit for performance reasons
 */
public enum LimitFeature implements IXMLSettingFeature {
	
	SYMBOLS("document symbols", "xml.symbols.maxItemsComputed");
	
	private final String name;

	private final String settingId;
	
	private LimitFeature(String name, String settingId) {
		this.name = name;
		this.settingId = settingId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSettingId() {
		return settingId;
	}
}
