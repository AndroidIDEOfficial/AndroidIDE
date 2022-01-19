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
package io.github.rosemoe.editor.syntax;

import android.util.SparseIntArray;

import java.util.Objects;

/**
 * This class manages the colors of editor.
 * You can use color ids that are not in pre-defined id pool due to new languages.
 * <p>
 * This is also the default color scheme of editor.
 * Be careful to change this class, because this can cause its
 * subclasses behave differently and some subclasses did not apply
 * their default colors to some color ids. So change to this can cause
 * sub themes to change as well.
 * <p>
 * Typically, you can use this class to set color of editor directly
 * with {@link #setColor(int, int)} in a thread with looper.
 * <p>
 * However, we also accept you to extend this class to customize
 * your own ColorScheme to use different default colors.
 * Subclasses is expected to override {@link #applyDefault()}
 * to define colors, though other methods are not final.
 * After overriding this method, you will have to call super class's
 * applyDefault() and then a series of {@link #setColor(int, int)} calls
 * to apply your colors.
 * <p>
 * Note that new colors can be added in newer version of editor,
 * it is dangerous not to call super.applyDefault(), which can cause
 * newer editor works wrongly.
 *
 * @author Rose
 */
public class EditorColorScheme {
    
    public static final int DIAGNOSTIC_ERROR = 38;
    public static final int DIAGNOSTIC_WARNING = 39;
    public static final int DIAGNOSTIC_HINT = 40;
    public static final int DIAGNOSTIC_INFO = 41;
    
    public static final int LOG_DEBUG = 5; // Text Normal
    public static final int LOG_INFO = 32;
    public static final int LOG_ERROR = 33;
    public static final int LOG_WARNING = 34;
    
    public static final int STDERR = 53;
    public static final int STDOUT = 54;
    
    //-----------------Highlight colors-----------
    
    public static final int XML_TAG = 52;
    public static final int FIELD = 35;
    public static final int STATIC_FIELD = 36;
    public static final int PACKAGE_NAME = 37;
    public static final int ENUM_TYPE = 42;
    public static final int INTERFACE = 43;
    public static final int ENUM = 44;
    public static final int PARAMETER = 45;
    public static final int CONSTRUCTOR = 46;
    public static final int STATIC_INIT = 47;
    public static final int INSTANCE_INIT = 48;
    public static final int TYPE_PARAM = 49;
    public static final int RESOURCE_VARIABLE = 50;
    public static final int EXCEPTION_PARAM = 51;
    public static final int ANNOTATION = 28;
    public static final int METHOD_DECLARATION = 27;
    public static final int METHOD_INVOCATION = 55;
    public static final int TYPE_NAME = 26;
    public static final int LOCAL_VARIABLE = 25;
    public static final int LITERAL = 24;
    public static final int OPERATOR = 23;
    public static final int COMMENT = 22;
    public static final int KEYWORD = 21;
    
    //-------------View colors---------------------
    
    public static final int NON_PRINTABLE_CHAR = 31;
    public static final int TEXT_SELECTED = 30;
    public static final int MATCHED_TEXT_BACKGROUND = 29;
    public static final int AUTO_COMP_PANEL_CORNER = 20;
    public static final int AUTO_COMP_PANEL_BG = 19;
    
    /**
     * No longer supported
     */
    public static final int LINE_BLOCK_LABEL = 18;
    
    public static final int LINE_NUMBER_PANEL_TEXT = 17;
    public static final int LINE_NUMBER_PANEL = 16;
    public static final int BLOCK_LINE_CURRENT = 15;
    public static final int BLOCK_LINE = 14;
    public static final int SCROLL_BAR_TRACK = 13;
    public static final int SCROLL_BAR_THUMB_PRESSED = 12;
    public static final int SCROLL_BAR_THUMB = 11;
    public static final int UNDERLINE = 10;
    public static final int CURRENT_LINE = 9;
    public static final int SELECTION_HANDLE = 8;
    public static final int SELECTION_INSERT = 7;
    public static final int SELECTED_TEXT_BACKGROUND = 6;
    public static final int TEXT_NORMAL = 5;
    public static final int WHOLE_BACKGROUND = 4;
    public static final int LINE_NUMBER_BACKGROUND = 3;
    public static final int LINE_NUMBER = 2;
    public static final int LINE_DIVIDER = 1;
    
    /**
     * Default comment color
     */
    protected static int COMMENT_DEFAULT = 0xffa8a8a8;
    
    /**
     * Min pre-defined color id
     */
    protected static final int START_COLOR_ID = 1;
    
