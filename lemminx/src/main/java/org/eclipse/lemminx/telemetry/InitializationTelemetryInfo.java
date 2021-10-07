/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.telemetry;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.lemminx.utils.platform.JVM;
import org.eclipse.lemminx.utils.platform.Memory;
import org.eclipse.lemminx.utils.platform.Platform;

/**
 * Telemetry data to collect.
 *
 * <ul>
 * <li>Server version information</li>
 * <li>JVM information</li>
 * </ul>
 *
 * @author Angelo ZERR
 *
 */
public class InitializationTelemetryInfo {

	public static final String JVM_MEMORY_MAX = "jvm.memory.max";
	public static final String JVM_MEMORY_TOTAL = "jvm.memory.total";
	public static final String JVM_MEMORY_FREE = "jvm.memory.free";
	public static final String JVM_IS_NATIVE_IMAGE = "server.is.native";
	public static final String JVM_RUNTIME = "jvm.runtime";
	public static final String JVM_VERSION = "jvm.version";
	public static final String JVM_NAME = "jvm.name";
	public static final String SERVER_VERSION_NUMBER = "server.version";

	/**
	 * Returns the init telemetry as a map
	 *
	 * @return the init telemetry as a map
	 */
	public static Map<String, Object> getInitializationTelemetryInfo() {
		Map<String, Object> initTelemetry = new HashMap<>();

		initTelemetry.put(SERVER_VERSION_NUMBER, Platform.getVersion().getVersionNumber());

		JVM jvm = Platform.getJVM();

		initTelemetry.put(JVM_NAME, jvm.getName());
		initTelemetry.put(JVM_VERSION, jvm.getVersion());
		initTelemetry.put(JVM_RUNTIME, jvm.getRuntime());
		initTelemetry.put(JVM_IS_NATIVE_IMAGE, jvm.isNativeImage());

		Memory memory = jvm.getMemory();

		initTelemetry.put(JVM_MEMORY_FREE, memory.getFree());
		initTelemetry.put(JVM_MEMORY_TOTAL, memory.getTotal());
		initTelemetry.put(JVM_MEMORY_MAX, memory.getMax());

		return initTelemetry;
	}

}
