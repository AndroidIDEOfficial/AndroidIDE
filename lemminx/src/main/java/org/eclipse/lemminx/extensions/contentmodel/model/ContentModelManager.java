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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider.Identifier;
import org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics.LSPXMLGrammarPool;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLFileAssociation;
import org.eclipse.lemminx.extensions.contentmodel.uriresolver.XMLCacheResolverExtension;
import org.eclipse.lemminx.extensions.contentmodel.uriresolver.XMLCatalogResolverExtension;
import org.eclipse.lemminx.extensions.contentmodel.uriresolver.XMLFileAssociationResolverExtension;
import org.eclipse.lemminx.uriresolver.CacheResourceDownloadingException;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager;
import org.eclipse.lemminx.uriresolver.ResolvedURIInfo;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.FilesUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.URIUtils;

/**
 * Content model manager used to load XML Schema, DTD.
 *
 */
public class ContentModelManager {

	private final Map<String, CMDocument> cmDocumentCache;

	private final URIResolverExtensionManager resolverManager;
	private final List<ContentModelProvider> modelProviders;

	private final XMLCacheResolverExtension cacheResolverExtension;
	private final XMLCatalogResolverExtension catalogResolverExtension;
	private final XMLFileAssociationResolverExtension fileAssociationResolver;
	private final XMLGrammarPool grammarPool;

	public ContentModelManager(URIResolverExtensionManager resolverManager) {
		this.resolverManager = resolverManager;
		modelProviders = new ArrayList<>();
		cmDocumentCache = Collections.synchronizedMap(new HashMap<>());
		fileAssociationResolver = new XMLFileAssociationResolverExtension();
		resolverManager.registerResolver(fileAssociationResolver);
		catalogResolverExtension = new XMLCatalogResolverExtension();
		resolverManager.registerResolver(catalogResolverExtension);
		cacheResolverExtension = new XMLCacheResolverExtension();
		resolverManager.registerResolver(cacheResolverExtension);
		grammarPool = new LSPXMLGrammarPool();
		// Use cache by default
		setUseCache(true);
	}

	/**
	 * Returns the owner document of the declared element which matches the given
	 * XML element and null otherwise.
	 *
	 * @param element the XML element
	 *
	 * @return the owner document of the declared element which matches the given
	 *         XML element and null otherwise.
	 */
	public Collection<CMDocument> findCMDocument(DOMElement element) {
		return findCMDocument(element.getOwnerDocument(), element.getNamespaceURI());
	}

	/**
	 * Returns the owner document of the declared element which matches the given
	 * XML element and null otherwise.
	 *
	 * @param element the XML element
	 *
	 * @return the owner document of the declared element which matches the given
	 *         XML element and null otherwise.
	 */
	public Collection<CMDocument> findCMDocument(DOMElement element, String namespaceURI) {
		return findCMDocument(element.getOwnerDocument(), namespaceURI);
	}

	public Collection<CMDocument> findCMDocument(DOMDocument xmlDocument, String namespaceURI) {
		return findCMDocument(xmlDocument, namespaceURI, true);
	}

