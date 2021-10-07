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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Children information.
 *
 */
public class ContainerDeclaration {

	private final Map<String, ElementDeclaration> container;

	public ContainerDeclaration() {
		this.container = new LinkedHashMap<>();
	}

	/**
	 * Returns the element information for the given name and create it if not
	 * found.
	 * 
	 * @param name the element name
	 * @return the element information for the given name and create it if not
	 *         found.
	 */
	public ElementDeclaration getElement(String name) {
		ElementDeclaration element = container.get(name);
		if (element != null) {
			return element;
		}
		element = new ElementDeclaration(name);
		addElement(element);
		return element;
	}

	public void addElement(ElementDeclaration element) {
		container.put(element.getName(), element);
	}

	/**
	 * Returns the elements information of the node.
	 * 
	 * @return the elements information of the node.
	 */
	public Collection<ElementDeclaration> getElements() {
		return container.values();
	}

}
