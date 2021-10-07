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
 * Abstract class for type definition.
 * 
 * @author Angelo ZERR
 *
 */
public abstract class AbstractTypeDefinitionParticipant implements ITypeDefinitionParticipant {

	@Override
	public final void findTypeDefinition(ITypeDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		DOMDocument document = request.getXMLDocument();
		if (!match(document)) {
			return;
		}
		doFindTypeDefinition(request, locations, cancelChecker);
	}

	/**
	 * Returns true if the type definition support is applicable for the given
	 * document and false otherwise.
	 * 
	 * @param document
	 * @return true if the type definition support is applicable for the given
	 *         document and false otherwise.
	 */
	protected abstract boolean match(DOMDocument document);

	/**
	 * Find the type definition
	 * 
	 * @param request
	 * @param locations
	 * @param cancelChecker
	 */
	protected abstract void doFindTypeDefinition(ITypeDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker);

}