	/**
	 * Returns the declared documents which match the given DOM document.
	 *
	 * @param xmlDocument  the DOM document.
	 * @param namespaceURI the namespace URI
	 * @return the declared documents which match the given DOM document.
	 */
	public Collection<CMDocument> findCMDocument(DOMDocument xmlDocument, String namespaceURI, boolean withInternal) {
		if (namespaceURI == null) {
			// This case comes from when an element has no namespace and XML Schema defines
			// elementFormDefault="unqualified"
			// --> we use the namespace from the DOM document

			// ex: XSD:
			// <xs:schema targetNamespace="urn:reports/itops"
			// elementFormDefault="unqualified"

			// ex : XML
			// <i:ITOpsReport xmlns:i="urn:reports/itops"
			// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			// xsi:schemaLocation="urn:reports/itops ../schema/reports/itops.xsd">
			// <templates> --> here template has a null namespace and the DOM document has
			// "urn:reports/itops" namespace
			// --> we use urn:reports/itops as namespace to get the DOM Document
			namespaceURI = xmlDocument.getNamespaceURI();
		}
		Collection<CMDocument> documents = new ArrayList<>();
		for (ContentModelProvider modelProvider : modelProviders) {
			// internal grammar
			if (withInternal) {
				CMDocument internalCMDocument = modelProvider.createInternalCMDocument(xmlDocument);
				if (internalCMDocument != null) {
					documents.add(internalCMDocument);
				}
			}
			// external grammar
			if (modelProvider.adaptFor(xmlDocument, false)) {
				// The content model provider can collect the system ids
				// ex for <?xml-model , the model provider which takes care of xml-model returns
				// the href of xml-model.
				Collection<Identifier> identifiers = modelProvider.getIdentifiers(xmlDocument, namespaceURI);
				for (Identifier identifier : identifiers) {
					String systemId = identifier.getSystemId();
					String publicId = identifier.getPublicId() != null ? identifier.getPublicId() : namespaceURI;
					// get the content model document from the current system id
					CMDocument cmDocument = findCMDocument(xmlDocument.getDocumentURI(), publicId, systemId,
							modelProvider);
					if (cmDocument != null) {
						documents.add(cmDocument);
					}
				}
			}
		}
		if (documents.isEmpty()) {
			CMDocument cmDocument = findCMDocument(xmlDocument.getDocumentURI(), namespaceURI, null, null);
			if (cmDocument != null) {
				documents.add(cmDocument);
			}
		}
		return documents;
	}

	/**
	 * Returns true if the given document is linked to the given grammar URI (XML
	 * Schema, DTD) and false otherwise.
	 *
	 * @param document   the DOM document
	 * @param grammarURI the grammar URI
	 * @return true if the given document is linked to the given grammar URI (XML
	 *         Schema, DTD) and false otherwise.
	 */
	public boolean dependsOnGrammar(DOMDocument document, String grammarURI) {
		if (StringUtils.isEmpty(grammarURI)) {
			return false;
		}
		for (ContentModelProvider modelProvider : modelProviders) {
			if (modelProvider.adaptFor(document, false)) {
				Collection<Identifier> identifiers = modelProvider.getIdentifiers(document, document.getNamespaceURI());
				for (Identifier identifier : identifiers) {
					String publicId = identifier.getPublicId();
					String systemId = identifier.getSystemId();
					String key = resolverManager.resolve(document.getDocumentURI(), publicId, systemId);
					if (grammarURI.equals(key)) {
						return true;
					}
				}
			}
		}
		String key = resolverManager.resolve(document.getDocumentURI(), null, null);
		return grammarURI.equals(key);
	}

	/**
	 * Returns informations about all referenced grammar (XSD, DTD) from the given
	 * DOM document.
	 *
	 * <p>
	 * In other words, it gives information about
	 *
	 * <ul>
	 * <li>which XSD, DTD files are bound to the DOM document.</li>
	 * <li>which binding strategies are used (catalog, file association,
	 * xsi:schemaLocation, xsi:noNamespaceSchemaLocation, DOCTYPE).</li>
	 * <li>the cache file path (for remote grammar with http://)</li>
	 *
	 * </p>
	 *
	 * @param document the DOM document.
	 *
	 * @return informations about all referenced grammar (XSD, DTD) from the given
	 *         DOM document.
	 */
	public Set<ReferencedGrammarInfo> getReferencedGrammarInfos(DOMDocument document) {
		Set<ReferencedGrammarInfo> referencedGrammarInfos = new LinkedHashSet<>();
		for (ContentModelProvider modelProvider : modelProviders) {
			if (modelProvider.adaptFor(document, false)) {
				Collection<Identifier> identifiers = modelProvider.getIdentifiers(document, null);
				for (Identifier identifier : identifiers) {
					String publicId = identifier.getPublicId();
					String systemId = identifier.getSystemId();
					fillReferencedGrammarInfo(document, publicId, systemId, identifier, referencedGrammarInfos);
				}
			}
		}
		fillReferencedGrammarInfo(document, document.getNamespaceURI(), null, null, referencedGrammarInfos);
		return referencedGrammarInfos;
	}

