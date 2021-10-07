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
 * Features that are associated with a particuliar settingId
 */
public interface IXMLSettingFeature {
	/**
	 * Returns a displayable name for this feature
	 * 
	 * @return a displayable name for this feature
	 */
	public String getName();

	/**
	 * Returns the related settingId for this feature
	 * 
	 * @return the related settingId for this feature
	 */
	public String getSettingId();
}