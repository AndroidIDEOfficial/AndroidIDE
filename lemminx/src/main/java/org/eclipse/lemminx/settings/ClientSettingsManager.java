/**
 *  Copyright (c) 2019 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.settings;

import org.eclipse.lemminx.extensions.contentmodel.settings.ContentModelSettings;

/**
 * ClientSettingsManager
 */
public class ClientSettingsManager {

	private XMLGeneralClientSettings generalXMLSettings;

	private ContentModelSettings cmSettings;
	
	public ClientSettingsManager() {

	}

	/**
	 * @return the cmSettings
	 */
	public ContentModelSettings getCmSettings() {
		return cmSettings;
	}

	/**
	 * @param cmSettings the cmSettings to set
	 */
	public void setCmSettings(ContentModelSettings cmSettings) {
		this.cmSettings = cmSettings;
	}

	/**
	 * @return the generalXMLSettings
	 */
	public XMLGeneralClientSettings getGeneralXMLSettings() {
		return generalXMLSettings;
	}

	/**
	 * @param generalXMLSettings the generalXMLSettings to set
	 */
	public void setGeneralXMLSettings(XMLGeneralClientSettings generalXMLSettings) {
		this.generalXMLSettings = generalXMLSettings;
	}
}