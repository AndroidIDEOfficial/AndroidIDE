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
package org.eclipse.lemminx.services.extensions.save;

/**
 * Abstract class for save context.
 *
 */
public abstract class AbstractSaveContext implements ISaveContext {

	private final String uri;
	private final Object settings;

	public AbstractSaveContext(Object settings) {
		this(null, settings);
	}

	public AbstractSaveContext(String uri) {
		this(uri, null);
	}

	AbstractSaveContext(String uri, Object settings) {
		this.uri = uri;
		this.settings = settings;
	}

	@Override
	public SaveContextType getType() {
		return uri != null ? SaveContextType.DOCUMENT : SaveContextType.SETTINGS;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public Object getSettings() {
		return settings;
	}

}
