/**
 *  Copyright (c) 2019 Red Hat, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.Collection;
import java.util.List;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.services.extensions.AbstractTypeDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.ITypeDefinitionRequest;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Extension to support XML type definition based on content model (XML Schema
 * type definition, etc)
 */
public class ContentModelTypeDefinitionParticipant extends AbstractTypeDefinitionParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		return true;
	}

	@Override
	protected void doFindTypeDefinition(ITypeDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
		DOMNode node = request.getNode();
		if (node == null) {
			return;
		}
		DOMElement element = null;
		if (node.isElement()) {
			element = (DOMElement) node;
		} else if (node.isAttribute()) {
			element = ((DOMAttr) node).getOwnerElement();
		}
		if (element != null) {
			Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(element);
			for (CMDocument cmDocument : cmDocuments) {
				LocationLink location = cmDocument.findTypeLocation(node);
				if (location != null) {
					locations.add(location);
				}
			}
		}
	}

}
