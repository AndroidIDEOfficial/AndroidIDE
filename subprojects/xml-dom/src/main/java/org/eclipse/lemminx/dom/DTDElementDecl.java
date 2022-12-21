/**
 *  Copyright (c) 2018 Angelo ZERR.
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
package org.eclipse.lemminx.dom;

import static org.eclipse.lemminx.utils.StringUtils.findEndWord;
import static org.eclipse.lemminx.utils.StringUtils.findStartWord;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * DTD Element Declaration <!ELEMENT
 * 
 * @see https://www.w3.org/TR/REC-xml/#dt-eldecl
 *
 */
public class DTDElementDecl extends DTDDeclNode {

	private static final Predicate<Character> isValidChar = (c) -> Character.isJavaIdentifierPart(c) || c == '-';

	/**
	 * Formats:
	 * 
	 * <!ELEMENT element-name category> or <!ELEMENT element-name (element-content)>
	 * 
	 */

	public DTDDeclParameter category;
	public DTDDeclParameter content;

	public DTDElementDecl(int start, int end) {
		super(start, end);
		setDeclType(start + 2, start + 9);
	}

	@Override
	public String getNodeName() {
		return getName();
	}

	public String getCategory() {
		return category != null ? category.getParameter() : null;
	}

	public void setCategory(int start, int end) {
		category = addNewParameter(start, end);
	}

	public String getContent() {
		return content != null ? content.getParameter() : null;
	}

	public void setContent(int start, int end) {
		content = addNewParameter(start, end);
	}

	@Override
	public short getNodeType() {
		return DOMNode.DTD_ELEMENT_DECL_NODE;
	}

	/**
	 * Returns the offset of the end of tag <!ELEMENT
	 * 
	 * @return the offset of the end of tag <!ELEMENT
	 */
	public int getEndElementTag() {
		return getStart() + "<!ELEMENT".length();
	}

	/**
	 * Returns the parameter (start/end offset) at the given offset and null
	 * otherwise.
	 * 
	 * <p>
	 * <!ELEMENT note (to,from,head|ing,body)> will return (start/end offset) of
	 * heading.
	 * </p>
	 *
	 * <p>
	 * <!ELEMENT n|ote (to,from,heading,body)> will return null.
	 * </p>
	 * 
	 * @param offset the offset
	 * @return the parameter (start/end offset) at the given offset and null
	 *         otherwise.
	 */
	public DTDDeclParameter getParameterAt(int offset) {
		// Check if offset is in the <!ELEMENT nam|e
		if (isInNameParameter(offset)) {
			return null;
		}
		// We are after the <!ELEMENT name, search the parameter
		String text = getOwnerDocument().getText();
		// Find the start word offset from the left of the offset (ex : (head|ing) will
		// return offset of 'h'
		int paramStart = findStartWord(text, offset, isValidChar);
		// Find the end word to the right of the offset (ex : (head|ing) will return
		// offset of 'g'
		int paramEnd = findEndWord(text, offset, isValidChar);
		if (paramStart == -1 || paramEnd == -1) {
			// no word
			return null;
		}
		return new DTDDeclParameter(this, paramStart, paramEnd);
	}

	@Override
	public DTDDeclParameter getReferencedElementNameAt(int offset) {
		return getParameterAt(offset);
	}

	/**
	 * Collect parameters which matches the given target.
	 * 
	 * @param target    the target
	 * @param collector the collector to collect parameters.
	 */
	public void collectParameters(DTDDeclParameter target, BiConsumer<DTDDeclParameter, DTDDeclParameter> collector) {
		DTDDeclParameter name = getNameParameter();
		if (name == null) {
			return;
		}
		int start = name.getEnd();
		int end = getEnd();

		String text = getOwnerDocument().getText();
		int wordStart = -1;
		int wordEnd = -1;
		// Loop for content after <!ELEMENT element-name (
		// to check if target checks words

		// Ex : if 'svg' is the word to find (the target node)
		// and we have <!ELEMENT element-name (svg, title, font-face)
		// we must collect svg as child
		for (int i = start; i < end; i++) {
			char c = text.charAt(i);
			// check if current character is valid for a word.
			if (isValidChar.test(c)) {
				if (wordStart == -1) {
					// start of the word
					wordStart = i;
				}
			} else if (wordStart != -1) {
				// current character is not valid, it's the end of the word.
				wordEnd = i;
			}
			if (wordStart != -1 && wordEnd != -1) {
				// a word was found
				boolean check = isMatchName(target.getParameter(), text, wordStart, wordEnd);
				if (check) {
					collector.accept(new DTDDeclParameter(this, wordStart, wordEnd), target);
				}
				wordStart = -1;
				wordEnd = -1;
			}
		}
	}

	/**
	 * Returns true if the word in the given <code>text</code> which starts at
	 * <code>wordStart</code> offset and ends at <code>wordEnd</code> matches the
	 * given <code>searchWord</code>
	 * 
	 * @param searchWord the word to search
	 * @param text       the text
	 * @param wordStart  the word start offset
	 * @param wordEnd    the word end offset
	 * @return true if the word in the given <code>text</code> which starts at
	 *         <code>wordStart</code> offset and ends at <code>wordEnd</code>
	 *         matches the given <code>searchName</code>
	 */
	private static boolean isMatchName(String searchWord, String text, int wordStart, int wordEnd) {
		int length = wordEnd - wordStart;
		if (searchWord.length() != length) {
			return false;
		}
		for (int j = 0; j < length; j++) {
			if ((searchWord.charAt(j) != text.charAt(wordStart + j))) {
				return false;
			}
		}
		return true;
	}

}
