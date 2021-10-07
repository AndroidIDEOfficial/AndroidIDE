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
package org.eclipse.lemminx;

import java.io.PrintStream;

/**
 * No-Op {@link PrintStream}.
 * 
 * @author Fred Bricon
 */
class NoOpPrintStream extends PrintStream {

	public NoOpPrintStream() {
		super(new NoOpOutputStream());
	}

}
