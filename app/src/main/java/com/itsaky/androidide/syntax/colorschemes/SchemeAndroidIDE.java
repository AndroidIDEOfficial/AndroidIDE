/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.syntax.colorschemes;

import io.github.rosemoe.editor.syntax.EditorColorScheme;

public class SchemeAndroidIDE extends EditorColorScheme {
    
    @Override
    public void applyDefault() {
        // Change the default comment color
        // This will make sure that all javadoc elements
        // which are not implemented in this scheme are 
        // same as other comments
        COMMENT_DEFAULT = 0xffbdbdbd;
        
        // Apply default colors
        super.applyDefault();
        
        // Apply customized colors
        setColor(WHOLE_BACKGROUND, 0xff212121);
        setColor(LINE_NUMBER_BACKGROUND, 0xff212121);
        setColor(LINE_DIVIDER, 0xff9e9e9e);
        setColor(LINE_NUMBER, 0xff424242);
        setColor(LINE_NUMBER_PANEL, 0xff000000);
        setColor(LINE_NUMBER_PANEL_TEXT, 0xffffffff);
        setColor(TEXT_NORMAL, 0xfff5f5f5);
        setColor(SELECTED_TEXT_BACKGROUND, 0xff757575);
        setColor(TEXT_SELECTED, 0xff424242);
        setColor(SELECTION_INSERT, 0xffff5252);
        setColor(SELECTION_HANDLE, 0xffff5252);
        setColor(CURRENT_LINE, 0xff313131);
        setColor(UNDERLINE, 0xffffffff);
        setColor(SCROLL_BAR_THUMB, 0xff757575);
        setColor(SCROLL_BAR_THUMB_PRESSED, 0xffff5252);
        setColor(SCROLL_BAR_TRACK, 0xffbdbdbd);
        setColor(BLOCK_LINE, 0xff424242);
        setColor(BLOCK_LINE_CURRENT, 0xffffffff);
        setColor(AUTO_COMP_PANEL_BG, 0xff757575);
        setColor(AUTO_COMP_PANEL_CORNER, 0xff9e9e9e);
        setColor(MATCHED_TEXT_BACKGROUND, 0xffFF8F00);
        setColor(NON_PRINTABLE_CHAR, 0xffdddddd);
        setColor(KEYWORD, 0xffff6060);
        setColor(COMMENT, COMMENT_DEFAULT);
        setColor(OPERATOR, 0xff4fc3f7);
        setColor(LITERAL, 0xff8bc34a);
        setColor(TYPE_NAME, 0xff4fc3f7);
        setColor(LOCAL_VARIABLE, 0xfff5f5f5);
        setColor(RESOURCE_VARIABLE, 0xfff5f5f5);
        setColor(PARAMETER, 0xfff5f5f5);
        setColor(EXCEPTION_PARAM, 0xfff5f5f5);
        setColor(METHOD_DECLARATION, 0xfff5f5f5);
        setColor(CONSTRUCTOR, 0xfff5f5f5);
        setColor(ANNOTATION, 0xff4fc3f7);
        setColor(PACKAGE_NAME, 0xfff0be4b);
        setColor(FIELD, 0xfff0be4b);
        setColor(STATIC_INIT, 0xfff0be4b);
        setColor(INSTANCE_INIT, 0xfff0be4b);
        setColor(TYPE_PARAM, 0xfff0be4b);
        setColor(STATIC_FIELD, 0xffffb300);
        setColor(ENUM_TYPE, 0xff4fc3f7);
        setColor(INTERFACE, 0xff4fc3f7);
        setColor(ENUM, 0xffffb300);
        setColor(XML_TAG, 0xffff6060);
        
        setColor(DIAGNOSTIC_ERROR, 0xfff44336);
        setColor(DIAGNOSTIC_WARNING, 0xffFFEB3B);
        setColor(DIAGNOSTIC_INFO, 0xff4CAF50);
        setColor(DIAGNOSTIC_HINT, 0xffffffff);
        
        setColor(STDERR, 0xfff44336);
        setColor(STDOUT, 0xff4CAF50);
    }
}
