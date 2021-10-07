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
package org.eclipse.lemminx.extensions.catalog;

import java.io.IOException;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager.ResourceToDeploy;
import org.eclipse.lemminx.uriresolver.URIResolverExtension;

/**
 * Resolve the XML Schema of XML/Catalog
 *
 */
public class XMLCatalogURIResolverExtension implements URIResolverExtension {

	/**
	 * The XML Catalog namespace URI (=
	 * http://www.oasis-open.org/committees/entity/release/1.1/catalog.xsd)
	 */
	private static final String CATALOG_NAMESPACE_URI = "urn:oasis:names:tc:entity:xmlns:xml:catalog"; //$NON-NLS-1$

	private static final String CATALOG_SYSTEM = "http://www.oasis-open.org/committees/entity/release/1.1/catalog.xsd";

	private static final ResourceToDeploy CATALOG_RESOURCE = new ResourceToDeploy(CATALOG_SYSTEM,
			"schemas/catalog/catalog-1.1.xsd");
	private final XMLExtensionsRegistry extensionsRegistry;

	@Override
	public String getName() {
		return "embedded catalog.xsd";
	}

	public XMLCatalogURIResolverExtension(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	@Override
	public String resolve(String baseLocation, String publicId, String systemId) {
		if (!CATALOG_NAMESPACE_URI.equals(publicId)) {
			return null;
		}
		if (hasDTDorXMLSchema(baseLocation)) {
			return null;
		}
		try {
			return CacheResourcesManager.getResourceCachePath(CATALOG_RESOURCE).toFile().toURI().toString();
		} catch (Exception e) {
			// Do nothing?
		}
		return CATALOG_SYSTEM;
	}

	@Override
	public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException {
		if (hasDTDorXMLSchema(resourceIdentifier.getBaseSystemId())) {
			return null;
		}
		String publicId = resourceIdentifier.getNamespace();
		if (CATALOG_NAMESPACE_URI.equals(publicId)) {
			String baseLocation = resourceIdentifier.getBaseSystemId();
			String catalogFilePath = resolve(baseLocation, publicId, null);
			if (catalogFilePath != null) {
				return new XMLInputSource(publicId, catalogFilePath, catalogFilePath);
			}
		}
		return null;
	}

	private boolean hasDTDorXMLSchema(String uri) {
		DOMDocument document = extensionsRegistry.getDocumentProvider().getDocument(uri);
		if (document == null) {
			return false;
		}
		return document.hasDTD() || document.hasSchemaLocation() || document.hasNoNamespaceSchemaLocation();
	}
}
