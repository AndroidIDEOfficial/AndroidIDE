package com.itsaky.lsp.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.SemanticHighlightsParams;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.services.TextDocumentService;

public interface IDETextDocumentService extends TextDocumentService{
	
	@JsonRequest ("semanticHighlights")
	CompletableFuture<List<SemanticHighlight>> semanticHighlights (SemanticHighlightsParams params) ;
	
}
