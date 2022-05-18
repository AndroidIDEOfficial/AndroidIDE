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

package com.itsaky.lsp.java.providers;

import androidx.annotation.NonNull;

import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.java.models.JavaServerSettings;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Formats Java code using Google Java Format.
 *
 * @author Akash Yadav
 */
public class CodeFormatProvider {

  private static final ILogger LOG = ILogger.newInstance("JavaCodeFormatProvider");

  private final JavaServerSettings settings;

  public CodeFormatProvider(IServerSettings settings) {
    assert settings instanceof JavaServerSettings;
    this.settings = (JavaServerSettings) settings;
  }

  public CharSequence format(CharSequence input) {
    final long start = System.currentTimeMillis();
    try (final StringWriterCharSink sink = new StringWriterCharSink(); ) {
      final CharSource source = CharSource.wrap(input);
      final Formatter formatter = new Formatter(settings.getFormatterOptions());
      formatter.formatSource(source, sink);
      LOG.info("Java code formatted in", System.currentTimeMillis() - start + "ms");
      return sink.toString();
    } catch (Throwable e) {
      LOG.error("Failed to format code.", e);
      return input;
    }
  }

  private static class StringWriterCharSink extends CharSink implements AutoCloseable {

    private final StringWriter writer;

    private StringWriterCharSink() {
      this.writer = new StringWriter();
    }

    @NonNull
    @Override
    public Writer openStream() {
      return this.writer;
    }

    @Override
    public void close() throws Exception {
      this.writer.close();
    }

    @NonNull
    @Override
    public String toString() {
      return this.writer.toString();
    }
  }
}
