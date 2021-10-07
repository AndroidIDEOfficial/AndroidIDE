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
package org.eclipse.lemminx.xpath.matcher;

import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XPath attribute name matcher matches {@link Attr} name of {@link Element}.
 * 
 */
public class XPathAttributeNameMatcher extends AbstractXPathNodeMatcher {

	public static final String ANY_ELEMENT_NAME = "*";

	private final String prefix;
	private final String localName;
	private final boolean anyAttributeName;

	public XPathAttributeNameMatcher(String prefix, String localName, XPathMatcher matcher) {
		super(matcher);
		this.prefix = prefix;
		this.localName = localName;
		this.anyAttributeName = ANY_ELEMENT_NAME.equals(localName);
	}

	@Override
	public MatcherType getType() {
		return MatcherType.ATTRIBUTE;
	}

	public String getPrefix() {
		return prefix;
	}

	@Override
	public boolean match(Node testNode, Collection<String> wildcardValues) {
		if (!(testNode.getNodeType() == Node.ATTRIBUTE_NODE)) {
			return false;
		}
		if (anyAttributeName) {
			return true;
		}
		return localName.equals(testNode.getLocalName());
	}

	@Override
	public boolean isAny() {
		return false;
	}

}
