/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.telemetry;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ReferencedGrammarInfo;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.URIUtils;

public class DocumentTelemetryInfo {

	private static final String DOC_PROP_EXT = "file.extension";
	private static final String DOC_PROP_RESOLVER = "file.resolver";
	private static final String DOC_PROP_IDENTIFIER = "file.identifier";
	private static final String DOC_PROP_GRAMMAR_NONE = "file.grammar.none";
	private static final String DOC_PROP_GRAMMAR_DOCTYPE = "file.grammar.doctype";
	private static final String DOC_PROP_GRAMMAR_XMLMODEL = "file.grammar.xmlmodel";
	private static final String DOC_PROP_GRAMMAR_SCHEMALOC = "file.grammar.schemalocation";
	private static final String DOC_PROP_GRAMMAR_NONSSCHEMALOC = "file.grammar.nonsschemalocation";

	public static Map<String, Object> getDocumentTelemetryInfo (DOMDocument doc, ContentModelManager manager) {
		String uri = doc.getDocumentURI();
		int index = uri.lastIndexOf('.');
		String fileExtension = uri.substring(index + 1, uri.length()).toLowerCase();
		boolean isXML = !DOMUtils.isXSD(doc) && !DOMUtils.isDTD(uri);
		Set<ReferencedGrammarInfo> referencedGrammarInfos = manager.getReferencedGrammarInfos(doc);
		HashMap<String, Object> props = new HashMap<>();
		props.put(DOC_PROP_EXT, fileExtension);

		if (referencedGrammarInfos.isEmpty()) {
			if (isXML) {
				props.put(DOC_PROP_GRAMMAR_NONE, true);
			}
		} else {
			List<String> resolvers = new ArrayList<String>();
			List<String> identifiers = new ArrayList<String>();
			for (ReferencedGrammarInfo info : referencedGrammarInfos) {
				String kind = info.getBindingKind() == null ? "none" : info.getBindingKind();
				String resolver = info.getResolvedBy();
				String identifier = info.getIdentifierURI();
				if (isXML) {
					switch (kind) {
					case "none":
						props.put(DOC_PROP_GRAMMAR_NONE, true);
						break;
					case "doctype":
						props.put(DOC_PROP_GRAMMAR_DOCTYPE, true);
						break;
					case "xml-model":
						props.put(DOC_PROP_GRAMMAR_XMLMODEL, true);
						break;
					case "xsi:schemaLocation":
						props.put(DOC_PROP_GRAMMAR_SCHEMALOC, true);
						break;
					case "xsi:noNamespaceSchemaLocation":
						props.put(DOC_PROP_GRAMMAR_NONSSCHEMALOC, true);
						break;
					}
				}
				if (identifier != null) {
					try {
						// non-local identifiers only
						if (new URI(identifier).getScheme() != null && !URIUtils.isFileResource(identifier)) {
							int limit = Math.min(identifier.length(), 200); // 200 char limit
							identifiers.add(identifier.substring(0, limit));
						}
					} catch (URISyntaxException e) {
						// continue
					}
				}
				resolvers.add(resolver == null ? "default" : resolver);
			}

			if (resolvers.size() == 1) {
				props.put(DOC_PROP_RESOLVER, resolvers.iterator().next());
			} else {
				props.put(DOC_PROP_RESOLVER, resolvers);
			}
			if (!identifiers.isEmpty()) {
				if (identifiers.size() == 1) {
					props.put(DOC_PROP_IDENTIFIER, identifiers.iterator().next());
				} else {
					props.put(DOC_PROP_IDENTIFIER, identifiers);
				}
			}
		}
		return props;
	}
}
