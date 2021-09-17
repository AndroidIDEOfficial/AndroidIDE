package com.itsaky.lsp;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CodeAction {
    @SerializedName("title")
    public String title;
    
    @SerializedName("kind")
    public String kind;
    
    @SerializedName("diagnostics")
    public List<Diagnostic> diagnostics;
    
    @SerializedName("edit")
    public WorkspaceEdit edit;
    
    @SerializedName("command")
    public Command command;
    
    public static CodeAction NONE;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CodeAction) {
            CodeAction that = (CodeAction) obj;
            return this.title.equals(that.title)
            && this.kind.equals(that.kind);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
