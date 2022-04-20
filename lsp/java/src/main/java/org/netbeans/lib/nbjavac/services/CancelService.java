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

package org.netbeans.lib.nbjavac.services;

import com.sun.tools.javac.util.Context;

/**
 *
 * @author Tomas Zezula
 */
public class CancelService {

    /** The context key for the parameter name resolver. */
    protected static final Context.Key<CancelService> cancelServiceKey =
        new Context.Key<CancelService>();

    public static CancelService instance(Context context) {
        CancelService instance = context.get(cancelServiceKey);
        if (instance == null) {
            instance = new CancelService();
            context.put(cancelServiceKey, instance);
        }
        return instance;
    }

    protected CancelService() {
    }


    public boolean isCanceled () {
        return false;
    }

    public final void abortIfCanceled () {
        if (isCanceled()) {
            throw new CancelAbort ();
        }
    }

}
