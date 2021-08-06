package com.itsaky.lsp;
import com.google.gson.annotations.SerializedName;

public class WorkspaceSymbolParams {
    
    @SerializedName("query")
    public String query;

    public WorkspaceSymbolParams() {}

    public WorkspaceSymbolParams(String query) {
        this.query = query;
    }
}
