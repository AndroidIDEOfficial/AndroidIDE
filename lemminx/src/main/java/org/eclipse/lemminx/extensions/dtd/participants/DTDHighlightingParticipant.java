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

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDAttlistDecl;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.DTDElementDecl;
import org.eclipse.lemminx.extensions.dtd.utils.DTDUtils;
import org.eclipse.lemminx.services.extensions.IHighlightingParticipant;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * DTD highlight participant
 * 
 * @author Angelo ZERR
 *
 */

public class DTDHighlightingParticipant implements IHighlightingParticipant {

	@Override
	public void findDocumentHighlights(DOMNode node, Position position, int offset, List<DocumentHighlight> highlights,
			CancelChecker cancelChecker) {
		boolean findReferences = false;
		DTDDeclParameter parameter = null;
		DTDElementDecl elementDecl = null;

		if (node.isDTDElementDecl()) {
			elementDecl = (DTDElementDecl) node;
			if (elementDecl.isInNameParameter(offset)) {
				// <!ELEMENT na|me --> here cursor is in the name of <!ELEMENT
				// we must find all references from the <!ELEMENT which defines the name
				findReferences = true;
				parameter = elementDecl.getNameParameter();
			} else {
				// <!ELEMENT name (chi|ld --> here cursor is in the child element
				// we must find only the <!ELEMENT child
				parameter = elementDecl.getParameterAt(offset);
			}
		} else if (node.isDTDAttListDecl()) {
			DTDAttlistDecl attlistDecl = (DTDAttlistDecl) node;
			if (attlistDecl.isInNameParameter(offset)) {
				// <!ATTLIST na|me --> here cusror is in the name of <!ATTLIST
				// we must find only the <!ELEMENT name
				parameter = attlistDecl.getNameParameter();
			}
		}

		if (parameter == null) {
			return;
		}

		if (findReferences) {
			// case with <!ELEMENT na|me

			// highlight <!ELEMENT na|me
			DTDDeclParameter originNode = parameter;
			highlights.add(
					new DocumentHighlight(XMLPositionUtility.createRange(originNode), DocumentHighlightKind.Write));

			// highlight all references of na|me in ATTLIST and child of <!ELEMENT
			DTDUtils.searchDTDOriginElementDecls(elementDecl, (origin, target) -> {
				highlights
						.add(new DocumentHighlight(XMLPositionUtility.createRange(origin), DocumentHighlightKind.Read));
			}, cancelChecker);
		} else {
			// case with
			// - <!ELEMENT name (chi|ld
			// - <!ATTLIST na|me

			// highlight <!ELEMENT name (chi|ld or <!ATTLIST na|me
			DTDDeclParameter targetNode = parameter;
			highlights
					.add(new DocumentHighlight(XMLPositionUtility.createRange(targetNode), DocumentHighlightKind.Read));

			// highlight the target <!ELEMENT nam|e
			DTDUtils.searchDTDTargetElementDecl(parameter, true, targetName -> {
				highlights.add(
						new DocumentHighlight(XMLPositionUtility.createRange(targetName), DocumentHighlightKind.Write));
			});
		}
	}

}
