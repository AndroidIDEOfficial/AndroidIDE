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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.services.WorkspaceService;

/**
 * Workspace Service participant API.
 * @since 0.14.2
 */
public interface IWorkspaceServiceParticipant {

	/**
	 * Receive and handle notification about workspace folder changing on client side.
	 * @param params the workspace folders change description
	 * @see WorkspaceService#didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams)
	 */
	public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params);
}
