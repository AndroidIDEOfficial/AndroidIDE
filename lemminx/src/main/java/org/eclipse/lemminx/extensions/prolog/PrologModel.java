/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.prolog;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.AttributeCompletionItem;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.w3c.dom.NamedNodeMap;

import com.google.common.base.Charsets;

/**
 * This class holds values that represent the XSI xsd. Can be seen at
 * https://www.w3.org/2001/XMLSchema-instance
 */
public class PrologModel {
	public static final String VERSION_NAME = "version";
	public static final String ENCODING_NAME = "encoding";
	public static final String STANDALONE_NAME = "standalone";

	public static final String VERSION_1 = "1.0";
	public static final String VERSION_1_1 = "1.1";

	public static final String UTF_8 = Charsets.UTF_8.toString();
	public static final String ISO_8859_1 = Charsets.ISO_8859_1.toString();
	public static final String WINDOWS_1251 = "Windows-1251";
	public static final String WINDOWS_1252 = "Windows-1252";
	public static final String SHIFT_JIS = "Shift JIS";
	public static final String GB2312 = "GB2312";
	public static final String EUC_KR = "EUC-KR";

	public static final String YES = "yes";
	public static final String NO = "no";

	// Don't change order of list items
	public static final List<String> VERSION_VALUES = Arrays.asList(VERSION_1, VERSION_1_1);
	public static final List<String> ENCODING_VALUES = Arrays.asList(UTF_8, ISO_8859_1, WINDOWS_1251, WINDOWS_1252,
			SHIFT_JIS, GB2312, EUC_KR);
	public static final List<String> STANDALONE_VALUES = Arrays.asList(YES, NO);

	private static void createCompletionItem(String attrName, boolean canSupportSnippet, boolean generateValue,
			Range editRange, String defaultValue, Collection<String> enumerationValues, String documentation,
			ICompletionResponse response, SharedSettings sharedSettings) {
		CompletionItem item = new AttributeCompletionItem(attrName, canSupportSnippet, editRange, generateValue,
				defaultValue, enumerationValues, sharedSettings);
		MarkupContent markup = new MarkupContent();
		markup.setKind(MarkupKind.MARKDOWN);

		markup.setValue(StringUtils.getDefaultString(documentation));
		item.setDocumentation(markup);
		response.addCompletionItem(item);
	}

	public static void computeAttributeNameCompletionResponses(ICompletionRequest request, ICompletionResponse response,
			Range editRange, DOMDocument document, SharedSettings sharedSettings) throws BadLocationException {

		if (document.hasProlog() == false) {
			return;
		}
		int offset = document.offsetAt(editRange.getStart());
		DOMNode prolog = document.findNodeAt(offset);
		if (!prolog.isProlog()) {
			return;
		}
		boolean isSnippetsSupported = request.isCompletionSnippetsSupported();
		int attrIndex = getAttributeCompletionPosition(offset, prolog);

		if (attrIndex == 0) { // 1st attribute
			if (isCurrentAttributeEqual(VERSION_NAME, prolog, 0)) {
				return;
			}
			createCompletionItem(VERSION_NAME, isSnippetsSupported, true, editRange, VERSION_1, VERSION_VALUES, null,
					response, sharedSettings);
			return;
		}

		if (attrIndex == 1) { // 2nd attribute
			if (!isCurrentAttributeEqual(ENCODING_NAME, prolog, 1)) {
				createCompletionItem(ENCODING_NAME, isSnippetsSupported, true, editRange, UTF_8, ENCODING_VALUES, null,
						response, sharedSettings);
			} else {
				return;
			}

			if (!isCurrentAttributeEqual(STANDALONE_NAME, prolog, 1)) {
				createCompletionItem(STANDALONE_NAME, isSnippetsSupported, true, editRange, YES, STANDALONE_VALUES,
						null, response, sharedSettings);
			}
			return;
		}

		if (attrIndex == 2) { // 3rd attribute
			DOMAttr attrBefore = prolog.getAttributeAtIndex(1);
			if (!STANDALONE_NAME.equals(attrBefore.getName()) && !isCurrentAttributeEqual(STANDALONE_NAME, prolog, 2)) {
				createCompletionItem(STANDALONE_NAME, isSnippetsSupported, true, editRange, YES, STANDALONE_VALUES,
						null, response, sharedSettings);
			}
			return;
		}

	}

	public static void computeValueCompletionResponses(ICompletionRequest request, ICompletionResponse response,
			Range editRange, DOMDocument document) throws BadLocationException {

		if (document.hasProlog() == false) {
			return;
		}
		int offset = document.offsetAt(editRange.getStart());
		DOMNode prolog = document.findNodeAt(offset);
		if (!prolog.isProlog()) {
			return;
		}

		DOMAttr attr = prolog.findAttrAt(offset);
		if (VERSION_NAME.equals(attr.getName())) { // version
			createCompletionItemsForValues(VERSION_VALUES, editRange, document, request, response);
		}

		else if (ENCODING_NAME.equals(attr.getName())) { // encoding
			createCompletionItemsForValues(ENCODING_VALUES, editRange, document, request, response);
		}

		else if (STANDALONE_NAME.equals(attr.getName())) {
			createCompletionItemsForValues(STANDALONE_VALUES, editRange, document, request, response);
		}
	}

	private static void createCompletionItemsForValues(Collection<String> enumerationValues, Range editRange,
			DOMDocument document, ICompletionRequest request, ICompletionResponse response) {
		int sortText = 1;
		CompletionItem item;
		for (String option : enumerationValues) {
			String insertText = request.getInsertAttrValue(option);
			item = new CompletionItem();
			item.setLabel(option);
			item.setFilterText(insertText);
			item.setKind(CompletionItemKind.Enum);
			item.setTextEdit(Either.forLeft(new TextEdit(editRange, insertText)));
			item.setSortText(Integer.toString(sortText));
			sortText++;
			response.addCompletionItem(item);
		}
	}

	/**
	 * Returns the position the offset is in in relation to the attributes and their
	 * order
	 * 
	 * example:
	 * 
	 * <element a="1" b="2" | c="3">
	 * 
	 * This will return 2 since if you insert a new attribute there you can access
	 * it from the list of attributes with this index.
	 * 
	 * @param completionOffset
	 * @param element
	 * @return
	 */
	private static int getAttributeCompletionPosition(int completionOffset, DOMNode element) {

		NamedNodeMap attributeList = element.getAttributes();

		if (attributeList == null) {
			return 0;
		}

		int attributeListLength = attributeList.getLength();

		if (attributeListLength == 0) {
			return 0;
		}

		DOMAttr attr;

		for (int i = 0; i < attributeListLength; i++) {
			attr = element.getAttributeAtIndex(i);
			if (completionOffset <= attr.getStart()) {
				return i;
			}
		}

		return attributeListLength;
	}

	/**
	 * Returns true if the current attribute in the given position of the element's
	 * list of attributes equals the provided attributeName
	 * 
	 * @param attributeName
	 * @param element
	 * @param position
	 * @return
	 */
	private static boolean isCurrentAttributeEqual(String attributeName, DOMNode element, int index) {
		NamedNodeMap attributeList = element.getAttributes();

		if (attributeList == null) {
			return false;
		}

		if (index >= attributeList.getLength()) {
			return false;
		}

		if (attributeName.equals(element.getAttributeAtIndex(index).getName())) {
			return true;
		}

		return false;
	}

}