/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services.snippets;

import java.util.Map;

import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;

/**
 * CDATA snippet context used to filter the CDATA snippets.
 *
 */
public class CDATASnippetContext implements IXMLSnippetContext {

	public static IXMLSnippetContext DEFAULT_CONTEXT = new CDATASnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		if (SnippetContextUtils.canAcceptExpression(request)) {
			DOMElement parent = request.getParentElement();
			if (parent == null) {
				return false;
			}
			return true;
		}
		return false;
	}

}
