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
import org.eclipse.lemminx.dom.DTDElementDecl;
import org.eclipse.lemminx.extensions.dtd.utils.DTDUtils;
import org.eclipse.lemminx.services.extensions.AbstractReferenceParticipant;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.ReferenceContext;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * DTD reference
 * 
 * @author Angelo ZERR
 *
 */
public class DTDReferenceParticipant extends AbstractReferenceParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		return true;
	}

	@Override
	protected void findReferences(DOMNode node, Position position, int offset, ReferenceContext context,
			List<Location> locations, CancelChecker cancelChecker) {
		// DTD reference works only when references is done on an <!ELEMENT name
		if (!node.isDTDElementDecl()) {
			return;
		}
		DTDElementDecl elementDecl = (DTDElementDecl) node;
		if (!elementDecl.isInNameParameter(offset)) {
			return;
		}
		DTDUtils.searchDTDOriginElementDecls(elementDecl,
				(origin, target) -> locations.add(XMLPositionUtility.createLocation(origin)), cancelChecker);
	}

}
