/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.extensions.contentmodel.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;

/**
 * Content model element which abstracts element declaration from a given
 * grammar (XML Schema, DTD).
 */
public interface CMElementDeclaration {

	public static final Collection<CMElementDeclaration> ANY_ELEMENT_DECLARATIONS = Collections
			.unmodifiableCollection(Arrays.asList());

	/**
	 * Returns the declared element name.
	 * 
	 * @return the declared element name.
	 */
	String getName();

	/**
	 * Returns the target namespace and null otherwise.
	 * 
	 * @return the target namespace and null otherwise.
	 */
	String getNamespace();

	/**
	 * Returns the declared element name with the given prefix.
	 * 
	 * @return the declared element name with the given prefix.
	 */
	default String getName(String prefix) {
		String name = getName();
		if (prefix == null || prefix.isEmpty()) {
			return name;
		}
		return prefix + ":" + name;
	}

	/**
	 * Returns the attributes of this declared element.
	 * 
	 * @return the attributes element of this declared element.
	 */
	Collection<CMAttributeDeclaration> getAttributes();

	/**
	 * Returns the children declared element of this declared element.
	 * 
	 * @return the children declared element of this declared element.
	 */
	Collection<CMElementDeclaration> getElements();

	/**
	 * Returns the possible declared elements at the given offset of the given
	 * parent element.
	 * 
	 * @param parentElement the parent element
	 * @param offset        the offset
	 * @return the possible declared elements at the given offset of the given
	 *         parent element.
	 */
	Collection<CMElementDeclaration> getPossibleElements(DOMElement parentElement, int offset);

	/**
	 * Returns the declared element which matches the given XML tag name / namespace
	 * and null otherwise.
	 * 
	 * @param tag
	 * @param namespace
	 * @return the declared element which matches the given XML tag name / namespace
	 *         and null otherwise.
	 */
	CMElementDeclaration findCMElement(String tag, String namespace);

	/**
	 * Returns the declared attribute which match the given name and null otherwise.
	 * 
	 * @param attributeName
	 * @return the declared attribute which match the given name and null otherwise.
	 */
	CMAttributeDeclaration findCMAttribute(String attributeName);

	/**
	 * Returns formatted documentation of the declared element, according to
	 * settings defined in <code>request</code>.
	 * 
	 * @param request the request containing settings
	 * @return formatted documentation of the declared element, according to
	 *         settings defined in <code>request</code>.
	 */
	String getDocumentation(ISharedSettingsRequest request);

	/**
	 * Returns true if the element cannot contains element children or text content
	 * and false otherwise.
	 * 
	 * @return true if the element cannot contains element children or text content
	 *         and false otherwise.
	 */
	boolean isEmpty();

	/**
	 * Return the enumeration values.
	 * 
	 * @return the enumeration values.
	 */
	Collection<String> getEnumerationValues();

	/**
	 * Returns the documentation for the given enumeration value and null otherwise.
	 * 
	 * @param value   the enumeration value.
	 * @param request the shared settings.
	 * 
	 * @return the documentation for the given enumeration value and null otherwise.
	 */
	String getTextDocumentation(String value, ISharedSettingsRequest request);

	/**
	 * Returns the owner document URI where the element is declared.
	 * 
	 * @return the owner document URI where the element is declared.
	 */
	String getDocumentURI();

}
