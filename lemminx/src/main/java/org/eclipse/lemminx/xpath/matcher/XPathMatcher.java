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

import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.xpath.matcher.IXPathNodeMatcher.MatcherType;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * 
 * XPath matcher is used to match if a DOM Node match a XPath expression. It is
 * initialized with an XPath expression :
 * 
 * <ul>
 * <li>static expression like //element, //element/@attr, //element/text()</li>
 * <li>expression with wildcard like //element[@id='$0']</li>
 * </ul>
 * 
 */
public class XPathMatcher extends ArrayList<IXPathNodeMatcher> {

	private static final long serialVersionUID = 1L;

	private static final String ATTR_NODE_SELECTOR = "@";
	private static final String TEXT_NODE_SELECTOR = "text()";

	private int nbWildCard = -1;

	/**
	 * Constructor of XPath Matcher with a XPath expression.
	 * 
	 * @param xpathExpression
	 */
	public XPathMatcher(String xpathExpression) {
		// initialize XPath Matcher with XPath expression.
		parse(xpathExpression != null ? xpathExpression.trim() : "");
	}

	/**
	 * Parse the given XPath expression.
	 * 
	 * @param xpathExpression XPath expression.
	 */
	private void parse(String xpathExpression) {
		String prefix = null;
		String localName = null;
		if (xpathExpression.startsWith("/")) {
			xpathExpression = xpathExpression.substring(1, xpathExpression.length());
		}
		boolean endsWithAny = false;
		if (xpathExpression.endsWith("//")) {
			endsWithAny = true;
			xpathExpression = xpathExpression.substring(0, xpathExpression.length() - 2);
		}
		IXPathNodeMatcher nodeMatcher = null;
		String[] paths = xpathExpression.split("/");
		for (int i = 0; i < paths.length; i++) {
			localName = paths[i];
			if (StringUtils.isEmpty(localName)) {
				this.createAnyElementMatcher();
			} else {
				int indexNS = localName.indexOf(':');
				if (indexNS != -1) {
					prefix = localName.substring(0, indexNS);
					localName = localName.substring(indexNS + 1, localName.length());
				}
				int indexSquareBracket = localName.indexOf("[");
				if (indexSquareBracket == -1) {
					// Element name condition
					// ex : p=pipeline
					this.createAndAddNodeMatcher(prefix, localName);
				} else {
					// ex : p=transformer[@type='pipeline'][@src='p1']
					String elementName = localName.substring(0, indexSquareBracket);
					nodeMatcher = this.createAndAddNodeMatcher(prefix, elementName);

					String attributesCondition = localName.substring(indexSquareBracket, localName.length());
					// ex : attributesCondition=[@type='pipeline'][@src='p1']

					char c;
					StringBuilder attrName = null;
					StringBuilder attrValue = null;
					boolean firstQuote = false;
					boolean attrCondition = false;
					char[] chars = attributesCondition.toCharArray();
					for (int j = 0; j < chars.length; j++) {
						c = chars[j];
						if (attrName == null) {
							if (c == '[' || /* c == '@' || */c == ']') {
								continue;
							}
							attrName = new StringBuilder();
							attrName.append(c);
							attrCondition = (c == '@');
						} else {
							if (attrValue == null) {
								if (c != '=') {
									attrName.append(c);
								} else {
									attrValue = new StringBuilder();
								}
							} else {
								if (c == '\'') {
									if (!firstQuote) {
										firstQuote = true;
										continue;
									} else {
										// second quote, add attribute name
										// condition
										if (attrCondition) {
											XPathAttributeMatcher attributematcher = this.createAttributeMatcher(
													(XPathElementMatcher) nodeMatcher,
													attrName.toString().substring(1, attrName.length()),
													attrValue.toString());
											if (attributematcher.hasWildcard()) {
												int index = attributematcher.getIndexWildcard();
												if (index > nbWildCard) {
													nbWildCard = index;
												}
											}
										}
										attrName = null;
										attrValue = null;
										firstQuote = false;
										attrCondition = false;
									}
								} else {
									attrValue.append(c);
								}
							}
						}
					}
				}
			}
		}
		if (endsWithAny) {
			this.createAnyElementMatcher();
		}
	}

	/**
	 * Returns true if the given DOM Node match the XPath expression of this XPath
	 * matcher and false otherwise.
	 * 
	 * @param node the DOM Node to match.
	 * @return
	 */
	public boolean match(final Node node) {
		return match(node, null);
	}

