/**
 * Copyright © 2009-2017, Jonathan Hedley <jonathan@hedley.net>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.eclipse.lemminx.extensions.xsd.contentmodel;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

/**
 * HTML to plain-text. Uses jsoup to convert HTML input to lightly-formatted
 * plain-text. This is a fork of Jsoup's <a href=
 * "https://github.com/jhy/jsoup/blob/842977c381b8d48bf12719e3f5cf6fd669379957/src/main/java/org/jsoup/examples/HtmlToPlainText.java">HtmlToPlainText</a>
 *
 * @author Jonathan Hedley, jonathan@hedley.net
 */
public class HtmlToPlainText {

	/**
	 * Format an Element to plain-text
	 * @param element the root element to format
	 * @return formatted text
	 */
	public String getPlainText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor.traverse(formatter, element); // walk the DOM, and call .head() and .tail() for each node

		return formatter.toString();
	}

	// the formatting rules, implemented in a breadth-first DOM traverse
	private class FormattingVisitor implements NodeVisitor {
		private StringBuilder accum = new StringBuilder(); // holds the accumulated text
		private int listNesting;
		// hit when the node is first seen
		@Override
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode) {
				append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
			} else if (name.equals("ul")) {
				listNesting++;
			} else if (name.equals("li")) {
				append("\n ");
				for (int i = 1; i < listNesting; i++) {
					append("  ");
				}
				if (listNesting == 1) {
					append("* ");
				} else {
					append("- ");
				}
			} else if (name.equals("dt")) {
				append("  ");
			} else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr")) {
				append("\n");
			}
		}

		// hit when all of the node's children (if any) have been visited
		@Override
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
				append("\n");
			} else if (StringUtil.in(name, "th", "td")) {
				append(" ");
			} else if (name.equals("a")) {
				append(String.format(" <%s>", node.absUrl("href")));
			} else if (name.equals("ul")) {
				listNesting--;
			}
		}

		// appends text to the string builder with a simple word wrap method
		private void append(String text) {
			if (text.equals(" ") &&
					(accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
			{
				return; // don't accumulate long runs of empty spaces
			}
			accum.append(text);
		}

		@Override
		public String toString() {
			return accum.toString();
		}
	}
} 