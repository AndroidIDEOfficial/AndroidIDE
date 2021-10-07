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
import org.eclipse.lemminx.extensions.generators.xml2xsd.XMLSchemaGeneratorSettings;

/**
 * Code Action that creates a schema file referenced by
 * xsi:noNamespaceSchemaLocation if it is missing
 */
public class schema_reference_4CodeAction extends AbstractFixMissingGrammarCodeAction {

	@Override
	protected FileContentGeneratorSettings getFileContentGeneratorSettings() {
		return new XMLSchemaGeneratorSettings();
	}
}