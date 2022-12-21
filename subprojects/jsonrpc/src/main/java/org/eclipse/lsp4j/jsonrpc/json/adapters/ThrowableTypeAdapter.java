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
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * A type adapter for {@link Throwable}. This is used to report issues to the sender of a request.
 */
public class ThrowableTypeAdapter extends TypeAdapter<Throwable> {

  private final TypeToken<Throwable> typeToken;

  public ThrowableTypeAdapter(TypeToken<Throwable> typeToken) {
    this.typeToken = typeToken;
  }

  @Override
  public void write(JsonWriter out, Throwable throwable) throws IOException {
    if (throwable == null) {
      out.nullValue();
    } else if (throwable.getMessage() == null && throwable.getCause() != null) {
      write(out, throwable.getCause());
    } else {
      out.beginObject();
      if (throwable.getMessage() != null) {
        out.name("message");
        out.value(throwable.getMessage());
      }
      if (shouldWriteCause(throwable)) {
        out.name("cause");
        write(out, throwable.getCause());
      }
      out.endObject();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Throwable read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    in.beginObject();
    String message = null;
    Throwable cause = null;
    while (in.hasNext()) {
      String name = in.nextName();
      switch (name) {
        case "message":
          {
            message = in.nextString();
            break;
          }
        case "cause":
          {
            cause = read(in);
            break;
          }
        default:
          in.skipValue();
      }
    }
    in.endObject();

    try {
      Constructor<Throwable> constructor;
      if (message == null && cause == null) {
        constructor = (Constructor<Throwable>) typeToken.getRawType().getDeclaredConstructor();
        return constructor.newInstance();
      } else if (message == null) {
        constructor =
            (Constructor<Throwable>) typeToken.getRawType().getDeclaredConstructor(Throwable.class);
        return constructor.newInstance(cause);
      } else if (cause == null) {
        constructor =
            (Constructor<Throwable>) typeToken.getRawType().getDeclaredConstructor(String.class);
        return constructor.newInstance(message);
      } else {
        constructor =
            (Constructor<Throwable>)
                typeToken.getRawType().getDeclaredConstructor(String.class, Throwable.class);
        return constructor.newInstance(message, cause);
      }
    } catch (NoSuchMethodException e) {
      if (message == null && cause == null) return new RuntimeException();
      else if (message == null) return new RuntimeException(cause);
      else if (cause == null) return new RuntimeException(message);
      else return new RuntimeException(message, cause);
    } catch (Exception e) {
      throw new JsonParseException(e);
    }
  }

  private boolean shouldWriteCause(Throwable throwable) {
    Throwable cause = throwable.getCause();
    if (cause == null || cause.getMessage() == null || cause == throwable) return false;
    if (throwable.getMessage() != null && throwable.getMessage().contains(cause.getMessage()))
      return false;
    return true;
  }

  public static class Factory implements TypeAdapterFactory {

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (!Throwable.class.isAssignableFrom(typeToken.getRawType())) return null;

      return (TypeAdapter<T>) new ThrowableTypeAdapter((TypeToken<Throwable>) typeToken);
    }
  }
}
