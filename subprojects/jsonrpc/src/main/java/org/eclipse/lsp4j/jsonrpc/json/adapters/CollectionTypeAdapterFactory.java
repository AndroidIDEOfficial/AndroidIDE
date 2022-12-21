/******************************************************************************
 * Copyright (c) 2016 TypeFox and others.
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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link CollectionTypeAdapter.Factory} instead.
 */
@Deprecated
public class CollectionTypeAdapterFactory extends CollectionTypeAdapter.Factory {

  /**
   * @deprecated Use {@link CollectionTypeAdapter} instead.
   */
  @Deprecated
  protected static class Adapter<E> extends CollectionTypeAdapter<E> {

    public Adapter(
        Gson gson,
        Type elementType,
        TypeAdapter<E> elementTypeAdapter,
        Supplier<Collection<E>> constructor) {
      super(gson, elementType, elementTypeAdapter, constructor);
    }
  }
}
