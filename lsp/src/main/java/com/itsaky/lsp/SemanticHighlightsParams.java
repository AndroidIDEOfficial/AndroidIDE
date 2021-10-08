package com.itsaky.lsp;

import org.eclipse.lsp4j.TextDocumentIdentifier;

public class SemanticHighlightsParams {
	
	private TextDocumentIdentifier textDocument;
	
	public void setTextDocument(TextDocumentIdentifier document) {
		this.textDocument = document;
	}
	
	public TextDocumentIdentifier getDocument() {
		return this.textDocument;
	}
	
}