    /**
     * Max pre-defined color id
     */
    protected static final int END_COLOR_ID = 102;
    /**
     * Real color saver
     */
    protected final SparseIntArray mColors;
    
    private OnColorUpdateListener listener;
    
    public void setOnColorUpdateListener (OnColorUpdateListener listener) {
        this.listener = Objects.requireNonNull (listener);
    }
    
    public EditorColorScheme (OnColorUpdateListener listener) {
        this.listener = listener;
        mColors = new SparseIntArray ();
        applyDefault ();
    }
    
    /**
     * Subclasses only
     */
    protected EditorColorScheme () {
        this (type -> {});
    }
    
    /**
     * Apply default colors
     */
    public void applyDefault () {
        for (int i = START_COLOR_ID; i <= END_COLOR_ID; i++) {
            applyDefault (i);
        }
    }
    
    /**
     * Apply default color for the given type
     *
     * @param type The type
     */
    private void applyDefault (int type) {
        int color;
        switch (type) {
            case LINE_DIVIDER:
                color = 0xFFdddddd;
                break;
            case LINE_NUMBER:
                color = 0xFF808080;
                break;
            case LINE_NUMBER_BACKGROUND:
                color = 0xfff0f0f0;
                break;
            case OPERATOR:
                color = 0xFF0066D6;
                break;
            case STDOUT:
            case STDERR:
            case TEXT_NORMAL:
                color = 0xFF333333;
                break;
            case SELECTION_INSERT:
                color = 0xFF03EBEB;
                break;
            case UNDERLINE:
                color = 0xff000000;
                break;
            case SELECTION_HANDLE:
                color = 0xff03ebff;
                break;
            case ANNOTATION:
                color = 0xFF03A9F4;
                break;
            case CURRENT_LINE:
                color = 0x10000000;
                break;
            case SELECTED_TEXT_BACKGROUND:
                color = 0xFF9E9E9E;
                break;
            case KEYWORD:
                color = 0xFF2196F3;
                break;
            case COMMENT:
                color = COMMENT_DEFAULT;
                break;
            case LITERAL:
                color = 0xFF008080;
                break;
            case SCROLL_BAR_THUMB:
                color = 0xffd8d8d8;
                break;
            case SCROLL_BAR_THUMB_PRESSED:
                color = 0xFF27292A;
                break;
            case BLOCK_LINE:
                color = 0xffdddddd;
                break;
            case LINE_BLOCK_LABEL:
            case SCROLL_BAR_TRACK:
                color = 0;
                break;
            case LINE_NUMBER_PANEL:
                color = 0xdd000000;
                break;
            case BLOCK_LINE_CURRENT:
                color = 0xff999999;
                break;
            case LOCAL_VARIABLE:
            case TYPE_NAME:
            case TYPE_PARAM:
            case PARAMETER:
            case METHOD_DECLARATION:
            case ENUM_TYPE:
            case INTERFACE:
            case CONSTRUCTOR:
            case EXCEPTION_PARAM:
            case METHOD_INVOCATION:
                color = 0xff333333;
                break;
            case MATCHED_TEXT_BACKGROUND:
                color = 0xffffff00;
                break;
            case NON_PRINTABLE_CHAR:
                color = 0xff505050;
                break;
            case LOG_ERROR:
                color = 0xffc50e29;
                break;
            case LOG_WARNING:
                color = 0xffff7043;
                break;
            case LOG_INFO:
                color = 0xff4caf50;
                break;
            case FIELD:
            case STATIC_FIELD:
            case ENUM:
                color = 0xFFF0BE4B;
                break;
            case PACKAGE_NAME:
                color = 0xffF0BE4B;
                break;
            case DIAGNOSTIC_ERROR:
                color = 0xfff44336;
                break;
            case DIAGNOSTIC_WARNING:
                color = 0xffFF9800;
                break;
            case DIAGNOSTIC_INFO:
                color = 0xff4CAF50;
                break;
            default:
                color = 0xffffffff;
        }
        
        setColor (type, color);
    }
    
    /**
     * Apply a new color for the given type
     *
     * @param type  The type
     * @param color New color
     */
    public void setColor (int type, int color) {
        //Do not change if the old value is the same as new value
        //due to avoid unnecessary invalidate() calls
        int old = getColor (type);
        if (old == color) {
            return;
        }
        
        mColors.put (type, color);
        
        //Notify the listener
        listener.onColorUpdated (type);
    }
    
    /**
     * Get color by type
     *
     * @param type The type
     * @return The color for type
     */
    public int getColor (int type) {
        return mColors.get (type);
    }
    
    public interface OnColorUpdateListener {
        void onColorUpdated (int type);
    }
}
