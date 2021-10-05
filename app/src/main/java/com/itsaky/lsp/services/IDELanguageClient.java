package com.itsaky.lsp.services;

import com.itsaky.lsp.SemanticHighlight;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.services.LanguageClient;

public interface IDELanguageClient extends LanguageClient {
	
	@JsonNotification("document/semanticHighlights")
	void semanticHighlights(SemanticHighlight highlights);
	
}