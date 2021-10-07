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
package org.eclipse.lemminx.extensions.xsd.participants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.lemminx.client.CodeLensKind;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensRequest;
import org.eclipse.lemminx.services.extensions.codelens.ReferenceCommand;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XSD CodeLens to show references count between XSD types references.
 * 
 * @author Angelo ZERR
 *
 */
public class XSDCodeLensParticipant implements ICodeLensParticipant {

	@Override
	public void doCodeLens(ICodeLensRequest request, List<CodeLens> lenses, CancelChecker cancelChecker) {
		DOMDocument xmlDocument = request.getDocument();
		// XSD types CodeLens is applicable only for XML Schema file
		if (!DOMUtils.isXSD(xmlDocument)) {
			return;
		}
		boolean supportedByClient = request.isSupportedByClient(CodeLensKind.References);
		// Add references CodeLens for each xs:simpleType, xs:complexType, xs:element,
		// xs:group root element.
		Map<DOMElement, CodeLens> cache = new HashMap<>();
		XSDUtils.searchXSOriginAttributes(xmlDocument, (origin, target) -> {
			// Increment references count Codelens for the given target element
			DOMElement targetElement = target.getOwnerElement();
			CodeLens codeLens = cache.get(targetElement);
			if (codeLens == null) {
				Range range = XMLPositionUtility.createRange(target);
				codeLens = new CodeLens(range);
				codeLens.setCommand(
						new ReferenceCommand(xmlDocument.getDocumentURI(), range.getStart(), supportedByClient));
				cache.put(targetElement, codeLens);
				lenses.add(codeLens);
			} else {
				((ReferenceCommand) codeLens.getCommand()).increment();
			}
		}, cancelChecker);
	}

}
