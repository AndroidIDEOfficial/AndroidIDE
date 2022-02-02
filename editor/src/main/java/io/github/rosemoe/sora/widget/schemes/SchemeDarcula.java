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
 * picked from Android Studio
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
 */
public class SchemeDarcula extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xffbbb529);
        setColor(FUNCTION_NAME, 0xffffffff);
        setColor(IDENTIFIER_NAME, 0xffffffff);
        setColor(IDENTIFIER_VAR, 0xff9876aa);
        setColor(LITERAL, 0xff6a8759);
        setColor(OPERATOR, 0xffffffff);
        setColor(COMMENT, 0xff808080);
        setColor(KEYWORD, 0xffcc7832);
        setColor(WHOLE_BACKGROUND, 0xff2b2b2b);
        setColor(TEXT_NORMAL, 0xffffffff);
        setColor(LINE_NUMBER_BACKGROUND, 0xff313335);
        setColor(LINE_NUMBER, 0xff606366);
        setColor(LINE_DIVIDER, 0xff606366);
        setColor(SCROLL_BAR_THUMB, 0xffa6a6a6);
        setColor(SCROLL_BAR_THUMB_PRESSED, 0xff565656);
        setColor(SELECTED_TEXT_BACKGROUND, 0xff3676b8);
        setColor(MATCHED_TEXT_BACKGROUND, 0xff32593d);
        setColor(CURRENT_LINE, 0xff323232);
        setColor(SELECTION_INSERT, 0xffffffff);
        setColor(SELECTION_HANDLE, 0xffffffff);
        setColor(BLOCK_LINE, 0xff575757);
        setColor(BLOCK_LINE_CURRENT, 0xdd575757);
        setColor(NON_PRINTABLE_CHAR, 0xffdddddd);
        setColor(TEXT_SELECTED, 0xffffffff);
    }

}
