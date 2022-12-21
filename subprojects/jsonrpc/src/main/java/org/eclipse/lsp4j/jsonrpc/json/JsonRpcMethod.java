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
package org.eclipse.lsp4j.jsonrpc.json;

import com.google.gson.TypeAdapterFactory;

import java.lang.reflect.Type;

/** A description of a JSON-RPC method. */
public class JsonRpcMethod {

  private final String methodName;
  private final Type[] parameterTypes;
  private final Type returnType;
  private final TypeAdapterFactory returnTypeAdapterFactory;
  private final boolean isNotification;

  private JsonRpcMethod(
      String methodName,
      Type[] parameterTypes,
      Type returnType,
      TypeAdapterFactory returnTypeAdapterFactory,
      boolean isNotification) {
    if (methodName == null) throw new NullPointerException("methodName");
    this.methodName = methodName;
    this.parameterTypes = parameterTypes;
    this.returnType = returnType;
    this.returnTypeAdapterFactory = returnTypeAdapterFactory;
    this.isNotification = isNotification;
  }

  public static JsonRpcMethod notification(String name, Type... parameterTypes) {
    return new JsonRpcMethod(name, parameterTypes, Void.class, null, true);
  }

  public static JsonRpcMethod request(String name, Type returnType, Type... parameterTypes) {
    return new JsonRpcMethod(name, parameterTypes, returnType, null, false);
  }

  public static JsonRpcMethod request(
      String name,
      Type returnType,
      TypeAdapterFactory returnTypeAdapterFactory,
      Type... parameterTypes) {
    return new JsonRpcMethod(name, parameterTypes, returnType, returnTypeAdapterFactory, false);
  }

  public String getMethodName() {
    return methodName;
  }

  public Type[] getParameterTypes() {
    return parameterTypes;
  }

  public Type getReturnType() {
    return returnType;
  }

  public TypeAdapterFactory getReturnTypeAdapterFactory() {
    return returnTypeAdapterFactory;
  }

  public boolean isNotification() {
    return isNotification;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (isNotification) builder.append("JsonRpcMethod (notification) {\n");
    else builder.append("JsonRpcMethod (request) {\n");
    builder.append("\tmethodName: ").append(methodName).append('\n');
    if (parameterTypes != null)
      builder.append("\tparameterTypes: ").append(parameterTypes).append('\n');
    if (returnType != null) builder.append("\treturnType: ").append(returnType).append('\n');
    builder.append("}");
    return builder.toString();
  }
}
