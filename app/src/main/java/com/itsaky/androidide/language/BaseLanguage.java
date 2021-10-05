package com.itsaky.androidide.language;

import com.itsaky.androidide.app.StudioApp;
import io.github.rosemoe.editor.langs.AbstractEditorLanguage;
import java.io.File;

public abstract class BaseLanguage extends AbstractEditorLanguage {
    
    public BaseLanguage() {
        this(null);
    }
    
    public BaseLanguage(File file) {
        super(file);
    }
    
    @Override
    public boolean useTab() {
        return false;
    }
    
    public int getTabSize() {
        return StudioApp.getInstance().getPrefManager().getEditorTabSize();
    }
}
