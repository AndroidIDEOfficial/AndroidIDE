/**
 *  Copyright (c) 2019 Red Hat, Inc. and others.
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
package org.eclipse.lemminx.extensions.xsi;

import org.eclipse.lemminx.extensions.xsi.participants.XSICompletionParticipant;
import org.eclipse.lemminx.extensions.xsi.participants.XSIFormatterParticipant;
import org.eclipse.lemminx.extensions.xsi.participants.XSIHoverParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.IHoverParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.format.IFormatterParticipant;
import org.eclipse.lsp4j.InitializeParams;

/**
 * Plugin to handle the xml prolog: {@code <?xml ... ?>}.
 * 
 * 
 * Loaded by service loader in 'resources' folder.
 */
public class XSISchemaPlugin implements IXMLExtension {

	private final ICompletionParticipant completionParticipant = new XSICompletionParticipant();
	private final IHoverParticipant hoverParticipant = new XSIHoverParticipant();

	private final IFormatterParticipant formatterParticipant = new XSIFormatterParticipant();

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		registry.registerCompletionParticipant(completionParticipant);
		registry.registerHoverParticipant(hoverParticipant);
		registry.registerFormatterParticipant(formatterParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.unregisterCompletionParticipant(completionParticipant);
		registry.unregisterHoverParticipant(hoverParticipant);
		registry.unregisterFormatterParticipant(formatterParticipant);
	}

}