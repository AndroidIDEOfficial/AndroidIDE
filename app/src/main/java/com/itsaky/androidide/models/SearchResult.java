package com.itsaky.androidide.models;

import com.itsaky.lsp.Range;
import java.io.File;

public class SearchResult extends Range {
    public File file;
    public String line;
    public String match;
    
    public SearchResult(Range src, File file, String line, String match) {
        this.start = src.start;
        this.end = src.end;
        this.file = file;
        this.line = line;
        this.match = match;
    }
}
