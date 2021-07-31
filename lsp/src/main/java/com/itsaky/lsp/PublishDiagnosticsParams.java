package com.itsaky.lsp;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PublishDiagnosticsParams {
    public URI uri;
    public List<Diagnostic> diagnostics;

    public PublishDiagnosticsParams() {
        diagnostics = new ArrayList<>();
    }

    public PublishDiagnosticsParams(URI uri, List<Diagnostic> diagnostics) {
        this.uri = uri;
        this.diagnostics = diagnostics;
    }
}
