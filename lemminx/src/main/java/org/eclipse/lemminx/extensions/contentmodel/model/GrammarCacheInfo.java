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
package org.eclipse.lemminx.extensions.contentmodel.model;

/**
 * Grammar cache information.
 */
public class GrammarCacheInfo {

	private final String cachedResolvedUri;
	private final boolean downloading;
	private final Exception cacheError;

	public GrammarCacheInfo(String cachedResolvedUri, boolean downloading, Exception cacheError) {
		super();
		this.cachedResolvedUri = cachedResolvedUri;
		this.downloading = downloading;
		this.cacheError = cacheError;
	}

	/**
	 * Returns the file path cache.
	 * 
	 * @return the file path cache.
	 */
	public String getCachedResolvedUri() {
		return cachedResolvedUri;
	}

	/**
	 * Returns true if the grammar is downloading and false otherwise.
	 * 
	 * @return true if the grammar is downloading and false otherwise.
	 */
	public boolean isDownloading() {
		return downloading;
	}

	/**
	 * Returns the error while downloading and null otherwise.
	 * 
	 * @return the error while downloading and null otherwise.
	 */
	public Exception getCacheError() {
		return cacheError;
	}
}
