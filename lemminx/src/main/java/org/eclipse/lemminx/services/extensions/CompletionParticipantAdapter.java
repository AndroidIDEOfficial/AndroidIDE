/**
 *  Copyright (c) 2018 Angelo ZERR.
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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Completion participant adapter.
 *
 */
public class CompletionParticipantAdapter implements ICompletionParticipant {

	@Override
	public void onTagOpen(ICompletionRequest completionRequest, ICompletionResponse completionResponse, CancelChecker cancelChecker)
			throws Exception {
		// Do nothing
	}

	@Override
	public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
		// Do nothing
	}

	@Override
	public void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception {
		// Do nothing
	}

	@Override
	public void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception {
		// Do nothing
	}

	@Override
	public void onDTDSystemId(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception {
		// Do nothing
	}

}
