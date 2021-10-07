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
package org.eclipse.lemminx.extensions.contentmodel.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.lemminx.commons.SnippetsBuilder;
import org.eclipse.lemminx.extensions.contentmodel.model.CMAttributeDeclaration;
import org.eclipse.lemminx.extensions.contentmodel.model.CMElementDeclaration;
import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.MarkupContentFactory;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLBuilder;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;

/**
 * XML generator used to generate an XML fragment with formatting from a given
 * element declaration (XML Schema element declaration, DTD element, etc).
 */
public class XMLGenerator {

	private final SharedSettings sharedSettings;
	private final String whitespacesIndent;
	private final String lineDelimiter;
	private final boolean canSupportSnippets;
	private final boolean autoCloseTags;
	private int maxLevel;

	/**
	 * XML generator constructor.
	 * 
	 * @param sharedSettings     the settings containing formatting options (uses spaces or tabs for
	 *                           indentation, etc) and preferences
	 * @param whitespacesIndent  the whitespaces to use to indent XML children
	 *                           elements.
	 * @param lineDelimiter      the line delimiter to use when several XML elements
	 *                           must be generated.
	 * @param canSupportSnippets true if snippets can be supported and false
	 *                           otherwise.
	 */
	public XMLGenerator(SharedSettings sharedSettings,
			String whitespacesIndent, String lineDelimiter, boolean canSupportSnippets, int maxLevel) {
		this(sharedSettings, true, whitespacesIndent, lineDelimiter, canSupportSnippets, maxLevel);
	}

	public XMLGenerator(SharedSettings sharedSettings,
			boolean autoCloseTags, String whitespacesIndent,
			String lineDelimiter, boolean canSupportSnippets, int maxLevel) {
		this.sharedSettings = sharedSettings;
		this.autoCloseTags = autoCloseTags;
		this.whitespacesIndent = whitespacesIndent;
		this.lineDelimiter = lineDelimiter;
		this.canSupportSnippets = canSupportSnippets;
	}

	/**
	 * Returns the XML generated from the given element declaration.
	 * 
	 * @param elementDeclaration
	 * @param prefix
	 * @return the XML generated from the given element declaration.
	 */
	public String generate(CMElementDeclaration elementDeclaration, String prefix, boolean generateEndTag) {
		XMLBuilder xml = new XMLBuilder(sharedSettings, whitespacesIndent, lineDelimiter);
		generate(elementDeclaration, prefix, generateEndTag, 0, 0, xml, new ArrayList<CMElementDeclaration>());
		if (canSupportSnippets) {
			xml.addContent(SnippetsBuilder.tabstops(0)); // "$0"
		}
		return xml.toString();
	}

	private int generate(CMElementDeclaration elementDeclaration, String prefix, boolean generateEndTag, int level, int snippetIndex,
			XMLBuilder xml, List<CMElementDeclaration> generatedElements) {
		if (generatedElements.contains(elementDeclaration)) {
			return snippetIndex;
		}
		boolean autoCloseTags = this.autoCloseTags && generateEndTag;
		generatedElements.add(elementDeclaration);
		if (level > 0) {
			xml.linefeed();
			xml.indent(level);
		}
		xml.startElement(prefix, elementDeclaration.getName(), false);
		// Attributes
		Collection<CMAttributeDeclaration> attributes = elementDeclaration.getAttributes();
		snippetIndex = generate(attributes, level, snippetIndex, xml, elementDeclaration.getName());
		// Elements children
		Collection<CMElementDeclaration> children = elementDeclaration.getElements();
		if (children.size() > 0) {
			xml.closeStartElement();
			if ((level > maxLevel)) {
				level++;
				for (CMElementDeclaration child : children) {
					snippetIndex = generate(child, prefix, true, level, snippetIndex, xml, generatedElements);
				}
				level--;
				xml.linefeed();
				xml.indent(level);
			} else {
				if (canSupportSnippets) {
					snippetIndex++;
					xml.addContent(SnippetsBuilder.tabstops(snippetIndex));
				}
			}
			if (autoCloseTags) {
				xml.endElement(prefix, elementDeclaration.getName());
			}
		} else if (elementDeclaration.isEmpty() && autoCloseTags) {
			xml.selfCloseElement();
		} else {
			xml.closeStartElement();
			Collection<String> values = elementDeclaration.getEnumerationValues();
			if (!values.isEmpty()) {
				// The Element Text node has xs:enumeration.
				if (canSupportSnippets) {
					// Generate LSP choice.
					// Ex : <skill>${1|Java,Node,XML|}$2</skill>$0"
					snippetIndex++;
					xml.addContent(SnippetsBuilder.choice(snippetIndex, values));
				} else {
					// Generate the first item
					// Ex : <skill>Java</skill>"
					xml.addContent(values.iterator().next());
				}
			}
			if (canSupportSnippets) {
				snippetIndex++;
				xml.addContent(SnippetsBuilder.tabstops(snippetIndex));
			}
			if (autoCloseTags) {
				xml.endElement(prefix, elementDeclaration.getName());
			}
		}
		return snippetIndex;
	}

