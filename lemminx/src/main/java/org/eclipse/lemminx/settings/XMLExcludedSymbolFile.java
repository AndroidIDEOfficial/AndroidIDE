/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
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

import java.util.Objects;

/**
 * XMLExcludedSymbolFiles
 */
public class XMLExcludedSymbolFile extends PathPatternMatcher{

	public XMLExcludedSymbolFile(String pattern) {
		setPattern(pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}

		if(!(obj instanceof XMLExcludedSymbolFile)) {
			return false;
		}

		XMLExcludedSymbolFile comparison = (XMLExcludedSymbolFile) obj;
		if(!Objects.equals(comparison.getPattern(), getPattern())) {
			return false;
		}

		if(!Objects.equals(comparison.getPathMatcher(), getPathMatcher())) {
			return false;
		}
		return true;
	}

	
}