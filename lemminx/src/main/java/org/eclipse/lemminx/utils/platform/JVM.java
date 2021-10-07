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
 * JVM information
 *
 */
public class JVM {

	private final String name;

	private final String runtime;

	private final String version;

	private final boolean isNativeImage;

	private final Memory memory;

	private final String javaHome;

	public JVM() {
		this.name = Platform.getSystemProperty("java.vm.name"); // ex : OpenJDK 64-Bit Server VM
		this.runtime = Platform.getSystemProperty("java.runtime.name"); // ex : OpenJDK Runtime Environment
		this.version = Platform.getSystemProperty("java.version"); // ex : 11
		this.memory = new Memory();
		this.isNativeImage = "Substrate VM".equals(System.getProperty("java.vm.name"));
        
		this.javaHome = Platform.getSystemProperty("java.home");
	}

	/**
	 * Returns the JVM name
	 *
	 * @return the JVM name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the JVM version
	 *
	 * @return the JVM version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the JVM runtime name.
	 *
	 * @return the JVM runtime name.
	 */
	public String getRuntime() {
		return runtime;
	}

	/**
	 * Returns the information on the memory
	 *
	 * @return the information on the memory
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * Returns the value of the JAVA_HOME environment variable
	 *
	 * Do not include this information in telemetry, as it likely includes the
	 * user's name
	 *
	 * @return the value of the JAVA_HOME environment variable
	 */
	public String getJavaHome() {
		return javaHome;
	}

	/**
	 * Returns true if the server is a native image and false otherwise
	 *
	 * @return true if the server is a native image and false otherwise
	 */
	public boolean isNativeImage() {
		return isNativeImage;
	}
}
