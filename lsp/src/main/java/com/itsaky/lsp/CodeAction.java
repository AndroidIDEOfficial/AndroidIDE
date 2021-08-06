package com.itsaky.lsp;

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
}
