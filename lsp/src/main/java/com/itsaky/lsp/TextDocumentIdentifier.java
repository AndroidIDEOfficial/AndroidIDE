package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class TextDocumentIdentifier {
    
    @SerializedName("uri")
    public URI uri;

    public TextDocumentIdentifier() {}

    public TextDocumentIdentifier(URI uri) {
        this.uri = uri;
    }
}
