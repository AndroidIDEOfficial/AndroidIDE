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
package org.eclipse.lemminx.extensions.references;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;

public class XMLReferencesManager {

	private static final XMLReferencesManager INSTANCE = new XMLReferencesManager();

	public static XMLReferencesManager getInstance() {
		return INSTANCE;
	}

	private final List<XMLReferences> referencesCache;

	public XMLReferencesManager() {
		this.referencesCache = new ArrayList<>();
	}

	public XMLReferences referencesFor(Predicate<DOMDocument> documentPredicate) {
		XMLReferences references = new XMLReferences(documentPredicate);
		referencesCache.add(references);
		return references;
	}

	public void collect(DOMNode node, Consumer<DOMNode> collector) {
		DOMDocument document = node.getOwnerDocument();
		for (XMLReferences references : referencesCache) {
			if (references.canApply(document)) {
				try {
					references.collectNodes(node, collector);
				} catch (XPathExpressionException e) {
					// TODO!!!
					e.printStackTrace();
				}
			}
		}
	}

}
