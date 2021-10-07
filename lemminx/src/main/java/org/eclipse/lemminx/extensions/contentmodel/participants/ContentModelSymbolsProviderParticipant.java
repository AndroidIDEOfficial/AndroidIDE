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
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.List;
import java.util.Set;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider.Identifier;
import org.eclipse.lemminx.extensions.contentmodel.model.GrammarCacheInfo;
import org.eclipse.lemminx.extensions.contentmodel.model.ReferencedGrammarInfo;
import org.eclipse.lemminx.services.DocumentSymbolsResult;
import org.eclipse.lemminx.services.SymbolInformationResult;
import org.eclipse.lemminx.services.extensions.ISymbolsProviderParticipant;
import org.eclipse.lemminx.settings.XMLSymbolFilter;
import org.eclipse.lemminx.uriresolver.ResolvedURIInfo;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.SymbolKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Symbol participant to show referenced grammars information.
 *
 */
public class ContentModelSymbolsProviderParticipant implements ISymbolsProviderParticipant {

	private static final Range DUMMY_RANGE = new Range(new Position(0, 0), new Position(0, 1));

	private static final String GRAMMARS_CATEGORY = "Grammars";

	private final ContentModelManager contentModelManager;

	private boolean enabled;

	public ContentModelSymbolsProviderParticipant(ContentModelManager contentModelManager) {
		this.contentModelManager = contentModelManager;
		// By default, the referenced grammars are displayed in the outline
		setEnabled(true);
	}

	@Override
	public SymbolStrategy applyFor(DOMDocument document) {
		return SymbolStrategy.INSERT;
	}

	@Override
	public void findSymbolInformations(DOMDocument document, SymbolInformationResult symbols, XMLSymbolFilter filter,
			CancelChecker cancelChecker) {
		// Do nothing
	}

	@Override
	public void findDocumentSymbols(DOMDocument document, DocumentSymbolsResult symbols, XMLSymbolFilter filter,
			CancelChecker cancelChecker) {
		if (!isEnabled()) {
			return;
		}
		Set<ReferencedGrammarInfo> referencedGrammarInfos = contentModelManager.getReferencedGrammarInfos(document);
		if (referencedGrammarInfos.isEmpty()) {
			return;
		}
		// The DOM document has some referenced grammars, fill the outline symbols with
		// those information
		DocumentSymbol rootSymbol = new DocumentSymbol(GRAMMARS_CATEGORY, SymbolKind.Module, DUMMY_RANGE, DUMMY_RANGE);
		symbols.add(rootSymbol);

		List<DocumentSymbol> children = symbols.createList();
		rootSymbol.setChildren(children);

		for (ReferencedGrammarInfo info : referencedGrammarInfos) {
			ResolvedURIInfo resolvedInfo = info.getResolvedURIInfo();

			DocumentSymbol grammarInfoSymbol = new DocumentSymbol(resolvedInfo.getResolvedURI(), SymbolKind.File,
					DUMMY_RANGE, DUMMY_RANGE);
			children.add(grammarInfoSymbol);

			List<DocumentSymbol> grammarInfoSymbols = symbols.createList();
			grammarInfoSymbol.setChildren(grammarInfoSymbols);

			// Binding symbol information
			// ex: Binding: xsi:schemaLocation
			// --> binding name
			StringBuilder bindingName = new StringBuilder("Binding: ");
			bindingName.append(ReferencedGrammarInfo.getBindingKindAndResolvedBy(info));

			// --> binding range
			Identifier identifier = info.getIdentifier();
			DOMRange domRange = identifier != null ? identifier.getRange() : null;
			Range range = domRange != null ? XMLPositionUtility.createRange(domRange) : DUMMY_RANGE;
			DocumentSymbol bindingInfoSymbol = new DocumentSymbol(bindingName.toString(), SymbolKind.Property, range,
					range);
			grammarInfoSymbols.add(bindingInfoSymbol);

			// Cache symbol information
			DocumentSymbol cacheInfoSymbol = null;
			if (!info.isInCache()) {
				cacheInfoSymbol = new DocumentSymbol("Cache: false", SymbolKind.Property, DUMMY_RANGE, DUMMY_RANGE);
			} else {
				cacheInfoSymbol = new DocumentSymbol("Cache", SymbolKind.Property, DUMMY_RANGE, DUMMY_RANGE);
				cacheInfoSymbol.setChildren(symbols.createList());
				// Cache file symbol
				GrammarCacheInfo grammarCacheInfo = info.getGrammarCacheInfo();
				DocumentSymbol cacheFileSymbol = new DocumentSymbol(grammarCacheInfo.getCachedResolvedUri(),
						SymbolKind.File, DUMMY_RANGE, DUMMY_RANGE);
				cacheInfoSymbol.getChildren().add(cacheFileSymbol);
			}
			grammarInfoSymbols.add(cacheInfoSymbol);
		}

	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