	/**
	 * Returns true if the given DOM Node match the XPath expression of this XPath
	 * matcher and false otherwise. This method use wilcard values if XPath
	 * expression contains wildcard (like [@id='$0'].
	 * 
	 * @param node           the DOM Node to match.
	 * @param wildcardValues the list of wildcard values and null otherwise.
	 * @return
	 */
	public boolean match(final Node node, final Collection<String> wildcardValues) {
		if (node == null) {
			return false;
		}
		Node testNode = node;
		IXPathNodeMatcher condition = null;
		for (int i = super.size() - 1; i >= 0; i--) {
			if (testNode == null) {
				return false;
			}
			condition = super.get(i);
			if (condition.isAny()) {
				if (i > 0) {
					boolean previousConditionFounded = false;
					IXPathNodeMatcher previousElementCondition = super.get(i - 1);
					if (previousElementCondition == null) {
						return true;
					}
					while (testNode != null && testNode.getNodeType() != Node.DOCUMENT_NODE) {
						if (previousElementCondition.match(testNode, wildcardValues)) {
							previousConditionFounded = true;
							break;
						}
						testNode = testNode.getParentNode();
					}

					if (!previousConditionFounded) {
						return false;
					} else {
						i--;
					}

				} else {
					return true;
				}
			} else {
				if (!condition.match(testNode, wildcardValues)) {
					return false;
				}
			}
			testNode = getTestParentNode(testNode);
		}
		return true;
	}

	/**
	 * Returns list of wildcard values of the given DOM Node.
	 * 
	 * @param selectedNode
	 * @return
	 */
	public List<String> getWildcardValues(Node selectedNode) {
		List<String> wildcardValues = new ArrayList<>();
		if (nbWildCard == -1 || selectedNode == null) {
			// No wildcard in the XPath expression of this matcher or DOM node
			// is null
			return wildcardValues;
		}
		Node testNode = getTestNode(selectedNode);
		match(testNode, wildcardValues);
		return wildcardValues;
	}

	/**
	 * Returns the DOM Node to test to match.
	 * 
	 * @param node
	 * @return
	 */
	private Node getTestNode(Node node) {
		short nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ((Attr) node).getOwnerElement();
		case Node.TEXT_NODE:
			return ((Text) node).getParentNode();
		}
		return node;
	}

	private Node getTestParentNode(Node node) {
		short nodeType = node.getNodeType();
		switch (nodeType) {
		case Node.ATTRIBUTE_NODE:
			return ((Attr) node).getOwnerElement();
		default:
			return node.getParentNode();
		}
	}

	/**
	 * Returns the number of wilcard used in the XPath expression and -1 if there is
	 * no wildcard.
	 * 
	 * @return
	 */
	public int getNbWildCard() {
		return nbWildCard;
	}

	// ------------ XPath node matcher factory.

	/***
	 * Create and add the XPath node matcher.
	 * 
	 * @param prefix    the prefix.
	 * @param localName the local name for matching element node , '@attr' for
	 *                  attribute or 'text()' for text node.
	 * @return the XPath node matcher
	 */
	private IXPathNodeMatcher createAndAddNodeMatcher(String prefix, String localName) {
		IXPathNodeMatcher matcher = createNodeMatcher(prefix, localName);
		super.add(matcher);
		return matcher;
	}

	/***
	 * Create the node matcher.
	 * 
	 * @param prefix    the prefix.
	 * @param localName the local name for matching element node , '@attr' for
	 *                  attribute or 'text()' for text node.
	 * @return the XPath node matcher
	 */
	private IXPathNodeMatcher createNodeMatcher(String prefix, String localName) {
		if (TEXT_NODE_SELECTOR.equals(localName)) {
			return new XPathTextMatcher(this);
		} else if (localName.startsWith(ATTR_NODE_SELECTOR)) {
			return new XPathAttributeNameMatcher(prefix, localName.substring(1, localName.length()), this);
		}
		return new XPathElementMatcher(prefix, localName, this);
	}

	/**
	 * Create XPath any element matcher.
	 * 
	 * @return the XPath any element matcher.
	 */
	protected XPathElementMatcher createAnyElementMatcher() {
		XPathElementMatcher matcher = new XPathElementMatcher(null, XPathElementMatcher.ANY_ELEMENT_NAME, this);
		super.add(matcher);
		return matcher;
	}

	/**
	 * Create the XPath attribute matcher.
	 * 
	 * @param elementmatcher the parent element matcher.
	 * @param attrName       the attribute name to match.
	 * @param attrValue      the attribute value to match.
	 * 
	 * @return the XPath attribute matcher.
	 */
	protected XPathAttributeMatcher createAttributeMatcher(XPathElementMatcher elementmatcher, String attrName,
			String attrValue) {
		XPathAttributeMatcher matcher = new XPathAttributeMatcher(attrName, attrValue, this);
		elementmatcher.add(matcher);
		return matcher;
	}

	/**
	 * Returns the type (element, attribute, text) of the node to select.
	 * 
	 * @return the type (element, attribute, text) of the node to select.o
	 */
	public MatcherType getNodeSelectorType() {
		if (isEmpty()) {
			return MatcherType.ELEMENT;
		}
		IXPathNodeMatcher matcher = get(size() - 1);
		return matcher.getType();
	}
}