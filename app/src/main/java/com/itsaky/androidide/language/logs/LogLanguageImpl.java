/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.language.logs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;

import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

public class LogLanguageImpl extends IDELanguage {

  private static final LogAnalyzer analyzer = new LogAnalyzer();

  public void addLine(LogLine line) {
    analyzer.addLine(line);
  }

  @NonNull
  @Override
  public AnalyzeManager getAnalyzeManager() {
    return analyzer;
  }

  @Override
  public int getInterruptionLevel() {
    return INTERRUPTION_LEVEL_STRONG;
  }

  @Override
  public void requireAutoComplete(
      @NonNull ContentReference content,
      @NonNull CharPosition position,
      @NonNull CompletionPublisher publisher,
      @NonNull Bundle extraArguments)
      throws CompletionCancelledException {}

  @Override
  public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
    return 0;
  }

  @Override
  public CharSequence format(CharSequence text) {
    return text;
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return new NewlineHandler[0];
  }

  @Override
  public void destroy() {}

  private static class LogAnalyzer extends SimpleAnalyzeManager<Void> {

    private static final List<LogLine> lines = new ArrayList<>();

    public LogAnalyzer addLine(LogLine line) {
      if (line != null) lines.add(line);

      return this;
    }

    @Override
    protected Styles analyze(StringBuilder text, Delegate<Void> delegate) {
      final var styles = new Styles();
      final var colors = new MappedSpans.Builder();
      int lastLine = 0;
      for (int i = 0; i < lines.size() && !delegate.isCancelled(); i++) {
        if (i == 0) colors.addNormalIfNull();
        LogLine line = lines.get(i);
        colors.addIfNeeded(i, 0, SchemeAndroidIDE.forLogPriority(line.priority));
        lastLine = i;
      }
      colors.determine(lastLine);

      styles.spans = colors.build();

      return styles;
    }
  }
}
