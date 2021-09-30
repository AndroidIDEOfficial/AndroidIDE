package com.itsaky.androidide.models;

import java.io.File;
import org.eclipse.lsp4j.Range;

public class SearchResult extends Range {
    public File file;
    public String line;
    public String match;
    
    public SearchResult(Range src, File file, String line, String match) {
        this.setStart(src.getStart());
        this.setEnd(src.getEnd());
        this.file = file;
        this.line = line;
        this.match = match;
    }
}
