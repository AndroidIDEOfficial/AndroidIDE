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

import java.io.IOException;
import java.io.OutputStream;

/**
 * No-Op {@link OutputStream}.
 * 
 * @author Fred Bricon
 */
class NoOpOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
	}

	@Override
	public void write(byte[] b, int off, int len) {
	}

	@Override
	public void write(byte[] b) throws IOException {
	}

}
