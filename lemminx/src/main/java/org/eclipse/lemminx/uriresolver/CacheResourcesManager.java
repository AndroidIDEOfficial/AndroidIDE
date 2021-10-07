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
package org.eclipse.lemminx.uriresolver;

import static org.eclipse.lemminx.utils.ExceptionUtils.getRootCause;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.utils.FilesUtils;
import org.eclipse.lemminx.utils.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;

/**
 * Cache resources manager.
 *
 */
public class CacheResourcesManager {

	private static final String USER_AGENT_KEY = "User-Agent";
	private static final String USER_AGENT_VALUE = "LemMinX";

	protected final Cache<String, Boolean> unavailableURICache;

	private static final String CACHE_PATH = "cache";
	private static final Logger LOGGER = Logger.getLogger(CacheResourcesManager.class.getName());

	private final Map<String, CompletableFuture<Path>> resourcesLoading;
	private boolean useCache;

	private final Set<String> protocolsForCahe;

	class ResourceInfo {

		String resourceURI;

		CompletableFuture<Path> future;

	}

	/**
	 * Classpath resource to deploy into the lemminx cache
	 */
	public static class ResourceToDeploy {

		private final Path resourceCachePath;
		private final String resourceFromClasspath;

		/**
		 * @param resourceURI           - used to compute the path to deploy the
		 *                              resource to in the lemminx cache. Generally this
		 *                              is the URL to the resource. Ex.
		 *                              https://www.w3.org/2007/schema-for-xslt20.xsd
		 * @param resourceFromClasspath - the classpath location of the resource to
		 *                              deploy to the lemminx cache
		 */
		public ResourceToDeploy(String resourceURI, String resourceFromClasspath) {
			this(URI.create(resourceURI), resourceFromClasspath);
		}

		/**
		 * @param resourceURI           - used to compute the path to deploy the
		 *                              resource to in the lemminx cache. Generally this
		 *                              is the URL to the resource. Ex.
		 *                              https://www.w3.org/2007/schema-for-xslt20.xsd
		 * @param resourceFromClasspath - the classpath location of the resource to
		 *                              deploy to the lemminx cache
		 */
		public ResourceToDeploy(URI resourceURI, String resourceFromClasspath) {
			this.resourceCachePath = Paths.get(CACHE_PATH, resourceURI.getScheme(), resourceURI.getHost(),
					resourceURI.getPath());
			this.resourceFromClasspath = resourceFromClasspath.startsWith("/") ? resourceFromClasspath
					: "/" + resourceFromClasspath;
		}

		/**
		 * @return The computed path in the lemmix cache that the resource will be
		 *         stored at
		 */
		public Path getDeployedPath() throws IOException {
			return FilesUtils.getDeployedPath(resourceCachePath);
		}

		/**
		 * @return The path to the resource on the classpath
		 */
		public String getResourceFromClasspath() {
			return resourceFromClasspath;
		}
	}

	public CacheResourcesManager() {
		this(CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(30, TimeUnit.SECONDS).build());
	}

	public CacheResourcesManager(Cache<String, Boolean> cache) {
		resourcesLoading = new HashMap<>();
		protocolsForCahe = new HashSet<>();
		unavailableURICache = cache;
		addDefaultProtocolsForCache();
	}

	public Path getResource(final String resourceURI) throws IOException {
		Path resourceCachePath = getResourceCachePath(resourceURI);
		if (Files.exists(resourceCachePath)) {
			return resourceCachePath;
		}
		if (!FilesUtils.isIncludedInDeployedPath(resourceCachePath)) {
			throw new CacheResourceDownloadingException(resourceURI);
		}
		if (unavailableURICache.getIfPresent(resourceURI) != null) {
			LOGGER.info("Ignored unavailable schema URI: " + resourceURI + "\n");
			return null;
		}

		CompletableFuture<Path> f = null;
		synchronized (resourcesLoading) {
			if (resourcesLoading.containsKey(resourceURI)) {
				CompletableFuture<Path> future = resourcesLoading.get(resourceURI);
				throw new CacheResourceDownloadingException(resourceURI, future);
			}
			f = downloadResource(resourceURI, resourceCachePath);
			resourcesLoading.put(resourceURI, f);
		}

		if (f.getNow(null) == null) {
			throw new CacheResourceDownloadingException(resourceURI, f);
		}

		return resourceCachePath;
	}

