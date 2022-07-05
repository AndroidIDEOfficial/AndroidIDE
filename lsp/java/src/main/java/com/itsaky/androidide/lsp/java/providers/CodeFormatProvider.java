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

package com.itsaky.androidide.lsp.java.providers;

import androidx.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.googlejavaformat.java.Formatter;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.java.models.JavaServerSettings;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.lsp.models.Range;

import java.util.Collection;

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

  public CharSequence format(FormatCodeParams params) {
    final long start = System.currentTimeMillis();
    final String strContent = params.getContent().toString();
    CharSequence formatted;
    try {
      final Formatter formatter = new Formatter(settings.getFormatterOptions());
      if (params.getRange() != Range.NONE) {
        formatted = formatter.formatSource(strContent, getCharRanges(params.getRange()));
      } else {
        formatted = formatter.formatSource(strContent);
      }
    } catch (Throwable e) {
      LOG.error("Failed to format code.", e);
      formatted = params.getContent();
    }

    if (params.getRange() != Range.NONE) {
      final Range range = params.getRange();
      return formatted.subSequence(range.getStart().requireIndex(), range.getEnd().requireIndex());
    }

    LOG.info("Java code formatted in", System.currentTimeMillis() - start + "ms");

    return formatted;
  }

  @NonNull
  private Collection<com.google.common.collect.Range<Integer>> getCharRanges(
      @NonNull final Range range) {
    return ImmutableList.of(
        com.google.common.collect.Range.closedOpen(
            range.getStart().requireIndex(), range.getEnd().requireIndex()));
  }
}
