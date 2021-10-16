package com.itsaky.androidide.lsp.providers;

import com.itsaky.lsp.services.IDELanguageServer;
import java.util.ArrayList;
import java.util.List;
import java9.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionContext;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

/**
 * Provides code actions to editor. Handles cancellation of last incomplete request.
 */
public class CodeActionProvider {
    
    private CompletableFuture<List<Either<Command, CodeAction>>> lastRequest;
    
    public CompletableFuture<List<Either<Command, CodeAction>>> codeActions(IDELanguageServer server, TextDocumentIdentifier documentId, Range cursorRange, List<Diagnostic> diagnostics) {
        
        if(lastRequest != null && !lastRequest.isDone()) {
            lastRequest.cancel(true);
        }
        
        if(server == null || cursorRange == null || documentId == null)
            return null;
            
        final CodeActionParams params = new CodeActionParams();
        final CodeActionContext context = new CodeActionContext();
        context.setDiagnostics(diagnostics == null ? new ArrayList<Diagnostic>() : diagnostics);
        params.setContext(context);
        params.setTextDocument(documentId);
        params.setRange(cursorRange);
        
        return lastRequest = server.getTextDocumentService().codeAction(params);
    }
}
