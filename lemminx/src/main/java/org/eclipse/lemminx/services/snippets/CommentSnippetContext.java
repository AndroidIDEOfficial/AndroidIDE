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

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;

/**
 * Comment snippet context used to filter the comment snippets.
 *
 */
public class CommentSnippetContext extends DTDNodeSnippetContext {

	public static IXMLSnippetContext DEFAULT_CONTEXT = new CommentSnippetContext();

	@Override
	public boolean isMatch(ICompletionRequest request, Map<String, String> model) {
		if (super.isMatch(request, model)) {
			// completion was triggered inside a DTD or a DOCTYPE subset 
			// --> comments are allowed
			return true;
		}		
		DOMNode node = request.getNode();		
		if (node.isDoctype()) {
			// completion was triggered inside doctype declaration, ignore the snippets
			return false;
		}
		
		DOMDocument document = request.getXMLDocument();
		if (document.isBeforeProlog(request.getOffset())){
			// triggered before prolog
			return false;
		}
		return SnippetContextUtils.canAcceptExpression(request);
	}

}
