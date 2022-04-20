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
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 *
 * @author lahvac
 */
public class NBNames {

    public static final Context.Key<NBNames> nbNamesKey =
        new Context.Key<NBNames>();

    public static void preRegister(Context context) {
        context.put(nbNamesKey, new Context.Factory<NBNames>() {
            public NBNames make(Context c) {
                return new NBNames(c);
            }
        });
    }

    public static NBNames instance(Context context) {
        NBNames instance = context.get(nbNamesKey);
        if (instance == null)
            instance = new NBNames(context);
        return instance;
    }


    public final Name _org_netbeans_EnclosingMethod;
    public final Name _org_netbeans_TypeSignature;
    public final Name _org_netbeans_ParameterNames;
    public final Name _org_netbeans_SourceLevelAnnotations;
    public final Name _org_netbeans_SourceLevelParameterAnnotations;
    public final Name _org_netbeans_SourceLevelTypeAnnotations;

    protected NBNames(Context context) {
        Names n = Names.instance(context);

        _org_netbeans_EnclosingMethod = n.fromString("org.netbeans.EnclosingMethod");
        _org_netbeans_TypeSignature = n.fromString("org.netbeans.TypeSignature");
        _org_netbeans_ParameterNames = n.fromString("org.netbeans.ParameterNames");
        _org_netbeans_SourceLevelAnnotations = n.fromString("org.netbeans.SourceLevelAnnotations");
        _org_netbeans_SourceLevelParameterAnnotations = n.fromString("org.netbeans.SourceLevelParameterAnnotations");
        _org_netbeans_SourceLevelTypeAnnotations = n.fromString("org.netbeans.SourceLevelTypeAnnotations");
    }

}
