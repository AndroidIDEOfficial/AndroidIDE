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
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;

/** A specialized type adapter for collections that can handle single values. */
public class CollectionTypeAdapter<E> extends TypeAdapter<Collection<E>> {

  private final Gson gson;
  private final Type elementType;
  private final TypeAdapter<E> elementTypeAdapter;
  private final Supplier<Collection<E>> constructor;

  public CollectionTypeAdapter(
      Gson gson,
      Type elementType,
      TypeAdapter<E> elementTypeAdapter,
      Supplier<Collection<E>> constructor) {
    this.gson = gson;
    this.elementType = elementType;
    this.elementTypeAdapter = elementTypeAdapter;
    this.constructor = constructor;
  }

  @Override
  public void write(JsonWriter out, Collection<E> collection) throws IOException {
    if (collection == null) {
      out.nullValue();
      return;
    }
    out.beginArray();
    for (E element : collection) {
      if (element != null
          && elementType != element.getClass()
          && (elementType instanceof TypeVariable<?> || elementType instanceof Class<?>)) {
        @SuppressWarnings("unchecked")
        TypeAdapter<E> runtimeTypeAdapter =
            (TypeAdapter<E>) gson.getAdapter(TypeToken.get(element.getClass()));
        runtimeTypeAdapter.write(out, element);
      } else {
        elementTypeAdapter.write(out, element);
      }
    }
    out.endArray();
  }

  @Override
  public Collection<E> read(JsonReader in) throws IOException {
    JsonToken peek = in.peek();
    if (peek == JsonToken.NULL) {
      in.nextNull();
      return null;
    } else if (peek == JsonToken.BEGIN_ARRAY) {
      Collection<E> collection = constructor.get();
      in.beginArray();
      while (in.hasNext()) {
        E instance = elementTypeAdapter.read(in);
        collection.add(instance);
      }
      in.endArray();
      return collection;
    } else {
      Collection<E> collection = constructor.get();
      E instance = elementTypeAdapter.read(in);
      collection.add(instance);
      return collection;
    }
  }

  public static class Factory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (!Collection.class.isAssignableFrom(typeToken.getRawType())) return null;

      Type[] elementTypes = TypeUtils.getElementTypes(typeToken, Collection.class);
      if (elementTypes.length != 1) return null;
      Type elementType = elementTypes[0];
      TypeAdapter<?> elementTypeAdapter;
      if (elementType == Object.class) elementTypeAdapter = new JsonElementTypeAdapter(gson);
      else elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
      Supplier<Collection<Object>> constructor =
          getConstructor((Class<Collection<Object>>) typeToken.getRawType());
      return (TypeAdapter<T>) create(gson, elementType, elementTypeAdapter, constructor);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected TypeAdapter<?> create(
        Gson gson,
        Type elementType,
        TypeAdapter<?> elementTypeAdapter,
        Supplier<Collection<Object>> constructor) {
      return new CollectionTypeAdapter(gson, elementType, elementTypeAdapter, constructor);
    }

    protected <E> Supplier<Collection<E>> getConstructor(Class<? extends Collection<E>> rawType) {
      try {
        Constructor<? extends Collection<E>> constructor = rawType.getDeclaredConstructor();
        return () -> {
          try {
            return constructor.newInstance();
          } catch (Exception e) {
            throw new JsonParseException(e);
          }
        };
      } catch (Exception e) {
        if (SortedSet.class.isAssignableFrom(rawType)) return () -> new TreeSet<E>();
        else if (Set.class.isAssignableFrom(rawType)) return () -> new LinkedHashSet<E>();
        else if (Queue.class.isAssignableFrom(rawType)) return () -> new LinkedList<E>();
        else return () -> new ArrayList<E>();
      }
    }
  }
}
