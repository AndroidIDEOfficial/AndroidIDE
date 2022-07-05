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

package com.itsaky.lsp.java.utils;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.VMUtils;
import com.sun.tools.javac.api.JavacTaskImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Akash Yadav
 */
public class JavacTaskUtil {

  private static final ILogger LOG = ILogger.newInstance("JavacTaskUtil");

  public static void cleanup (JavacTaskImpl task) {
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
}
