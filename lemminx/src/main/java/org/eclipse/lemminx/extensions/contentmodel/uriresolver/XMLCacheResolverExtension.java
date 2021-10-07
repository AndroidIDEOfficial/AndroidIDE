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
package org.eclipse.lemminx.extensions.contentmodel.uriresolver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.uriresolver.CacheResourceDownloadedException;
import org.eclipse.lemminx.uriresolver.CacheResourcesManager;
import org.eclipse.lemminx.uriresolver.URIResolverExtension;

/**
 * URI resolver which, on the first access, downloads the XML Schema or DTD from
 * "http(s)" or "ftp" URIs to the file system. On subsequent calls, the locally
 * cached file is used instead of being remotely accessed. This cache
 * drastically improves the resolution performance of some XML Schemas (ex:
 * xml.xsd)
 */
public class XMLCacheResolverExtension implements URIResolverExtension {

	private static class XMLFileInputSource extends XMLInputSource {

		private final Path file;

		public XMLFileInputSource(XMLResourceIdentifier resourceIdentifier, Path file) {
			super(resourceIdentifier);
			this.file = file;
		}

		@Override
		public InputStream getByteStream() {
			// Load the file input stream only if it is used
			InputStream input = super.getByteStream();
			if (input == null) {
				try {
					super.setByteStream(Files.newInputStream(file));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return super.getByteStream();
		}
	}

	private final CacheResourcesManager cacheResourcesManager;

	public XMLCacheResolverExtension() {
		this.cacheResourcesManager = new CacheResourcesManager();
	}

	@Override
	public String resolve(String baseLocation, String publicId, String systemId) {
		// Don't resolve the URI
		return null;
	}

	@Override
	public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException {
		String url = resourceIdentifier.getExpandedSystemId();
		// Try to get the downloaded resource. In the case where the resource is
		// downloading but takes too long, a CacheResourceDownloadingException is
		// thrown.
		Path file = getCachedResource(url);
		if (file != null) {
			// The resource was downloaded locally, use it.
			return new XMLFileInputSource(resourceIdentifier, file);
		}
		return null;
	}

	/**
	 * Returns the cached resource path from the given url and null otherwise.
	 * 
	 * @param url the url
	 * @return the cached resource path from the given url and null otherwise.
	 * @throws IOException
	 * @throws CacheResourceDownloadedException throws when resource is downloading.
	 */
	public Path getCachedResource(String url) throws IOException, CacheResourceDownloadedException {
		// Cache is used only for resource coming from "http(s)" or "ftp".
		if (canUseCache(url)) {
			// Try to get the downloaded resource. In the case where the resource is
			// downloading but takes too long, a CacheResourceDownloadingException is
			// thrown.
			return cacheResourcesManager.getResource(url);
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if cache is enabled and url comes from "http(s)" or
	 * "ftp" and <code>false</code> otherwise.
	 * 
	 * @param url
	 * @return <code>true</code> if cache is enabled and url comes from "http(s)" or
	 *         "ftp" and <code>false</code> otherwise.
	 */
	public boolean canUseCache(String url) {
		return cacheResourcesManager.canUseCache(url);
	}

	/**
	 * Set <code>true</code> if cache must be used, <code>false</code> otherwise.
	 * 
	 * @param useCache <code>true</code> if cache must be used, <code>false</code>
	 *                 otherwise.
	 */
	public void setUseCache(boolean useCache) {
		cacheResourcesManager.setUseCache(useCache);
	}

	/**
	 * Returns <code>true</code> if cache must be used, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if cache must be used, <code>false</code>
	 *         otherwise.
	 */
	public boolean isUseCache() {
		return cacheResourcesManager.isUseCache();
	}

	/**
	 * Remove the cache directory (.lemminx/cache) if it exists.
	 * 
	 * @throws IOException if the delete of directory (.lemminx/cache) cannot be
	 *                     done.
	 */
	public void evictCache() throws IOException {
		cacheResourcesManager.evictCache();
	}
}
