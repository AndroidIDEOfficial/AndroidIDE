/******************************************************************************
 * Copyright (c) 2018 TypeFox and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc.json.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/** A type adapter that reads every input into a tree of {@link JsonElement}s. */
public class JsonElementTypeAdapter extends TypeAdapter<Object> {

  private final Gson gson;
  private final TypeAdapter<JsonElement> adapter;

  public JsonElementTypeAdapter(Gson gson) {
    this.gson = gson;
    this.adapter = gson.getAdapter(JsonElement.class);
  }

  @Override
  public void write(JsonWriter out, Object value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else if (value instanceof JsonElement) {
      adapter.write(out, (JsonElement) value);
    } else {
      gson.toJson(value, value.getClass(), out);
      ;
    }
  }

  @Override
  public JsonElement read(JsonReader in) throws IOException {
    return adapter.read(in);
  }

  /**
   * This factory should not be registered with a GsonBuilder because it always matches. Use it as
   * argument to a {@link com.google.gson.annotations.JsonAdapter} annotation like this:
   * {@code @JsonAdapter(JsonElementTypeAdapter.Factory.class)}
   */
  public static class Factory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return (TypeAdapter<T>) new JsonElementTypeAdapter(gson);
    }
  }
}
