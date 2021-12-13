package io.github.rosemoe.editor.langs;

import io.github.rosemoe.editor.interfaces.EditorLanguage;
import java.io.File;

public abstract class AbstractEditorLanguage implements EditorLanguage {
    
    private File file;
    
    public AbstractEditorLanguage() {
        this(null);
    }
    
    public AbstractEditorLanguage(File file) {
        this.file = file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public boolean needsWholePreviousContentForIndent() {
        return false;
    }
}
