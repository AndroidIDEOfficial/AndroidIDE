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
import java.util.List;
import java.util.Map;

/**
 * Element information.
 *
 */
public class ElementDeclaration extends ContainerDeclaration {

	private final String name;

	private ElementDeclaration parent;

	private final Map<String, AttributeDeclaration> attributes;

	private boolean hasCharacterContent;

	private ChildrenProperties childrenProperties;

	private int occurrences;

	private boolean hasAttributeId;

	public ElementDeclaration(String name) {
		this.name = name;
		this.attributes = new LinkedHashMap<>();
		this.childrenProperties = new ChildrenProperties();
		setHasCharacterContent(false);
		this.occurrences = 0;
	}

	/**
	 * Returns the element name.
	 * 
	 * @return the element name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the attribute information for the given name and create it if not
	 * found.
	 * 
	 * @param name the attribute name
	 * @return the attribute information for the given name and create it if not
	 *         found.
	 */
	public AttributeDeclaration getAttribute(String name) {
		AttributeDeclaration attribute = attributes.get(name);
		if (attribute != null) {
			return attribute;
		}
		attribute = new AttributeDeclaration(name, this);
		attributes.put(name, attribute);
		return attribute;
	}

	public Collection<AttributeDeclaration> getAttributes() {
		return attributes.values();
	}

	/**
	 * Returns true if element has character content and false otherwise.
	 * 
	 * @return true if element has character content and false otherwise.
	 */
	public boolean hasCharacterContent() {
		return hasCharacterContent;
	}

	void setHasCharacterContent(boolean hasCharacterContent) {
		this.hasCharacterContent = hasCharacterContent;
	}

	@Override
	public void addElement(ElementDeclaration element) {
		super.addElement(element);
		element.setParent(this);
	}

	public ElementDeclaration getParent() {
		return parent;
	}

	void setParent(ElementDeclaration parent) {
		this.parent = parent;
	}

	public void addChildHierarchy(List<String> tags) {
		childrenProperties.addChildHierarchy(tags);
	}

	public ChildrenProperties getChildrenProperties() {
		return childrenProperties;
	}

	/**
	 * Increment the DOM element occurrence.
	 */
	public void incrementOccurrences() {
		this.occurrences++;
	}

	/**
	 * Returns the occurrences of DOM element.
	 * 
	 * @return the occurrences of DOM element.
	 */
	public int getOccurrences() {
		return occurrences;
	}

	/**
	 * Returns true if the element have an attribute ID and false otherwise.
	 * 
	 * @return true if the element have an attribute ID and false otherwise.
	 */
	public boolean hasAttributeId() {
		return hasAttributeId;
	}

	/**
	 * Set true if the compute of the attribute ID has been done and false
	 * otherwise.
	 * 
	 * @param doneID
	 */
	void setAttributeID(boolean doneID) {
		this.hasAttributeId = doneID;
	}
}
