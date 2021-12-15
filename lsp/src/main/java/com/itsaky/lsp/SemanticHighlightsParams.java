/************************************************************************************
 * This file is part of Java Language Server (https://github.com/itsaky/java-language-server)
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * Java Language Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Java Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Java Language Server.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/

package com.itsaky.lsp;

import org.eclipse.lsp4j.TextDocumentIdentifier;

public class SemanticHighlightsParams {

  public SemanticHighlightsParams () {
    this (null);
  }

  public SemanticHighlightsParams(TextDocumentIdentifier _textDocument) {
    this._textDocument = _textDocument;
  }

  private TextDocumentIdentifier _textDocument;
  
  public TextDocumentIdentifier getTextDocument() {
    return this._textDocument;
  }
  
  public void setTextDocument(final TextDocumentIdentifier textDocument) {
    this._textDocument = textDocument;
  }
}