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
package org.eclipse.lemminx.extensions.generators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.services.IXMLFullFormatter;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract class to generate a grammar (XSD, DTD, etc) from a given XML source.
 * 
 * <p>
 * The process is to build a generic {@link Grammar} instance from the XML
 * source and each implementation uses this grammar information to generate the
 * proper grammar.
 * </p>
 * 
 * @param <T> the grammar generator settings.
 */
public abstract class AbstractXML2GrammarGenerator<T extends FileContentGeneratorSettings>
		implements IFileContentGenerator<Document, T> {

	@Override
	public String generate(Document prototypeDocument, SharedSettings sharedSettings, T generatorSettings,
			IXMLFullFormatter formatter) {
		// Generate the grammar from the XML source document
		String newText = doGenerate(prototypeDocument, sharedSettings, generatorSettings);
		if (formatter == null) {
			return newText;
		}
		// Format the generated grammar.
		return formatter.formatFull(newText, "grammar." + getFileExtension(), sharedSettings);
	}

	/**
	 * Returns the grammar file extension (ex : xsd, dtd) to generate.
	 * 
	 * @return the grammar file extension (ex : xsd, dtd) to generate.
	 */
	protected abstract String getFileExtension();

	private String doGenerate(Document sourceDocument, SharedSettings sharedSettings, T generatorSettings) {
		// Create the generic grammar information from the XML source document.
		Grammar grammar = createGrammar(sourceDocument, isFlat());
		XMLBuilder builder = new XMLBuilder(sharedSettings, "", "");
		// Generate the grammar content from the grammar information.
		generate(grammar, generatorSettings, builder);
		return builder.toString();
	}

	/**
	 * Returns true if element declaration must be stored as flat mode and false
	 * otherwise.
	 * 
	 * <ul>
	 * 
	 * <li>flat=true: helpful for DTD which declares <!ELEMENT without
	 * hierarchy.</li>
	 * <li>flat=false: helpful for XSD which declares xs:element with
	 * hierarchy.</li>
	 * </ul>
	 * 
	 * @return true if element declaration must be stored as flat mode and false
	 *         otherwise.
	 */
	protected boolean isFlat() {
		return false;
	}

	/**
	 * Generate the grammar content from the given grammar information into the
	 * given builder.
	 * 
	 * @param grammar         the grammar information.
	 * @param grammarSettings the grammar settings
	 * @param out             the XML builder to update.
	 */
	protected abstract void generate(Grammar grammar, T grammarSettings, XMLBuilder out);

	/**
	 * Create the grammar from the given XML document.
	 * 
	 * @param sourceDocument the XML source document.
	 * @param flat           flat mode
	 * 
	 * @return the grammar from the given XML document.
	 */
	private static Grammar createGrammar(Document sourceDocument, boolean flat) {
		Grammar grammar = new Grammar();
		// Update default namespace
		String defaultNamespace = null;
		Element documentElement = sourceDocument.getDocumentElement();
		if (documentElement != null) {
			defaultNamespace = documentElement.getAttribute(DOMAttr.XMLNS_ATTR);
		}
		grammar.setDefaultNamespace(defaultNamespace);
		// Update elements information
		fillElements(sourceDocument, grammar, grammar, flat);
		return grammar;
	}

	private static void fillElements(Node node, Grammar grammar, ContainerDeclaration parent, boolean flat) {
		NodeList children = node.getChildNodes();
		// Parent
		if (parent instanceof ElementDeclaration) {
			List<String> tags = new ArrayList<>();
			ElementDeclaration parentDecl = (ElementDeclaration) parent;
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) child;
					String localName = element.getLocalName();
					if (!StringUtils.isEmpty(localName)) {
						tags.add(localName);
					}
				}
			}
			parentDecl.addChildHierarchy(tags);
		}

		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) child;
				if (element.getLocalName() != null) {
					// Element has tag name.
					ElementDeclaration elementDecl = getElementDecl(grammar, parent, flat, element);
					// Update has text
					if (!elementDecl.hasCharacterContent()) {
						elementDecl.setHasCharacterContent(hasCharacterContent(element));
					}
					// Update element occurrences
					elementDecl.incrementOccurrences();
					// Collect attributes
					NamedNodeMap attributes = element.getAttributes();
					if (attributes != null) {
						for (int j = 0; j < attributes.getLength(); j++) {
							Attr attr = (Attr) attributes.item(j);
							if (!isIgnore(attr)) {
								// Attribute must be added in the grammar
								AttributeDeclaration attributeDecl = elementDecl.getAttribute(attr.getName());
								// Update attribute occurrences
								attributeDecl.incrementOccurrences();
								// Update attribute value
								attributeDecl.addValue(attr.getValue());
							}
						}
					}
					fillElements(element, grammar, elementDecl, flat);
				}
			}
		}
	}

	/**
	 * Returns true if the given attribute must be ignore and false otherwise.
	 * 
	 * @param attr the DOM attribute.
	 * 
	 * @return true if the given attribute must be ignore and false otherwise.o
	 */
	private static boolean isIgnore(Attr attr) {
		String name = attr.getName();
		if (StringUtils.isEmpty(name)) {
			// the attribute name is empty, ignore it
			return true;
		}
		if (DOMAttr.isXmlns(name)) {
			// the attribute is a XML namespace (xmlns="http://...."), ignore it
			return true;
		}
		if (name.indexOf(':') != -1) {
			// the attribute have prefix, ignore it (ex : xsi:schemaLocation).
			return true;
		}
		return false;
	}

	/**
	 * Returns the existing or new element declaration from the given DOM element.
	 * 
	 * @param grammar   the grammar.
	 * @param container the element declaration container.
	 * @param flat      true if element declaration must be stored as flat mode.
	 * @param element   the DOM element.
	 * 
	 * @return the existing or new element declaration from the given DOM element.
	 */
	private static ElementDeclaration getElementDecl(Grammar grammar, ContainerDeclaration container, boolean flat,
			Element element) {
		String name = element.getLocalName();
		if (flat) {
			ElementDeclaration elementDecl = grammar.getElement(name);
			container.addElement(elementDecl);
			return elementDecl;
		}
		return container.getElement(name);
	}

	/**
	 * Returns true if the given DOM element has text content and false otherwise.
	 * 
	 * @param element the DOM element.
	 * @return true if the given DOM element has text content and false otherwise.
	 */
	private static boolean hasCharacterContent(Element element) {
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				return true;
			}
		}
		return false;
	}

}
