package com.itsaky.androidide.language;

import com.itsaky.androidide.app.StudioApp;
import io.github.rosemoe.editor.interfaces.EditorLanguage;

public abstract class BaseLanguage implements EditorLanguage {

    @Override
    public boolean useTab() {
        return false;
    }
    
    public int getTabSize() {
        return StudioApp.getInstance().getPrefManager().getEditorTabSize();
    }
}
