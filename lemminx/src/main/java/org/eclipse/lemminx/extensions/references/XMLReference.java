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
package org.eclipse.lemminx.extensions.references;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.xpath.matcher.XPathMatcher;
import org.w3c.dom.NodeList;

/**
 * An XML reference declaration.
 *
 */
public class XMLReference {

	private final String from;

	private final List<XPathExpression> tos;

	private XPathMatcher matcher;

	public XMLReference(String from) {
		this.from = from;
		this.matcher = new XPathMatcher(from);
		this.tos = new ArrayList<>();
	}

	public XMLReference to(String to) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		this.tos.add(xPath.compile(to));
		return this;
	}

	boolean match(DOMNode node) {
		return matcher.match(node);
	}

	void collect(DOMNode node, Consumer<DOMNode> collector) throws XPathExpressionException {
		for (XPathExpression expression : tos) {
			NodeList result = (NodeList) expression.evaluate(node, XPathConstants.NODESET);
			for (int i = 0; i < result.getLength(); i++) {
				collector.accept((DOMNode) result.item(i));
			}
		}

	}

}
