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
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.ITypeDefinitionRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.Position;

/**
 * Type definition request implementation.
 *
 */
class TypeDefinitionRequest extends AbstractPositionRequest implements ITypeDefinitionRequest {

	public TypeDefinitionRequest(DOMDocument xmlDocument, Position position, XMLExtensionsRegistry extensionsRegistry)
			throws BadLocationException {
		super(xmlDocument, position, extensionsRegistry);
	}

}
