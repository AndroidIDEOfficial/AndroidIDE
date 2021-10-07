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
package org.eclipse.lemminx.extensions.contentmodel.uriresolver;

import java.io.IOException;

import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.util.XMLCatalogResolver;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;

/**
 * Extension of Xerces XML catalog resolver to support include of XSD.
 * 
 * @author Angelo ZERR
 *
 */
class LSPXMLCatalogResolver extends XMLCatalogResolver {

	public LSPXMLCatalogResolver(String[] catalogs) {
		super(catalogs);
	}

	@Override
	public String resolveIdentifier(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
		// The default Xerces XML catalog implementation promote the namespace
		// information froml the resource identifier
		// See
		// https://github.com/apache/xerces2-j/blob/cf0c517a41b31b0242b96ab1af9627a3ab07fcd2/src/org/apache/xerces/util/XMLCatalogResolver.java#L418
		// For XSD include the resolve identifier is not computed correctly.
		if (isXSDIncludeWithNamespace(resourceIdentifier)) {
			return resourceIdentifier.getExpandedSystemId();
		}
		return super.resolveIdentifier(resourceIdentifier);
	}

	/**
	 * Returns true if it's an XSD include/import with namespace and false
	 * otherwise.
	 * 
	 * @param resourceIdentifier the resource indetifier.
	 * 
	 * @return true if it's an XSD include/import with namespace and false
	 *         otherwise.
	 */
	private boolean isXSDIncludeWithNamespace(XMLResourceIdentifier resourceIdentifier) {
		if (resourceIdentifier != null && resourceIdentifier instanceof XSDDescription
				&& resourceIdentifier.getNamespace() != null) {
			XSDDescription description = (XSDDescription) resourceIdentifier;
			int contextType = description.getContextType();
			return contextType == XSDDescription.CONTEXT_INCLUDE;
		}
		return false;
	}
}
