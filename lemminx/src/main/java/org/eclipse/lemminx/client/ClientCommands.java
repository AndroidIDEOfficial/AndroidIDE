/*******************************************************************************
* Copyright (c) 2019-2020 Red Hat Inc. and others.
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
 * Commonly used client commands
 * 
 * @author Angelo ZERR
 *
 */
public class ClientCommands {

	private ClientCommands() {
	}

	/**
	 * Show references
	 */
	public static final String SHOW_REFERENCES = "xml.show.references";

	/**
	 * Open settings command. This custom command is sent to the client in order to
	 * have the client open its settings UI.
	 */
	public static final String OPEN_SETTINGS = "xml.open.settings";

	/**
	 * Open the binding wizard to bind the XML to a grammar/schema.
	 */
	public static final String OPEN_BINDING_WIZARD = "xml.open.binding.wizard";

	/**
	 * Client command to open URI
	 */
	public static final String OPEN_URI = "xml.open.uri";

}