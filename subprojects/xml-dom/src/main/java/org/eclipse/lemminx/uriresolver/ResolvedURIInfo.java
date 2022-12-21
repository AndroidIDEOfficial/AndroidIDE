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
package org.eclipse.lemminx.uriresolver;

/**
 * Resolver URI information.
 *
 */
public class ResolvedURIInfo {

	private final String resolvedURI;

	private final URIResolverExtension resolver;

	public ResolvedURIInfo(String resolvedURI, URIResolverExtension resolver) {
		this.resolvedURI = resolvedURI;
		this.resolver = resolver;
	}

	/**
	 * Returns the resolved URI.
	 * 
	 * @return the resolved URI.
	 */
	public String getResolvedURI() {
		return resolvedURI;
	}

	/**
	 * Returns the resolver name {@link URIResolverExtension#getName()} (ex :
	 * 'default', 'file association) used to resolve the URI.
	 * 
	 * @return the resolver name {@link URIResolverExtension#getName()} (ex :
	 *         'default', 'file association) used to resolve the URI.
	 */
	public String getResolverName() {
		return resolver.getName();
	}

}
