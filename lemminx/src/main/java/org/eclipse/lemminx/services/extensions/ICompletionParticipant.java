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
 * Completion participant API.
 *
 */
public interface ICompletionParticipant {

	void onTagOpen(ICompletionRequest completionRequest, ICompletionResponse completionResponse, CancelChecker cancelChecker) throws Exception;

	void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception;

	void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception;

	/**
	 * Collects and stores attribute value completion items within the provided completion
	 * response <code>response</code>
	 * 
	 * @param valuePrefix the attribute value before the offset in which completion was invoked
	 * @param request     the completion request
	 * @param response    the completion response
	 * @throws Exception
	 */
	void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception;
	

	/**
	 * Collects and stores systemId completion items within the provided completion
	 * response <code>response</code>
	 * 
	 * @param valuePrefix the systemId value before the offset in which completion was invoked
	 * @param request     the completion request
	 * @param response    the completion response
	 * @throws Exception
	 */
	void onDTDSystemId(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception;

}
