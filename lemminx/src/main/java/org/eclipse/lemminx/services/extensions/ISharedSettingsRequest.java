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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lemminx.settings.SharedSettings;

/**
 * Shared settings request API
 */
public interface ISharedSettingsRequest {

	/**
	 * Returns <code>true</code> if the client can support the given Markup kind for
	 * documentation and <code>false</code> otherwise.
	 * 
	 * @param kind the markup kind
	 * @return <code>true</code> if the client can support the given Markup kind for
	 *         documentation and <code>false</code> otherwise.
	 */
	boolean canSupportMarkupKind(String kind);

	/**
	 * Returns the sharedSettings instance
	 * 
	 * @return the sharedSettings instance
	 */
	SharedSettings getSharedSettings();
}
