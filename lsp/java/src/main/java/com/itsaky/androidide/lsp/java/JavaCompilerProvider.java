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

package com.itsaky.androidide.lsp.java;

import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService;
import com.itsaky.androidide.projects.api.ModuleProject;

/**
 * Provides {@link JavaCompilerService} instances for different {@link ModuleProject}s.
 *
 * @author Akash Yadav
 */
public class JavaCompilerProvider {

  private static JavaCompilerProvider sInstance;
  private volatile JavaCompilerService compilerService;

  private JavaCompilerProvider() {}

  public static JavaCompilerProvider getInstance() {
    if (sInstance == null) {
      sInstance = new JavaCompilerProvider();
    }

    return sInstance;
  }

  public static JavaCompilerService get(ModuleProject module) {
    return JavaCompilerProvider.getInstance().forModule(module);
  }

  /** For internal use only. */
  public JavaCompilerService getCompilerService() {
    return compilerService;
  }

  public synchronized JavaCompilerService forModule(ModuleProject module) {
    // A module instance is set to the compiler only in case the project is initialized or
    // this method was called with other mdoule instance.
    if (compilerService == null || compilerService.getModule() != module) {
      compilerService = new JavaCompilerService(module);
    }

    return compilerService;
  }

  public synchronized void destory() {
    if (compilerService != null) {
      compilerService.destroy();
    }
    compilerService = null;
  }
}