	public String generate(Collection<CMAttributeDeclaration> attributes, String tagName) {
		XMLBuilder xml = new XMLBuilder(sharedSettings, whitespacesIndent, lineDelimiter);
		generate(attributes, 0, 0, xml, tagName);
		return xml.toString();
	}

	private int generate(Collection<CMAttributeDeclaration> attributes, int level, int snippetIndex, XMLBuilder xml,
			String tagName) {
		List<CMAttributeDeclaration> requiredAttributes = new ArrayList<>();
		for (CMAttributeDeclaration att : attributes) {
			if (att.isRequired()) {
				requiredAttributes.add(att);
			}
		}
		int attributesSize = requiredAttributes.size();
		for (CMAttributeDeclaration attributeDeclaration : requiredAttributes) {
			if (canSupportSnippets) {
				snippetIndex++;
			}
			String defaultValue = attributeDeclaration.getDefaultValue();
			Collection<String> enumerationValues = attributeDeclaration.getEnumerationValues();
			String value = generateAttributeValue(defaultValue, enumerationValues, canSupportSnippets, snippetIndex,
					false, sharedSettings);
			if (attributesSize != 1) {
				xml.addAttribute(attributeDeclaration.getName(), value, level, true);
			} else {
				xml.addSingleAttribute(attributeDeclaration.getName(), value, true);
			}
		}
		return snippetIndex;
	}

	/**
	 * Creates the string value for a CompletionItem TextEdit
	 * 
	 * Can create an enumerated TextEdit if given a collection of values.
	 */
	public static String generateAttributeValue(String defaultValue, Collection<String> enumerationValues,
			boolean canSupportSnippets, int snippetIndex, boolean withQuote, SharedSettings sharedSettings) {
		StringBuilder value = new StringBuilder();
		String quotation = sharedSettings.getPreferences().getQuotationAsString();
		if (withQuote) {
			value.append("=").append(quotation);
		}
		if (!canSupportSnippets) {
			if (defaultValue != null) {
				value.append(defaultValue);
			}
		} else {
			// Snippets syntax support
			if (enumerationValues != null && !enumerationValues.isEmpty()) {
				SnippetsBuilder.choice(snippetIndex, enumerationValues, value);
			} else {
				if (defaultValue != null) {
					SnippetsBuilder.placeholders(snippetIndex, defaultValue, value);
				} else {
					SnippetsBuilder.tabstops(snippetIndex, value);
				}
			}
		}
		if (withQuote) {
			value.append(quotation);
			if (canSupportSnippets) {
				SnippetsBuilder.tabstops(0, value); // "$0"
			}
		}
		return value.toString();
	}

