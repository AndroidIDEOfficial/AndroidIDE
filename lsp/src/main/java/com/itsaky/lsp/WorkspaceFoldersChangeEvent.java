package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WorkspaceFoldersChangeEvent {
    @SerializedName("added")
    public List<WorkspaceFolder> added;
    
    @SerializedName("removed")
    public List<WorkspaceFolder> removed;
}
