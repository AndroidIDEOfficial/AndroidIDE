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

import static com.itsaky.androidide.lexers.log.LogToken.DATE;
import static com.itsaky.androidide.lexers.log.LogToken.MESSAGE;
import static com.itsaky.androidide.lexers.log.LogToken.PID;
import static com.itsaky.androidide.lexers.log.LogToken.PRIORITY;
import static com.itsaky.androidide.lexers.log.LogToken.TAG;
import static com.itsaky.androidide.lexers.log.LogToken.TID;
import static com.itsaky.androidide.lexers.log.LogToken.TIME;
import static com.itsaky.androidide.lexers.log.LogToken.UNKNOWN;
import static com.itsaky.androidide.lexers.log.LogToken.WS;
import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Tokenizer for tokenizing a log line. The tokenizer does not expect multiline input.
 *
 * @author Akash Yadav
 */
public class LogLineTokenizer {

  /** The EOF token is returned if nothing is left to tokenize. */
  public static final LogToken EOF = new LogToken("", -1, -1, -1);

  /** The log line input. */
  public final char[] input;

  /** Whether the tokenizer should skip whitespace tokens. */
  public final boolean skipWs;

  /** Current state of the lexer */
  public int lexerState = -1;

  public int index = -1;
  private boolean parseSimple = false;

  public LogLineTokenizer(String input) {
    this(input, true);
  }

  public LogLineTokenizer(String input, final boolean skipWs) {
    this(input.toCharArray(), skipWs);
  }

  public LogLineTokenizer(final char[] input, final boolean skipWs) {
    this.input = input;
    this.skipWs = skipWs;
  }

  public LogLineTokenizer(final char[] input) {
    this(input, true);
  }

  /**
   * Set to <code>true</code>> if parsing the simple version of <code>LogLine</code>.
   *
   * @param parseSimple The new flag.
   */
  public void setParseSimple(final boolean parseSimple) {
    this.parseSimple = parseSimple;
  }

  /**
   * Generate the next token.
   *
   * @return The next token.
   */
  public LogToken next() {
    ++index;
    if (index >= input.length) {
      return EOF;
    }

    final var text = new StringBuilder();
    final var c = input[index];
    if (isWhitespace(c)) {
      final var whitespace = parseWhitespace(text);

      return skipWs ? next() : whitespace;
    } else if (c == '-' && index == 0) {
      // Lines like these must be handled as well
      // --------- beginning of system
      // --------- beginning of main

      index = input.length;
      return createToken(new StringBuilder().append(input), 0, MESSAGE);
    }

    if (parseSimple) {
      return nextSimpleToken(text);
    }

    return nextToken(text);
  }

  /**
   * Returns all the tokens in the input.
   *
   * @return The list of tokens.
   */
  public List<LogToken> allTokens() {
    final var tokens = new ArrayList<LogToken>(12);
    while (true) {
      final var token = next();
      if (token == EOF) {
        break;
      }
      tokens.add(token);
    }

    return tokens;
  }

  /**
   * Generate the next token.
   *
   * @param text The token text is appended to this `StringBuilder` instance.
   * @return The next token or {@link #EOF} if no tokens are available.
   */
  protected LogToken nextToken(final StringBuilder text) {
    Function<StringBuilder, LogToken> function;
    int nextState;
    switch (lexerState) {
      case UNKNOWN:
        function = this::parseDate;
        nextState = DATE;
        break;
      case DATE:
        function = this::parseTime;
        nextState = TIME;
        break;
      case TIME:
        function = this::parsePid;
        nextState = PID;
        break;
      case PID:
        function = this::parseTid;
        nextState = TID;
        break;
      case TID:
        function = this::parseTag;
        nextState = TAG;
        break;
      case TAG:
        function = this::parsePriority;
        nextState = PRIORITY;
        break;
      case PRIORITY:
        function = this::parseMessage;
        nextState = MESSAGE;
        break;
      default:
        return EOF;
    }

    final var token = function.apply(text);
    lexerState = nextState;
    return token;
  }

  /** Parse the message token. */
  protected LogToken parseMessage(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          text.append(c);
          return false;
        });
    return createToken(text, start, MESSAGE);
  }

  /** Parse the thread ID token. */
  protected LogToken parseDate(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (isDigit(c) || c == '-') {
            text.append(c);
            return false;
          }

          return true;
        });
    return createToken(text, start, DATE);
  }

  /**
   * Generate the next token for the simple version of <code>LogLine</code>.
   *
   * @param text The token text is appended to this `StringBuilder` instance.
   * @return The next token or {@link #EOF} if no tokens are available.
   */
  protected LogToken nextSimpleToken(final StringBuilder text) {
    Function<StringBuilder, LogToken> function;
    int nextState;
    switch (lexerState) {
      case UNKNOWN:
        function = this::parseTag;
        nextState = TAG;
        break;
      case TAG:
        function = this::parsePriority;
        nextState = PRIORITY;
        break;
      case PRIORITY:
        function = this::parseMessage;
        nextState = MESSAGE;
        break;
      default:
        return EOF;
    }

    final var token = function.apply(text);
    lexerState = nextState;
    return token;
  }

  /** Parse the priority token. */
  protected LogToken parsePriority(final StringBuilder text) {
    if (!isWhitespace(input[index + 1])) {
      throw new IllegalStateException("Expecting a single-character priority string");
    }

    final var start = index;
    text.append(input[start]);
    ++index;
    return createToken(text, start, PRIORITY);
  }

  /** Parse whitespace tokens. */
  protected LogToken parseWhitespace(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (isWhitespace(c)) {
            text.append(c);
            return false;
          }
          return true;
        });
    return createToken(text, start, WS);
  }

  /**
   * Create a new {@link LogToken}.
   *
   * @param text The token text.
   * @param start The start index of token.
   * @param type The type of the token.
   * @return The token.
   */
  protected LogToken createToken(final StringBuilder text, final int start, final int type) {
    return new LogToken(text.toString(), start, --index, type);
  }

  /**
   * Iterates over the input and invokes the given function with the characters in the input. If the
   * function returns <code>true</code>, iteration stops.
   *
   * @param function The function.
   */
  protected void iterateInput(Function<Character, Boolean> function) {
    Objects.requireNonNull(function);
    for (; index < input.length; index++) {
      if (function.apply(input[index])) {
        break;
      }
    }
  }

  /** Parse the process ID token. */
  private LogToken parsePid(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (c == '/' || !isDigit(c)) {
            return true;
          }

          text.append(c);
          return false;
        });
    final var token = createToken(text, start, PID);

    // Skip the '/'
    ++index;

    return token;
  }

  /** Parse the time token. */
  private LogToken parseTime(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (isDigit(c) || c == ':' || c == '.') {
            text.append(c);
            return false;
          }

          return true;
        });

    return createToken(text, start, TIME);
  }

  /** Parse the thread ID token. */
  private LogToken parseTid(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (isDigit(c)) {
            text.append(c);
            return false;
          }

          return true;
        });
    return createToken(text, start, TID);
  }

  /** Parse the log tag token. */
  private LogToken parseTag(final StringBuilder text) {
    final var start = index;
    iterateInput(
        c -> {
          if (isWhitespace(c)) {
            return true;
          }
          text.append(c);
          return false;
        });
    return createToken(text, start, TAG);
  }
}
