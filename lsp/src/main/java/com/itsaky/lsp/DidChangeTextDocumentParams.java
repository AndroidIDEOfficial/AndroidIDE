package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class DidChangeTextDocumentParams {
    @SerializedName("textDocument")
    public VersionedTextDocumentIdentifier textDocument = new VersionedTextDocumentIdentifier();
    
    @SerializedName("contentChanges")
    public List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<>();
}