	/**
	 * Returns a properly formatted documentation string with source.
	 * 
	 * If there is no content then null is returned.
	 * 
	 * @param documentation
	 * @param schemaURI
	 * @return
	 */
	public static String generateDocumentation(String documentation, String schemaURI, boolean html) {
		if (StringUtils.isBlank(documentation)) {
			return null;
		}

		StringBuilder doc = new StringBuilder(documentation);

		if (schemaURI != null) {
			doc.append(System.lineSeparator());
			doc.append(System.lineSeparator());
			if (html) {
				doc.append("<p>");
			}
			doc.append("Source: ");
			if (html) {
				doc.append("<a href=\"");
				doc.append(schemaURI);
				doc.append("\">");
			}
			doc.append(getFileName(schemaURI));
			if (html) {
				doc.append("</a>");
				doc.append("</p>");
			}
		}
		return doc.toString();
	}

	/**
	 * Returns the file name from the given schema URI
	 * 
	 * @param schemaURI the schema URI
	 * @return the file name from the given schema URI
	 */
	private static String getFileName(String schemaURI) {
		int index = schemaURI.lastIndexOf('/');
		if (index == -1) {
			index = schemaURI.lastIndexOf('\\');
		}
		if (index == -1) {
			return schemaURI;
		}
		return schemaURI.substring(index + 1, schemaURI.length());
	}

	/**
	 * Returns a markup content for element documentation and null otherwise.
	 * 
	 * @param cmElement
	 * @param support
	 * @return a markup content for element documentation and null otherwise.
	 */
	public static MarkupContent createMarkupContent(CMElementDeclaration cmElement, ISharedSettingsRequest support) {
		String documentation = XMLGenerator.generateDocumentation(cmElement.getDocumentation(support),
				cmElement.getDocumentURI(), support.canSupportMarkupKind(MarkupKind.MARKDOWN));
		if (documentation != null) {
			return MarkupContentFactory.createMarkupContent(documentation, MarkupKind.MARKDOWN, support);
		}
		return null;
	}

	/**
	 * Returns a markup content for attribute name documentation and null otherwise.
	 * 
	 * @param cmAttribute  the attribute declaration
	 * @param ownerElement the owner element declaration
	 * @param request      the request
	 * @return a markup content for attribute name documentation and null otherwise.
	 */
	public static MarkupContent createMarkupContent(CMAttributeDeclaration cmAttribute,
			CMElementDeclaration ownerElement, ISharedSettingsRequest request) {
		String documentation = XMLGenerator.generateDocumentation(cmAttribute.getAttributeNameDocumentation(request),
				ownerElement.getDocumentURI(), request.canSupportMarkupKind(MarkupKind.MARKDOWN));
		if (documentation != null) {
			return MarkupContentFactory.createMarkupContent(documentation, MarkupKind.MARKDOWN, request);
		}
		return null;
	}

	/**
	 * Returns a markup content for attribute value documentation and null
	 * otherwise.
	 * 
	 * @param cmAttribute
	 * @param attributeValue
	 * @param ownerElement
	 * @param support
	 * @return a markup content for attribute value documentation and null
	 *         otherwise.
	 */
	public static MarkupContent createMarkupContent(CMAttributeDeclaration cmAttribute, String attributeValue,
			CMElementDeclaration ownerElement, ISharedSettingsRequest support) {
		String documentation = XMLGenerator.generateDocumentation(cmAttribute.getAttributeValueDocumentation(attributeValue, support),
				ownerElement.getDocumentURI(), support.canSupportMarkupKind(MarkupKind.MARKDOWN));
		if (documentation != null) {
			return MarkupContentFactory.createMarkupContent(documentation, MarkupKind.MARKDOWN, support);
		}
		return null;
	}

	/**
	 * Returns a markup content for element text documentation and null otherwise.
	 * 
	 * @param cmElement   element declaration.
	 * @param textContent the text content.
	 * @param support     markup kind support.
	 * 
	 * @return a markup content for element text documentation and null otherwise.
	 */
	public static MarkupContent createMarkupContent(CMElementDeclaration cmElement, String textContent,
			ISharedSettingsRequest support) {
		String documentation = XMLGenerator.generateDocumentation(cmElement.getTextDocumentation(textContent, support),
				cmElement.getDocumentURI(), support.canSupportMarkupKind(MarkupKind.MARKDOWN));
		if (documentation != null) {
			return MarkupContentFactory.createMarkupContent(documentation, MarkupKind.MARKDOWN, support);
		}
		return null;
	}
}
