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
package org.eclipse.lemminx.extensions.xsl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager;
import org.eclipse.lemminx.uriresolver.URIResolverExtension;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager.ResourceToDeploy;

/**
 * Resolve the XSL XML Schema to use according the xsl:stylesheet/@version
 *
 */
public class XSLURIResolverExtension implements URIResolverExtension {

	/**
	 * The XSL namespace URI (= http://www.w3.org/1999/XSL/Transform)
	 */
	private static final String XSL_NAMESPACE_URI = "http://www.w3.org/1999/XSL/Transform"; //$NON-NLS-1$

	private static final ResourceToDeploy XML_SCHEMA_10 = new ResourceToDeploy("https://www.w3.org/1999/11/xslt10.xsd",
			"/schemas/xslt/xslt-1.0.xsd");

	private static final Map<String, ResourceToDeploy> XSL_RESOURCES;

	public String getName() {
		return "embedded xslt.xsd";
	}

	static {
		XSL_RESOURCES = new HashMap<>();
		XSL_RESOURCES.put("1.0", XML_SCHEMA_10);
		XSL_RESOURCES.put("2.0",
				new ResourceToDeploy("https://www.w3.org/2007/schema-for-xslt20.xsd", "/schemas/xslt/xslt-2.0.xsd"));
		XSL_RESOURCES.put("3.0", new ResourceToDeploy("https://www.w3.org/TR/xslt-30/schema-for-xslt30.xsd",
				"/schemas/xslt/xslt-3.0.xsd"));
	}

	private final IXMLDocumentProvider documentProvider;

	public XSLURIResolverExtension(IXMLDocumentProvider documentProvider) {
		this.documentProvider = documentProvider;
	}

	@Override
	public String resolve(String baseLocation, String publicId, String systemId) {
		if (!XSL_NAMESPACE_URI.equals(publicId)) {
			return null;
		}
		String version = getVersion(baseLocation);
		ResourceToDeploy xmlSchema = getXMLSchemaForXSL(version);
		try {
			Path outFile = CacheResourcesManager.getResourceCachePath(xmlSchema);
			return outFile.toFile().toURI().toString();
		} catch (Exception e) {
			// Do nothing?
		}
		return null;
	}

	@Override
	public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException {
		String publicId = resourceIdentifier.getNamespace();
		if (XSL_NAMESPACE_URI.equals(publicId)) {
			String baseLocation = resourceIdentifier.getBaseSystemId();
			String xslFilePath = resolve(baseLocation, publicId, null);
			if (xslFilePath != null) {
				return new XMLInputSource(publicId, xslFilePath, xslFilePath);
			}
		}
		return null;
	}

	private static ResourceToDeploy getXMLSchemaForXSL(String version) {
		return XSL_RESOURCES.getOrDefault(version, XML_SCHEMA_10);
	}

	/**
	 * Returns the version coming from xsl:stylesheet/@version of the XML document
	 * retrieved by the given uri
	 * 
	 * @param uri
	 * @return the version coming from xsl:stylesheet/@version of the XML document
	 *         retrieved by the given uri
	 */
	private String getVersion(String uri) {
		if (documentProvider == null) {
			return null;
		}
		DOMDocument document = documentProvider.getDocument(uri);
		if (document != null) {
			DOMElement element = document.getDocumentElement();
			if (element != null) {
				String version = element.getAttribute("version");
				if (version != null) {
					return version;
				}
			}
		}
		return "1.0";
	}

}