	private void fillReferencedGrammarInfo(DOMDocument document, String publicId, String systemId,
			Identifier identifier, Set<ReferencedGrammarInfo> referencedGrammarInfos) {
		ResolvedURIInfo resolvedURIInfo = resolverManager.resolveInfo(document.getDocumentURI(), publicId, systemId);
		if (resolvedURIInfo != null) {
			GrammarCacheInfo grammarCacheInfo = null;
			String cachedResolvedUri = null;
			boolean downloading = false;
			Exception cacheError = null;
			String resolvedUri = resolvedURIInfo.getResolvedURI();
			if (cacheResolverExtension.canUseCache(resolvedUri)) {
				// The DTD/XML Schema comes from http://, ftp:// etc and cache manager is
				// activated
				// Try to load the DTD/XML Schema with the cache manager
				try {
					Path file = cacheResolverExtension.getCachedResource(resolvedUri);
					if (file != null) {
						cachedResolvedUri = file.toUri().toString();
					}
				} catch (CacheResourceDownloadingException e) {
					// the DTD/XML Schema is downloading
					downloading = true;
				} catch (Exception e) {
					// other error like network which is not available
					cacheError = e;
				} finally {
					if (cachedResolvedUri == null) {
						try {
							cachedResolvedUri = CacheResourcesManager.getResourceCachePath(resolvedUri).toUri()
									.toString();
						} catch (IOException e) {
							// should never occur
						}
					}
				}
				grammarCacheInfo = new GrammarCacheInfo(cachedResolvedUri, downloading, cacheError);
			}
			if (identifier == null) {
				identifier = new Identifier(publicId, systemId, null, null);
			}
			referencedGrammarInfos.add(new ReferencedGrammarInfo(resolvedURIInfo, grammarCacheInfo, identifier));
		}
	}

	/**
	 * Returns the content model document loaded by the given uri and null
	 * otherwise.
	 *
	 * @param publicId      the public identifier.
	 * @param systemId      the expanded system identifier.
	 * @param modelProvider
	 * @return the content model document loaded by the given uri and null
	 *         otherwise.
	 */
	private CMDocument findCMDocument(String uri, String publicId, String systemId,
			ContentModelProvider modelProvider) {
		// Resolve the XML Schema/DTD uri (file, http, etc)
		String resolvedUri = resolverManager.resolve(uri, publicId, systemId);
		if (resolvedUri == null) {
			return null;
		}
		// the XML Schema, DTD can be resolved
		if (modelProvider == null) {
			// the model provider cannot be get with standard mean (xsi:schemaLocation,
			// xsi:noNamespaceSchemaLocation, doctype)
			// try to get it by using extension (ex: .xsd, .dtd)
			modelProvider = getModelProviderByURI(resolvedUri);
		}
		if (modelProvider == null) {
			return null;
		}
		// Try to get the document from the cache
		CMDocument cmDocument = getCMDocumentFromCache(resolvedUri);
		if (cmDocument != null) {
			return cmDocument;
		}
		if (cacheResolverExtension.canUseCache(resolvedUri)) {
			// The DTD/XML Schema comes from http://, ftp:// etc and cache manager is
			// activated
			// Try to load the DTD/XML Schema with the cache manager
			try {
				Path file = cacheResolverExtension.getCachedResource(resolvedUri);
				if (file != null) {
					cmDocument = modelProvider.createCMDocument(file.toUri().toString());
				}
			} catch (CacheResourceDownloadingException e) {
				// the DTD/XML Schema is downloading
				return null;
			} catch (Exception e) {
				// other error like network which is not available
				cmDocument = modelProvider.createCMDocument(resolvedUri);
			}
		} else {
			cmDocument = modelProvider.createCMDocument(resolvedUri);
		}
		// Cache the document
		if (cmDocument != null) {
			cache(resolvedUri, cmDocument);
		}
		return cmDocument;
	}

	private CMDocument getCMDocumentFromCache(String key) {
		CMDocument document = null;
		synchronized (cmDocumentCache) {
			document = cmDocumentCache.get(key);
			if (document != null && document.isDirty()) {
				cmDocumentCache.remove(key);
				return null;
			}
		}
		return document;
	}

