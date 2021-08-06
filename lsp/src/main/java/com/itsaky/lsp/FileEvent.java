package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class FileEvent {
    
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("type")
    public int type;

    public FileEvent(URI uri, int type) {
        this.uri = uri;
        this.type = type;
    }
}
