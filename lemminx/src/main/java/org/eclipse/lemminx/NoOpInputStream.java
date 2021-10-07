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
import java.io.InputStream;

/**
 * No-Op {@link InputStream}
 * 
 * @author Fred Bricon
 */
class NoOpInputStream extends InputStream {

	@Override
	public int read() throws IOException {
		return -1;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return -1;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return -1;
	}
	
	@Override
	public int available() throws IOException {
		return 0;
	}
	
	@Override
 	public synchronized void mark(int readlimit) {
 	}
	
	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void reset() throws IOException {
	}
	
	@Override
	public long skip(long n) throws IOException {
		return 0;
	}
	
	@Override
	public void close() throws IOException {
	}

}
