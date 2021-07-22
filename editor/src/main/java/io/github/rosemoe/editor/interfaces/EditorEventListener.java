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

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;

/**
 * Listener for editor
 *
 * @author Rose
 */
public interface EditorEventListener {

    /**
     * Before format
     *
     * @return true if you want to cancel this operation
     */
    boolean onRequestFormat(CodeEditor editor, boolean async);

    /**
     * Format failed
     *
     * @return true if you do not want editor to handle this exception
     */
    boolean onFormatFail(CodeEditor editor, Throwable cause);

    /**
     * Format succeeded
     */
    void onFormatSucceed(CodeEditor editor);

    /**
     * CodeEditor's setText is called
     */
    void onNewTextSet(CodeEditor editor);

    /**
     * @see io.github.rosemoe.editor.text.ContentListener#afterDelete(Content, int, int, int, int, CharSequence)
     * Note:do not change content at this time
     */
    void afterDelete(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence deletedContent);

    /**
     * @see io.github.rosemoe.editor.text.ContentListener#afterInsert(Content, int, int, int, int, CharSequence)
     * Note:do not change content at this time
     */
    void afterInsert(CodeEditor editor, CharSequence content, int startLine, int startColumn, int endLine, int endColumn, CharSequence insertedContent);

    /**
     * @see io.github.rosemoe.editor.text.ContentListener#beforeReplace(Content)
     * Note:do not change content at this time
     */
    void beforeReplace(CodeEditor editor, CharSequence content);

}
