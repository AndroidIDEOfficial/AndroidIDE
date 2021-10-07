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
package org.eclipse.lemminx.services.extensions.diagnostics;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * SAX content handler to stop immediatly the XML parsing by checking
 * {@link CancelChecker#checkCanceled()}
 *
 */
public class LSPContentHandler implements ContentHandler {

	private final CancelChecker monitor;

	public LSPContentHandler(CancelChecker monitor) {
		this.monitor = monitor;
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void startDocument() throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		monitor.checkCanceled();
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void endDocument() throws SAXException {
		monitor.checkCanceled();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		monitor.checkCanceled();
	}
}
