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
package org.eclipse.lemminx.extensions.xsd.participants.diagnostics;

import java.io.IOException;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarLoader;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * Xerces {@link XMLGrammarPreparser} doesn't provide the capability to override
 * the {@link XMLErrorReporter} which is required to get XMLl This class
 * overrides {@link XMLGrammarPreparser#preparseGrammar(String, XMLInputSource)}
 * to use LSP error reporter.
 *
 */
public class LSPXMLGrammarPreparser extends XMLGrammarPreparser {

	private XMLErrorReporter errorReporter;

	@Override
	public Grammar preparseGrammar(String type, XMLInputSource is) throws XNIException, IOException {
		// if (fLoaders.containsKey(type)) {
		// XMLGrammarLoaderContainer xglc = (XMLGrammarLoaderContainer) getLoader(type);
		XMLGrammarLoader gl = getLoader(type);
		// if (xglc.modCount != fModCount) {
		// make sure gl's been set up with all the "basic" properties:
		gl.setProperty(SYMBOL_TABLE, fSymbolTable);
		gl.setProperty(ENTITY_RESOLVER, fEntityResolver);
		gl.setProperty(ERROR_REPORTER, errorReporter);
		// potentially, not all will support this one...
		if (fGrammarPool != null) {
			try {
				gl.setProperty(GRAMMAR_POOL, fGrammarPool);
			} catch (Exception e) {
				// too bad...
			}
		}
		// xglc.modCount = fModCount;
		// }
		return gl.loadGrammar(is);
		// }
		// return null;
	}

	@Override
	public void setProperty(String propId, Object value) {
		if ("http://apache.org/xml/properties/internal/error-reporter".equals(propId)) {
			this.setErrorReporter((XMLErrorReporter) value);
		}
		super.setProperty(propId, value);
	}

	public void setErrorReporter(XMLErrorReporter errorReporter) {
		this.errorReporter = errorReporter;
	}

}
