package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

public class WorkspaceFolder {
    
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("name")
    public String name;
}
