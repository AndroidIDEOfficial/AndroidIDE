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

package com.itsaky.androidide.tooling.impl.util;

/**
 * @author Akash Yadav
 */

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.lsp4j.jsonrpc.services.JsonDelegate;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

public final class AnnotationUtil {

  private AnnotationUtil() {
  }

  public static void findDelegateSegments(Class<?> clazz, Set<Class<?>> visited,
      Consumer<Method> acceptor) {
    if (clazz == null || !visited.add(clazz)) {
      return;
    }
    findDelegateSegments(clazz.getSuperclass(), visited, acceptor);
    for (Class<?> interf : clazz.getInterfaces()) {
      findDelegateSegments(interf, visited, acceptor);
    }
    for (Method method : clazz.getDeclaredMethods()) {
      if (isDelegateMethod(method)) {
        acceptor.accept(method);
      }
    }
  }

  public static boolean isDelegateMethod(Method method) {
    if (!method.isSynthetic()) {
      JsonDelegate jsonDelegate = method.getAnnotation(JsonDelegate.class);
      if (jsonDelegate != null) {
        if (!(method.getParameterCount() == 0 && method.getReturnType().isInterface())) {
          throw new IllegalStateException(
              "The method " + method.toString() + " is not a proper @JsonDelegate method.");
        }
        return true;
      }
    }
    return false;
  }


  /**
   * Depth first search for annotated methods in hierarchy.
   */
  public static void findRpcMethods(Class<?> clazz, Set<Class<?>> visited,
      Consumer<MethodInfo> acceptor) {
    if (clazz == null || !visited.add(clazz)) {
      return;
    }
    findRpcMethods(clazz.getSuperclass(), visited, acceptor);
    for (Class<?> interf : clazz.getInterfaces()) {
      findRpcMethods(interf, visited, acceptor);
    }
    String segment = getSegment(clazz);
    for (Method method : clazz.getDeclaredMethods()) {
      MethodInfo methodInfo = createMethodInfo(method, segment);
      if (methodInfo != null) {
        acceptor.accept(methodInfo);
      }
    }
  }

  protected static String getSegment(Class<?> clazz) {
    JsonSegment jsonSegment = clazz.getAnnotation(JsonSegment.class);
    return jsonSegment == null ? "" : jsonSegment.value() + "/";
  }

  protected static MethodInfo createMethodInfo(Method method, String segment) {
    if (!method.isSynthetic()) {
      JsonRequest jsonRequest = method.getAnnotation(JsonRequest.class);
      if (jsonRequest != null) {
        return createRequestInfo(method, segment, jsonRequest);
      }
      JsonNotification jsonNotification = method.getAnnotation(JsonNotification.class);
      if (jsonNotification != null) {
        return createNotificationInfo(method, segment, jsonNotification);
      }
    }
    return null;
  }

  protected static MethodInfo createNotificationInfo(Method method, String segment,
      JsonNotification jsonNotification) {
    MethodInfo methodInfo = createMethodInfo(method, jsonNotification.useSegment(), segment,
        jsonNotification.value());
    methodInfo.isNotification = true;
    return methodInfo;
  }

  protected static MethodInfo createRequestInfo(Method method, String segment,
      JsonRequest jsonRequest) {
    return createMethodInfo(method, jsonRequest.useSegment(), segment, jsonRequest.value());
  }

  protected static MethodInfo createMethodInfo(Method method, boolean useSegment, String segment,
      String value) {
    method.setAccessible(true);

    MethodInfo methodInfo = new MethodInfo();
    methodInfo.method = method;
    methodInfo.parameterTypes = getParameterTypes(method);
    methodInfo.name = getMethodName(method, useSegment, segment, value);
    return methodInfo;
  }

  protected static String getMethodName(Method method, boolean useSegment, String segment,
      String value) {
    String name = value != null && value.length() > 0 ? value : method.getName();
    return useSegment ? segment + name : name;
  }

  protected static Type[] getParameterTypes(Method method) {
    return Arrays.stream(method.getParameters()).map(t -> t.getParameterizedType())
        .toArray(Type[]::new);
  }

  public static class MethodInfo {

    private static Type[] EMPTY_TYPE_ARRAY = {};
    public String name;
    public Method method;
    public Type[] parameterTypes = EMPTY_TYPE_ARRAY;
    public boolean isNotification = false;
  }

  public static class DelegateInfo {

    public Method method;
    public Object delegate;
  }
}

