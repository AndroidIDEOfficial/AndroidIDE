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

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.VMUtils;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTaskImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

/**
 * @author Akash Yadav
 */
public class JavacTaskUtil {

  private static final ILogger LOG = ILogger.newInstance("JavacTaskUtil");

  public static void cleanup(JavacTaskImpl task) {
    if (VMUtils.isJvm()) {
      jvmCleanup(task);
    } else {
      task.cleanup();
    }
  }

  public static void jvmCleanup(@NonNull JavacTaskImpl task) {
    try {
      final Class<?> klass = JavacTaskImpl.class;
      final Method method = klass.getDeclaredMethod("cleanup");
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
      method.invoke(task);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      LOG.error("Unable to cleanup JavacTaskImpl", e);
      throw new RuntimeException(e);
    }
  }

  public static Iterable<? extends CompilationUnitTree> parse(
      JavacTaskImpl task, JavaFileObject... sources) throws IOException {

    if (!VMUtils.isJvm()) {
      return task.parse(sources);
    }

    // No method JavacTaskImpl.parse(JavaFileObject[]) in jdk
    return task.parse();
  }

  public static Iterable<? extends Element> enterTrees(
      JavacTaskImpl task, Iterable<? extends CompilationUnitTree> trees) throws IOException {

    if (!VMUtils.isJvm()) {
      return task.enterTrees(trees);
    }

    // No method JavacTaskImpl.enterTrees(Iterable) in jdk
    return task.enter(trees);
  }

  public static Iterable<? extends Element> analyze(
      JavacTaskImpl task, Iterable<? extends Element> elements) {
    // No difference in method signature defined in nb-javac-android and jdk
    return task.analyze(elements);
  }
}
