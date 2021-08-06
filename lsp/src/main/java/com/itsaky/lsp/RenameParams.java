package com.itsaky.lsp;


import com.google.gson.annotations.SerializedName;

public class RenameParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("position")
    public Position position;
    
    @SerializedName("newName")
    public String newName;
}
