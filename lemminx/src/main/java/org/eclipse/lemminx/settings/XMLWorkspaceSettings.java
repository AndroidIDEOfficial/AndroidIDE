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
package org.eclipse.lemminx.settings;

import org.eclipse.lsp4j.WorkspaceClientCapabilities;

/**
 * A wrapper around LSP {@link WorkspaceClientCapabilities}.
 *
 */
public class XMLWorkspaceSettings {

	private WorkspaceClientCapabilities workspace;

	public void setCapabilities(WorkspaceClientCapabilities workspace) {
		this.workspace = workspace;
	}

	/**
	 * Returns true if the client can support the given resource operation kind and
	 * false otherwise.
	 * 
	 * @param kind the resource operation kind.
	 * @return true if the client can support the given resource operation kind and
	 *         false otherwise.
	 */
	public boolean isResourceOperationSupported(String kind) {
		return workspace != null && workspace.getWorkspaceEdit() != null
				&& workspace.getWorkspaceEdit().getResourceOperations() != null
				&& workspace.getWorkspaceEdit().getResourceOperations().contains(kind);
	}

}