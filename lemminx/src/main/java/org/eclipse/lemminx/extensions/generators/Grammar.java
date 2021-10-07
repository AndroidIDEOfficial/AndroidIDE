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
package org.eclipse.lemminx.extensions.generators;

/**
 * Grammar information.
 *
 */
public class Grammar extends ContainerDeclaration {

	private String defaultNamespace;

	/**
	 * Returns the default namespace and null otherwise.
	 * 
	 * @return the default namespace and null otherwise.
	 */
	public String getDefaultNamespace() {
		return defaultNamespace;
	}

	/**
	 * Set the default namespace.
	 * 
	 * @param defaultNamespace the default namespace
	 */
	void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}

}
