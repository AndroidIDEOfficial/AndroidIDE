package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TextDocumentEdit {
    @SerializedName("textDocument")
    public VersionedTextDocumentIdentifier textDocument;
    
    @SerializedName("edits")
    public List<TextEdit> edits;
}
