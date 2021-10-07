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

import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Type Definition participant API.
 *
 */
public interface ITypeDefinitionParticipant {

	/**
	 * Find type definition.
	 * 
	 * @param request
	 * @param locations
	 * @param cancelChecker
	 */
	void findTypeDefinition(ITypeDefinitionRequest request, List<LocationLink> locations, CancelChecker cancelChecker);

}
