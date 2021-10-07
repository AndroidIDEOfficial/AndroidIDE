/**
 *  Copyright (c) 2020 Red Hat, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.entities;

import org.eclipse.lemminx.extensions.entities.participants.EntitiesCompletionParticipant;
import org.eclipse.lemminx.extensions.entities.participants.EntitiesDefinitionParticipant;
import org.eclipse.lemminx.extensions.entities.participants.EntitiesHoverParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IHoverParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.InitializeParams;

/**
 * Plugin for managing used entities (ex : &amp;)
 */
public class EntitiesPlugin implements IXMLExtension {

	private ICompletionParticipant completionParticipant;

	private IDefinitionParticipant definitionParticipant;

	private IHoverParticipant hoverParticipant;

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		completionParticipant = new EntitiesCompletionParticipant();
		registry.registerCompletionParticipant(completionParticipant);
		definitionParticipant = new EntitiesDefinitionParticipant();
		registry.registerDefinitionParticipant(definitionParticipant);
		hoverParticipant = new EntitiesHoverParticipant();
		registry.registerHoverParticipant(hoverParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.unregisterCompletionParticipant(completionParticipant);
		registry.unregisterDefinitionParticipant(definitionParticipant);
		registry.unregisterHoverParticipant(hoverParticipant);
	}

}