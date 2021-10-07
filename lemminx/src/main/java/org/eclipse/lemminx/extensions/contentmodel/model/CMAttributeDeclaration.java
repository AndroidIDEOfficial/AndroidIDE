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

import java.util.Collection;

import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;

/**
 * Content model element which abstracts attribute declaration from a given
 * grammar (XML Schema, DTD).
 */
public interface CMAttributeDeclaration {

	/**
	 * Returns the declared element name.
	 * 
	 * @return the declared element name.
	 */
	String getName();
	
	String getDefaultValue();

	Collection<String> getEnumerationValues(); 
	
	/**
	 * Returns formatted documentation of the declared
	 * attribute according to settings defined in <code>request</code>
	 * 
	 * @param request the request that contains settings
	 * @return formatted documentation of the declared
	 * attribute according to settings defined in <code>request</code>
	 */
	String getAttributeNameDocumentation(ISharedSettingsRequest request);

	/**
	 * Returns formatted documentation about <code>value</code>,
	 * according to settings defined in <code>request</code>
	 * 
	 * @param value the attribute value to find documentation for
	 * @param request the request containing settings
	 * @return formatted documentation about <code>value</code>,
	 * according to settings defined in <code>request</code>
	 */
	String getAttributeValueDocumentation(String value, ISharedSettingsRequest request);

	/**
	 * Returns true if the attribute is required and false otherwise.
	 * 
	 * @return true if the attribute is required and false otherwise.
	 */
	boolean isRequired();
}
