package com.itsaky.androidide.interfaces;

import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.DidChangeWatchedFilesParams;
import com.itsaky.lsp.DidCloseTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.DidSaveTextDocumentParams;
import com.itsaky.lsp.TextDocumentPositionParams;
import java.io.File;

public interface JLSRequestor {

    void hideSignature();

    void didOpen(DidOpenTextDocumentParams params);
    void didClose(DidCloseTextDocumentParams params);
    void didChange(DidChangeTextDocumentParams params);
    void didSave(DidSaveTextDocumentParams params);
    void didChangeWatchedFiles(DidChangeWatchedFilesParams params);
    
    void signatureHelp(TextDocumentPositionParams params, File file);
}
