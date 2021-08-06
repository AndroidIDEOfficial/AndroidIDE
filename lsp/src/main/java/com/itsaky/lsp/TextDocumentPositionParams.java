package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class TextDocumentPositionParams {
   
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("position")
    public Position position;

    public TextDocumentPositionParams() {}

    public TextDocumentPositionParams(TextDocumentIdentifier textDocument, Position position) {
        this.textDocument = textDocument;
        this.position = position;
    }
}
