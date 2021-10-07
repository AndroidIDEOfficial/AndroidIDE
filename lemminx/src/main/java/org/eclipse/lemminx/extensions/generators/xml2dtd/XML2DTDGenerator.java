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
package org.eclipse.lemminx.extensions.generators.xml2dtd;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.lemminx.extensions.generators.AbstractXML2GrammarGenerator;
import org.eclipse.lemminx.extensions.generators.AttributeDeclaration;
import org.eclipse.lemminx.extensions.generators.Cardinality;
import org.eclipse.lemminx.extensions.generators.ElementDeclaration;
import org.eclipse.lemminx.extensions.generators.Grammar;
import org.eclipse.lemminx.utils.XMLBuilder;

/**
 * File Generator implementation to generate DTD from a given XML source.
 * 
 */
public class XML2DTDGenerator extends AbstractXML2GrammarGenerator<DTDGeneratorSettings> {

	@Override
	protected void generate(Grammar grammar, DTDGeneratorSettings settings, XMLBuilder dtd) {
		for (ElementDeclaration elementDecl : grammar.getElements()) {
			boolean hasCharacterContent = elementDecl.hasCharacterContent();

			// <!ELEMENT
			dtd.startDTDElementDecl();
			dtd.addParameter(elementDecl.getName());

			Collection<ElementDeclaration> children = elementDecl.getElements();
			if (children.isEmpty()) {
				if (hasCharacterContent) {
					dtd.addContent(" (#PCDATA)");
				} else {
					dtd.addContent(" EMPTY");
				}
			} else {
				// There are children
				if (!hasCharacterContent) {

					boolean sequenced = elementDecl.getChildrenProperties().isSequenced();
					if (sequenced) {
						// All elements have the same child elements in the same sequence
						dtd.addContent(" (");
						boolean first = true;
						for (Map.Entry<String, Cardinality> elementInfo : elementDecl.getChildrenProperties()
								.getCardinalities().entrySet()) {
							if (!first) {
								dtd.addContent(",");
							}
							first = false;
							dtd.addContent(elementInfo.getKey());
							Cardinality cardinality = elementInfo.getValue();
							if (cardinality.getMin() == 0 && cardinality.getMax() == 1) {
								// ? 0-1
								dtd.addContent("?");
							} else if (cardinality.getMin() == 0 && cardinality.getMax() > 1) {
								// * 0-n
								dtd.addContent("*");
							} else if (cardinality.getMax() > 1) {
								// + 1-n
								dtd.addContent("+");
							}
						}
						dtd.addContent(")");

					} else {
						// the children don't always appear in the same sequence;
						dtd.addContent(" (");
						boolean first = true;
						for (ElementDeclaration elementInfo : children) {
							if (!first) {
								dtd.addContent("|");
							}
							first = false;
							dtd.addContent(elementInfo.getName());
						}
						dtd.addContent(")*");
					}
				} else {
					// Mixed content
					dtd.addContent("(#PCDATA");
					if (hasCharacterContent) {
						for (ElementDeclaration elementInfo : children) {
							dtd.addContent("|");
							dtd.addContent(elementInfo.getName());
						}
					}
					dtd.addContent(")*");
				}
			}
			dtd.closeStartElement();

			// <!ATTLIST
			Collection<AttributeDeclaration> attributes = elementDecl.getAttributes();
			if (!attributes.isEmpty()) {
				for (AttributeDeclaration attribute : attributes) {

					// <!ATTLIST elementname
					dtd.startDTDAttlistDecl();
					dtd.addParameter(elementDecl.getName());

					boolean required = attribute.isRequired();
					boolean isID = attribute.isID(settings);
					boolean fixed = attribute.isFixedValue(settings);
					boolean enums = attribute.isEnums(settings);
					String tokentype = (attribute.isAllNMTOKENs() ? "NMTOKEN" : "CDATA");

					// <!ATTLIST elementname attname
					dtd.addParameter(attribute.getName());
					if (isID) {
						dtd.addParameter("ID");
					} else if (fixed) {
						String val = attribute.getValues().first();
						dtd.addParameter(tokentype);
						dtd.addParameter("#FIXED");
						dtd.addParameter("\"" + val + "\"");
					} else if (enums) {
						dtd.addContent(" (");
						SortedSet<String> values = attribute.getValues();
						boolean first = true;
						for (String value : values) {
							if (!first) {
								dtd.addContent("|");
							}
							dtd.addContent(value);
							first = false;
						}
						dtd.addContent(")");
					} else {
						dtd.addParameter(tokentype);
					}

					if (!fixed) {
						if (required) {
							dtd.addParameter("#REQUIRED");
						} else {
							dtd.addParameter("#IMPLIED");
						}
					}

					dtd.closeStartElement();
				}
			}
		}
	}

	@Override
	protected String getFileExtension() {
		return "dtd";
	}

	@Override
	protected boolean isFlat() {
		return true;
	}

}
