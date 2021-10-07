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
package org.eclipse.lemminx.extensions.xerces.xmlmodel;

import static org.eclipse.lemminx.extensions.xerces.xmlmodel.XMLModelAwareParserConfiguration.ERROR_REPORTER_FOR_GRAMMAR;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.eclipse.lemminx.utils.StringUtils;

/**
 * XML model validator which process validation with XML Schema:
 * 
 * <pre>
 * 	&lt;?xml-model href="http://www.docbook.org/xml/5.0/xsd/docbook.xsd"?&gt;
 * </pre>
 *
 */
public class XMLModelSchemaValidator extends XMLSchemaValidator implements XMLModelValidator {

	private static final String XMLNS_ATTR = "xmlns";

	private XMLErrorReporter errorReporter;
	private boolean rootElement;
	private String href;

	public XMLModelSchemaValidator() {
		rootElement = true;
	}

	@Override
	public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
		super.reset(componentManager);
		// force XML Schema validation
		fDoValidation = true;
		// Get error reporter.
		try {
			XMLErrorReporter value = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER_FOR_GRAMMAR);
			if (value != null) {
				errorReporter = value;
			}
		} catch (XMLConfigurationException e) {
			errorReporter = null;
		}
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public void setLocator(XMLLocator locator) {

	}

	@Override
	public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
		if (rootElement) {
			// on the document element, we associate the XML Schema declared in <?xml-model
			// processing instruction
			String defaultNamespace = attributes.getValue(XMLNS_ATTR);
			if (StringUtils.isEmpty(defaultNamespace)) {
				// XML doesn't define a xmlns attribute in the root element -> same support than
				// xsi:noNamespaceSchemaLocation. Ex:
				/**
				 * <?xml-model href="http://www.docbook.org/xml/5.0/xsd/docbook.xsd"?> <book>
				 **/
				String noNamespaceSchemaLocation = href;
				XMLSchemaLoader.processExternalHints(null, noNamespaceSchemaLocation, fLocationPairs, errorReporter);
			} else {
				// XML defines a xmlns attribute in the root element -> same support than
				// xsi:schemaLocation. Ex:
				/**
				 * <?xml-model href="http://www.docbook.org/xml/5.0/xsd/docbook.xsd"?>
				 * <book xmlns="http://docbook.org/ns/docbook">
				 **/
				String schemaLocation = defaultNamespace + ' ' + href;
				XMLSchemaLoader.processExternalHints(schemaLocation, null, fLocationPairs, errorReporter);
			}
			rootElement = false;
		}
		super.startElement(element, attributes, augs);
	}
}