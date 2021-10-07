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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.DocumentSymbolsResult;
import org.eclipse.lemminx.services.SymbolInformationResult;
import org.eclipse.lemminx.settings.XMLSymbolFilter;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Symbols provider participant API.
 *
 */
public interface ISymbolsProviderParticipant {

	public enum SymbolStrategy {
		UNADAPTABLE, INSERT, REPLACE;
	}

	/**
	 * Returns the symbol strategy to apply for the given DOM document :
	 * 
	 * <ul>
	 * <li>{@link SymbolStrategy#UNADAPTABLE} : means that the participant is not
	 * applicable for the document</li>
	 * <li>{@link SymbolStrategy#INSERT} : means that the participant will insert
	 * symbols on the top of the standards symbols. *
	 * <li>{@link SymbolStrategy#REPLACE} : means that the participant will replace
	 * the standard XML symbols with their own symbols.</li></li>
	 * </ul>
	 * 
	 * @param document the DOM document.
	 * 
	 * @return the symbol strategy to apply for the given DOM document.
	 */
	SymbolStrategy applyFor(DOMDocument document);

	/**
	 * Fill the given symbol information result with custom symbol informations.
	 * 
	 * @param document      the DOM document.
	 * @param symbols       the symbols to update.
	 * @param filter        the symbol filter.
	 * @param cancelChecker the cancel checker.
	 */
	void findSymbolInformations(DOMDocument document, SymbolInformationResult symbols, XMLSymbolFilter filter,
			CancelChecker cancelChecker);

	/**
	 * Fill the given document symbol result with custom document symbol.
	 * 
	 * @param document      the DOM document.
	 * @param symbols       the symbols to update.
	 * @param filter        the symbol filter.
	 * @param cancelChecker the cancel checker.
	 */
	void findDocumentSymbols(DOMDocument document, DocumentSymbolsResult symbols, XMLSymbolFilter filter,
			CancelChecker cancelChecker);

}
