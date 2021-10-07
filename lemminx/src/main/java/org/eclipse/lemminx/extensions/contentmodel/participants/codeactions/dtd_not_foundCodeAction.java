/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import org.eclipse.lemminx.extensions.generators.FileContentGeneratorSettings;
import org.eclipse.lemminx.extensions.generators.xml2dtd.DTDGeneratorSettings;

/**
 * Code Action that creates a DTD file referenced with System ID of DOCTYPE.
 */
public class dtd_not_foundCodeAction extends AbstractFixMissingGrammarCodeAction {

	@Override
	protected FileContentGeneratorSettings getFileContentGeneratorSettings() {
		return new DTDGeneratorSettings();
	}
}