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
package org.eclipse.lemminx.services.extensions.codelens;

import java.util.List;

import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * CodeLens participant API.
 *
 */
public interface ICodeLensParticipant {

	void doCodeLens(ICodeLensRequest request, List<CodeLens> lenses, CancelChecker cancelChecker);

}
