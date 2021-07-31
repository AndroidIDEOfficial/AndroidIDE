package com.itsaky.androidide.tools;

import java.io.File;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

public class SourceJavaFileObject extends SimpleJavaFileObject {

    private File file;
    private String source;
    
    public SourceJavaFileObject(File file, String source) {
        super(URI.create("source"), Kind.SOURCE);
        this.file = file;
        this.source = source;
    }
    
    public File getFile() {
        return this.file;
    }

    @Override
    public String getName() {
        return file.getAbsolutePath();
    }
    
    @Override
    public CharSequence getCharContent(boolean p1) {
        return source;
    }
}
