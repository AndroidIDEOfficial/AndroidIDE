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
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.List;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.URI.MalformedURIException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.dom.XMLModel;

/**
 * XML model utilities.
 * 
 */
public class XMLModelUtils {

	/**
	 * Returns the DOM range of the href of the xml-model processing instruction
	 * which matches the given hrefLocation.
	 * 
	 * @param document     the DOM document.
	 * @param hrefLocation the href location.
	 * @return the DOM range of the href of the xml-model processing instruction
	 *         which matches the given hrefLocation.
	 */
	public static DOMRange getHrefNode(DOMDocument document, String hrefLocation) {
		if (hrefLocation == null) {
			return null;
		}
		// Check if location comes from a xml-model/@href
		List<XMLModel> xmlModels = document.getXMLModels();
		if (!xmlModels.isEmpty()) {
			String documentURI = document.getDocumentURI();
			for (XMLModel xmlModel : xmlModels) {
				String href = xmlModel.getHref();
				String xmlModelLocation = getResolvedLocation(documentURI, href);
				if (hrefLocation.equals(xmlModelLocation)) {
					return xmlModel.getHrefNode();
				}
			}
		}
		return null;
	}

	/**
	 * Returns the expanded system location
	 *
	 * @return the expanded system location
	 */
	private static String getResolvedLocation(String documentURI, String location) {
		if (location == null) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}
}
