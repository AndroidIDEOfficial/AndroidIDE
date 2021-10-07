/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.telemetry;

/**
 * Telemetry event
 *
 */
public class TelemetryEvent {

	public final String name;
	public final Object properties;

	TelemetryEvent() {
		this("", null);
	}

	TelemetryEvent(String name, Object properties) {
		this.name = name;
		this.properties = properties;
	}
}