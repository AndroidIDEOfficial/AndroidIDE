/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.settings;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Creates TypeAdapters that are wrapped in a try/catch statement, so if
 * anything goes wrong when deserializing an object, it is set to null and not
 * parsed.
 *
 */
public class FaultTolerantTypeAdapterFactory implements TypeAdapterFactory {

	private static final Logger LOGGER = Logger.getLogger(FaultTolerantTypeAdapterFactory.class.getName());

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		return wrapTypeAdapter(gson.getDelegateAdapter(this, type));
	}

	/**
	 * Wrap the reader of the given TypeAdapter with a try/catch, and skip reading
	 * the value if an error occurs
	 *
	 * @param <T>         The type that the TypeAdapter
	 * @param typeAdapter the TypeAdapter to wrap
	 * @return the wrapped instance of the TypeAdapter
	 */
	private <T> TypeAdapter<T> wrapTypeAdapter(TypeAdapter<T> typeAdapter) {
		return new TypeAdapter<T>() {

			@Override
			public void write(JsonWriter out, T value) throws IOException {
				typeAdapter.write(out, value);
			}

			@Override
			public T read(JsonReader in) throws IOException {
				try {
					return typeAdapter.read(in);
				} catch (Exception e) {
					LOGGER.warning("Encountered an invalid setting. Using the default value. " + //
							"Please check your settings for outdated or invalid settings.\n" +
							e);
					in.skipValue();
					return null;
				}
			}

		};
	}

}
