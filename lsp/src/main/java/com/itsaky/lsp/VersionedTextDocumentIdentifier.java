package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class VersionedTextDocumentIdentifier {
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("version")
    public int version;
}
