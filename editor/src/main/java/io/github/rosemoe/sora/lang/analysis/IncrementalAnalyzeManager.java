/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.lang.analysis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.github.rosemoe.sora.lang.styling.Span;

/**
 * Interface for line based analyze managers
 * @param <S> State type at line endings
 * @param <T> Token type
 */
public interface IncrementalAnalyzeManager<S, T> extends AnalyzeManager {

    /**
     * Get the initial at document start
     */
    S getInitialState();

    /**
     * Compare the two states.
     * Return true if they equal
     */
    boolean stateEquals(S state, S another);

    /**
     * Tokenize for the given line
     */
    LineTokenizeResult<S, T> tokenizeLine(CharSequence line, S state);

    /**
     * Generate spans for the line
     */
    List<Span> generateSpansForLine(LineTokenizeResult<S, T> tokens);

    /**
     * Saved state
     */
    class LineTokenizeResult<S_, T_> {

        /**
         * State at line end
         */
        public S_ state;

        /**
         * Tokens on this line
         */
        public List<T_> tokens;

        /**
         * Spans. If spans are generated as well you can directly return them here to avoid
         * {@link #generateSpansForLine(LineTokenizeResult)} calls.
         */
        public List<Span> spans;

        public LineTokenizeResult(@NonNull S_ state, @Nullable List<T_> tokens) {
            this.state = state;
            this.tokens = tokens;
        }

        public LineTokenizeResult(@NonNull S_ state, @Nullable List<T_> tokens, @Nullable List<Span> spans) {
            this.state = state;
            this.tokens = tokens;
            this.spans = spans;
        }

        protected LineTokenizeResult<S_, T_> clearSpans() {
            spans = null;
            return this;
        }

    }



}
