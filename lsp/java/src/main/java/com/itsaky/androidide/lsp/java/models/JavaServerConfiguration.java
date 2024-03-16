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

package com.itsaky.androidide.lsp.java.models;

import androidx.annotation.NonNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Configuration for the java language server.
 *
 * @author Akash Yadav
 */
public class JavaServerConfiguration {

  private Set<Path> classPaths;
  private Set<Path> sourceDirs;

  public JavaServerConfiguration() {
    this(Collections.emptySet(), Collections.emptySet());
  }

  public JavaServerConfiguration(Set<Path> classPaths, Set<Path> sourcePaths) {
    this.classPaths = classPaths;
    this.sourceDirs = sourcePaths;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClassPaths());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JavaServerConfiguration that = (JavaServerConfiguration) o;
    return Objects.equals(getClassPaths(), that.getClassPaths());
  }

  @NonNull
  @Override
  public String toString() {
    return "JavaServerConfiguration{" + "classPaths=" + classPaths + '}';
  }

  public Set<Path> getClassPaths() {
    return classPaths;
  }

  public JavaServerConfiguration setClassPaths(Set<Path> classPaths) {
    this.classPaths = classPaths;
    return this;
  }

  public Set<Path> getSourceDirs() {
    return sourceDirs;
  }
}
