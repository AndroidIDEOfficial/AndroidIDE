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

/**
 * Exception thrown when a resource (XML Schema, DTD) has error while
 * downloading.
 *
 */
public class CacheResourceDownloadedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CacheResourceDownloadedException(String message, Throwable cause) {
		super(message, cause);
	}
}
