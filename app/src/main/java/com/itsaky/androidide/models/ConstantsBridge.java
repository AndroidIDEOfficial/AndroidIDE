/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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


package com.itsaky.androidide.models;

import java.util.List;

public class ConstantsBridge {
    
	public static boolean EDITORPREF_SIZE_CHANGED = false;
	public static boolean EDITORPREF_FLAGS_CHANGED = false;
    public static boolean EDITORPREF_DRAW_HEX_CHANGED = false;
    public static boolean CLASS_LOAD_SUCCESS = true;
	
    public static boolean SPLASH_TO_MAIN = false;
    
	public static List<String> PROJECT_DEXES = null;
    
    // Password unzipping the JLS stored in assets
    // Real password is admin@androidide.com
    // This will he further encrypted by StringFog at compile time
    public static final String JLS_ZIP_PASSWORD_HASH = "865dabea6f06fea1a3dd572e58a4a81c";
    
    // Password for GradleApi zip
    // Real password is gradle-api@admin@androidide
    public static final String GRADLE_API_ZIP_PASSWORD_HASH = "b50b462f8e8c5789f32428d7e1967da2";
    
    // Always compared in lowercase
    public static final String CUSTOM_COMMENT_WARNING_TOKEN = "#warn ";
    
    public static final String VIRTUAL_KEYS =
    "["
    + "\n  ["
    + "\n    \"ESC\","
    + "\n    {"
    + "\n      \"key\": \"/\","
    + "\n      \"popup\": \"\\\\\""
    + "\n    },"
    + "\n    {"
    + "\n      \"key\": \"-\","
    + "\n      \"popup\": \"|\""
    + "\n    },"
    + "\n    \"HOME\","
    + "\n    \"UP\","
    + "\n    \"END\","
    + "\n    \"PGUP\""
    + "\n  ],"
    + "\n  ["
    + "\n    \"TAB\","
    + "\n    \"CTRL\","
    + "\n    \"ALT\","
    + "\n    \"LEFT\","
    + "\n    \"DOWN\","
    + "\n    \"RIGHT\","
    + "\n    \"PGDN\""
    + "\n  ]"
    + "\n]";
}
