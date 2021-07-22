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
package io.github.rosemoe.editor.widget.schemes;

import io.github.rosemoe.editor.widget.EditorColorScheme;

/**
 * ColorScheme for editor
 * picked from GitHub site
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
 */
public final class SchemeGitHub extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xff6f42c1);
        setColor(FUNCTION_NAME, 0xff24292e);
        setColor(IDENTIFIER_NAME, 0xff24292e);
        setColor(IDENTIFIER_VAR, 0xff24292e);
        setColor(LITERAL, 0xff032f62);
        setColor(OPERATOR, 0xff005cc5);
        setColor(COMMENT, 0xff6a737d);
        setColor(KEYWORD, 0xffde3a49);
        setColor(WHOLE_BACKGROUND, 0xffffffff);
        setColor(TEXT_NORMAL, 0xff24292e);
        setColor(LINE_NUMBER_BACKGROUND, 0xffffffff);
        setColor(LINE_NUMBER, 0xffbec0c1);
        setColor(SELECTION_INSERT, 0xffc7edcc);
        setColor(SELECTION_HANDLE, 0xffc7edcc);
    }

}
