/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.dtd.participants;

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.dom.DTDDeclNode;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.extensions.dtd.utils.DTDUtils;
import org.eclipse.lemminx.services.extensions.AbstractDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionRequest;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * DTD definition participant
 * 
 * @author Angelo ZERR
 *
 */
public class DTDDefinitionParticipant extends AbstractDefinitionParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		// Not applicable for XML Schema
		return !DOMUtils.isXSD(document);
	}

	@Override
	protected void doFindDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		DOMNode node = request.getNode();
		int offset = request.getOffset();
		// DTD definition is applicable only for <!ELEMENT and <!ATTLIST
		if (!(node.isDTDElementDecl() || node.isDTDAttListDecl())) {
			return;
		}
		// Get the parameter which defines the name which references an <!ELEMENT
		// - <!ATTLIST elt -> we search the 'elt' in <!ELEMENT elt
		// - <!ELEMENT elt (child1 -> we search the 'child1' in <!ELEMENT child1
		DTDDeclParameter originName = ((DTDDeclNode) node).getReferencedElementNameAt(offset);
		if (originName != null) {
			DTDUtils.searchDTDTargetElementDecl(originName, true, targetElementName -> {
				LocationLink location = XMLPositionUtility.createLocationLink((DOMRange) originName,
						(DOMRange) targetElementName);
				locations.add(location);
			});
		}
	}

}
