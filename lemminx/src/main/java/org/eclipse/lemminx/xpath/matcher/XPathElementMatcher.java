/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.xpath.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



/**
 * XPath element matcher matches {@link Attr} of {@link Element} and element
 * name.
 * 
 */
public class XPathElementMatcher extends AbstractXPathNodeMatcher {

	public static final String ANY_ELEMENT_NAME = "*";
	private List<XPathAttributeMatcher> attributes = null;

	private final String prefix;
	private final String localName;
	private final boolean anyElementName;

	public XPathElementMatcher(String prefix, String localName,
			XPathMatcher matcher) {
		super(matcher);
		this.prefix = prefix;
		this.localName = localName;
		this.anyElementName = ANY_ELEMENT_NAME.equals(localName);
	}

	public MatcherType getType() {
		return MatcherType.ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#match
	 * (org.w3c.dom.Node, java.util.Collection)
	 */
	public boolean match(Node testNode, Collection<String> wildcardValues) {
		// test match for element name.
		if (!matchElement(testNode)) {
			return false;
		}
		// element name is matched, test match for element attributes.
		return matchAttributes(testNode, wildcardValues);
	}

	/**
	 * Returns true if element (name) is matched and false otherwise.
	 * 
	 * @param testNode
	 * @return
	 */
	private boolean matchElement(Node testNode) {
		String localName = testNode.getLocalName();
		if (localName == null) {
			localName = testNode.getNodeName();
		}
		return matchElement(localName);
	}

	/**
	 * Returns true if element name is matched and false otherwise.
	 * 
	 * @param testNode
	 * @return
	 */
	private boolean matchElement(String localName) {
		if (anyElementName) {
			// any (//) is defined, element is matched.
			return true;
		}
		// test if element name of the element match the element name matcher.
		return this.localName.equals(localName);
	}

	/**
	 * Returns true if attributes of the element match the list of attribute
	 * matcher.
	 * 
	 * @param testNode
	 * @param wildcardValues
	 * @return
	 */
	private boolean matchAttributes(Node testNode,
			Collection<String> wildcardValues) {
		if (attributes == null) {
			// No attributes matcher defined, match is OK.
			return true;
		}
		// Loop for each attributes matcher
		for (XPathAttributeMatcher attribute : attributes) {
			if (!attribute.match(testNode, wildcardValues)) {
				// on eattribuete is not matched.
				return false;
			}
		}
		// teh whole attributes are matched.
		return true;
	}

	/**
	 * Returns the prefix.
	 * 
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Returns the element name to match.
	 * 
	 * @return
	 */
	public String getLocalName() {
		return localName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#isAny()
	 */
	public boolean isAny() {
		return anyElementName;
	}

	/**
	 * Add XPath attribute matcher.
	 * 
	 * @param matcher
	 */
	public void add(XPathAttributeMatcher matcher) {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}
		attributes.add(matcher);

	}

}