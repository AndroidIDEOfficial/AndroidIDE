/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.utils.platform;

import org.eclipse.lemminx.utils.StringUtils;

/**
 * Platform information about OS and JVM.
 */
public class Platform {

	private static final String UNKNOWN_VALUE = "unknown";

	private static final OS os = new OS();
	private static final JVM jvm = new JVM();
	private static final Version version = new Version();

	public static final boolean isWindows = getOS().isWindows();
	public static String SLASH = isWindows ? "\\" : "/";

	private Platform() {
	}

	/**
	 * Returns the OS information
	 *
	 * @return the OS information
	 */
	public static OS getOS() {
		return os;
	}

	/**
	 * Returns the JVM information
	 *
	 * @return the JVM information
	 */
	public static JVM getJVM() {
		return jvm;
	}

	/**
	 * Returns the version information
	 *
	 * @return the version information
	 */
	public static Version getVersion() {
		return version;
	}

	/**
	 * Returns the system property from the given key and "unknown" otherwise.
	 *
	 * @param key the property system key
	 * @return the system property from the given key and "unknown" otherwise.
	 */
	static String getSystemProperty(String key) {
		try {
			String property = System.getProperty(key);
			return StringUtils.isEmpty(property) ? UNKNOWN_VALUE : property;
		} catch (SecurityException e) {
			return UNKNOWN_VALUE;
		}
	}

	/**
	 * Returns the server details, using the format:<br/>
	 *
	 * <pre>
	 * LemMinX Server info:
	 *  - Version : (build version)
	 *  - Java : (path to java.home])
	 *  - Git : ([Branch] short commit id - commit message)
	 * </pre>
	 *
	 * @return the formatted server details
	 */
	public static String details() {
		StringBuilder details = new StringBuilder();
		details.append("LemMinX Server info:");
		append(details, "Version", version.getVersionNumber());
		if (jvm.isNativeImage()) {
			append(details, "Native Image", null);
		} else {
			append(details, "Java", jvm.getJavaHome());
		}
		append(details, "VM Version", jvm.getVersion());
		append(details, "Git", null);
		String branch = version.getBranch();
		if (!Version.MAIN_BRANCH.equals(branch)) {
			details.append(" [Branch ").append(branch).append("]");
		}
		details.append(" ").append(version.getShortCommitId()).append(" - ").append(version.getCommitMessage());
		return details.toString();
	}

	private static void append(StringBuilder sb, String key, String value) {
		sb.append(System.lineSeparator()).append(" - ").append(key);
		if (value != null) {
			sb.append(" : ").append(value);
		}
	}
}