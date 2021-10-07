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
package org.eclipse.lemminx.extensions.xmlmodel;

import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider;
import org.eclipse.lemminx.extensions.xmlmodel.contentmodel.CMXMLModelContentModelProvider;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.InitializeParams;

/**
 * Plugin for managing xml-model association
 */
public class XMLModelPlugin implements IXMLExtension {

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		// register xml-model content model provider
		ContentModelManager modelManager = registry.getComponent(ContentModelManager.class);
		ContentModelProvider modelProvider = new CMXMLModelContentModelProvider(modelManager);
		modelManager.registerModelProvider(modelProvider);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
	}

}