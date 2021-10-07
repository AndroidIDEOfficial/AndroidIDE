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
package org.eclipse.lemminx.extensions.references.participants;

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.references.XMLReferencesManager;
import org.eclipse.lemminx.services.extensions.AbstractDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionRequest;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

public class XMLReferencesDefinitionParticipant extends AbstractDefinitionParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		return true;
	}

	@Override
	protected void doFindDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		DOMNode origin = request.getNode();
		XMLReferencesManager.getInstance().collect(origin, target -> {
			LocationLink location = XMLPositionUtility.createLocationLink(origin, target);
			locations.add(location);
		});
	}

}
