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

import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.xpath.matcher.IXPathNodeMatcher.MatcherType;

/**
 * XML symbol filter used to show/hide DOM attributes, text nodes specified with
 * XPath expressions:
 * 
 * <pre>
 * "xml.symbols.filters": [
   // Declaration of symbols filter for maven 'pom.xml' to show all text nodes in the Outline.
   {
      "pattern": "pom.xml",
      "expressions" :[
         {
            "xpath": "//text()"
         }
      ]
   },
   // Declaration of symbols filter for Spring beans to show all @id of the elements in the Outline.
   {
      "pattern": "bean*.xml",
      "expressions" :[
         {
            "xpath": "//@id"
         }
      ]
   }
]
 * </pre>
 */
public class XMLSymbolFilter extends PathPatternMatcher {

	public static final XMLSymbolFilter DEFAULT;

	static {
		DEFAULT = new XMLSymbolFilter();
	}

	private XMLSymbolExpressionFilter[] expressions;

	/**
	 * Set the expression list filter.
	 * 
	 * @param expressions the expression list filter.
	 */
	public void setExpressions(XMLSymbolExpressionFilter[] expressions) {
		this.expressions = expressions;
	}

	/**
	 * Returns the expression list filter.
	 * 
	 * @return the expression list filter.
	 */
	public XMLSymbolExpressionFilter[] getExpressions() {
		return expressions;
	}

	/**
	 * Returns true if the given node is a symbol and false otherwise.
	 * 
	 * @param node the DOM node.
	 * 
	 * @return true if the given node is a symbol and false otherwise.
	 */
	public boolean isNodeSymbol(DOMNode node) {
		if (node == null) {
			return false;
		}
		if (expressions != null && expressions.length > 0) {
			// loop for list of XPath expression and returns the first expression which
			// matches the given node.
			for (XMLSymbolExpressionFilter expression : expressions) {
				if (expression.match(node)) {
					return !expression.isExcluded();
				}
			}
		}
		// By default DOM attributes and Text nodes are excluded.
		return node.isElement() || //
				node.isDoctype() || //
				node.isProcessingInstruction() || //
				node.isProlog() || //
				node.isDTDElementDecl() || //
				node.isDTDAttListDecl() || //
				node.isDTDEntityDecl() || //
				node.isDTDNotationDecl();
	}

	/**
	 * Returns true if the filter have an expression for the given type (element,
	 * attribute, text) and false otherwise.
	 * 
	 * @param matcherType the matcher type.
	 * 
	 * @return true if the filter have an expression for the given type (element,
	 *         attribute, text) and false otherwise.
	 */
	public boolean hasFilterFor(MatcherType matcherType) {
		if (expressions == null) {
			return false;
		}
		for (XMLSymbolExpressionFilter expression : expressions) {
			if (expression.isFilterFor(matcherType)) {
				return true;
			}
		}
		return false;
	}
}
