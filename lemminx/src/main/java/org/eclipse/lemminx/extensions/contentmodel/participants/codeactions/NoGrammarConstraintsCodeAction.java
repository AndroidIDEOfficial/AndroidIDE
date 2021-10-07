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
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import static org.eclipse.lemminx.client.ClientCommands.OPEN_BINDING_WIZARD;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.extensions.generators.FileContentGeneratorManager;
import org.eclipse.lemminx.extensions.generators.xml2dtd.DTDGeneratorSettings;
import org.eclipse.lemminx.extensions.generators.xml2xsd.XMLSchemaGeneratorSettings;
import org.eclipse.lemminx.services.XMLCompletions;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLBuilder;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

/**
 * Code Action to bind a XML to a grammar (DTD, XSD) by generating the grammar.
 */
public class NoGrammarConstraintsCodeAction implements ICodeActionParticipant {

	private static final Logger LOGGER = Logger.getLogger(XMLCompletions.class.getName());

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		try {
			DOMElement documentElement = document.getDocumentElement();
			if (documentElement == null || !documentElement.hasTagName()) {
				return;
			}

			FileContentGeneratorManager generator = componentProvider.getComponent(FileContentGeneratorManager.class);

			// ---------- XSD

			String schemaURI = getGrammarURI(document.getDocumentURI(), "xsd");
			String schemaFileName = getFileName(schemaURI);
			String schemaTemplate = generator.generate(document, sharedSettings, new XMLSchemaGeneratorSettings());

			// xsi:noNamespaceSchemaLocation
			// Create code action to create the XSD file with the generated XSD content
			TextDocumentEdit noNamespaceSchemaLocationEdit = createXSINoNamespaceSchemaLocationEdit(schemaFileName,
					document);
			CodeAction noNamespaceSchemaLocationAction = createGrammarFileAndBindIt(
					"Generate '" + schemaFileName + "' and bind with xsi:noNamespaceSchemaLocation", schemaURI,
					schemaTemplate, noNamespaceSchemaLocationEdit, diagnostic);
			codeActions.add(noNamespaceSchemaLocationAction);

			// xml-model
			TextDocumentEdit xsdWithXMLModelEdit = createXmlModelEdit(schemaFileName, null, document, sharedSettings);
			CodeAction xsdWithXmlModelAction = createGrammarFileAndBindIt(
					"Generate '" + schemaFileName + "' and bind with xml-model", schemaURI, schemaTemplate,
					xsdWithXMLModelEdit, diagnostic);
			codeActions.add(xsdWithXmlModelAction);

			// ---------- DTD

			String dtdURI = getGrammarURI(document.getDocumentURI(), "dtd");
			String dtdFileName = getFileName(dtdURI);
			String dtdTemplate = generator.generate(document, sharedSettings, new DTDGeneratorSettings());

			// <!DOCTYPE ${1:root-element} SYSTEM \"${2:file}.dtd\">
			TextDocumentEdit dtdWithDocType = createDocTypeEdit(dtdFileName, document, sharedSettings);
			CodeAction docTypeAction = createGrammarFileAndBindIt(
					"Generate '" + dtdFileName + "' and bind with DOCTYPE", dtdURI, dtdTemplate, dtdWithDocType,
					diagnostic);
			codeActions.add(docTypeAction);

			// xml-model
			TextDocumentEdit dtdWithXMLModelEdit = createXmlModelEdit(dtdFileName, null, document, sharedSettings);
			CodeAction dtdWithXmlModelAction = createGrammarFileAndBindIt(
					"Generate '" + dtdFileName + "' and bind with xml-model", dtdURI, dtdTemplate, dtdWithXMLModelEdit,
					diagnostic);
			codeActions.add(dtdWithXmlModelAction);

			// ---------- Open Binding Wizard
			if (sharedSettings.isBindingWizardSupport()) {
				String documentURI = document.getDocumentURI();
				String title = "Bind to existing grammar/schema";
				List<Object> commandParams = Arrays.asList(documentURI);
				CodeAction bindWithExistingGrammar = CodeActionFactory.createCommand(title, OPEN_BINDING_WIZARD, commandParams, diagnostic);
				codeActions.add(bindWithExistingGrammar);
			}

		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "In NoGrammarConstraintsCodeAction position error", e);
		}
	}

	/**
	 * Returns the unique grammar URI file.
	 *
	 * @param documentURI   the XML document URI.
	 * @param fileExtension the grammar file extension.
	 *
	 * @return the unique grammar URI file.
	 */
	static String getGrammarURI(String documentURI, String fileExtension) {
		int index = documentURI.lastIndexOf('.');
		if (index > 1 && documentURI.charAt(index - 1) == '/') {
			// case with file which starts with '.' (ex .project, .classpath).
			index = -1;
		}
		String grammarWithoutExtension = index != -1 ? documentURI.substring(0, index) : documentURI;
		String grammarURI = grammarWithoutExtension + "." + fileExtension;
		int i = 1;
		try {
			while (Files.exists(Paths.get(new URI(grammarURI)))) {
				grammarURI = grammarWithoutExtension + (i++) + "." + fileExtension;
			}
		} catch (Exception e) {
			// Do nothing
		}
		return grammarURI;
	}

	static String getFileName(String schemaURI) {
		return new File(schemaURI).getName();
	}

	private static CodeAction createGrammarFileAndBindIt(String title, String grammarURI, String grammarContent,
			TextDocumentEdit boundEdit, Diagnostic diagnostic) {
		CodeAction codeAction = CodeActionFactory.createFile(title, grammarURI, grammarContent, diagnostic);
		codeAction.getEdit().getDocumentChanges().add(Either.forLeft(boundEdit));
		return codeAction;
	}

	public static TextDocumentEdit createXSINoNamespaceSchemaLocationEdit(String schemaFileName, DOMDocument document)
			throws BadLocationException {
		String delimiter = document.getTextDocument().lineDelimiter(0);
		DOMElement documentElement = document.getDocumentElement();
		int beforeTagOffset = documentElement.getStartTagOpenOffset();
		int afterTagOffset = beforeTagOffset + 1 + documentElement.getTagName().length();

		StringBuilder insertText = new StringBuilder();

		insertText.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		insertText.append(delimiter);

		insertText.append(" xsi:noNamespaceSchemaLocation=\"");
		insertText.append(schemaFileName);
		insertText.append("\"");

		Position position = document.positionAt(afterTagOffset);
		return CodeActionFactory.insertEdit(insertText.toString(), position, document.getTextDocument());
	}

	public static TextDocumentEdit createXSISchemaLocationEdit(String schemaFileName, String targetNamespace,
			DOMDocument document) throws BadLocationException {
		String delimiter = document.getTextDocument().lineDelimiter(0);
		DOMElement documentElement = document.getDocumentElement();
		int beforeTagOffset = documentElement.getStartTagOpenOffset();
		int afterTagOffset = beforeTagOffset + 1 + documentElement.getTagName().length();

		StringBuilder insertText = new StringBuilder();
		insertText.append(" xmlns=\"");
		insertText.append(targetNamespace);
		insertText.append("\"");
		insertText.append(delimiter);

		insertText.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		insertText.append(delimiter);

		insertText.append(" xsi:schemaLocation=\"");
		insertText.append(targetNamespace);
		insertText.append(" ");
		insertText.append(schemaFileName);
		insertText.append("\"");

		Position position = document.positionAt(afterTagOffset);
		return CodeActionFactory.insertEdit(insertText.toString(), position, document.getTextDocument());
	}

	public static TextDocumentEdit createXmlModelEdit(String schemaFileName, String targetNamespace,
			DOMDocument document, SharedSettings sharedSettings) throws BadLocationException {
		String delimiter = document.getTextDocument().lineDelimiter(0);
		DOMElement documentElement = document.getDocumentElement();
		int beforeTagOffset = documentElement.getStartTagOpenOffset();

		// Insert Text edit for xml-model
		XMLBuilder xsdWithXmlModel = new XMLBuilder(sharedSettings, null, delimiter);
		xsdWithXmlModel.startPrologOrPI("xml-model");
		xsdWithXmlModel.addSingleAttribute("href", schemaFileName, true);
		xsdWithXmlModel.endPrologOrPI();
		xsdWithXmlModel.linefeed();

		String xmlModelInsertText = xsdWithXmlModel.toString();
		Position xmlModelPosition = document.positionAt(beforeTagOffset);

		if (StringUtils.isEmpty(targetNamespace)) {
			return CodeActionFactory.insertEdit(xmlModelInsertText, xmlModelPosition, document.getTextDocument());
		}

		StringBuilder xmlNamespaceInsertText = new StringBuilder();
		xmlNamespaceInsertText.append(" xmlns=\"");
		xmlNamespaceInsertText.append(targetNamespace);
		xmlNamespaceInsertText.append("\" ");

		int afterTagOffset = beforeTagOffset + 1 + documentElement.getTagName().length();
		Position xmlNamespacePosition = document.positionAt(afterTagOffset);

		List<TextEdit> edits = Arrays.asList( //
				// insert xml-model before root tag element
				CodeActionFactory.insertEdit(xmlModelInsertText, xmlModelPosition),
				// insert xml namespace inside root tag element
				CodeActionFactory.insertEdit(xmlNamespaceInsertText.toString(), xmlNamespacePosition));
		return CodeActionFactory.insertEdits(document.getTextDocument(), edits);
	}

	public static TextDocumentEdit createDocTypeEdit(String dtdFileName, DOMDocument document,
			SharedSettings sharedSettings) throws BadLocationException {
		String delimiter = document.getTextDocument().lineDelimiter(0);
		DOMElement documentElement = document.getDocumentElement();
		int beforeTagOffset = documentElement.getStartTagOpenOffset();

		XMLBuilder docType = new XMLBuilder(sharedSettings, null, delimiter);
		docType.startDoctype();
		docType.addParameter(documentElement.getLocalName());
		docType.addContent(" SYSTEM \"");
		docType.addContent(dtdFileName);
		docType.addContent("\"");
		docType.endDoctype();
		docType.linefeed();

		String insertText = docType.toString();
		Position position = document.positionAt(beforeTagOffset);
		return CodeActionFactory.insertEdit(insertText, position, document.getTextDocument());
	}

}