	private void cache(String key, CMDocument cmDocument) {
		synchronized (cmDocumentCache) {
			cmDocumentCache.put(key, cmDocument);
		}
	}

	/**
	 * Returns the model provider by the given uri and null otherwise.
	 *
	 * @param uri the grammar URI
	 *
	 * @return the model provider by the given uri and null otherwise.
	 */
	public ContentModelProvider getModelProviderByURI(String uri) {
		for (ContentModelProvider modelProvider : modelProviders) {
			if (modelProvider.adaptFor(uri)) {
				return modelProvider;
			}
		}
		return null;
	}

	/**
	 * Set up XML catalogs.
	 *
	 * @param catalogs list of XML catalog files.
	 * @return true if catalogs changed and false otherwise
	 */
	public boolean setCatalogs(String[] catalogs) {
		return catalogResolverExtension.setCatalogs(catalogs);
	}

	/**
	 * Refresh the XML catalogs.
	 */
	public void refreshCatalogs() {
		catalogResolverExtension.refreshCatalogs();
	}

	/**
	 * Set file associations.
	 *
	 * @param fileAssociations
	 * @return true if file associations changed and false otherwise
	 */
	public boolean setFileAssociations(XMLFileAssociation[] fileAssociations) {
		return this.fileAssociationResolver.setFileAssociations(fileAssociations);
	}

	public void setRootURI(String rootUri) {
		rootUri = URIUtils.sanitizingUri(rootUri);
		fileAssociationResolver.setRootUri(rootUri);
		catalogResolverExtension.setRootUri(rootUri);
	}

	public void setUseCache(boolean useCache) {
		cacheResolverExtension.setUseCache(useCache);
		if (!useCache) {
			grammarPool.clear();
		}
	}

	/**
	 * Remove the referenced grammar from the given document and clear the Xerces
	 * grammar cache which stores the XSD, DTD grammar.
	 *
	 * @param document the DOM document
	 *
	 * @throws IOException if the delete of grammar file cannot be done.
	 */
	public void evictCacheFor(DOMDocument document) throws IOException {
		// Get the referenced grammars
		Set<ReferencedGrammarInfo> referencedGrammarInfos = getReferencedGrammarInfos(document);
		if (referencedGrammarInfos.isEmpty()) {
			return;
		}
		// Loop for each referenced grammars and deleted the XSD, DTD file if it is in
		// the lemminx cache.
		int nbDeletedFiles = 0;
		try {
			for (ReferencedGrammarInfo referencedGrammarInfo : referencedGrammarInfos) {
				if (referencedGrammarInfo.isInCache()) {
					// Delete XSD/DTD file
					String resolvedURI = referencedGrammarInfo.getGrammarCacheInfo().getCachedResolvedUri();
					Files.deleteIfExists(FilesUtils.getPath(resolvedURI));
					// remove the XSD/DTD content model document from the cache.
					cmDocumentCache.remove(resolvedURI);
					nbDeletedFiles++;
				}
				// TODO : get XSD, DTD dependencies from the current referenced grammar to
				// delete the file too.
			}
		} finally {
			if (nbDeletedFiles > 0) {
				// TODO : clear only the DTD, XSD which has been deleted from the cache.
				grammarPool.clear();
			}
		}
	}

	/**
	 * Remove the cache directory (.lemminx/cache) if it exists and clear the Xerces
	 * grammar cache which stores the XSD, DTD grammars.
	 *
	 * @throws IOException if the delete of directory (.lemminx/cache) cannot be
	 *                     done.
	 */
	public void evictCache() throws IOException {
		try {
			cacheResolverExtension.evictCache();
		} finally {
			// clear Xerces cache
			grammarPool.clear();
			// clear the XSD/DTD content model documents.
			cmDocumentCache.clear();
		}

	}

	public void registerModelProvider(ContentModelProvider modelProvider) {
		modelProviders.add(modelProvider);
	}

	public void unregisterModelProvider(ContentModelProvider modelProvider) {
		modelProviders.remove(modelProvider);
	}

	public XMLGrammarPool getGrammarPool() {
		return cacheResolverExtension.isUseCache() ? grammarPool : null;
	}

}