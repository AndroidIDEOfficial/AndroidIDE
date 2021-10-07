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
package org.eclipse.lemminx.extensions.general;

import org.eclipse.lemminx.extensions.general.completion.FilePathCompletionParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.InitializeParams;

/**
 * FilePathPlugin
 */
public class FilePathPlugin implements IXMLExtension {

	private final FilePathCompletionParticipant completionParticipant;

	public FilePathPlugin() {
		completionParticipant = new FilePathCompletionParticipant();
	}

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		registry.registerCompletionParticipant(completionParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.unregisterCompletionParticipant(completionParticipant);
	}

}