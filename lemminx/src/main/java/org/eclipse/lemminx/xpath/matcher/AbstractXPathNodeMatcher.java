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

/**
 * Abstract class for XPath node matcher.
 * 
 */
public abstract class AbstractXPathNodeMatcher implements IXPathNodeMatcher {

	private XPathMatcher ownerMatcher;

	public AbstractXPathNodeMatcher(XPathMatcher ownerMatcher) {
		this.ownerMatcher = ownerMatcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.wst.xml.search.core.xpath.matcher.IXPathNodeMatcher#
	 * getOwnerMatcher()
	 */
	public XPathMatcher getOwnerMatcher() {
		return ownerMatcher;
	}

}