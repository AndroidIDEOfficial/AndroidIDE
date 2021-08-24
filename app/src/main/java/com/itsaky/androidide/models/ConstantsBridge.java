package com.itsaky.androidide.models;

import java.util.List;

public class ConstantsBridge {
    
	public static boolean EDITORPREF_SIZE_CHANGED = false;
	public static boolean EDITORPREF_FLAGS_CHANGED = false;
    public static boolean EDITORPREF_DRAW_HEX_CHANGED = false;
    public static boolean CLASS_LOAD_SUCCESS = true;
	
	public static List<String> PROJECT_DEXES = null;
    
    // Password unzipping the JLS stored in assets
    // Real password is admin@androidide.com
    // This will he further encrypted by StringFog at compile time
    public static final String JLS_ZIP_PASSWORD_HASH = "865dabea6f06fea1a3dd572e58a4a81c";
    
    // Password for GradleApi zip
    // Real password is gradle-api@admin@androidide
    public static final String GRADLE_API_ZIP_PASSWORD_HASH = "b50b462f8e8c5789f32428d7e1967da2";
}
