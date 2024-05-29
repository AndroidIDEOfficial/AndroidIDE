/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.wst.xml.core.internal.contentmodel.modelquery;
package org.eclipse.lemminx.uriresolver;

import java.net.URI;
import java.util.Map;

public interface IExternalGrammarLocationProvider {

	String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation"; //$NON-NLS-1$
	String NO_NAMESPACE_SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation"; //$NON-NLS-1$
	String DOCTYPE = "http://apache.org/xml/properties/dtd/external-doctype"; //$NON-NLS-1$

	/**
	 * Provided the file path URI, this will return the external schema location.
	 * The schema location is associated to a specific property, which serves as the
	 * key in the map. The supported properties are:
	 * 
	 * <p>
	 * <b>http://apache.org/xml/properties/schema/external-schemaLocation</b> - The
	 * value for this property follows the same rules as the schemaLocation
	 * attribute for an XML document (i.e., multiple schemas are acceptable and must
	 * be white-space separated).
	 * </p>
	 * 
	 * <p>
	 * <b>http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation</b>
	 * - The value for this property follows the same rules as the
	 * noNamespaceSchemaLocation attribute for an XML document (i.e., only one
	 * schema is allowed).
	 * 
	 * @param filePath the path for the XML file
	 * @return a {@link Map} associating the external-schemaLocation and/or
	 *         external-noNamespaceSchemaLocation to the schema location
	 */
	Map<String, String> getExternalGrammarLocation(URI fileURI);
}