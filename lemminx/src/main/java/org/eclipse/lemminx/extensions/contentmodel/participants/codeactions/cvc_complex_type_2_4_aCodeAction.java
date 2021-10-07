/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/l-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants.codeactions;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.lemminx.commons.CodeActionFactory;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.CMElementDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.IComponentProvider;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.LevenshteinDistance;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

/**
 * cvc_complex_type_2_4_a
 */
public class cvc_complex_type_2_4_aCodeAction implements ICodeActionParticipant {

	private static final float MAX_DISTANCE_DIFF_RATIO = 0.4f;

	@Override
	public void doCodeAction(Diagnostic diagnostic, Range range, DOMDocument document, List<CodeAction> codeActions,
			SharedSettings sharedSettings, IComponentProvider componentProvider) {
		try {
			int offset = document.offsetAt(diagnostic.getRange().getStart());
			DOMNode node = document.findNodeAt(offset);
			if (node != null && node.isElement()) {
				// Get element from the diagnostic
				DOMElement element = (DOMElement) node;
				String localName = element.getLocalName();

				Collection<CMElementDeclaration> possibleElements = getPossibleElements(element, componentProvider);
				if (possibleElements != null) {

					// When added to these collections, the names will be ordered alphabetically
					Collection<String> otherElementNames = new TreeSet<String>(Collator.getInstance());
					Collection<String> similarElementNames = new TreeSet<String>(Collator.getInstance());

					// Try to collect similar names coming from tag name
					for (CMElementDeclaration possibleElement : possibleElements) {
						String possibleElementName = possibleElement.getName();
						if (isSimilar(possibleElementName, localName)) {
							similarElementNames.add(possibleElementName);
						} else {
							otherElementNames.add(possibleElementName);
						}
					}

					// Create ranges for the replace.
					boolean selectLocalNameOnly = element.getPrefix() != null;
					List<Range> ranges = new ArrayList<>();
					Range startRange, endRange;
					if (selectLocalNameOnly) {
						startRange = XMLPositionUtility.selectStartTagLocalName(element);
						endRange = XMLPositionUtility.selectEndTagLocalName(element);
					} else {
						startRange = XMLPositionUtility.selectStartTagName(element);
						endRange = XMLPositionUtility.selectEndTagName(element);
					}
					ranges.add(startRange);

					if (endRange != null) {
						ranges.add(endRange);
					}

					if (!similarElementNames.isEmpty()) {
						// // Add code actions for each similar elements
						for (String elementName : similarElementNames) {
							CodeAction similarCodeAction = CodeActionFactory.replaceAt(
									"Did you mean '" + elementName + "'?", elementName, document.getTextDocument(),
									diagnostic, ranges);
							codeActions.add(similarCodeAction);
						}
					} else {
						// Add code actions for each possible elements
						for (String elementName : otherElementNames) {
							CodeAction otherCodeAction = CodeActionFactory.replaceAt(
									"Replace with '" + elementName + "'", elementName, document.getTextDocument(),
									diagnostic, ranges);
							codeActions.add(otherCodeAction);
						}
					}
				}
			}

		} catch (Exception e) {
			// Do nothing
		}
	}

	/**
	 * Returns the possible elements for the given DOM element.
	 * 
	 * @param element           the DOM element
	 * @param componentProvider the component provider
	 * @return the possible elements for the given DOM element.
	 * @throws Exception
	 */
	private static Collection<CMElementDeclaration> getPossibleElements(DOMElement element,
			IComponentProvider componentProvider) throws Exception {
		ContentModelManager contentModelManager = componentProvider.getComponent(ContentModelManager.class);

		String prefix = element.getPrefix();
		DOMElement parentElement = element.getParentElement();
		String parentPrefix = parentElement.getPrefix();
		// check if prefix is the same than the parent profix
		if (prefix != null && !prefix.equals(parentPrefix)) {
			// We are in the case
			// <b:bean><camel:beani

			// returns the all element for the camel XML Schema.
			String namespaceURI = element.getNamespaceURI();
			List<CMElementDeclaration> possibleElements = new ArrayList<>();
			for (CMDocument cmDocument : contentModelManager.findCMDocument(parentElement, namespaceURI)) {
				possibleElements.addAll(cmDocument.getElements());
			}
			return possibleElements;
		}

		List<CMElementDeclaration> possibleElements = new ArrayList<>();
		for (CMDocument cmDocument : contentModelManager.findCMDocument(parentElement)) {
			CMElementDeclaration cmElement = cmDocument.findCMElement(parentElement);
			if (cmElement != null) {
				possibleElements.addAll(cmElement.getPossibleElements(parentElement, element.getStart()));
			}
		}
		return possibleElements;
	}

	private static boolean isSimilar(String reference, String current) {
		int threshold = Math.round(MAX_DISTANCE_DIFF_RATIO * reference.length());
		LevenshteinDistance levenshteinDistance = new LevenshteinDistance(threshold);
		return levenshteinDistance.apply(reference, current) != -1;
	}
}