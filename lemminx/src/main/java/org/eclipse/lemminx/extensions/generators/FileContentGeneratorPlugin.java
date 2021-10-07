/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.generators;

import org.eclipse.lemminx.services.IXMLFullFormatter;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lsp4j.InitializeParams;

/**
 * Plugin to register the {@link FileContentGeneratorManager} in the component
 * registry.
 *
 */
public class FileContentGeneratorPlugin implements IXMLExtension {

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		if (registry instanceof IXMLFullFormatter) {
			IXMLFullFormatter formatter = (IXMLFullFormatter) registry;
			FileContentGeneratorManager manager = new FileContentGeneratorManager(formatter);
			registry.registerComponent(manager);
		}
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {

	}

}
