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

package com.itsaky.lsp.java.compiler;

import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.util.PathUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class SourceFileObject implements JavaFileObject {
  /** path is the absolute path to this file on disk */
  final Path path;
  /** contents is the text in this file, or null if we should use the text in FileStore */
  final String contents;
  /** if contents is set, the modified time of contents */
  final Instant modified;

  public SourceFileObject(Path path) {
    this(path, null, Instant.EPOCH);
  }

  public SourceFileObject(Path path, String contents, Instant modified) {
    if (!FileStore.isJavaFile(path)) throw new RuntimeException(path + " is not a java source");
    this.path = path;
    this.contents = contents;
    this.modified = modified;
  }

  private static Kind kindFromExtension(String name) {
    for (Kind candidate : Kind.values()) {
      if (name.endsWith(candidate.extension)) {
        return candidate;
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass() != SourceFileObject.class) return false;
    SourceFileObject that = (SourceFileObject) other;
    return PathUtils.isSameFile(this.path, that.path);
  }

  @Override
  public int hashCode() {
    return this.path.hashCode();
  }

  @Override
  public Kind getKind() {
    String name = path.getFileName().toString();
    return kindFromExtension(name);
  }

  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    return path.getFileName().toString().equals(simpleName + kind.extension);
  }

  @Override
  public NestingKind getNestingKind() {
    return null;
  }

  @Override
  public Modifier getAccessLevel() {
    return null;
  }

  @Override
  public URI toUri() {
    return path.toUri();
  }

  @Override
  public String getName() {
    return path.toString();
  }

  @Override
  public InputStream openInputStream() {
    if (contents != null) {
      byte[] bytes = contents.getBytes();
      return new ByteArrayInputStream(bytes);
    }
    return FileStore.inputStream(path);
  }

  @Override
  public OutputStream openOutputStream() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Reader openReader(boolean ignoreEncodingErrors) {
    if (contents != null) {
      return new StringReader(contents);
    }
    return FileStore.bufferedReader(path);
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    if (contents != null) {
      return contents;
    }
    return FileStore.contents(path);
  }

  @Override
  public Writer openWriter() {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getLastModified() {
    if (contents != null) {
      return modified.toEpochMilli();
    }
    return FileStore.modified(path).toEpochMilli();
  }

  @Override
  public boolean delete() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return path.toString();
  }
}
