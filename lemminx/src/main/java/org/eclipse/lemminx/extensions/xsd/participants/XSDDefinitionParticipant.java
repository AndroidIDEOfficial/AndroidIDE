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

import java.util.List;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils.BindingType;
import org.eclipse.lemminx.services.extensions.AbstractDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionRequest;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XSD definition which manages the following definition:
 * 
 * <ul>
 * <li>xs:element/@type -> xs:complexType/@name</li>
 * <li>xs:extension/@base -> xs:complexType/@name</li>
 * <li>xs:element/@ref -> xs:complexType/@name</li>
 * </ul>
 * 
 * @author Angelo ZERR
 *
 */
public class XSDDefinitionParticipant extends AbstractDefinitionParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		return DOMUtils.isXSD(document);
	}

	@Override
	protected void doFindDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {

		// - xs:element/@type -> xs:complexType/@name
		// - xs:extension/@base -> xs:complexType/@name
		// - xs:element/@ref -> xs:complexType/@name
		DOMNode node = request.getNode();
		if (!node.isAttribute()) {
			return;
		}
		DOMAttr attr = (DOMAttr) node;
		BindingType bindingType = XSDUtils.getBindingType(attr);
		if (bindingType != BindingType.NONE) {
			XSDUtils.searchXSTargetAttributes(attr, bindingType, true, true, (targetNamespacePrefix, targetAttr) -> {
				LocationLink location = XMLPositionUtility.createLocationLink(attr.getNodeAttrValue(),
						targetAttr.getNodeAttrValue());
				locations.add(location);
			});
		}
	}

}
