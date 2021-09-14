package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

public class DidChangeWatchedFilesParams {
    
    @SerializedName("changes")
    public List<FileEvent> changes = new ArrayList<>();

    public DidChangeWatchedFilesParams(List<FileEvent> changes) {
        this.changes = changes;
    }
    
    public DidChangeWatchedFilesParams() {
        changes = new ArrayList<>();
    }
}
