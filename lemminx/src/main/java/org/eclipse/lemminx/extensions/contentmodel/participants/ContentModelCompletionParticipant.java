/**
 *  Copyright (c) 2018-2020 Angelo ZERR
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
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.model.CMAttributeDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.CMElementDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.utils.XMLGenerator;
import org.eclipse.lemminx.services.AttributeCompletionItem;
import org.eclipse.lemminx.services.extensions.CompletionParticipantAdapter;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lemminx.uriresolver.CacheResourceDownloadingException;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Extension to support XML completion based on content model (XML Schema
 * completion, etc)
 */
public class ContentModelCompletionParticipant extends CompletionParticipantAdapter {

	@Override
	public void onTagOpen(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
		try {
			DOMDocument document = request.getXMLDocument();
			ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
			DOMElement parentElement = request.getParentElement();
			if (parentElement == null) {
				// XML is empty, in case of XML file associations, a XML Schema/DTD can be bound
				// check if it's root element (in the case of XML file associations, the link to
				// XML Schema is done with pattern and not with XML root element)
				Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(document, null);
				for (CMDocument cmDocument : cmDocuments) {
					fillWithChildrenElementDeclaration(null, null, cmDocument.getElements(), null, false, request,
							response);
				}
				return;
			}
			// Try to retrieve XML Schema/DTD element declaration for the parent element
			// where completion was triggered.
			Collection<CMDocument> cmRootDocuments = contentModelManager.findCMDocument(parentElement,
					parentElement.getNamespaceURI());

			String defaultPrefix = null;
			for (CMDocument cmDocument : cmRootDocuments) {
				CMElementDeclaration cmElement = cmDocument.findCMElement(parentElement,
						parentElement.getNamespaceURI());
				if (cmElement != null) {
					defaultPrefix = parentElement.getPrefix();
					fillWithPossibleElementDeclaration(parentElement, cmElement, defaultPrefix, contentModelManager,
							request, response);
				}
			}
			if (parentElement.isDocumentElement()) {
				// completion on root document element
				Collection<String> prefixes = parentElement.getAllPrefixes();
				for (String prefix : prefixes) {
					if (defaultPrefix != null && prefix.equals(defaultPrefix)) {
						continue;
					}
					String namespaceURI = parentElement.getNamespaceURI(prefix);
					if (!hasNamespace(namespaceURI, cmRootDocuments)) {
						// The model document root doesn't define the namespace, try to load the
						// external model document (XML Schema, DTD)
						Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(parentElement,
								namespaceURI);
						for (CMDocument cmDocument : cmDocuments) {
							fillWithChildrenElementDeclaration(parentElement, null, cmDocument.getElements(), prefix,
									true, request, response);
						}
					}
				}
			}
		} catch (CacheResourceDownloadingException e) {
			// XML Schema, DTD is loading, ignore this error
		}
	}

