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

/**
 * CodeLens kind supported by the client.
 * 
 * @author Angelo ZERR
 * 
 * @see https://github.com/microsoft/language-server-protocol/issues/788
 */
public class CodeLensKind {

	private CodeLensKind() {
	}

	public static final String References = "references";

	public static final String Implementations = "implementations";

	public static final String Association = "association";

	public static final String OpenUri = "open.uri";
}
