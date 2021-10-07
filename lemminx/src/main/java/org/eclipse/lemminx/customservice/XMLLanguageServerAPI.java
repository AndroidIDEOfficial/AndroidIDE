/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.customservice;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

/**
 * XML custom services.
 *
 */
@JsonSegment("xml")
public interface XMLLanguageServerAPI {

	@JsonRequest
	CompletableFuture<AutoCloseTagResponse> closeTag(TextDocumentPositionParams params);

	@JsonRequest
	CompletableFuture<Position> matchingTagPosition(TextDocumentPositionParams params);
}


