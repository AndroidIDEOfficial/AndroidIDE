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
package org.eclipse.lemminx.commons;

import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * Multiple cancel checker.
 * 
 * @author Angelo ZERR
 *
 */
public class MultiCancelChecker implements CancelChecker {

	private CancelChecker[] checkers;

	public MultiCancelChecker(CancelChecker... checkers) {
		this.checkers = checkers;
	}

	@Override
	public void checkCanceled() {
		for (CancelChecker cancelChecker : checkers) {
			cancelChecker.checkCanceled();
		}
	}
}