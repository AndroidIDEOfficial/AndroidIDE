/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/Microsoft/vscode-html-languageservice
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.lemminx.dom.parser;

/**
 * Scanner API.
 *
 */
public interface Scanner {

	TokenType scan();

	TokenType getTokenType();

	/**
	 * Starting offset position of the current token
	 * 
	 * @return int of token's start offset
	 */
	int getTokenOffset();

	int getTokenLength();

	/**
	 * Ending offset position of the current token
	 * 
	 * @return int of token's end offset
	 */
	int getTokenEnd();

	String getTokenText();

	String getTokenError();

	ScannerState getScannerState();

	/**
	 * @return True if the token's Text is empty or all whitespace
	 */
	boolean isTokenTextBlank();
}
