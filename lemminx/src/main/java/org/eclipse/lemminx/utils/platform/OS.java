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

/**
 * OS information
 */
public class OS {

	private final String name;

	private final String version;

	private final String arch;

	private final transient boolean isWindows;

	public OS() {
		this.name = Platform.getSystemProperty("os.name");
		this.version = Platform.getSystemProperty("os.version");
		this.arch = Platform.getSystemProperty("os.arch");
        
        // Directly make this false as we are using this in AndroidIDE
		isWindows = false;
	}

	/**
	 * Returns the OS name.
	 *
	 * @return the OS name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the OS version.
	 *
	 * @return the OS version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the OS arch.
	 *
	 * @return the OS arch.
	 */
	public String getArch() {
		return arch;
	}

	/**
	 * Returns true if the operating system is Windows and false otherwise
	 *
	 * @return true if the operating system is Windows and false otherwise
	 */
	public boolean isWindows() {
		return isWindows;
	}
}
