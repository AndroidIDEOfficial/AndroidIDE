/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.lemminx.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.eclipse.lemminx.settings.FaultTolerantTypeAdapterFactory;
import org.eclipse.lsp4j.jsonrpc.json.adapters.EitherTypeAdapter;

/**
 * JSONUtility
 */
public class JSONUtility {

	private JSONUtility(){}

	public static <T> T toModel(Object object, Class<T> clazz) {
		if (object == null) {
			return null;
		}
		if (clazz == null) {
			throw new IllegalArgumentException("Class can not be null");
		}
		if (object instanceof JsonElement) {
			Gson gson = getDefaultGsonBuilder().create();
			return gson.fromJson((JsonElement) object, clazz);
		}
		if (clazz.isInstance(object)) {
			return clazz.cast(object);
		}
		return null;
	}

	private static GsonBuilder getDefaultGsonBuilder() {
		return new GsonBuilder() //
				// required to deserialize XMLFormattingOptions which extends FormattingOptions
				// which uses Either
				.registerTypeAdapterFactory(new EitherTypeAdapter.Factory()) //
				.registerTypeAdapterFactory(new FaultTolerantTypeAdapterFactory());
	}

}