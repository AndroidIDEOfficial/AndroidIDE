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

import io.github.rosemoe.editor.widget.SymbolPairMatch;

/**
 * Language for editor
 * <p>
 * A Language helps editor to highlight text and provide auto-completion.
 * Implement this interface when you want to add new language support for editor.
 * <p>
 * <strong>NOTE:</strong> A language must not be single instance.
 * One language instance should always serves for only one editor.
 * It means that you should not give a language object to other editor instances
 * after it has been applied to one editor.
 * This is to provide better connection between auto completion provider and code analyzer.
 *
 * @author Rose
 */
public interface EditorLanguage {

    /**
     * Get CodeAnalyzer of this language object
     *
     * @return CodeAnalyzer
     */
    CodeAnalyzer getAnalyzer();

    /**
     * Get AutoCompleteProvider of this language object
     *
     * @return AutoCompleteProvider
     */
    AutoCompleteProvider getAutoCompleteProvider();

    /**
     * Called by editor to check whether this is a character for auto completion
     *
     * @param ch Character to check
     * @return Whether is character for auto completion
     */
    boolean isAutoCompleteChar(char ch);

    /**
     * Get advance for indent
     *
     * @param content Content of a line
     * @return Advance space count
     */
    int getIndentAdvance(String content);

    /**
     * Whether use tab to format
     *
     * @return Whether use tab
     */
    boolean useTab();

    /**
     * Format the given content
     *
     * @param text Content to format
     * @return Formatted code
     */
    CharSequence format(CharSequence text);

    /**
     * Returns language specified symbol pairs.
     * The method is called only once when the language is applied.
     */
    SymbolPairMatch getSymbolPairs();

    /**
     * Get newline handlers of this language.
     * This method is called each time the user presses ENTER key.
     * <p>
     * Pay attention to the performance as this method is called frequently
     *
     * @return NewlineHandlers , maybe null
     */
    NewlineHandler[] getNewlineHandlers();

}
