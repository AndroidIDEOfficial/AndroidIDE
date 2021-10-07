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
package org.eclipse.lemminx.client;

import org.eclipse.lsp4j.DynamicRegistrationCapabilities;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * Extended capabilities specific to the `textDocument/codeLens` request. This capability doesn't belong to LSP specification. See proposal at 
 * https://github.com/microsoft/language-server-protocol/issues/788
 * 
 * @author Angelo ZERR
 * 
 * @see https://github.com/microsoft/language-server-protocol/issues/788
 */
@SuppressWarnings("all")
public class ExtendedCodeLensCapabilities extends DynamicRegistrationCapabilities {
	/**
	 * Specific capabilities for the `CodeLensKind` in the `textDocument/codeLens`
	 * request.
	 */
	private CodeLensKindCapabilities codeLensKind;

	public ExtendedCodeLensCapabilities() {
	}

	public ExtendedCodeLensCapabilities(final Boolean dynamicRegistration) {
		super(dynamicRegistration);
	}

	public ExtendedCodeLensCapabilities(final CodeLensKindCapabilities codeLensKind) {
		this.codeLensKind = codeLensKind;
	}

	public ExtendedCodeLensCapabilities(final CodeLensKindCapabilities codeLensKind,
			final Boolean dynamicRegistration) {
		super(dynamicRegistration);
		this.codeLensKind = codeLensKind;
	}

	/**
	 * Specific capabilities for the `CodeLensKind` in the `textDocument/codeLens`
	 * request.
	 */
	@Pure
	public CodeLensKindCapabilities getCodeLensKind() {
		return this.codeLensKind;
	}

	/**
	 * Specific capabilities for the `CodeLensKind` in the `textDocument/codeLens`
	 * request.
	 */
	public void setCodeLensKind(final CodeLensKindCapabilities codeLensKind) {
		this.codeLensKind = codeLensKind;
	}

	@Override
	@Pure
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this);
		b.add("codeLensKind", this.codeLensKind);
		b.add("dynamicRegistration", getDynamicRegistration());
		return b.toString();
	}

	@Override
	@Pure
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		ExtendedCodeLensCapabilities other = (ExtendedCodeLensCapabilities) obj;
		if (this.codeLensKind == null) {
			if (other.codeLensKind != null) {
				return false;
			}
		} else if (!this.codeLensKind.equals(other.codeLensKind)) {
			return false;
		}
		return true;
	}

	@Override
	@Pure
	public int hashCode() {
		return 31 * super.hashCode() + ((this.codeLensKind == null) ? 0 : this.codeLensKind.hashCode());
	}
}