	private boolean hasNamespace(String namespaceURI, Collection<CMDocument> cmRootDocuments) {
		if (cmRootDocuments.isEmpty()) {
			return false;
		}
		for (CMDocument cmDocument : cmRootDocuments) {
			if (cmDocument.hasNamespace(namespaceURI)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fill with possible element declarations.
	 * 
	 * @param parentElement       the parent DOM element
	 * @param cmElement           the content model element declaration
	 * @param defaultPrefix
	 * @param contentModelManager
	 * @param request
	 * @param response
	 * @throws BadLocationException
	 */
	private static void fillWithPossibleElementDeclaration(DOMElement parentElement, CMElementDeclaration cmElement,
			String defaultPrefix, ContentModelManager contentModelManager, ICompletionRequest request,
			ICompletionResponse response) throws BadLocationException {
		// Get possible elements
		Collection<CMElementDeclaration> possibleElements = cmElement.getPossibleElements(parentElement,
				request.getOffset());
		boolean isAny = CMElementDeclaration.ANY_ELEMENT_DECLARATIONS.equals(possibleElements);
		Collection<CMDocument> cmDocuments = null;
		if (isAny) {
			// It's a xs:any, get the XML Schema/DTD document to retrieve the all elements
			// declarations
			cmDocuments = contentModelManager.findCMDocument(parentElement);
		}
		fillWithChildrenElementDeclaration(parentElement, cmDocuments, possibleElements, defaultPrefix, false, request,
				response);
	}

	/**
	 * Fill with children element declarations
	 * 
	 * @param element
	 * @param cmDocument
	 * @param cmElements
	 * @param defaultPrefix
	 * @param forceUseOfPrefix
	 * @param request
	 * @param response
	 * @throws BadLocationException
	 */
	private static void fillWithChildrenElementDeclaration(DOMElement element, Collection<CMDocument> cmDocuments,
			Collection<CMElementDeclaration> cmElements, String defaultPrefix, boolean forceUseOfPrefix,
			ICompletionRequest request, ICompletionResponse response) throws BadLocationException {
		XMLGenerator generator = request.getXMLGenerator();
		if (cmDocuments != null) {
			// xs:any case
			Set<String> tags = new HashSet<>();

			// Fill with all element declarations from the XML Schema/DTD document
			Set<CMElementDeclaration> processedElements = new HashSet<>();
			for (CMDocument cmDocument : cmDocuments) {
				Collection<CMElementDeclaration> elements = cmDocument.getElements();
				fillCompletionItem(elements, element, defaultPrefix, forceUseOfPrefix, request, response, generator,
						tags, processedElements);
			}
			// Fill with all element tags from the DOM document
			Document document = element.getOwnerDocument();
			NodeList list = document.getChildNodes();
			addTagName(list, tags, request, response);
		} else {
			for (CMElementDeclaration child : cmElements) {
				addCompletionItem(child, element, defaultPrefix, forceUseOfPrefix, request, response, generator, null);
			}
		}
	}

	private static void fillCompletionItem(Collection<CMElementDeclaration> elements, DOMElement element,
			String defaultPrefix, boolean forceUseOfPrefix, ICompletionRequest request, ICompletionResponse response,
			XMLGenerator generator, Set<String> tags, Set<CMElementDeclaration> processedElements) {
		for (CMElementDeclaration child : elements) {
			if (!processedElements.contains(child)) {
				processedElements.add(child);
				addCompletionItem(child, element, defaultPrefix, forceUseOfPrefix, request, response, generator, tags);
				fillCompletionItem(child.getElements(), element, defaultPrefix, forceUseOfPrefix, request, response,
						generator, tags, processedElements);
			}
		}
	}

	/**
	 * Add completion item with all tag names of the node list.
	 * 
	 * @param list
	 * @param tags
	 * @param request
	 * @param response
	 */
	private static void addTagName(NodeList list, Set<String> tags, ICompletionRequest request,
			ICompletionResponse response) {
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (Node.ELEMENT_NODE == node.getNodeType()) {
				DOMElement elt = (DOMElement) node;
				if (elt.hasTagName()) {
					String tagName = elt.getTagName();
					if (!tags.contains(tagName)) {
						CompletionItem item = new CompletionItem(tagName);
						item.setKind(CompletionItemKind.Property);
						item.setFilterText(request.getFilterForStartTagName(tagName));
						String xml = elt.getOwnerDocument().getText().substring(elt.getStart(), elt.getEnd());
						item.setTextEdit(Either.forLeft(new TextEdit(request.getReplaceRange(), xml)));
						response.addCompletionItem(item);
						tags.add(item.getLabel());
					}
				}
				addTagName(elt.getChildNodes(), tags, request, response);
			}
		}
	}

	private static void addCompletionItem(CMElementDeclaration elementDeclaration, DOMElement parentElement,
			String defaultPrefix, boolean forceUseOfPrefix, ICompletionRequest request, ICompletionResponse response,
			XMLGenerator generator, Set<String> tags) {
		String prefix = forceUseOfPrefix ? defaultPrefix
				: (parentElement != null ? parentElement.getPrefix(elementDeclaration.getNamespace()) : null);
		String label = elementDeclaration.getName(prefix);
		if (tags != null) {
			if (tags.contains(label)) {
				return;
			} else {
				tags.add(label);
			}
		}

		CompletionItem item = new CompletionItem(label);
		item.setFilterText(request.getFilterForStartTagName(label));
		item.setKind(CompletionItemKind.Property);
		MarkupContent documentation = XMLGenerator.createMarkupContent(elementDeclaration, request);
		item.setDocumentation(documentation);
		String xml = generator.generate(elementDeclaration, prefix,
				isGenerateEndTag(request.getNode(), request.getOffset(), label));
		item.setTextEdit(Either.forLeft(new TextEdit(request.getReplaceRange(), xml)));
		item.setInsertTextFormat(InsertTextFormat.Snippet);
		response.addCompletionItem(item, true);
	}

	private static boolean isGenerateEndTag(DOMNode node, int offset, String tagName) {
		if (node == null) {
			return true;
		}
		return node.getOrphanEndElement(offset, tagName) == null;
	}

	@Override
	public void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception {
		// otherwise, manage completion based on XML Schema, DTD.
		DOMElement parentElement = request.getNode().isElement() ? (DOMElement) request.getNode() : null;
		if (parentElement == null) {
			return;
		}
		try {
			Range fullRange = request.getReplaceRange();
			boolean canSupportSnippet = request.isCompletionSnippetsSupported();
			ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
			// Completion on attribute name based on
			// - internal grammar (ex: DOCTYPE with subset)
			// - external grammar (ex : DOCTYPE SYSTEM, xs:schemaLocation, etc)
			Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(parentElement);
			for (CMDocument cmDocument : cmDocuments) {
				CMElementDeclaration cmElement = cmDocument.findCMElement(parentElement,
						parentElement.getNamespaceURI());
				if (cmElement != null) {
					fillAttributesWithCMAttributeDeclarations(parentElement, fullRange, cmElement, canSupportSnippet,
							generateValue, request, response);
				}
			}
		} catch (CacheResourceDownloadingException e) {
			// XML Schema, DTD is loading, ignore this error
		}
	}

	private void fillAttributesWithCMAttributeDeclarations(DOMElement parentElement, Range fullRange,
			CMElementDeclaration cmElement, boolean canSupportSnippet, boolean generateValue,
			ICompletionRequest request, ICompletionResponse response) {

		Collection<CMAttributeDeclaration> attributes = cmElement.getAttributes();
		if (attributes == null) {
			return;
		}
		for (CMAttributeDeclaration cmAttribute : attributes) {
			String attrName = cmAttribute.getName();
			if (!parentElement.hasAttribute(attrName)) {
				CompletionItem item = new AttributeCompletionItem(attrName, canSupportSnippet, fullRange, generateValue,
						cmAttribute.getDefaultValue(), cmAttribute.getEnumerationValues(), request.getSharedSettings());
				MarkupContent documentation = XMLGenerator.createMarkupContent(cmAttribute, cmElement, request);
				item.setDocumentation(documentation);
				response.addCompletionAttribute(item);
			}
		}
	}

	@Override
	public void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker)
			throws Exception {
		DOMElement parentElement = request.getNode().isElement() ? (DOMElement) request.getNode() : null;
		if (parentElement == null) {
			return;
		}
		try {
			ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
			// Completion on attribute value based on
			// - internal grammar (ex: DOCTYPE with subset)
			// - external grammar (ex : DOCTYPE SYSTEM, xs:schemaLocation, etc)
			Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(parentElement);
			for (CMDocument cmDocument : cmDocuments) {
				CMElementDeclaration cmElement = cmDocument.findCMElement(parentElement,
						parentElement.getNamespaceURI());
				if (cmElement != null) {
					fillAttributeValuesWithCMAttributeDeclarations(cmElement, request, response);
				}
			}
		} catch (CacheResourceDownloadingException e) {
			// XML Schema, DTD is loading, ignore this error
		}
	}

	private void fillAttributeValuesWithCMAttributeDeclarations(CMElementDeclaration cmElement,
			ICompletionRequest request, ICompletionResponse response) {
		String attributeName = request.getCurrentAttributeName();
		CMAttributeDeclaration cmAttribute = cmElement.findCMAttribute(attributeName);
		if (cmAttribute != null) {
			Range fullRange = request.getReplaceRange();
			cmAttribute.getEnumerationValues().forEach(value -> {
				CompletionItem item = new CompletionItem();
				item.setLabel(value);
				String insertText = request.getInsertAttrValue(value);
				item.setLabel(value);
				item.setKind(CompletionItemKind.Value);
				item.setFilterText(insertText);
				item.setTextEdit(Either.forLeft(new TextEdit(fullRange, insertText)));
				MarkupContent documentation = XMLGenerator.createMarkupContent(cmAttribute, value, cmElement, request);
				item.setDocumentation(documentation);
				response.addCompletionItem(item);
			});
		}
	}

	@Override
	public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
		try {
			ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
			DOMElement parentElement = request.getParentElement();
			if (parentElement != null) {
				Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(parentElement);
				for (CMDocument cmDocument : cmDocuments) {
					CMElementDeclaration cmElement = cmDocument.findCMElement(parentElement,
							parentElement.getNamespaceURI());
					Collection<String> values = cmElement != null ? cmElement.getEnumerationValues()
							: Collections.emptyList();
					if (!values.isEmpty()) {
						// Completion for xs:enumeration inside Element Text node
						DOMDocument document = parentElement.getOwnerDocument();
						int startOffset = parentElement.getStartTagCloseOffset() + 1;
						Position start = parentElement.getOwnerDocument().positionAt(startOffset);
						Position end = request.getPosition();
						int endOffset = parentElement.getEndTagOpenOffset();
						if (endOffset > 0) {
							end = document.positionAt(endOffset);
						}
						int completionOffset = request.getOffset();
						String tokenStart = StringUtils.getWhitespaces(document.getText(), startOffset,
								completionOffset);
						Range fullRange = new Range(start, end);
						values.forEach(value -> {
							CompletionItem item = new CompletionItem();
							item.setLabel(value);
							String insertText = value;
							item.setLabel(value);
							item.setKind(CompletionItemKind.Value);
							item.setFilterText(tokenStart + insertText);
							item.setTextEdit(Either.forLeft(new TextEdit(fullRange, insertText)));
							MarkupContent documentation = XMLGenerator.createMarkupContent(cmElement, value, request);
							item.setDocumentation(documentation);
							response.addCompletionItem(item);
						});
					}
				}
			}
		} catch (CacheResourceDownloadingException e) {
			// XML Schema, DTD is loading, ignore this error
		}
	}
}
