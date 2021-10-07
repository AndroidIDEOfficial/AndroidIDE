/**
 *  Copyright (c) 2018 Angelo ZERR.
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
package org.eclipse.lemminx.services.extensions;

import java.util.List;

import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Definition participant API.
 *
 */
public interface IDefinitionParticipant {

	/**
	 * Find definition.
	 * 
	 * @param request
	 * @param locations
	 * @param cancelChecker
	 */
	void findDefinition(IDefinitionRequest request, List<LocationLink> locations, CancelChecker cancelChecker);

}
