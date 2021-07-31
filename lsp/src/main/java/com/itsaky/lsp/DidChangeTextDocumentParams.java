package com.itsaky.lsp;

import java.util.ArrayList;
import java.util.List;

public class DidChangeTextDocumentParams {
    public VersionedTextDocumentIdentifier textDocument = new VersionedTextDocumentIdentifier();
    public List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<>();
}
