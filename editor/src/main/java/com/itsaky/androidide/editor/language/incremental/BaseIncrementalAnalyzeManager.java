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

package com.itsaky.androidide.editor.language.incremental;

import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.FIXME_COMMENT;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TODO_COMMENT;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.withoutCompletion;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.ArrayUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.EvictingQueue;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import io.github.rosemoe.sora.lang.analysis.AsyncIncrementalAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.lang.styling.TextStyle;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.util.IntPair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import kotlin.Pair;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

/**
 * Base class for implementing an {@link AsyncIncrementalAnalyzeManager} in AndroidIDE.
 *
 * @author Akash Yadav
 */
public abstract class BaseIncrementalAnalyzeManager extends
                                                    AsyncIncrementalAnalyzeManager<LineState, IncrementalToken> {

  protected final Lexer lexer;
  private final int[] multilineStartTypes;
  private final int[] multilineEndTypes;
  private final int[] blockTokens;

  public BaseIncrementalAnalyzeManager(final Class<? extends Lexer> lexer) {
    Objects.requireNonNull(lexer, "Cannot create analyzer manager for null lexer");
    this.lexer = createLexerInstance(lexer);

    var multilineTokenTypes = getMultilineTokenStartEndTypes();
    verifyMultilineTypes(multilineTokenTypes);

    this.multilineStartTypes = multilineTokenTypes[0];
    this.multilineEndTypes = multilineTokenTypes[1];

    var tokens = getCodeBlockTokens();
    if (tokens == null) {
      tokens = new int[0];
    }

    this.blockTokens = tokens;
  }

  @NonNull
  private Lexer createLexerInstance(final Class<? extends Lexer> lexer) {
    try {
      final var constructor = lexer.getConstructor(CharStream.class);
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor.newInstance(createStream(""));
    } catch (Throwable err) {
      throw new RuntimeException("Unable to create Lexer instance", err);
    }
  }

  @NonNull
  protected CharStream createStream(@NonNull CharSequence source) {
    Objects.requireNonNull(source);
    try {
      return CharStreams.fromReader(new CharSequenceReader(source));
    } catch (IOException e) {
      throw new RuntimeException("Cannot create CharStream for source", e);
    }
  }

  private void verifyMultilineTypes(@NonNull final int[][] types) {
    Preconditions.checkState(types.length == 2,
      "There must be exact two inner int[] in multiline token types");

    final var start = types[0];
    final var end = types[1];
    Preconditions.checkState(start.length > 0, "Invalid start token types");
    Preconditions.checkState(end.length > 0, "Invalid end token types");
  }

  /**
   * Returns the token types which start and end a multiline token.
   *
   * @return A <b>2xn</b> matrix where int[] at index 0 specifies token types which start a
   * multiline token and int[] 1 specifies tokens which end the multiline token. For example,
   * <p>[['/', '*'], ['*', '/']].
   * <p>But instead of characters, there must be token types.
   */
  protected abstract int[][] getMultilineTokenStartEndTypes();

  /**
   * Get the token types for left and right braces.
   *
   * @return An array of left and right brace token types.
   */
  protected abstract int[] getCodeBlockTokens();

  @Override
  public LineState getInitialState() {
    return new LineState();
  }

  @Override
  public boolean stateEquals(final LineState state, final LineState another) {
    return state.equals(another);
  }

  @Override
  public LineTokenizeResult<LineState, IncrementalToken> tokenizeLine(final CharSequence lineText,
                                                                      final LineState state,
                                                                      final int line
  ) {
    final var tokens = new ArrayList<IncrementalToken>();
    var newState = 0;
    var stateObj = new LineState();
    if (state.state == LineState.NORMAL) {
      newState = tokenizeNormal(lineText, 0, tokens, stateObj, state.lexerMode);
    } else if (state.state == LineState.INCOMPLETE) {
      final var result = fillIncomplete(lineText, tokens, state.lexerMode);
      newState = IntPair.getFirst(result);
      if (newState == LineState.NORMAL) {
        newState = tokenizeNormal(lineText, IntPair.getSecond(result), tokens, stateObj,
          state.lexerMode);
      } else {
        newState = LineState.INCOMPLETE;
      }
    }
    stateObj.state = newState;
    stateObj.lexerMode = lexer._mode;
    return new LineTokenizeResult<>(stateObj, tokens);
  }

  @Override
  public List<Span> generateSpansForLine(
    final LineTokenizeResult<LineState, IncrementalToken> tokens
  ) {
    var result = generateSpans(tokens);

    Objects.requireNonNull(result);

    if (result.isEmpty()) {
      result.add(Span.obtain(0, TextStyle.makeStyle(SchemeAndroidIDE.TEXT_NORMAL)));
    }

    return result;
  }

  /**
   * Generate spans for the given {@link LineTokenizeResult}.
   *
   * @param tokens The tokenization result.
   * @return The spans for the tokens.
   */
  protected abstract List<Span> generateSpans(
    final LineTokenizeResult<LineState, IncrementalToken> tokens
  );

  @Override
  public List<CodeBlock> computeBlocks(final Content text,
                                       final AsyncIncrementalAnalyzeManager<LineState, IncrementalToken>.CodeBlockAnalyzeDelegate delegate
  ) {
    final var stack = new Stack<CodeBlock>();
    final var blocks = new ArrayList<CodeBlock>();
    var line = 0;
    var maxSwitch = 0;
    var currSwitch = 0;
    while (delegate.isNotCancelled() && line < text.getLineCount()) {
      final var tokens = getState(line);
      final var checkForIdentifiers = tokens.state.state == LineState.NORMAL ||
        (tokens.state.state == LineState.INCOMPLETE && tokens.tokens.size() > 1);
      if (!tokens.state.hasBraces && !checkForIdentifiers) {
        line++;
        continue;
      }

      for (int i = 0; i < tokens.tokens.size(); i++) {
        final var token = tokens.tokens.get(i);
        var offset = token.getStartIndex();
        if (isCodeBlockStart(token)) {
          if (stack.isEmpty()) {
            if (currSwitch > maxSwitch) {
              maxSwitch = currSwitch;
            }
            currSwitch = 0;
          }
          currSwitch++;
          CodeBlock block = new CodeBlock();
          block.startLine = line;
          block.startColumn = offset;
          stack.push(block);
        } else if (isCodeBlockEnd(token)) {
          if (!stack.isEmpty()) {
            CodeBlock block = stack.pop();
            block.endLine = line;
            block.endColumn = offset;
            if (block.startLine != block.endLine) {
              blocks.add(block);
            }
          }
        }
      }

      line++;
    }
    blocks.sort(CodeBlock.COMPARATOR_END);
    return blocks;
  }

  protected boolean isCodeBlockStart(IncrementalToken token) {
    return blockTokens.length == 2 && token.getType() == blockTokens[0];
  }

  protected boolean isCodeBlockEnd(IncrementalToken token) {
    return blockTokens.length == 2 && token.getType() == blockTokens[1];
  }

  @SuppressWarnings("UnstableApiUsage")
  protected boolean isIncompleteTokenStart(EvictingQueue<IncrementalToken> q) {
    return matchTokenTypes(this.multilineStartTypes, q);
  }

  @SuppressWarnings("UnstableApiUsage")
  protected boolean isIncompleteTokenEnd(EvictingQueue<IncrementalToken> q) {
    return matchTokenTypes(this.multilineEndTypes, q);
  }

  /**
   * Called when the analyzer finds an incomplete token in a lne.
   *
   * @param token The incomplete token.
   */
  protected abstract void handleIncompleteToken(IncrementalToken token);

  /**
   * Gets the next token from lexer and return an {@link IncrementalToken}.
   *
   * @return The next {@link IncrementalToken}.
   */
  protected IncrementalToken nextToken() {
    return new IncrementalToken(lexer.nextToken());
  }

  /**
   * Pop (remove) required number of tokens after an incomplete token has been encountered. <br>
   *
   * <p>For example: <br>
   * When ['/', '*'] tokens are encountered, the '*' token must be removed. In this case, <code>
   * incompleteToken</code> parameter will point to '/' token.
   *
   * <p>By default, this method removes only the last token.
   *
   * @param incompleteToken The token which is the start of the incomplete token.
   * @param tokens          The list of tokens from which extra tokens must be removed.
   */
  protected void popTokensAfterIncomplete(@NonNull IncrementalToken incompleteToken,
                                          @NonNull List<IncrementalToken> tokens
  ) {
    tokens.remove(tokens.size() - 1);
  }

  protected void handleLineCommentSpan(@NonNull IncrementalToken token, @NonNull List<Span> spans,
                                       int offset
  ) {
    var commentType = SchemeAndroidIDE.COMMENT;

    // highlight special line comments
    var commentText = token.getText();
    if (commentText.length() > 2) {
      commentText = commentText.substring(2);
      commentText = commentText.trim();
      if ("todo".equalsIgnoreCase(commentText.substring(0, 4))) {
        commentType = TODO_COMMENT;
      } else if ("fixme".equalsIgnoreCase(commentText.substring(0, 5))) {
        commentType = FIXME_COMMENT;
      }
    }
    spans.add(Span.obtain(offset, withoutCompletion(commentType)));
  }

  /**
   * Called when the <code>state</code> in {@link #tokenizeLine(CharSequence, LineState, int)} is
   * {@link LineState#NORMAL}.
   *
   * @param line   The line source.
   * @param column The column in <code>line</code>.
   * @param tokens The list of tokens that must be updated as the <code>line</code> is scanned.
   * @param st     The state object whose state must be after after the <code>line</code> has been
   *               scanned.
   * @return The new state.
   */
  @SuppressWarnings("UnstableApiUsage")
  protected int tokenizeNormal(final CharSequence line, final int column,
                               final List<IncrementalToken> tokens, final LineState st,
                               final int lexerMode
  ) {
    lexer.setInputStream(createStream(line));
    if (lexer._mode != lexerMode) {
      lexer.pushMode(lexerMode);
    }
    final var queues = createEvictingQueueForTokens();
    final var start = queues.getFirst();
    final var end = queues.getSecond();
    var isInIncompleteToken = false;
    var state = LineState.NORMAL;
    IncrementalToken token;
    IncrementalToken incompleteToken = null;

    while ((token = nextToken()) != null) {
      if (token.getType() == IncrementalToken.EOF) {
        break;
      }

      // Skip to the token just after 'column'
      if (token.getStartIndex() < column) {
        continue;
      }

      if (!isInIncompleteToken) {
        if (token.getStartIndex() == column && !tokens.isEmpty()) {
          token.type = tokens.get(tokens.size() - 1).getType();
        }

        tokens.add(token);
      }
      start.add(token);
      end.add(token);
      final var type = token.getType();
      if (ArrayUtils.contains(getCodeBlockTokens(), type)) {
        st.hasBraces = true;
      }

      if (start.remainingCapacity() == 0 && isIncompleteTokenStart(start)) {
        isInIncompleteToken = true;
        incompleteToken = start.poll();

        // Pop extra tokens from the list.
        popTokensAfterIncomplete(Objects.requireNonNull(incompleteToken), tokens);
      } else if (end.remainingCapacity() == 0 && isIncompleteTokenEnd(end)) {
        // This should most probably not happen because, if a comment starts and ends on the same
        // line, the lexer will create a token for the whole comment
        // But still we handle this case...
        isInIncompleteToken = false;
        incompleteToken = null;
      }

      if (isInIncompleteToken) {
        state = LineState.INCOMPLETE;
      }
    }

    if (incompleteToken != null) {
      incompleteToken.incomplete = true;
      handleIncompleteToken(incompleteToken);
    }

    return state;
  }

  /**
   * Called when the <code>state</code> in {@link #tokenizeLine(CharSequence, LineState, int)} is
   * {@link LineState#INCOMPLETE}.
   *
   * @param line   The line source.
   * @param tokens The list of tokens that must be updated as the <code>line</code> is scanned.
   * @return The state and offset.
   */
  @SuppressWarnings("UnstableApiUsage")
  protected long fillIncomplete(CharSequence line, final List<IncrementalToken> tokens,
                                final int lexerMode
  ) {
    lexer.setInputStream(createStream(line));
    if (lexer._mode != lexerMode) {
      lexer.pushMode(lexerMode);
    }
    final var queue = createEvictingQueueForTokens();
    final var end = queue.getSecond();
    final var allTokens = lexer.getAllTokens()
      .stream()
      .map(IncrementalToken::new)
      .collect(Collectors.toList());
    if (allTokens.isEmpty()) {
      return IntPair.pack(LineState.INCOMPLETE, 0);
    }
    var completed = false;
    var index = 0;
    for (index = 0; index < allTokens.size(); index++) {
      final IncrementalToken token = allTokens.get(index);
      if (token.getType() == Token.EOF) {
        break;
      }

      end.add(token);
      if (end.remainingCapacity() == 0 && isIncompleteTokenEnd(end)) {
        completed = true;
        break;
      }
    }

    final var first = allTokens.get(0);
    final int offset = allTokens.get(completed ? index : index - 1).getStartIndex();
    first.startIndex = 0;
    handleIncompleteToken(first);
    tokens.add(first);
    if (completed) {
      return IntPair.pack(LineState.NORMAL, offset);
    } else {
      return IntPair.pack(LineState.INCOMPLETE, offset);
    }
  }

  @SuppressWarnings("UnstableApiUsage")
  @NonNull
  private Pair<EvictingQueue<IncrementalToken>, EvictingQueue<IncrementalToken>> createEvictingQueueForTokens() {
    return new Pair<>(EvictingQueue.create(multilineStartTypes.length),
      EvictingQueue.create(multilineEndTypes.length));
  }

  @SuppressWarnings("UnstableApiUsage")
  private boolean matchTokenTypes(@NonNull int[] types,
                                  @NonNull EvictingQueue<IncrementalToken> tokens
  ) {
    final var arr = tokens.toArray(new IncrementalToken[0]);
    for (int i = 0; i < types.length; i++) {
      if (types[i] != arr[i].getType()) {
        return false;
      }
    }
    return true;
  }
}
