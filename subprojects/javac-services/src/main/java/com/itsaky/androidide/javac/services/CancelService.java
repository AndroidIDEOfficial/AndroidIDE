/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.itsaky.androidide.javac.services;

import openjdk.tools.javac.util.Context;

/**
 * @author Tomas Zezula
 */
public class CancelService {

  /** The context key for the parameter name resolver. */
  protected static final Context.Key<CancelService> cancelServiceKey = new Context.Key<>();

  protected CancelService() {}

  public static CancelService instance(Context context) {
    CancelService instance = context.get(cancelServiceKey);
    if (instance == null) {
      instance = new CancelService();
      context.put(cancelServiceKey, instance);
    }
    return instance;
  }

  public static void preRegister(Context context, CancelService impl) {
    context.put(CancelService.cancelServiceKey, impl);
  }

  public final void abortIfCanceled() {
    if (isCanceled()) {
      throw new CancelAbort();
    }
  }

  public boolean isCanceled() {
    return false;
  }
}
