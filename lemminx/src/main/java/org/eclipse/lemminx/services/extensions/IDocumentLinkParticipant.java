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

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lsp4j.DocumentLink;

/**
 * Document link participant API.
 *
 */
public interface IDocumentLinkParticipant {

	/**
	 * Find document links of the given XML document.
	 * 
	 * @param document
	 * @param links
	 */
	void findDocumentLinks(DOMDocument document, List<DocumentLink> links);

}
