/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others. All rights reserved. This program
 * and the accompanying materials which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Red Hat Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.lemminx.customservice;

import org.eclipse.lsp4j.Range;

public class AutoCloseTagResponse {
	public String snippet;
	public Range range;

	public AutoCloseTagResponse(String snippet, Range range) {
		this.snippet = snippet;
		this.range = range;
	}

	public AutoCloseTagResponse(String snippet) {
		this.snippet = snippet;
	}
}