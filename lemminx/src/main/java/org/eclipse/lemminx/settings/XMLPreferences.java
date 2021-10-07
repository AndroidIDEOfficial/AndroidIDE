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
package org.eclipse.lemminx.settings;

/**
 * XML Preferences
 *
 */
public class XMLPreferences {
	
	public static final QuoteStyle DEFAULT_QUOTE_STYLE = QuoteStyle.doubleQuotes;
	
	public static final SchemaDocumentationType DEFAULT_SCHEMA_DOCUMENTATION_TYPE = SchemaDocumentationType.all;

	private QuoteStyle quoteStyle;

	private SchemaDocumentationType showSchemaDocumentationType;

	public XMLPreferences() {
		this.quoteStyle = DEFAULT_QUOTE_STYLE;
		this.showSchemaDocumentationType = DEFAULT_SCHEMA_DOCUMENTATION_TYPE;
	}

	/**
	 * Returns the actual quotation value as a char.
	 * 
	 * Either a {@code '} or {@code "}.
	 * 
	 * Defaults to {@code "}.
	 */
	public char getQuotationAsChar() {
		QuoteStyle style = getQuoteStyle();
		return QuoteStyle.doubleQuotes.equals(style) ? '\"' : '\'';
	}

	/**
	 * Returns the actual quotation value as a String.
	 * 
	 * Either a {@code '} or {@code "}.
	 * 
	 * Defaults to {@code "}.
	 */
	public String getQuotationAsString() {
		return Character.toString(getQuotationAsChar());
	}

	/**
	 * Sets the quote style
	 * 
	 * @param quoteStyle
	 */
	public void setQuoteStyle(QuoteStyle quoteStyle) {
		this.quoteStyle = quoteStyle;
	}

	/**
	 * Returns the quote style
	 * 
	 * @return
	 */
	public QuoteStyle getQuoteStyle() {
		return this.quoteStyle;
	}

	/**
	 * Returns the showSchemaDocumentationType
	 */
	public SchemaDocumentationType getShowSchemaDocumentationType() {
		return this.showSchemaDocumentationType;
	}

	/**
	 * Sets the showSchemaDocumentationType
	 * 
	 * @param showSchemaDocumentationType
	 */
	public void setShowSchemaDocumentationType(SchemaDocumentationType showSchemaDocumentationType) {
		this.showSchemaDocumentationType = showSchemaDocumentationType;
	}

	/**
	 * Merges the contents of <code>newPreferences</code> to the current
	 * <code>XMLPreferences</code> instance
	 * 
	 * @param newPreferences
	 */
	public void merge(XMLPreferences newPreferences) {
		this.setQuoteStyle(newPreferences.getQuoteStyle());
		this.setShowSchemaDocumentationType(newPreferences.getShowSchemaDocumentationType());
	}
}
