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

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics.LSPErrorReporterForXML;
import org.eclipse.lemminx.extensions.xerces.ExternalXMLDTDValidator;

/**
 * This class is the configuration used to parse XML 1.0 and XML 1.1 documents
 * and provides support for xml-model association.
 *
 * @see https://www.w3.org/TR/xml-model/
 */
public class XMLModelAwareParserConfiguration extends XIncludeAwareParserConfiguration {

	public static final String ERROR_REPORTER_FOR_GRAMMAR = ERROR_REPORTER + "-grammar";

	protected boolean xmlModelEnabled = true;
	private XMLModelHandler xmlModelHandler;

	private XMLErrorReporter reporterForGrammar;

	/** Default constructor. */
	public XMLModelAwareParserConfiguration() {
		this(null, null, null);
	} // <init>()

	/**
	 * Constructs a parser configuration using the specified symbol table.
	 *
	 * @param symbolTable The symbol table to use.
	 */
	public XMLModelAwareParserConfiguration(SymbolTable symbolTable) {
		this(symbolTable, null, null);
	} // <init>(SymbolTable)

	/**
	 * Constructs a parser configuration using the specified symbol table and
	 * grammar pool.
	 * <p>
	 *
	 * @param symbolTable The symbol table to use.
	 * @param grammarPool The grammar pool to use.
	 */
	public XMLModelAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
		this(symbolTable, grammarPool, null);
	} // <init>(SymbolTable,XMLGrammarPool)

	/**
	 * Constructs a parser configuration using the specified symbol table, grammar
	 * pool, and parent settings.
	 * <p>
	 *
	 * @param symbolTable        The symbol table to use.
	 * @param grammarPool        The grammar pool to use.
	 * @param reporterForGrammar The parent settings.
	 */
	public XMLModelAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool,
			LSPErrorReporterForXML reporterForGrammar) {
		super(symbolTable, grammarPool);
		this.reporterForGrammar = reporterForGrammar;
	}

	@Override
	protected void configurePipeline() {
		super.configurePipeline();
		configureXMLModelPipeline();
	}

	@Override
	protected void configureXML11Pipeline() {
		super.configureXML11Pipeline();
		configureXMLModelPipeline();
	}

	private void configureXMLModelPipeline() {
		if (xmlModelEnabled) {
			// If the xml-model handler was not in the pipeline insert it.
			if (xmlModelHandler == null) {
				xmlModelHandler = new XMLModelHandler();
				// add XMLModel component
				// setProperty(XMLModel_HANDLER, fXMLModelHandler);
				addCommonComponent(xmlModelHandler);
				xmlModelHandler.reset(this);
			}
			// configure XML document pipeline: insert after DTDValidator and
			// before XML Schema validator
			XMLDocumentSource prev = null;
			if (fFeatures.get(XMLSCHEMA_VALIDATION) == Boolean.TRUE) {
				// we don't have to worry about fSchemaValidator being null, since
				// super.configurePipeline() instantiated it if the feature was set
				prev = fSchemaValidator.getDocumentSource();
			}
			// Otherwise, insert after the last component in the pipeline
			else {
				prev = fLastComponent;
				fLastComponent = xmlModelHandler;
			}

			XMLDocumentHandler next = prev.getDocumentHandler();
			prev.setDocumentHandler(xmlModelHandler);
			xmlModelHandler.setDocumentSource(prev);
			if (next != null) {
				xmlModelHandler.setDocumentHandler(next);
				next.setDocumentSource(xmlModelHandler);
			}
		}
	}

	@Override
	protected void checkProperty(String propertyId) throws XMLConfigurationException {
		if (ExternalXMLDTDValidator.DOCTYPE.equals(propertyId)) {
			return;
		}
		if (ERROR_REPORTER_FOR_GRAMMAR.equals(propertyId)) {
			return;
		}
		super.checkProperty(propertyId);
	}

	@Override
	public Object getProperty(String propertyId) throws XMLConfigurationException {
		if (ERROR_REPORTER_FOR_GRAMMAR.equals(propertyId)) {
			return reporterForGrammar;
		}
		return super.getProperty(propertyId);
	}

	public XMLErrorReporter getReporterForGrammar() {
		return reporterForGrammar;
	}
}
