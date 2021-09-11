package com.itsaky.androidide.utils;

import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.views.SymbolInputView.Symbol;
import java.io.File;
import com.itsaky.androidide.app.StudioApp;

public class Symbols {
	
	public static Symbol[] forFile(File file) {
		if(file.isFile()) {
			if(file.getName().endsWith(EditorFragment.EXT_JAVA)
			|| file.getName().endsWith(EditorFragment.EXT_GRADLE))
				return javaSymbols();
			
			if(file.getName().endsWith(EditorFragment.EXT_XML))
				return xmlSymbols();
		}
		
		return new Symbol[0];
	}
    
    private static Symbol tab() {
        String tab = createTabSpaces();
        return new Symbol("↹", tab, tab.length());
    }
    
    public static String createTabSpaces() {
        int size = StudioApp.getInstance().getPrefManager().getEditorTabSize();
        String tab = "";
        for(int i=1;i<=size;i++) {
            tab += " ";
        }
        return tab;
    }
    
    public static Symbol[] javaSymbols() {
		return new Symbol[]{tab(),
			new Symbol("{", "{}"),
			new Symbol("}"),
			new Symbol("(", "()"),
			new Symbol(")"),
			new Symbol(";"),
			new Symbol("="),
			new Symbol("\"", "\"\""),
			new Symbol("|"),
			new Symbol("&"),
			new Symbol("!"),
			new Symbol("[", "[]"),
			new Symbol("]"),
			new Symbol("<", "<>"),
			new Symbol(">"),
			new Symbol("+"),
			new Symbol("-"),
			new Symbol("/"),
			new Symbol("*"),
			new Symbol("?"),
			new Symbol(":"),
			new Symbol("_")};
	}
	
	public static Symbol[] xmlSymbols() {
		return new Symbol[]{tab(),
			new Symbol("<", "<>"),
			new Symbol(">"),
			new Symbol("/"),
			new Symbol("="),
			new Symbol("\"", "\"\""),
			new Symbol(":"),
			new Symbol("@"),
			new Symbol("+"),
			new Symbol("(", "()"),
			new Symbol(")"),
			new Symbol(";"),
			new Symbol(","),
			new Symbol("."),
			new Symbol("?"),
			new Symbol("|"),
			new Symbol("\\"),
			new Symbol("&"),
			new Symbol("[", "[]"),
			new Symbol("]"),
			new Symbol("{", "{}"),
			new Symbol("}"),
			new Symbol("_"),
			new Symbol("-")};
	}
    
}
