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

import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XPath attribute matcher matches {@link Attr} of {@link Element}.
 * 
 * 
 */
public class XPathAttributeMatcher extends AbstractXPathNodeMatcher {

	private final String attrName;
	private final String attrValue;
	private int indexWildcard = -1;

	public XPathAttributeMatcher(String attrName, String attrValue, XPathMatcher matcher) {
		super(matcher);
		this.attrName = attrName;
		this.attrValue = attrValue;
		if (attrValue.startsWith("$")) {
			// attribute value has wilcard (ex : [@id='$0'])
			try {
				// get the wildcard index (ex : 0 for [@id='$0']).
				indexWildcard = Integer.parseInt(attrValue.substring(1, attrValue.length()));
			} catch (NumberFormatException e) {

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#getType()
	 */
	public MatcherType getType() {
		return MatcherType.ATTRIBUTE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#match
	 * (org.w3c.dom.Node, java.util.Collection)
	 */
	public boolean match(Node testNode, Collection<String> wildcardValues) {
		if (testNode.getNodeType() == Node.ELEMENT_NODE) {
			// Node to test is element
			Element element = (Element) testNode;
			if (indexWildcard != -1) {
				// wildcard is defined in the attribute matcher.
				if (element.hasAttribute(attrName)) {
					// element define, attribute, match is OK.
					if (wildcardValues != null) {
						// wildcard values is filled, add the attribute value
						wildcardValues.add(element.getAttribute(attrName));
					}
					// element tested define the attribute, match is OK
					return true;
				}
				return false;
			}
			// No wildcard defined, test if element has attribute attrName and
			// if value is OK.
			String testAttrValue = element.getAttribute(attrName);
			return attrValue.equals(testAttrValue);
		}
		return false;
	}

	/**
	 * Returns the attribute name to match.
	 * 
	 * @return
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * Returns the attribute value to match.
	 * 
	 * @return
	 */
	public String getAttrValue() {
		return attrValue;
	}

	/**
	 * Returns true if attrValue is wilcard or false otherwise.
	 * 
	 * @return
	 */
	public boolean hasWildcard() {
		return indexWildcard != -1;
	}

	/**
	 * Returns the wildcard index and -1 if no wildcard is defined.
	 * 
	 * @return
	 */
	public int getIndexWildcard() {
		return indexWildcard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#isAny()
	 */
	public boolean isAny() {
		return false;
	}
}