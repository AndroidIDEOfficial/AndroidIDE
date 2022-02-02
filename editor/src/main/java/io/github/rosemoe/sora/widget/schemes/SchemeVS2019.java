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
package io.github.rosemoe.sora.widget.schemes;

/**
 * ColorScheme for editor
 * picked from Visual Studio 2019
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
 */
public class SchemeVS2019 extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xff4ec9b0);
        setColor(FUNCTION_NAME, 0xffdcdcdc);
        setColor(IDENTIFIER_NAME, 0xff4ec9b0);
        setColor(IDENTIFIER_VAR, 0xffdcdcaa);
        setColor(LITERAL, 0xffd69d85);
        setColor(OPERATOR, 0xffdcdcdc);
        setColor(COMMENT, 0xff57a64a);
        setColor(KEYWORD, 0xff569cd6);
        setColor(WHOLE_BACKGROUND, 0xff1e1e1e);
        setColor(TEXT_NORMAL, 0xffdcdcdc);
        setColor(LINE_NUMBER_BACKGROUND, 0xff1e1e1e);
        setColor(LINE_NUMBER, 0xff2b9eaf);
        setColor(LINE_DIVIDER, 0xff2b9eaf);
        setColor(SCROLL_BAR_THUMB, 0xff3e3e42);
        setColor(SCROLL_BAR_THUMB_PRESSED, 0xff9e9e9e);
        setColor(SELECTED_TEXT_BACKGROUND, 0xff3676b8);
        setColor(MATCHED_TEXT_BACKGROUND, 0xff653306);
        setColor(CURRENT_LINE, 0xff464646);
        setColor(SELECTION_INSERT, 0xffffffff);
        setColor(SELECTION_HANDLE, 0xffffffff);
        setColor(BLOCK_LINE, 0xff717171);
        setColor(BLOCK_LINE_CURRENT, 0);
        setColor(NON_PRINTABLE_CHAR, 0xffdddddd);
        setColor(TEXT_SELECTED, 0xffffffff);
    }

}
