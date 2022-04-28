/******************************************************************************
 * Copyright (c) 2018 TypeFox and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;

/**
 * An exception thrown when accessing the JSON-RPC communication channel fails.
 */
public class JsonRpcException extends RuntimeException {
	
	/**
	 * Whether the given exception indicates that the currently accessed stream has been closed.
	 */
	public static boolean indicatesStreamClosed(Throwable thr) {
		return thr instanceof InterruptedIOException
				|| thr instanceof ClosedChannelException
				|| thr instanceof IOException && "Pipe closed".equals(thr.getMessage())
				|| thr instanceof SocketException && 
					("Connection reset".equals(thr.getMessage()) || 
					 "Socket closed".equals(thr.getMessage()) || 
					 "Broken pipe (Write failed)".equals(thr.getMessage()))
				|| thr instanceof JsonRpcException && indicatesStreamClosed(thr.getCause());
	}

	private static final long serialVersionUID = -7952794305289314670L;
	
	public JsonRpcException(Throwable cause) {
		super(cause);
	}

}
