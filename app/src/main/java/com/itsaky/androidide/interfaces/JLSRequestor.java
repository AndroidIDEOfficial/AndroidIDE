package com.itsaky.androidide.interfaces;

import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.DidCloseTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.DidSaveTextDocumentParams;

public interface JLSRequestor {
    void didOpen(DidOpenTextDocumentParams params);
    void didClose(DidCloseTextDocumentParams params);
    void didChange(DidChangeTextDocumentParams params);
    void didSave(DidSaveTextDocumentParams params);
}
