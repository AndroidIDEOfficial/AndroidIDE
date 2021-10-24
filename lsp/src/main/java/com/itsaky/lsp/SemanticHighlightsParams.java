package com.itsaky.lsp;

import org.eclipse.lsp4j.TextDocumentIdentifier;

public class SemanticHighlightsParams {
  
  private TextDocumentIdentifier _textDocument;
  
  
  public TextDocumentIdentifier getTextDocument() {
    return this._textDocument;
  }
  
  public void setTextDocument(final TextDocumentIdentifier textDocument) {
    this._textDocument = textDocument;
  }
}