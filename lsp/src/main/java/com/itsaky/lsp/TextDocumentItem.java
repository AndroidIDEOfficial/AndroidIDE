package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class TextDocumentItem {
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("languageId")
    public String languageId;
    
    @SerializedName("version")
    public int version;
    
    @SerializedName("text")
    public String text;
}
