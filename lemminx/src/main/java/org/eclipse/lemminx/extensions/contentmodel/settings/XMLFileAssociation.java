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
package org.eclipse.lemminx.extensions.contentmodel.settings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.lemminx.settings.PathPatternMatcher;
import org.eclipse.lemminx.uriresolver.IExternalGrammarLocationProvider;
import org.eclipse.lemminx.utils.DOMUtils;

/**
 * XML file association between a XML file pattern (glob) and an XML Schema file
 * (systemId).
 **/
public class XMLFileAssociation extends PathPatternMatcher {

	private transient Map<String, String> externalSchemaLocation;
	private String systemId;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
		this.externalSchemaLocation = null;
	}

	public Map<String, String> getExternalSchemaLocation() {
		if (externalSchemaLocation == null) {
			this.externalSchemaLocation = new HashMap<>();
			if (DOMUtils.isXSD(systemId)) {
				this.externalSchemaLocation.put(IExternalGrammarLocationProvider.NO_NAMESPACE_SCHEMA_LOCATION,
						systemId);
			} else {
				this.externalSchemaLocation.put(IExternalGrammarLocationProvider.DOCTYPE, systemId);
			}
		}
		return externalSchemaLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPattern() == null) ? 0 : getPattern().hashCode());
		result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		XMLFileAssociation other = (XMLFileAssociation) obj;
		String thisPattern = getPattern();
		String otherPattern = other.getPattern();
		if (thisPattern == null) {
			if (otherPattern != null) {
				return false;
			}
		} else if (!thisPattern.equals(otherPattern)) {
			return false;
		}
		if (systemId == null) {
			if (other.systemId != null) {
				return false;
			}
		} else if (!systemId.equals(other.systemId)) {
			return false;
		}
		return true;
	}
}
