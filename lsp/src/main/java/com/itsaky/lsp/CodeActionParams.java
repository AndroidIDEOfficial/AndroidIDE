package com.itsaky.lsp;

public class CodeActionParams {
    public TextDocumentIdentifier textDocument;
    public Range range;
    public CodeActionContext context = new CodeActionContext();
}
