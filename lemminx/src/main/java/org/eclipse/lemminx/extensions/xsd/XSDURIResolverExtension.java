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
package org.eclipse.lemminx.extensions.xsd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager.ResourceToDeploy;
import org.eclipse.lemminx.uriresolver.URIResolverExtension;

/**
 * Resolve the XSD XML Schema and DTD dependencies.
 *
 */
public class XSDURIResolverExtension implements URIResolverExtension {

	/**
	 * The XMLSchema namespace URI (= http://www.w3.org/2001/XMLSchema)
	 */
	private static final String SCHEMA_FOR_SCHEMA_URI_2001 = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

	private static final List<ResourceToDeploy> SCHEMA_URI_2001_RESOURCES = Arrays.asList(
			new ResourceToDeploy("http://www.w3.org/2001/XMLSchema.xsd", "schemas/xsd/XMLSchema.xsd"),
			new ResourceToDeploy("http://www.w3.org/2001/XMLSchema.dtd", "schemas/xsd/XMLSchema.dtd"),
			new ResourceToDeploy("http://www.w3.org/2001/datatypes.dtd", "schemas/xsd/datatypes.dtd"));

	/**
	 * The Namespace namespace URI (= http://www.w3.org/XML/1998/namespace)
	 */
	private static final String SCHEMA_FOR_NAMESPACE_URI_1998 = "http://www.w3.org/XML/1998/namespace"; //$NON-NLS-1$

	private static final ResourceToDeploy NAMESPACE_URI_1998_RESOURCE = new ResourceToDeploy(
			"https://www.w3.org/2001/xml.xsd", "schemas/xsd/xml.xsd");

	public String getName() {
		return "embedded xml.xsd";
	}

	public XSDURIResolverExtension(IXMLDocumentProvider documentProvider) {

	}

	@Override
	public String resolve(String baseLocation, String publicId, String systemId) {
		if (SCHEMA_FOR_SCHEMA_URI_2001.equals(publicId)) {
			try {
				// Deploy XML Schema and DTDs on file systems.
				for (ResourceToDeploy resource : SCHEMA_URI_2001_RESOURCES) {
					CacheResourcesManager.getResourceCachePath(resource);
				}
				// Returns the patch of "http://www.w3.org/2001/XMLSchema.xsd" file system
				return SCHEMA_URI_2001_RESOURCES.get(0).getDeployedPath().toFile().toURI().toString();
			} catch (Exception e) {
				// Do nothing?
			}
		} else if (SCHEMA_FOR_NAMESPACE_URI_1998.equals(publicId)) {
			try {
				return CacheResourcesManager.getResourceCachePath(NAMESPACE_URI_1998_RESOURCE).toFile().toURI()
						.toString();
			} catch (Exception e) {
				// Do nothing?
			}
		}
		return null;
	}

	@Override
	public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException {
		String publicId = resourceIdentifier.getNamespace();
		if (SCHEMA_FOR_SCHEMA_URI_2001.equals(publicId) || SCHEMA_FOR_NAMESPACE_URI_1998.equals(publicId)) {
			String baseLocation = resourceIdentifier.getBaseSystemId();
			String xslFilePath = resolve(baseLocation, publicId, null);
			if (xslFilePath != null) {
				return new XMLInputSource(publicId, xslFilePath, xslFilePath);
			}
		}
		return null;
	}
}
