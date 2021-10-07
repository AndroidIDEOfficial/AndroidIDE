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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.XMLBuilder;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

/**
 * Code action to fix EntityNotDeclaredCodeAction error.
 * 
 * Precondition: {@link Diagnostic#getMessage()} should be in this format:
 * 
 * "The entity \"ENTITY NAME\" was referenced, but not declared."
 * 
 */
public class EntityNotDeclaredCodeAction implements ICodeActionParticipant {

	private static final Logger LOGGER = Logger.getLogger(EntityNotDeclaredCodeAction.class.getName());

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {

		try {
			String entityName = getEntityName(diagnostic, document);

			if (entityName == null) {
				return;
			}

			DOMDocumentType docType = document.getDoctype();
			if (docType != null) {

				// Case 1: <!DOCTYPE exists
				// Add <!ENTITY nbsp "entity-value"> in the subset.
				// If the subset does not exist, add subset too

				// ie:
				// <!DOCTYPE article [
				// <!ELEMENT article (#PCDATA)>
				// <!ENTITY nbsp "entity-value">
				// ]>
				addEntityCodeAction(entityName, diagnostic, document, sharedSettings, codeActions);
			} else {
				// Case 2: <!DOCTYPE does not exist
				// Generate:
				// <!DOCTYPE article [
				// <!ENTITY nbsp "entity-value">
				// ]>
				addDoctypeAndEntityCodeAction(entityName, diagnostic, document, sharedSettings, codeActions);
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "In EntityNotDeclaredCodeAction the DOMDocument offset(s) is at a BadLocation", e);
		}
	}

	/**
	 * Add a code action that inserts entity declaration to the current DOCTYPE's
	 * internal subset
	 * 
	 * If the internal subset does not exist, the code action inserts it as well
	 * 
	 * @param entityName  the entity name for the entity declaration
	 * @param diagnostic  the code action's diagnostic
	 * @param document    the DOMDocument
	 * @param settings    the settings
	 * @param codeActions the list of code actions
	 * @throws BadLocationException
	 */
	private void addEntityCodeAction(String entityName, Diagnostic diagnostic, DOMDocument document,
			SharedSettings settings, List<CodeAction> codeActions) throws BadLocationException {

		DOMDocumentType docType = document.getDoctype();
		Position docTypeEnd = document.positionAt(docType.getEnd());
		Position insertPosition = getEntityInsertPosition(document);
		String message = "Declare ENTITY " + entityName;
		String delimiter = document.lineDelimiter(insertPosition.getLine());

		XMLBuilder insertString = new XMLBuilder(settings, null, delimiter);

		boolean hasInternalSubset = docType.getInternalSubset() != null;
		if (!hasInternalSubset) {
			String doctypeContent = docType.getTextContent();

			if (!Character.isWhitespace(doctypeContent.charAt(doctypeContent.length() - 2))) {
				// doctype ends with " >", "\n>", "\r\n>", etc.
				insertString.startDoctypeInternalSubset();
			} else {
				insertString.startUnindentedDoctypeInternalSubset();
			}

			if (insertPosition.getLine() > 0) {
				insertString.linefeed();
			}
		} else if (insertPosition.getCharacter() != 0) {
			insertString.linefeed(); // add the new entity in a new line
		}

		insertString.indent(1);
		addEntityDeclaration(entityName, insertString);

		if (docType.getInternalSubset() == null) {
			insertString.linefeed().endDoctypeInternalSubset();
		} else if (docTypeEnd.getLine() == insertPosition.getLine()) {
			insertString.linefeed();
		}

		CodeAction action = CodeActionFactory.insert(message, insertPosition, insertString.toString(),
				document.getTextDocument(), diagnostic);
		codeActions.add(action);
	}

	/**
	 * Add a code action that inserts the DOCTYPE declaration containing an internal
	 * subset with an entity declaration
	 * 
	 * @param entityName  the entity name for the entity declaration
	 * @param diagnostic  the code action's diagnostic
	 * @param document    the DOMDocument
	 * @param settings    the settings
	 * @param codeActions the list of code actions
	 * @throws BadLocationException
	 */
	private void addDoctypeAndEntityCodeAction(String entityName, Diagnostic diagnostic, DOMDocument document,
			SharedSettings settings, List<CodeAction> codeActions) throws BadLocationException {
		Position insertPosition = getDoctypeInsertPosition(document);

		String delimiter = document.lineDelimiter(insertPosition.getLine());
		String message = "Declare DOCTYPE containing ENTITY " + entityName;
		DOMElement root = document.getDocumentElement();
		if (root == null) {
			return;
		}

		XMLBuilder insertString = new XMLBuilder(settings, null, delimiter);
		if (insertPosition.getCharacter() > 0) {
			insertString.linefeed();
		}
		insertString.startDoctype().addParameter(root.getTagName()).startDoctypeInternalSubset().linefeed().indent(1);

		addEntityDeclaration(entityName, insertString);

		insertString.linefeed().endDoctypeInternalSubset().closeStartElement();

		Position rootStartPosition = document.positionAt(root.getStart());
		if (insertPosition.getLine() == rootStartPosition.getLine()) {
			insertString.linefeed();
		}

		CodeAction action = CodeActionFactory.insert(message, insertPosition, insertString.toString(),
				document.getTextDocument(), diagnostic);
		codeActions.add(action);
	}

	private Position getDoctypeInsertPosition(DOMDocument document) throws BadLocationException {
		if (!document.hasProlog()) {
			return new Position(0, 0);
		}
		int prologEnd = document.getChildren().get(0).getEnd();
		return document.positionAt(prologEnd);
	}

	private Position getEntityInsertPosition(DOMDocument document) throws BadLocationException {
		TextDocument textDocument = document.getTextDocument();
		DOMDocumentType docType = document.getDoctype();

		String subset = docType.getInternalSubset();
		if (subset == null) { // subset ([]) does not exist
			return textDocument.positionAt(docType.getEnd() - 1);
		}

		DOMNode lastChild = docType.getLastChild();
		if (lastChild != null) {
			return textDocument.positionAt(lastChild.getEnd());
		}

		// empty subset exists
		String subsetValue = "[" + subset + "]";
		int index = docType.getTextContent().indexOf(subsetValue);
		if (index >= 0) { // if statement should always satisfy
			index += subsetValue.length() - 1; // index of ]
			return docType.getOwnerDocument().positionAt(index + docType.getStart());
		}

		return null;
	}

	/**
	 * Returns entity name from the error range and error message
	 * 
	 * TODO: This code is a workaround until this issue is fixed:
	 * https://github.com/microsoft/language-server-protocol/issues/887
	 * 
	 * @param diagnostic the diagnostic
	 * @param doc        the DOM document
	 * @return entity name from the error range and error message.
	 * @throws BadLocationException
	 */
	private static String getEntityName(Diagnostic diagnostic, DOMDocument doc) throws BadLocationException {
		Range range = diagnostic.getRange();
		String name = doc.getText().substring(doc.offsetAt(range.getStart()), doc.offsetAt(range.getEnd()));
		String removedAmpAndSemiColon = name.substring(1, name.length() - 1);
		if (!diagnostic.getMessage().contains("\"" + removedAmpAndSemiColon + "\"")) {
			return null;
		}
		return removedAmpAndSemiColon;
	}

	private static void addEntityDeclaration(String entityName, XMLBuilder builder) {
		builder.addDeclTagStart("ENTITY") //
				.addParameter(entityName) //
				.addParameter("\"entity-value\"")//
				.closeStartElement();
	}
}