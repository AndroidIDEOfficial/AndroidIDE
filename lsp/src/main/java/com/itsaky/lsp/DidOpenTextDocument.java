package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DidOpenTextDocument {
    @SerializedName("textDocument")
    public TextDocumentItem textDocument;
}
