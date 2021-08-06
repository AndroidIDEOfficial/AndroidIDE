package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PublishDiagnosticsParams {
    
    @SerializedName("uri")
    public URI uri;
    
    @SerializedName("diagnostics")
    public List<Diagnostic> diagnostics;

    public PublishDiagnosticsParams() {
        diagnostics = new ArrayList<>();
    }

    public PublishDiagnosticsParams(URI uri, List<Diagnostic> diagnostics) {
        this.uri = uri;
        this.diagnostics = diagnostics;
    }
}
