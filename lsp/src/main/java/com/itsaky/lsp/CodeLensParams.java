package com.itsaky.lsp;
import com.google.gson.annotations.SerializedName;

public class CodeLensParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;

    public CodeLensParams() {}

    public CodeLensParams(TextDocumentIdentifier textDocument) {
        this.textDocument = textDocument;
    }
}
