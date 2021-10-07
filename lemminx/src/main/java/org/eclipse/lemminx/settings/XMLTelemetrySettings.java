/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.settings;

/**
 * Settings for telemetry
 *
 * @author datho7561
 */
public class XMLTelemetrySettings {

	private boolean enabled = false;

	public XMLTelemetrySettings() {
	}

	/**
	 * Returns true if telemetry is enabled and false otherwise
	 *
	 * @return true if telemetry is enabled and false otherwise
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set if telemetry should be enabled
	 *
	 * @param enabled true if telemetry should be enabled, and false if telemetry should be disabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
