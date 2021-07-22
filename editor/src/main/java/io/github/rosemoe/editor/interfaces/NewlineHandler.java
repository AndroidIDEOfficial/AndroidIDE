/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.interfaces;

/**
 * Perform text processing when user enters '\n' and selection size is 0
 */
public interface NewlineHandler {

    /**
     * Checks whether the given input matches the requirement to invoke this handler
     *
     * @param beforeText Text of line before cursor
     * @param afterText  Text of line after cursor
     * @return Whether this handler should be called
     */
    boolean matchesRequirement(String beforeText, String afterText);

    /**
     * Handle newline and return processed content to insert
     *
     * @param beforeText Text of line before cursor
     * @param afterText  Text of line after cursor
     * @return Actual content to insert
     */
    HandleResult handleNewline(String beforeText, String afterText, int tabSize);

    class HandleResult {

        /**
         * Text to insert
         */
        public final CharSequence text;

        /**
         * Count to shift left from the end of {@link HandleResult#text}
         */
        public final int shiftLeft;

        public HandleResult(CharSequence text, int shiftLeft) {
            this.text = text;
            this.shiftLeft = shiftLeft;
            if (shiftLeft < 0 || shiftLeft > text.length()) {
                throw new IllegalArgumentException("invalid shiftLeft");
            }
        }

    }

}
