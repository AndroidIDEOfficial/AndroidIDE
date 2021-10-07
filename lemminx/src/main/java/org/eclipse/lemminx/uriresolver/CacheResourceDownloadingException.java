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

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Exception thrown when a resource (XML Schema, DTD) is downloading.
 *
 */
public class CacheResourceDownloadingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_LOADING_MSG = "The resource ''{0}'' is downloading.";

	private static final String RESOURCE_NOT_IN_DEPLOYED_PATH_MSG = "The resource ''{0}'' cannot be downloaded in the cache path.";
	
	private final String resourceURI;

	private final CompletableFuture<Path> future;

	public CacheResourceDownloadingException(String resourceURI) {
		super(MessageFormat.format(RESOURCE_NOT_IN_DEPLOYED_PATH_MSG, resourceURI));
		this.resourceURI = resourceURI;
		this.future = null;
	}

	public CacheResourceDownloadingException(String resourceURI, CompletableFuture<Path> future) {
		super(MessageFormat.format(RESOURCE_LOADING_MSG, resourceURI));
		this.resourceURI = resourceURI;
		this.future = future;
	}

	/**
	 * Returns the resource URI which is downloading.
	 * 
	 * @return the resource URI which is downloading.
	 */
	public String getResourceURI() {
		return resourceURI;
	}

	/**
	 * Returns true if it's a DTD which id downloading and false otherwise.
	 * 
	 * @return true if it's a DTD which id downloading and false otherwise.
	 */
	public boolean isDTD() {
		return resourceURI != null && resourceURI.endsWith(".dtd");
	}

	public CompletableFuture<Path> getFuture() {
		return future;
	}
}
