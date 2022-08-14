/******************************************************************************
 * Copyright (c) 2016-2017 TypeFox and others.
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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** A custom type adapter for enums that uses integer values. */
public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {

  private static String VALUE_FIELD_NAME = "value";
  private final Map<String, T> nameToConstant = new HashMap<>();
  private final Map<Integer, T> valueToConstant = new HashMap<>();
  private final Map<T, Integer> constantToValue = new HashMap<>();

  EnumTypeAdapter(Class<T> classOfT) throws IllegalAccessException {
    try {
      Field valueField = classOfT.getDeclaredField(VALUE_FIELD_NAME);
      if (valueField.getType() != int.class && valueField.getType() != Integer.class)
        throw new IllegalArgumentException("The field 'value' must contain an integer value.");
      valueField.setAccessible(true);
      for (T constant : classOfT.getEnumConstants()) {
        nameToConstant.put(constant.name(), constant);
        Integer constValue = (Integer) valueField.get(constant);
        valueToConstant.put(constValue, constant);
        constantToValue.put(constant, constValue);
      }
    } catch (NoSuchFieldException e) {
      for (T constant : classOfT.getEnumConstants()) {
        nameToConstant.put(constant.name(), constant);
        int constValue = constant.ordinal();
        valueToConstant.put(constValue, constant);
        constantToValue.put(constant, constValue);
      }
    }
  }

  @Override
  public void write(JsonWriter out, T value) throws IOException {
    if (value != null) out.value(constantToValue.get(value));
    else out.value((String) null);
  }

  @Override
  public T read(JsonReader in) throws IOException {
    JsonToken peek = in.peek();
    if (peek == JsonToken.NULL) {
      in.nextNull();
      return null;
    } else if (peek == JsonToken.NUMBER) {
      return valueToConstant.get(in.nextInt());
    } else {
      String string = in.nextString();
      try {
        return valueToConstant.get(Integer.parseInt(string));
      } catch (NumberFormatException e) {
        return nameToConstant.get(string);
      }
    }
  }

  public static class Factory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      Class<?> rawType = typeToken.getRawType();
      if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) return null;
      if (!rawType.isEnum()) rawType = rawType.getSuperclass();
      try {
        return new EnumTypeAdapter(rawType);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
