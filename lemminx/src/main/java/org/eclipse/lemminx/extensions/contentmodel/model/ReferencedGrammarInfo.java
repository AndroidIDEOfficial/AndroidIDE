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
package org.eclipse.lemminx.extensions.contentmodel.model;

import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider.Identifier;
import org.eclipse.lemminx.uriresolver.ResolvedURIInfo;
import org.eclipse.lemminx.uriresolver.URIResolverExtension;

/**
 * Class which holds a referenced grammar information.
 * 
 * <p>
 * In other words, it gives information about
 * 
 * <ul>
 * <li>which XSD, DTD file is bound to the DOM document.</li>
 * <li>which binding strategies is used (catalog, file association,
 * xsi:schemaLocation, xsi:noNamespaceSchemaLocation, DOCTYPE).</li>
 * <li>the cache file path (for remote grammar with http://)</li>
 * 
 * </p>
 */
public class ReferencedGrammarInfo {

	private final ResolvedURIInfo resolvedURIInfo;
	private final GrammarCacheInfo grammarCacheInfo;

	private final Identifier identifier;

	public ReferencedGrammarInfo(ResolvedURIInfo resolvedURIInfo, GrammarCacheInfo grammarCacheInfo,
			Identifier identifier) {
		this.resolvedURIInfo = resolvedURIInfo;
		this.grammarCacheInfo = grammarCacheInfo;
		this.identifier = identifier;
	}

	/**
	 * Returns the resolved URI information (the result of the resolve and the
	 * resolution strategy (ex : catalog, file association, etc)).
	 * 
	 * @return the resolved URI information (the result of the resolve and the
	 *         resolution strategy
	 */
	public ResolvedURIInfo getResolvedURIInfo() {
		return resolvedURIInfo;
	}

	/**
	 * Returns the grammar cache information and null otherwise. (grammar is not a
	 * remote grammar with http://)
	 * 
	 * @return the grammar cache information and null otherwise.
	 */
	public GrammarCacheInfo getGrammarCacheInfo() {
		return grammarCacheInfo;
	}

	/**
	 * Returns the identifier (xml-model, xsi:schemaLocation,
	 * xsi:noNamespaceSchemaLocation, DOCTYPE) and null otherwise.
	 * 
	 * @return the identifier (xml-model, xsi:schemaLocation,
	 *         xsi:noNamespaceSchemaLocation, DOCTYPE) and null otherwise.
	 */
	public Identifier getIdentifier() {
		return identifier;
	}

	/**
	 * Returns true if the grammar is in the .lemminx cache and false otherwise.
	 * 
	 * @return true if the grammar is in the .lemminx cache and false otherwise.
	 */
	public boolean isInCache() {
		return grammarCacheInfo != null;
	}

	/**
	 * Returns the system or public identifier URI and null otherwise.
	 * 
	 * @return the system or public identifier URI and null otherwise.
	 */
	public String getIdentifierURI() {
		if (identifier == null) {
			return null;
		}
		String publicId = identifier.getPublicId();
		String systemId = identifier.getSystemId();
		return publicId != null ? publicId : systemId;
	}

	/**
	 * Returns the binding kind and null otherwise. Binding kind can have values:
	 * 
	 * <ul>
	 * <li>xsi:schemaLocation</li>
	 * <li>xsi:noNamespaceSchemaLocation</li>
	 * <li>doctype</li>
	 * <li>xml-model</li>
	 * </ul>
	 * 
	 * @return the binding kind and null otherwise.
	 */
	public String getBindingKind() {
		return identifier != null ? identifier.getKind() : null;
	}

	/**
	 * Returns the resolver which is used to resolve the identifier URI and null
	 * otherwise. Resolved by can have values:
	 * 
	 * <ul>
	 * <li>catalog</li>
	 * <li>file association</li>
	 * <li>embedded catalog.xsd</li>
	 * <li>embedded xml.xsd</li>
	 * <li>embedded xslt.xsd</li>
	 * </ul>
	 * 
	 * @return the resolver which is used to resolve the identifier URI and null
	 *         otherwise.
	 */
	public String getResolvedBy() {
		String resolvedBy = resolvedURIInfo != null ? resolvedURIInfo.getResolverName() : null;
		return URIResolverExtension.DEFAULT.equals(resolvedBy) ? null : resolvedBy;
	}

	/**
	 * Returns the binding kind and resolved by label.
	 * 
	 * @param info the referenced grammar information.
	 * 
	 * @return the binding kind and resolved by label.
	 */
	public static String getBindingKindAndResolvedBy(ReferencedGrammarInfo info) {
		StringBuilder bindingName = new StringBuilder();
		String bindingKind = info.getBindingKind();
		boolean hasKind = bindingKind != null;
		if (hasKind) {
			bindingName.append(bindingKind);
		}
		String resolverBy = info.getResolvedBy();
		if (resolverBy != null) {
			if (bindingName.length() > 0) {
				bindingName.append(" ");
			}
			bindingName.append("with ");
			bindingName.append(resolverBy);
		}
		return bindingName.toString();
	}
}
