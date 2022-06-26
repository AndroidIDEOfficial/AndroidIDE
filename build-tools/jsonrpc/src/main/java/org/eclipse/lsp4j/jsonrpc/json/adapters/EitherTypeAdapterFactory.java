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
import com.google.gson.reflect.TypeToken;

import org.eclipse.lsp4j.jsonrpc.messages.Either;

/**
 * @deprecated Use {@link EitherTypeAdapter.Factory} instead.
 */
@Deprecated
public class EitherTypeAdapterFactory extends EitherTypeAdapter.Factory {

  /**
   * @deprecated Use {@link EitherTypeAdapter} instead.
   */
  @Deprecated
  protected static class Adapter<L, R> extends EitherTypeAdapter<L, R> {

    public Adapter(Gson gson, TypeToken<Either<L, R>> typeToken) {
      super(gson, typeToken);
    }
  }
}
