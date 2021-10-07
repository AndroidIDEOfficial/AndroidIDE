/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDAttlistDecl;
import org.eclipse.lemminx.dom.DTDDeclParameter;
import org.eclipse.lemminx.dom.DTDElementDecl;
import org.eclipse.lemminx.dom.DTDNotationDecl;
import org.eclipse.lemminx.services.LimitList.ResultLimitExceededException;
import org.eclipse.lemminx.services.extensions.ISymbolsProviderParticipant;
import org.eclipse.lemminx.services.extensions.ISymbolsProviderParticipant.SymbolStrategy;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.settings.XMLSymbolFilter;
import org.eclipse.lemminx.settings.XMLSymbolSettings;
import org.eclipse.lemminx.xpath.matcher.IXPathNodeMatcher.MatcherType;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.SymbolKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;

/**
 * XML symbol provider.
 *
 */
class XMLSymbolsProvider {

	private static class SymbolsProviderParticipantResult {

		private final Collection<ISymbolsProviderParticipant> replaceParticipants;
		private final Collection<ISymbolsProviderParticipant> insertParticipants;

		public SymbolsProviderParticipantResult(Collection<ISymbolsProviderParticipant> replaceParticipants,
				Collection<ISymbolsProviderParticipant> insertParticipants) {
			this.replaceParticipants = replaceParticipants != null ? replaceParticipants : Collections.emptyList();
			this.insertParticipants = insertParticipants != null ? insertParticipants : Collections.emptyList();
		}

		public Collection<ISymbolsProviderParticipant> getReplaceParticipants() {
			return replaceParticipants;
		}

