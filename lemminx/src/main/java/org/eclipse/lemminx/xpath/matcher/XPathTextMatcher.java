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

import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * XPath text matcher matches {@link Text}.
 *
 */
public class XPathTextMatcher extends AbstractXPathNodeMatcher {

	public XPathTextMatcher(XPathMatcher ownerMatcher) {
		super(ownerMatcher);
	}

	@Override
	public MatcherType getType() {
		return MatcherType.TEXT;
	}

	@Override
	public boolean match(Node testNode, Collection<String> wildcardValues) {
		return testNode.getNodeType() == Node.TEXT_NODE;
	}

	@Override
	public boolean isAny() {
		return false;
	}

}
