/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


package com.itsaky.androidide.lsp.providers;

import com.itsaky.lsp.services.IDELanguageServer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
