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
package org.eclipse.lemminx.services.extensions;

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Abstract class for definition.
 * 
 * @author Angelo ZERR
 *
 */
public abstract class AbstractDefinitionParticipant implements IDefinitionParticipant {

	@Override
	public final void findDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		DOMDocument document = request.getXMLDocument();
		if (!match(document)) {
			return;
		}
		doFindDefinition(request, locations, cancelChecker);
	}

	/**
	 * Returns true if the definition support is applicable for the given document
	 * and false otherwise.
	 * 
	 * @param document
	 * @return true if the definition support is applicable for the given document
	 *         and false otherwise.
	 */
	protected abstract boolean match(DOMDocument document);

	/**
	 * Find the definition
	 * 
	 * @param request
	 * @param locations
	 * @param cancelChecker
	 */
	protected abstract void doFindDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker);

}
