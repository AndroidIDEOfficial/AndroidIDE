/**
 *  Copyright (c) 2021 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.contentmodel.settings;

/**
 * XML Namespaces settings.
 *
 */
public class XMLNamespacesSettings {

	public XMLNamespacesSettings() {
		setEnabled(NamespacesEnabled.always);
	}

	private NamespacesEnabled enabled;

	public void setEnabled(NamespacesEnabled enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
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
		XMLNamespacesSettings other = (XMLNamespacesSettings) obj;
		if (enabled != other.enabled) {
			return false;
		}
		return true;
	}

	public NamespacesEnabled getEnabled() {
		return enabled;
	}
}
