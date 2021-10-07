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

import org.eclipse.lemminx.commons.snippets.ISnippetRegistryLoader;
import org.eclipse.lemminx.commons.snippets.SnippetRegistry;

/**
 * Load default XML snippets.
 *
 */
public class XMLSnippetRegistryLoader implements ISnippetRegistryLoader {

	@Override
	public void load(SnippetRegistry registry) throws Exception {
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("new-xml-snippets.json"),
				NewFileSnippetContext.XML_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("catalog-snippets.json"),
				NewFileSnippetContext.XML_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("new-xsd-snippets.json"),
				NewFileSnippetContext.XSD_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("cdata-snippets.json"),
				CDATASnippetContext.DEFAULT_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("comment-snippets.json"),
				CommentSnippetContext.DEFAULT_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("doctype-snippets.json"),
				DocTypeSnippetContext.DEFAULT_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("xml-declaration-snippets.json"),
				XMLDeclarationSnippetContext.DEFAULT_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("dtdnode-snippets.json"),
				DTDNodeSnippetContext.DEFAULT_CONTEXT);
		registry.registerSnippets(XMLSnippetRegistryLoader.class.getResourceAsStream("processing-instruction-snippets.json"),
				ProcessingInstructionSnippetContext.DEFAULT_CONTEXT);
	}

}
