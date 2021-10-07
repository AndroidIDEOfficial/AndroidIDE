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
package org.eclipse.lemminx.extensions.xerces;

import static org.eclipse.lemminx.extensions.xerces.xmlmodel.XMLModelAwareParserConfiguration.ERROR_REPORTER_FOR_GRAMMAR;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDDescription;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.lemminx.extensions.xerces.xmlmodel.msg.XMLModelMessageFormatter;

/**
 * Xerces uses {@link XMLDTDValidator} which gets the DOCTYPE dtd file from the
 * declared DOCTYPE.
 * 
 * This class extends {@link XMLDTDValidator} to set the DOCTYPE dtd file with
 * {@link ExternalXMLDTDValidator#setExternalDoctype(String)}
 *
 */
public class ExternalXMLDTDValidator extends XMLDTDValidator {

	public final static String DOCTYPE = "http://apache.org/xml/properties/dtd/external-doctype"; //$NON-NLS-1$ ;

	private static final String DTD_NOT_FOUND_KEY = "dtd_not_found";

	private static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;

	private boolean rootElement;
	private XMLLocator locator;
	private XMLEntityManager entityManager;

	private String externalDoctype;

	private XMLErrorReporter reporterForGrammar;

	public ExternalXMLDTDValidator() {
		rootElement = true;
	}

	public void setExternalDoctype(String externalDoctype) {
		this.externalDoctype = externalDoctype;
	}

	public void setLocator(XMLLocator locator) {
		this.locator = locator;
	}

	@Override
	public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext,
			Augmentations augs) throws XNIException {
		super.startDocument(locator, encoding, namespaceContext, augs);
		setLocator(locator);
	}

	@Override
	public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
		tryToBindWithExternalDTD(element);
		super.startElement(element, attributes, augs);
	}

	private void tryToBindWithExternalDTD(QName element) {
		if (rootElement) {
			rootElement = false;
			fValidation = fDTDValidation = externalDoctype != null;
			if (fValidation) {
				QName fRootElement = getRootElement();
				String rootElementName = element.localpart;

				// save root element state
				fRootElement.setValues(null, rootElementName, rootElementName, null);

				String eid = null;
				try {
					eid = XMLEntityManager.expandSystemId(externalDoctype, locator.getExpandedSystemId(), false);
				} catch (java.io.IOException e) {
				}
				XMLDTDDescription grammarDesc = new XMLDTDDescription(null, externalDoctype,
						locator.getExpandedSystemId(), eid, rootElementName);
				fDTDGrammar = fGrammarBucket.getGrammar(grammarDesc);
				if (fDTDGrammar == null) {
					// give grammar pool a chance...
					//
					// Do not bother checking the pool if no public or system identifier was
					// provided.
					// Since so many different DTDs have roots in common, using only a root name as
					// the
					// key may cause an unexpected grammar to be retrieved from the grammar pool.
					// This scenario
					// would occur when an ExternalSubsetResolver has been queried and the
					// XMLInputSource returned contains an input stream but no external identifier.
					// This can never happen when the instance document specified a DOCTYPE. --
					// mrglavas
					if (fGrammarPool != null) {
						fDTDGrammar = (DTDGrammar) fGrammarPool.retrieveGrammar(grammarDesc);
					}
				}
				if (fDTDGrammar == null) {

					XMLDTDLoader loader = new XMLDTDLoader(fSymbolTable, fGrammarPool);
					loader.setProperty("http://apache.org/xml/properties/internal/error-reporter", reporterForGrammar);
					loader.setEntityResolver(entityManager);
					try {
						fDTDGrammar = (DTDGrammar) loader.loadGrammar(new XMLInputSource(null, eid, null));
					} catch (Exception e) {
						// DTD declared in xml-model href="" doesn't exist, report the error and disable
						// the DTD validation.
						fErrorReporter.reportError(locator, XMLModelMessageFormatter.XML_MODEL_DOMAIN,
								DTD_NOT_FOUND_KEY, new Object[] { element, eid }, XMLErrorReporter.SEVERITY_ERROR);
						super.fValidation = false;
					}
				} else {
					// we've found a cached one;so let's make sure not to read
					// any external subset!
					fValidationManager.setCachedDTD(true);
				}
			}
		}
	}

	@Override
	public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
		tryToBindWithExternalDTD(element);
		super.emptyElement(element, attributes, augs);
	}

	private QName getRootElement() {
		try {
			// fRootElement is declared as private in the XMLDTDValidator, we must use ugly
			// Java reflection to get the field.
			return ReflectionUtils.getFieldValue(this, "fRootElement");
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
		entityManager = (XMLEntityManager) componentManager.getProperty(ENTITY_MANAGER);
		// get external DOCTYPE
		try {
			setExternalDoctype((String) componentManager.getProperty(DOCTYPE));
		} catch (XMLConfigurationException e) {
			setExternalDoctype(null);
		}
		super.reset(componentManager);
		reporterForGrammar = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER_FOR_GRAMMAR);
		fValidation = false;
		fDTDValidation = false;
	}
}
