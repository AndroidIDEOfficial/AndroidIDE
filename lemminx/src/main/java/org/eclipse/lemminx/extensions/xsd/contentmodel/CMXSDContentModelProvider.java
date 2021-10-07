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
package org.eclipse.lemminx.extensions.xsd.contentmodel;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.xs.XSLoaderImpl;
import org.apache.xerces.xs.XSModel;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.NoNamespaceSchemaLocation;
import org.eclipse.lemminx.dom.SchemaLocation;
import org.eclipse.lemminx.dom.SchemaLocationHint;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider;
import org.eclipse.lemminx.uriresolver.CacheResourceDownloadingException;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;

/**
 * XSD content model provider.
 */
public class CMXSDContentModelProvider implements ContentModelProvider {

	private static final String XSI_SCHEMA_LOCATION_BINDING_KIND = "xsi:schemaLocation";

	private static final String XSI_NO_NAMESPACE_SCHEMA_LOCATION_BINDING_KIND = "xsi:noNamespaceSchemaLocation";
	private final URIResolverExtensionManager resolverExtensionManager;

	public CMXSDContentModelProvider(URIResolverExtensionManager resolverExtensionManager) {
		this.resolverExtensionManager = resolverExtensionManager;
	}

	@Override
	public boolean adaptFor(DOMDocument document, boolean internal) {
		if (internal) {
			return false;
		}
		return document.hasSchemaLocation() || document.hasNoNamespaceSchemaLocation();
	}

	@Override
	public boolean adaptFor(String uri) {
		return DOMUtils.isXSD(uri);
	}

	@Override
	public Collection<Identifier> getIdentifiers(DOMDocument xmlDocument, String namespaceURI) {
		Collection<Identifier> identifiers = new ArrayList<>();
		SchemaLocation schemaLocation = xmlDocument.getSchemaLocation();
		if (schemaLocation != null) {
			if (namespaceURI == null) {
				for (SchemaLocationHint locationHint : schemaLocation.getSchemaLocationHints()) {
					String location = locationHint.getHint();
					if (!StringUtils.isEmpty(location)) {
						identifiers.add(new Identifier(null, location, locationHint, XSI_SCHEMA_LOCATION_BINDING_KIND));
					}
				}
			} else {
				SchemaLocationHint locationHint = schemaLocation.getLocationHint(namespaceURI);
				if (locationHint != null) {
					String location = locationHint.getHint();
					if (!StringUtils.isEmpty(location)) {
						identifiers.add(new Identifier(null, location, locationHint, XSI_SCHEMA_LOCATION_BINDING_KIND));
					}
				}
			}
		} else {
			NoNamespaceSchemaLocation noNamespaceSchemaLocation = xmlDocument.getNoNamespaceSchemaLocation();
			if (noNamespaceSchemaLocation != null) {
				if (namespaceURI == null) {
					// xsi:noNamespaceSchemaLocation doesn't define namespaces
					String location = noNamespaceSchemaLocation.getLocation();
					if (!StringUtils.isEmpty(location)) {
						identifiers.add(
								new Identifier(null, location, noNamespaceSchemaLocation.getAttr().getNodeAttrValue(),
										XSI_NO_NAMESPACE_SCHEMA_LOCATION_BINDING_KIND));
					}
				}
			}
		}
		return identifiers;
	}

	@Override
	public CMDocument createCMDocument(String key) {
		XSLoaderImpl loader = getLoader();
		XSModel model = loader.loadURI(key);
		if (model != null) {
			// XML Schema can be loaded
			return new CMXSDDocument(model, loader);
		}
		return null;
	}

	@Override
	public CMDocument createInternalCMDocument(DOMDocument xmlDocument) {
		return null;
	}

	public XSLoaderImpl getLoader() {
		XSLoaderImpl loader = new XSLoaderImpl();
		loader.setParameter("http://apache.org/xml/properties/internal/entity-resolver", resolverExtensionManager);
		loader.setParameter(Constants.DOM_ERROR_HANDLER, new DOMErrorHandler() {

			@Override
			public boolean handleError(DOMError error) {
				if (error.getRelatedException() instanceof CacheResourceDownloadingException) {
					throw ((CacheResourceDownloadingException) error.getRelatedException());
				}
				return false;
			}
		});
		return loader;
	}

}
