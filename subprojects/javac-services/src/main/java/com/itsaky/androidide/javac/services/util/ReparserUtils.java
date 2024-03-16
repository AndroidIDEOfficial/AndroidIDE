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

package com.itsaky.androidide.javac.services.util;

import androidx.annotation.Nullable;
import com.itsaky.androidide.javac.services.visitors.UnEnter;
import com.itsaky.androidide.utils.VMUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import openjdk.tools.javac.comp.Enter;
import openjdk.tools.javac.parser.JavacParser;
import openjdk.tools.javac.parser.LazyDocCommentTable;
import openjdk.tools.javac.tree.DocCommentTable;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.util.Context;
import openjdk.tools.javac.util.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Akash Yadav
 */
public class ReparserUtils {

  private static final Logger LOG = LoggerFactory.getLogger(ReparserUtils.class);

  private static final boolean isAndroid = !VMUtils.isJvm();
  private static boolean needUnenterScanner = true;
  private static Method unenter = null;
  private static Method lineMapBuild = null;
  private static Field lazyDocCommentsTable = null;
  private static Field parserDocComments = null;

  public static boolean canReparse() {
    if (isAndroid) {
      return true;
    }

    initIfNecessary();
    return unenter != null
        && lineMapBuild != null
        && lazyDocCommentsTable != null
        && parserDocComments != null;
  }

  public static void buildLineMap(Position.LineMap lines, char[] arr, int size) {
    if (!canReparse()) {
      LOG.warn("Cannot perform reparse");
      return;
    }

    if (isAndroid) {
      ((Position.LineMapImpl) lines).build(arr, size);
      return;
    }

    try {
      lineMapBuild.invoke(lines, arr, size);
    } catch (Throwable e) {
      LOG.error("Unable to build line map", e);
    }
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public static Map<JCTree, LazyDocCommentTable.Entry> getLazyDocCommentsTable(
      DocCommentTable docs) {
    if (!canReparse()) {
      LOG.warn("Cannot perform reparse");
      return null;
    }

    if (isAndroid) {
      return ((LazyDocCommentTable) docs).table;
    }

    try {
      return (Map<JCTree, LazyDocCommentTable.Entry>) lazyDocCommentsTable.get(docs);
    } catch (Throwable e) {
      LOG.error("Cannot get doc comment table", e);
      throw new RuntimeException(e);
    }
  }

  public static void unenter(Context context, JCTree.JCCompilationUnit cu, JCTree tree) {
    if (!canReparse()) {
      LOG.warn("Cannot perform reparse");
      return;
    }

    if (isAndroid) {
      Enter.instance(context).unenter(cu, tree);
      return;
    }

    if (needUnenterScanner) {
      new UnEnter(Enter.instance(context), cu.modle).scan(tree);
      return;
    }

    try {
      unenter.invoke(Enter.instance(context), cu, tree);
    } catch (Throwable err) {
      LOG.error("Cannot perform unenter", err);
    }
  }

  @Nullable
  public static DocCommentTable getDocComments(JavacParser parser) {
    if (!canReparse()) {
      LOG.warn("Cannot perform reparse");
      return null;
    }

    if (isAndroid) {
      return parser.getDocComments();
    }

    try {
      return (DocCommentTable) parserDocComments.get(parser);
    } catch (Throwable e) {
      LOG.error("Unablet to get DocCommentTable", e);
      throw new RuntimeException(e);
    }
  }

  private static void initIfNecessary() {
    if (isAndroid) {
      return;
    }

    if (unenter == null) {
      unenter =
          tryReflectMethod(Enter.class, "unenter", JCTree.JCCompilationUnit.class, JCTree.class);
      needUnenterScanner = unenter == null;
    }

    if (lineMapBuild == null) {
      lineMapBuild =
          tryReflectMethod(
              "openjdk.tools.javac.util.Position$LineMapImpl", "build", char[].class, int.class);
    }

    if (lazyDocCommentsTable == null) {
      lazyDocCommentsTable = tryReflectField(LazyDocCommentTable.class, "table");
    }

    if (parserDocComments == null) {
      parserDocComments = tryReflectField(JavacParser.class, "docComments");
    }
  }

  @SuppressWarnings("SameParameterValue")
  @Nullable
  private static Method tryReflectMethod(String klass, String methodName, Class<?>... argTypes) {
    try {
      return tryReflectMethod(Class.forName(klass), methodName, argTypes);
    } catch (ClassNotFoundException noMethod) {
      return null;
    }
  }

  @Nullable
  private static Method tryReflectMethod(Class<?> klass, String methodName, Class<?>... argTypes) {
    try {
      final Method method = klass.getDeclaredMethod(methodName, argTypes);
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
      return method;
    } catch (NoSuchMethodException noMethod) {
      return null;
    }
  }

  @Nullable
  private static Field tryReflectField(Class<?> klass, String fieldName) {
    try {
      final Field field = klass.getDeclaredField(fieldName);
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      return field;
    } catch (NoSuchFieldException noMethod) {
      return null;
    }
  }
}