	private CompletableFuture<Path> downloadResource(final String resourceURI, Path resourceCachePath) {
		return CompletableFuture.supplyAsync(() -> {
			LOGGER.info("Downloading " + resourceURI + " to " + resourceCachePath + "...");
			long start = System.currentTimeMillis();
			URLConnection conn = null;
			try {
				String actualURI = resourceURI;
				URL url = new URL(actualURI);
				conn = url.openConnection();
				conn.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
				/* XXX: This should really be implemented using HttpClient or similar */
				int allowedRedirects = 5;
				while (conn.getHeaderField("Location") != null && allowedRedirects > 0) //$NON-NLS-1$
				{
					allowedRedirects--;
					url = new URL(actualURI = conn.getHeaderField("Location")); //$NON-NLS-1$
					conn = url.openConnection();
					conn.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
				}

				// Download resource in a temporary file
				Path path = Files.createTempFile(resourceCachePath.getFileName().toString(), ".lemminx");
				try (ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
						FileOutputStream fos = new FileOutputStream(path.toFile())) {
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}

				// Move the temporary file in the lemminx cache folder.
				Path dir = resourceCachePath.getParent();
				if (!Files.exists(dir)) {
					Files.createDirectories(dir);
				}
				Files.move(path, resourceCachePath);
				long elapsed = System.currentTimeMillis() - start;
				LOGGER.info("Downloaded " + resourceURI + " to " + resourceCachePath + " in " + elapsed + "ms");
			} catch (Exception e) {
				// Do nothing
				unavailableURICache.put(resourceURI, true);
				Throwable rootCause = getRootCause(e);
				String error = "[" + rootCause.getClass().getTypeName() + "] " + rootCause.getMessage();
				LOGGER.log(Level.SEVERE,
						"Error while downloading " + resourceURI + " to " + resourceCachePath + " : " + error);
				throw new CacheResourceDownloadedException(
						"Error while downloading '" + resourceURI + "' to " + resourceCachePath + ".", e);
			} finally {
				synchronized (resourcesLoading) {
					resourcesLoading.remove(resourceURI);
				}
				if (conn != null && conn instanceof HttpURLConnection) {
					((HttpURLConnection) conn).disconnect();
				}
			}
			return resourceCachePath;
		});
	}

	public static Path getResourceCachePath(String resourceURI) throws IOException {
		URI uri = URI.create(resourceURI);
		return getResourceCachePath(uri);
	}

	public static Path getResourceCachePath(URI uri) throws IOException {
		Path resourceCachePath = uri.getPort() > 0
				? Paths.get(CACHE_PATH, uri.getScheme(), uri.getHost(), String.valueOf(uri.getPort()), uri.getPath())
				: Paths.get(CACHE_PATH, uri.getScheme(), uri.getHost(), uri.getPath());
		return FilesUtils.getDeployedPath(resourceCachePath);
	}

	/**
	 * Try to get the cached {@link ResourceToDeploy#resourceCachePath} in cache
	 * file system and if it is not found, create the file with the given content of
	 * {@link ResourceToDeploy#resourceFromClasspath} stored in classpath.
	 *
	 * @param resource the resource to deploy if needed.
	 *
	 * @return the cached {@link ResourceToDeploy#resourceCachePath} in cache file
	 *         system.
	 * @throws IOException
	 */
	public static Path getResourceCachePath(ResourceToDeploy resource) throws IOException {
		Path outFile = resource.getDeployedPath();
		if (!outFile.toFile().exists()) {
			try (InputStream in = CacheResourcesManager.class
					.getResourceAsStream(resource.getResourceFromClasspath())) {
				FilesUtils.saveToFile(in, outFile);
			}
		}
		return outFile;
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
		return isUseCache() && isUseCacheFor(url);
	}

	/**
	 * Set <code>true</code> if cache must be used, <code>false</code> otherwise.
	 *
	 * @param useCache <code>true</code> if cache must be used, <code>false</code>
	 *                 otherwise.
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * Returns <code>true</code> if cache must be used, <code>false</code>
	 * otherwise.
	 *
	 * @return <code>true</code> if cache must be used, <code>false</code>
	 *         otherwise.
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * Remove the cache directory (.lemminx/cache) if it exists.
	 *
	 * @throws IOException if the delete of directory (.lemminx/cache) cannot be
	 *                     done.
	 */
	public void evictCache() throws IOException {
		// Get the cache directory path
		Path cachePath = FilesUtils.getDeployedPath(Paths.get(CACHE_PATH));
		if (Files.exists(cachePath)) {
			// Remove the cache directory
			MoreFiles.deleteDirectoryContents(cachePath, RecursiveDeleteOption.ALLOW_INSECURE);
		}
	}

	/**
	 * Add protocol for using cache when url will start with the given protocol.
	 *
	 * @param protocol the protocol to add.
	 */
	public void addProtocolForCahe(String protocol) {
		protocolsForCahe.add(formatProtocol(protocol));
	}

	/**
	 * Remove protocol to avoid using cache when url will start with the given
	 * protocol.
	 *
	 * @param protocol the protocol to remove.
	 */
	public void removeProtocolForCahe(String protocol) {
		protocolsForCahe.remove(formatProtocol(protocol));
	}

	/**
	 * Add ':' separator if the given protocol doesn't contain it.
	 *
	 * @param protocol the protocol to format.
	 *
	 * @return the protocol concat with ':'.
	 */
	private static String formatProtocol(String protocol) {
		if (!protocol.endsWith(":")) {
			return protocol + ":";
		}
		return protocol;
	}

	/**
	 * Returns true if the cache must be used for the given url and false otherwise.
	 *
	 * @param url the url.
	 *
	 * @return true if the cache must be used for the given url and false otherwise.
	 */
	private boolean isUseCacheFor(String url) {
		if (StringUtils.isEmpty(url)) {
			return false;
		}
		for (String protocol : protocolsForCahe) {
			if (url.startsWith(protocol)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add http, https, ftp protocol to use cache.
	 */
	private void addDefaultProtocolsForCache() {
		addProtocolForCahe("http");
		addProtocolForCahe("https");
		addProtocolForCahe("ftp");
	}

}
