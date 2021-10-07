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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.generators.FileContentGeneratorManager;
import org.eclipse.lemminx.extensions.generators.FileContentGeneratorSettings;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * Code Action which manages missing referenced grammar file (DTD, XSD).
 */
public abstract class AbstractFixMissingGrammarCodeAction implements ICodeActionParticipant {

	private static final String FILE_SCHEME = "'file:///";

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {

		String missingFilePath = getPathFromDiagnostic(diagnostic);
		if (StringUtils.isEmpty(missingFilePath)) {
			return;
		}
		Path p = Paths.get(missingFilePath);
		if (p.toFile().exists()) {
			return;
		}

		// Generate XSD from the DOM document
		FileContentGeneratorManager generator = componentProvider.getComponent(FileContentGeneratorManager.class);
		String schemaTemplate = generator.generate(document, sharedSettings, getFileContentGeneratorSettings());

		// Create code action to create the XSD file with the generated XSD content
		CodeAction makeSchemaFile = CodeActionFactory.createFile("Generate missing file '" + p.toFile().getName() + "'",
				"file:///" + missingFilePath, schemaTemplate, diagnostic);

		codeActions.add(makeSchemaFile);
	}

	/**
	 * Extract the file to create from the diagnostic. Example diagnostic:
	 * schema_reference.4: Failed to read schema document
	 * 'file:///home/dthompson/Documents/TestFiles/potationCoordination.xsd',
	 * because 1) could not find the document; 2) the document could not be read; 3)
	 * the root element of the document is not <xsd:schema>.
	 */
	private String getPathFromDiagnostic(Diagnostic diagnostic) {
		String message = diagnostic.getMessage();
		int startIndex = message.indexOf(FILE_SCHEME);
		if (startIndex != -1) {
			int endIndex = message.lastIndexOf("'");
			return message.substring(startIndex + FILE_SCHEME.length(), endIndex);
		}

		return null;
	}

	/**
	 * Returns the grammar settings used to generate the missing grammar file(XSD,
	 * DTD).
	 * 
	 * @return the grammar settings used to generate the missing grammar file(XSD,
	 *         DTD).
	 */
	protected abstract FileContentGeneratorSettings getFileContentGeneratorSettings();

}