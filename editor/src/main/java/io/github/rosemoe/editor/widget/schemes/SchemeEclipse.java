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
 * picked from Eclipse IDE for Java Developers Version 2019-12 (4.14.0)
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
 */
public final class SchemeEclipse extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xff646464);
        setColor(FUNCTION_NAME, 0xff000000);
        setColor(IDENTIFIER_NAME, 0xff000000);
        setColor(IDENTIFIER_VAR, 0xffb8633e);
        setColor(LITERAL, 0xff2a00ff);
        setColor(OPERATOR, 0xff3a0000);
        setColor(COMMENT, 0xff3f7f5f);
        setColor(KEYWORD, 0xff7f0074);
        setColor(WHOLE_BACKGROUND, 0xffffffff);
        setColor(TEXT_NORMAL, 0xff000000);
        setColor(LINE_NUMBER_BACKGROUND, 0xffffffff);
        setColor(LINE_NUMBER, 0xff787878);
        setColor(SELECTED_TEXT_BACKGROUND, 0xff3399ff);
        setColor(MATCHED_TEXT_BACKGROUND, 0xffd4d4d4);
        setColor(CURRENT_LINE, 0xffe8f2fe);
        setColor(SELECTION_INSERT, 0xff03ebeb);
        setColor(SELECTION_HANDLE, 0xff03ebeb);
        setColor(BLOCK_LINE, 0xffd8d8d8);
        setColor(BLOCK_LINE_CURRENT, 0);
    }

}
