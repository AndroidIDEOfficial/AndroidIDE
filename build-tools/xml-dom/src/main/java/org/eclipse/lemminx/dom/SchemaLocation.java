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
package org.eclipse.lemminx.dom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * The declared "xsi:schemaLocation"
 */
public class SchemaLocation {

	private final Map<String, SchemaLocationHint> schemaLocationValuePairs;

	private final DOMAttr attr;

	// The text to match is of the form:
	// http://example.org/schema/root root.xsd http://example.org/schema/bison bison.xsd http://example.org/schema/potato potato.xsd
	private static final Pattern SCHEMA_LOCATION_PAIR_PATTERN = Pattern.compile("\\s*([^\\s]+)\\s+([^\\s]+)\\s*");

	public SchemaLocation(DOMAttr attr) {
		this.attr = attr;
		this.schemaLocationValuePairs = new HashMap<>();
		String value = attr.getValue();
		Matcher locPairMatcher = SCHEMA_LOCATION_PAIR_PATTERN.matcher(value);
		while (locPairMatcher.find()) {
			String namespaceURI = locPairMatcher.group(1);
			String locationHint = locPairMatcher.group(2);
			if (namespaceURI == null || locationHint == null) {
				break;
			}
			DOMRange valNode = attr.getNodeAttrValue();
			// http://example.org/schema/root |root.xsd http://example.org/schema/bison bison.xsd
			int start = valNode.getStart() + locPairMatcher.start(2) + 1;
			// http://example.org/schema/root root.xsd| http://example.org/schema/bison bison.xsd
			int end = valNode.getStart() + locPairMatcher.end(2) + 1;
			schemaLocationValuePairs.put(namespaceURI,
					new SchemaLocationHint(start, end, locationHint, this));
		}
	}

	/**
	 * Returns the location hint, as a string, that is associated with the given namespace URI.
	 * 
	 * If the given namespace URI was not referred to in this xsi:schemaLocation, then null is returned.
	 * 
	 * @param namespaceURI The namespace URI to find the location hint for
	 * @return The associated location hint, as a string, or null
	 *         if the namespace was not referred to in xsi:schemaLocation
	 */
	public SchemaLocationHint getLocationHint(String namespaceURI) {
		return schemaLocationValuePairs.get(namespaceURI);
	}

	public DOMAttr getAttr() {
		return attr;
	}

	/**
	 * Returns all the location hints given in this xsi:schemaLocation attribute
	 *
	 * @return A Collection of all the location hints as <code>SchemaLocationHint</code>
	 */
	public Collection<SchemaLocationHint> getSchemaLocationHints() {
		return schemaLocationValuePairs.values();
	}

}
