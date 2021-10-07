/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.settings;

/**
 * XML logs settings.
 *
 */
public class LogsSettings {

	private String file;

	private boolean client;

	/**
	 * Returns the file path of logs and null otherwise.
	 * 
	 * @return the file path of logs and null otherwise.
	 */
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * Returns <code>true</code> if LSP client is enabled and <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if LSP client is enabled and <code>false</code>
	 *         otherwise.
	 */
	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}

}