		public Collection<ISymbolsProviderParticipant> getInsertParticipants() {
			return insertParticipants;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(XMLSymbolsProvider.class.getName());
	private final XMLExtensionsRegistry extensionsRegistry;

	public XMLSymbolsProvider(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	// -------------- Symbol informations

	public SymbolInformationResult findSymbolInformations(DOMDocument xmlDocument, XMLSymbolSettings symbolSettings,
			CancelChecker cancelChecker) {
		AtomicLong limit = symbolSettings.getMaxItemsComputed() >= 0
				? new AtomicLong(symbolSettings.getMaxItemsComputed())
				: null;
		SymbolInformationResult symbols = new SymbolInformationResult(limit);
		XMLSymbolFilter filter = symbolSettings.getFilterFor(xmlDocument.getDocumentURI());

		try {
			// Process symbols participants
			if (processSymbolsParticipants(xmlDocument, symbols, null, filter, cancelChecker)) {
				return symbols;
			}

			// Process default symbol providers
			boolean isDTD = xmlDocument.isDTD();
			boolean hasFilterForAttr = filter.hasFilterFor(MatcherType.ATTRIBUTE);
			for (DOMNode node : xmlDocument.getRoots()) {
				try {
					findSymbolInformations(node, "", symbols, (node.isDoctype() && isDTD), filter, hasFilterForAttr,
							cancelChecker);
				} catch (BadLocationException e) {
					LOGGER.log(Level.SEVERE,
							"XMLSymbolsProvider#findSymbolInformations was given a BadLocation by a 'node' variable",
							e);
				}
			}
		} catch (ResultLimitExceededException e) {
			symbols.setResultLimitExceeded(true);
		}
		return symbols;
	}

	private void findSymbolInformations(DOMNode node, String container, List<SymbolInformation> symbols,
			boolean ignoreNode, XMLSymbolFilter filter, boolean hasFilterForAttr, CancelChecker cancelChecker)
			throws BadLocationException {
		if (!isNodeSymbol(node, filter)) {
			return;
		}
		String name = "";
		if (!ignoreNode) {
			name = nodeToName(node, filter);
			DOMDocument xmlDocument = node.getOwnerDocument();
			Range range = getSymbolRange(node);
			Location location = new Location(xmlDocument.getDocumentURI(), range);
			SymbolInformation symbol = new SymbolInformation(name, getSymbolKind(node), location, container);
			symbols.add(symbol);
		}
		final String containerName = name;
		if (node.isElement()) {
			boolean collectAttributes = hasFilterForAttr && node.hasAttributes();
			if (collectAttributes) {
				// Collect attributes from the DOM element
				for (DOMAttr attr : node.getAttributeNodes()) {
					findSymbolInformations(attr, containerName, symbols, false, filter, hasFilterForAttr,
							cancelChecker);
				}
			}
		}
		node.getChildren().forEach(child -> {
			try {
				findSymbolInformations(child, containerName, symbols, false, filter, hasFilterForAttr, cancelChecker);
			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE, "XMLSymbolsProvider was given a BadLocation by the provided 'node' variable",
						e);
			}
		});
	}

	// -------------- Document symbols

	public DocumentSymbolsResult findDocumentSymbols(DOMDocument xmlDocument, XMLSymbolSettings symbolSettings,
			CancelChecker cancelChecker) {
		AtomicLong limit = symbolSettings.getMaxItemsComputed() >= 0
				? new AtomicLong(symbolSettings.getMaxItemsComputed())
				: null;
		DocumentSymbolsResult symbols = new DocumentSymbolsResult(limit);
		XMLSymbolFilter filter = symbolSettings.getFilterFor(xmlDocument.getDocumentURI());

		try {
			// Process symbols participants
			if (processSymbolsParticipants(xmlDocument, null, symbols, filter, cancelChecker)) {
				return symbols;
			}

			// Process default symbol providers
			boolean isDTD = xmlDocument.isDTD();
			boolean hasFilterForAttr = filter.hasFilterFor(MatcherType.ATTRIBUTE);
			List<DOMNode> nodesToIgnore = new ArrayList<>();
			xmlDocument.getRoots().forEach(node -> {
				try {
					if ((node.isDoctype() && isDTD)) {
						nodesToIgnore.add(node);
					}
					findDocumentSymbols(node, symbols, nodesToIgnore, filter, hasFilterForAttr, cancelChecker);
				} catch (BadLocationException e) {
					LOGGER.log(Level.SEVERE,
							"XMLSymbolsProvider#findDocumentSymbols was given a BadLocation by a 'node' variable", e);
				}
			});
		} catch (ResultLimitExceededException e) {
			symbols.setResultLimitExceeded(true);
		}
		return symbols;
	}

	private void findDocumentSymbols(DOMNode node, DocumentSymbolsResult symbols, List<DOMNode> nodesToIgnore,
			XMLSymbolFilter filter, boolean hasFilterForAttr, CancelChecker cancelChecker) throws BadLocationException {
		if (!isNodeSymbol(node, filter)) {
			return;
		}
		cancelChecker.checkCanceled();

		boolean hasChildNodes = node.hasChildNodes();
		DocumentSymbolsResult childrenSymbols = symbols;
		if (nodesToIgnore == null || !nodesToIgnore.contains(node)) {
			String name;
			Range selectionRange;

			if (nodesToIgnore != null && node.isDTDAttListDecl()) { // attlistdecl with no elementdecl references
				DTDAttlistDecl decl = (DTDAttlistDecl) node;
				name = decl.getElementName();
				selectionRange = getSymbolRange(node, true);
			} else { // regular node
				name = nodeToName(node, filter);
				selectionRange = getSymbolRange(node);
			}
			boolean collectAttributes = hasFilterForAttr && node.hasAttributes();
			Range range = selectionRange;
			childrenSymbols = hasChildNodes || node.isDTDElementDecl() || node.isDTDAttListDecl() || collectAttributes
					? symbols.createList()
					: DocumentSymbolsResult.EMPTY_LIMITLESS_LIST;
			DocumentSymbol symbol = new DocumentSymbol(name, getSymbolKind(node), range, selectionRange, null,
					childrenSymbols);
			symbols.add(symbol);

			if (node.isElement()) {
				if (collectAttributes) {
					// Collect attributes from the DOM element
					for (DOMAttr attr : node.getAttributeNodes()) {
						findDocumentSymbols(attr, childrenSymbols, null, filter, hasFilterForAttr, cancelChecker);
					}
				}
			} else {
				if (node.isDTDElementDecl() || (nodesToIgnore != null && node.isDTDAttListDecl())) {
					// In the case of DTD ELEMENT we try to add in the children the DTD ATTLIST
					Collection<DOMNode> attlistDecls;
					if (node.isDTDElementDecl()) {
						DTDElementDecl elementDecl = (DTDElementDecl) node;
						String elementName = elementDecl.getName();
						attlistDecls = node.getOwnerDocument().findDTDAttrList(elementName);
					} else {
						attlistDecls = new ArrayList<>();
						attlistDecls.add(node);
					}

					for (DOMNode attrDecl : attlistDecls) {
						findDocumentSymbols(attrDecl, childrenSymbols, null, filter, hasFilterForAttr, cancelChecker);
						if (attrDecl instanceof DTDAttlistDecl) {
							DTDAttlistDecl decl = (DTDAttlistDecl) attrDecl;
							List<DTDAttlistDecl> otherAttributeDecls = decl.getInternalChildren();
							if (otherAttributeDecls != null) {
								for (DTDAttlistDecl internalDecl : otherAttributeDecls) {
									findDocumentSymbols(internalDecl, childrenSymbols, null, filter, hasFilterForAttr,
											cancelChecker);
								}
							}
						}
						nodesToIgnore.add(attrDecl);
					}
				}
			}
		}
		if (!hasChildNodes) {
			return;
		}
		final DocumentSymbolsResult childrenOfChild = childrenSymbols;
		node.getChildren().forEach(child -> {
			try {
				findDocumentSymbols(child, childrenOfChild, nodesToIgnore, filter, hasFilterForAttr, cancelChecker);
			} catch (BadLocationException e) {
				LOGGER.log(Level.SEVERE, "XMLSymbolsProvider was given a BadLocation by the provided 'node' variable",
						e);
			}
		});
	}

	private boolean isNodeSymbol(DOMNode node, XMLSymbolFilter filter) {
		return !node.isText() && filter.isNodeSymbol(node);
	}

	private static Range getSymbolRange(DOMNode node) throws BadLocationException {
		return getSymbolRange(node, false);
	}

	private static Range getSymbolRange(DOMNode node, boolean useAttlistElementName) throws BadLocationException {
		Position start;
		Position end;
		DOMDocument xmlDocument = node.getOwnerDocument();

		if (node.isDTDAttListDecl() && !useAttlistElementName) {
			DTDAttlistDecl attlistDecl = (DTDAttlistDecl) node;
			DTDDeclParameter attributeNameDecl = attlistDecl.attributeName;

			if (attributeNameDecl != null) {
				start = xmlDocument.positionAt(attributeNameDecl.getStart());
				end = xmlDocument.positionAt(attributeNameDecl.getEnd());
				return new Range(start, end);
			}
		}
		start = xmlDocument.positionAt(node.getStart());
		end = xmlDocument.positionAt(node.getEnd());
		return new Range(start, end);
	}

	private static SymbolKind getSymbolKind(DOMNode node) {
		if (node.isProcessingInstruction() || node.isProlog()) {
			return SymbolKind.Property;
		} else if (node.isDoctype()) {
			return SymbolKind.Struct;
		} else if (node.isDTDElementDecl()) {
			return SymbolKind.Property;
		} else if (node.isDTDEntityDecl()) {
			return SymbolKind.Namespace;
		} else if (node.isDTDAttListDecl()) {
			return SymbolKind.Key;
		} else if (node.isDTDNotationDecl()) {
			return SymbolKind.Variable;
		} else if (node.isAttribute()) {
			return SymbolKind.Constant;
		}
		return SymbolKind.Field;
	}

	private static String nodeToName(DOMNode node, XMLSymbolFilter filter) {
		String name = nodeToNameOrNull(node, filter);
		return name != null ? name : "?";
	}

	private static String nodeToNameOrNull(DOMNode node, XMLSymbolFilter filter) {
		if (node.isElement()) {
			DOMElement element = (DOMElement) node;
			if (element.hasTagName()) {
				DOMNode firstChild = node.getFirstChild();
				if (firstChild != null && firstChild.isText() && filter.isNodeSymbol(firstChild)) {
					return element.getTagName() + ": " + firstChild.getNodeValue();
				}
				return element.getTagName();
			}
		} else if (node.isAttribute()) {
			DOMAttr attr = (DOMAttr) node;
			if (attr.getName() != null) {
				return "@" + attr.getName() + ": " + attr.getValue();
			}
		} else if (node.isProcessingInstruction() || node.isProlog()) {
			return ((ProcessingInstruction) node).getTarget();
		} else if (node.isDoctype()) {
			return "DOCTYPE:" + ((DocumentType) node).getName();
		} else if (node.isDTDElementDecl()) {
			return ((DTDElementDecl) node).getName();
		} else if (node.isDTDAttListDecl()) {
			DTDAttlistDecl attr = (DTDAttlistDecl) node;
			return attr.getAttributeName();
		} else if (node.isDTDEntityDecl()) {
			return node.getNodeName();
		} else if (node.isDTDNotationDecl()) {
			DTDNotationDecl notation = (DTDNotationDecl) node;
			return notation.getName();
		}

		return null;
	}

	private boolean processSymbolsParticipants(DOMDocument xmlDocument, SymbolInformationResult symbolInformations,
			DocumentSymbolsResult documentSymbols, XMLSymbolFilter filter, CancelChecker cancelChecker) {
		// Get symbol providers participants
		SymbolsProviderParticipantResult resultParticipant = getSymbolsProviderParticipant(xmlDocument);

		// Process replace symbol providers participants
		Collection<ISymbolsProviderParticipant> replaceParticipants = resultParticipant.getReplaceParticipants();
		if (!replaceParticipants.isEmpty()) {
			boolean successfulReplace = false;
			if (replaceParticipants.size() > 1) {
				LOGGER.log(Level.WARNING, "There are '" + replaceParticipants.size() + "' replace participants");
			}
			for (ISymbolsProviderParticipant replaceParticipant : replaceParticipants) {
				try {
					if (symbolInformations != null) {
						replaceParticipant.findSymbolInformations(xmlDocument, symbolInformations, filter, cancelChecker);
					}
					if (documentSymbols != null) {
						replaceParticipant.findDocumentSymbols(xmlDocument, documentSymbols, filter, cancelChecker);
					}
					successfulReplace = true;
				} catch (CancellationException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE,
							"Error while processing symbols for '" + replaceParticipant.getClass().getName() + "'.", e);
				}
			}
			// If at least 1 replace participant didn't crash, use the replace result;
			// otherwise just use the default
			if (successfulReplace) {
				return true;
			}
		}

		// Process insert symbol providers participants
		Collection<ISymbolsProviderParticipant> insertParticipants = resultParticipant.getInsertParticipants();
		if (!insertParticipants.isEmpty()) {
			for (ISymbolsProviderParticipant insertParticipant : insertParticipants) {
				try {
					if (symbolInformations != null) {
						insertParticipant.findSymbolInformations(xmlDocument, symbolInformations, filter, cancelChecker);
					}
					if (documentSymbols != null) {
						insertParticipant.findDocumentSymbols(xmlDocument, documentSymbols, filter, cancelChecker);
					}
				} catch (CancellationException e) {
					throw e;
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE,
							"Error while processing symbols for the participant '"
									+ insertParticipant.getClass().getName() + "'.",
							e);
				}
			}
		}
		return false;
	}

	private SymbolsProviderParticipantResult getSymbolsProviderParticipant(DOMDocument xmlDocument) {
		Collection<ISymbolsProviderParticipant> all = extensionsRegistry.getSymbolsProviderParticipants();
		Collection<ISymbolsProviderParticipant> insertParticipant = null;
		Collection<ISymbolsProviderParticipant> replaceParticipant = null;
		for (ISymbolsProviderParticipant participant : all) {
			SymbolStrategy strategy = participant.applyFor(xmlDocument);
			switch (strategy) {
			case INSERT:
				if (insertParticipant == null) {
					insertParticipant = new ArrayList<>();
				}
				insertParticipant.add(participant);
				break;
			case REPLACE:
				if (replaceParticipant == null) {
					replaceParticipant = new ArrayList<>();
				}
				replaceParticipant.add(participant);
			default:
				// Do nothing
			}
		}
		return new SymbolsProviderParticipantResult(replaceParticipant, insertParticipant);
	}
}
