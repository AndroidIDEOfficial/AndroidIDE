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

import static com.google.common.collect.Range.closedOpen;

import androidx.annotation.NonNull;
import com.google.common.collect.ImmutableList;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.Replacement;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.java.models.JavaServerSettings;
import com.itsaky.androidide.lsp.models.CodeFormatResult;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.lsp.models.IndexedTextEdit;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.utils.StopWatch;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Formats Java code using Google Java Format.
 *
 * @author Akash Yadav
 */
public class CodeFormatProvider {

  private static final Logger LOG = LoggerFactory.getLogger(CodeFormatProvider.class);

  private final JavaServerSettings settings;

  public CodeFormatProvider(IServerSettings settings) {
    assert settings instanceof JavaServerSettings;
    this.settings = (JavaServerSettings) settings;
  }

  public CodeFormatResult format(FormatCodeParams params) {
    try {
      final StopWatch watch = new StopWatch("Code formatting");
      final String content = params.getContent().toString();
      final Formatter formatter = new Formatter(settings.getFormatterOptions());

      if (params.getRange() == Range.NONE) {
        String formatted;
        try {
          formatted = formatter.formatSource(content);
        } catch (FormatterException e) {
          e.printStackTrace();
          formatted = content;
        }
        return CodeFormatResult.forWholeContent(content, formatted);
      }

      final Collection<com.google.common.collect.Range<Integer>> ranges =
          getCharRanges(content, params.getRange());

      final ImmutableList<Replacement> replacements =
          formatter.getFormatReplacements(content, ranges);

      watch.log();
      return createResult(replacements);
    } catch (Throwable e) {
      LOG.error("Failed to format code.", e);
      return CodeFormatResult.NONE;
    }
  }

  private CodeFormatResult createResult(final ImmutableList<Replacement> replacements) {
    final CodeFormatResult result = new CodeFormatResult(true);
    for (final Replacement replacement : replacements) {
      final com.google.common.collect.Range<Integer> range = replacement.getReplaceRange();
      final IndexedTextEdit edit = new IndexedTextEdit();
      edit.setNewText(replacement.getReplacementString());
      edit.setStart(range.lowerEndpoint());
      edit.setEnd(range.upperEndpoint());
      result.getIndexedTextEdits().add(edit);
    }
    return result;
  }

  @NonNull
  private Collection<com.google.common.collect.Range<Integer>> getCharRanges(
      final String content, @NonNull final Range range) {

    int start, end;
    if (range == Range.NONE) {
      start = 0;
      end = content.length();
    } else {
      start = range.getStart().requireIndex();
      end = range.getEnd().requireIndex();
    }

    return ImmutableList.of(closedOpen(start, end));
  }
}
