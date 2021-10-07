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
package org.eclipse.lemminx.extensions.references;

import org.eclipse.lemminx.extensions.references.participants.XMLReferencesCompletionParticipant;
import org.eclipse.lemminx.extensions.references.participants.XMLReferencesDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.InitializeParams;

public class XMLReferencesPlugin implements IXMLExtension {

	private final ICompletionParticipant completionParticipant;
	private final IDefinitionParticipant definitionParticipant;

	public XMLReferencesPlugin() {
		completionParticipant = new XMLReferencesCompletionParticipant();
		definitionParticipant = new XMLReferencesDefinitionParticipant();
	}

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		registry.registerCompletionParticipant(completionParticipant);
		registry.registerDefinitionParticipant(definitionParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.unregisterCompletionParticipant(completionParticipant);
		registry.unregisterDefinitionParticipant(definitionParticipant);
	}

}
