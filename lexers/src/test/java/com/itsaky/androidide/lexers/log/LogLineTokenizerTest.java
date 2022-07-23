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

package com.itsaky.androidide.lexers.log;

import static com.google.common.truth.Truth.assertThat;
import static com.itsaky.androidide.lexers.log.LogToken.DATE;
import static com.itsaky.androidide.lexers.log.LogToken.MESSAGE;
import static com.itsaky.androidide.lexers.log.LogToken.PID;
import static com.itsaky.androidide.lexers.log.LogToken.PRIORITY;
import static com.itsaky.androidide.lexers.log.LogToken.TAG;
import static com.itsaky.androidide.lexers.log.LogToken.TID;
import static com.itsaky.androidide.lexers.log.LogToken.TIME;
import static com.itsaky.androidide.lexers.log.LogToken.WS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.stream.Collectors;

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4.class)
public class LogLineTokenizerTest {

  private static final String FULL =
      "07-23 09:14:07.656 25903/25903 ..mizeRestrictionManager_ E sInstance is null, start a new sInstance";
  private static final String SIMPLE =
      "ToolingApiServer          D   ToolingApiServerImpl       Got initialize request  InitializeProjectMessage(directory=/storage/emulated/0/AndroidIDEProjects/sora-editor, gradleInstallation=/data/data/com.itsaky.androidide/files/home/gradle-7.5)  ";

  @Test
  public void testFullLineWithWs() {
    final var line = FULL;
    final var tokenizer = new LogLineTokenizer(line, false);
    final var tokens = tokenizer.allTokens();
    final var types = tokens.stream().map(token -> token.type).collect(Collectors.toList());
    final var texts =
        tokens.stream()
            .map(token -> line.substring(token.startIndex, token.endIndex + 1))
            .collect(Collectors.toList());

    assertThat(types)
        .containsExactly(DATE, WS, TIME, WS, PID, TID, WS, TAG, WS, PRIORITY, WS, MESSAGE);
    assertThat(texts)
        .containsExactly(
            "07-23",
            " ",
            "09:14:07.656",
            " ",
            "25903",
            "25903",
            " ",
            "..mizeRestrictionManager_",
            " ",
            "E",
            " ",
            "sInstance is null, start a new sInstance");
  }

  @Test
  public void testFullLineWithoutWs() {
    final var line = FULL;
    final var tokenizer = new LogLineTokenizer(line);
    final var tokens = tokenizer.allTokens();
    final var types = tokens.stream().map(token -> token.type).collect(Collectors.toList());
    final var texts =
        tokens.stream()
            .map(token -> line.substring(token.startIndex, token.endIndex + 1))
            .collect(Collectors.toList());

    assertThat(types).containsExactly(DATE, TIME, PID, TID, TAG, PRIORITY, MESSAGE);
    assertThat(texts)
        .containsExactly(
            "07-23",
            "09:14:07.656",
            "25903",
            "25903",
            "..mizeRestrictionManager_",
            "E",
            "sInstance is null, start a new sInstance");
  }

  @Test
  public void testSimpleLineWithWs() {
    final var line = SIMPLE;
    final var tokenizer = new LogLineTokenizer(line, false);
    tokenizer.setParseSimple(true);
    final var tokens = tokenizer.allTokens();
    final var types = tokens.stream().map(token -> token.type).collect(Collectors.toList());
    final var texts =
        tokens.stream()
            .map(token -> line.substring(token.startIndex, token.endIndex + 1))
            .collect(Collectors.toList());

    assertThat(types).containsExactly(TAG, WS, PRIORITY, WS, MESSAGE);
    assertThat(texts)
        .containsExactly(
            "ToolingApiServer",
            "          ",
            "D",
            "   ",
            "ToolingApiServerImpl       Got initialize request  InitializeProjectMessage(directory=/storage/emulated/0/AndroidIDEProjects/sora-editor, gradleInstallation=/data/data/com.itsaky.androidide/files/home/gradle-7.5)  ");
  }

  @Test
  public void testSimpleLineWithoutWs() {
    final var line = SIMPLE;
    final var tokenizer = new LogLineTokenizer(line);
    tokenizer.setParseSimple(true);
    final var tokens = tokenizer.allTokens();
    final var types = tokens.stream().map(token -> token.type).collect(Collectors.toList());
    final var texts =
        tokens.stream()
            .map(token -> line.substring(token.startIndex, token.endIndex + 1))
            .collect(Collectors.toList());

    assertThat(types).containsExactly(TAG, PRIORITY, MESSAGE);
    assertThat(texts)
        .containsExactly(
            "ToolingApiServer",
            "D",
            "ToolingApiServerImpl       Got initialize request  InitializeProjectMessage(directory=/storage/emulated/0/AndroidIDEProjects/sora-editor, gradleInstallation=/data/data/com.itsaky.androidide/files/home/gradle-7.5)  ");
  }
}
