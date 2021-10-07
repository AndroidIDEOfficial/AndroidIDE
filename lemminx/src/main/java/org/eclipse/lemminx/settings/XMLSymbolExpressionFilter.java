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

import org.eclipse.lemminx.xpath.matcher.IXPathNodeMatcher.MatcherType;
import org.eclipse.lemminx.xpath.matcher.XPathMatcher;
import org.w3c.dom.Node;

/**
 * XML Symbol expression filter.
 */
public class XMLSymbolExpressionFilter {

	private transient XPathMatcher matcher;

	private String xpath;

	private boolean excluded;

	/**
	 * Returns the XPath expression.
	 * 
	 * @return the XPath expression.
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * Set the XPath expression.
	 * 
	 * @param xpath the XPath expression.
	 */
	public void setXpath(String xpath) {
		this.xpath = xpath;
		this.matcher = null;
	}

	/**
	 * Returns true if the filter which matches a node must exclude the node as
	 * symbol and false otherwise.
	 * 
	 * @return true if the filter which matches a node must exclude the node as
	 *         symbol and false otherwise.
	 */
	public boolean isExcluded() {
		return excluded;
	}

	/**
	 * Set true if the filter which matches a node must exclude the node as symbol
	 * and false otherwise.
	 * 
	 * @param excluded true if the filter which matches a node must exclude the node
	 *                 as symbol and false otherwise.
	 */
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

	/**
	 * Returns true if the given node match the XPath expression and false
	 * otherwise.
	 * 
	 * @param node the DOM node.
	 * 
	 * @return true if the given node match the XPath expression and false
	 *         otherwise.
	 */
	public boolean match(Node node) {
		return getMatcher().match(node);
	}

	/**
	 * Return true if the filter have is for the given type (element, attribute,
	 * text) and false otherwise.
	 * 
	 * @param matcherType the matcher type.
	 * 
	 * @return true if the filter have is for the given type (element, attribute,
	 *         text) and false otherwise.
	 */
	public boolean isFilterFor(MatcherType matcherType) {
		return getMatcher().getNodeSelectorType() == matcherType;
	}

	private XPathMatcher getMatcher() {
		if (matcher == null) {
			matcher = new XPathMatcher(xpath);
		}
		return matcher;
	}
}
