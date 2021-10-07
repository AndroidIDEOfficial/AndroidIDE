/**
 * Copyright (c) 2018 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * @author Dennis Huebner <dennis.huebner@gmail.com> - Initial contribution and API
 */
package org.eclipse.lemminx;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.services.LanguageClient;

public class XMLServerSocketLauncher {

	private static final int DEFAULT_PORT = 5_008;
	
	/**
	 * Calls {@link #launch(String[])}
	 */
	public static void main(String[] args) throws Exception {
		new XMLServerSocketLauncher().launch(args);
	}
	
	/**
	 * Launches {@link XMLLanguageServer} using asynchronous server-socket channel and makes it accessible through the JSON
	 * RPC protocol defined by the LSP.
	 * 
	 * @param args standard launch arguments. may contain <code>--port</code> argument to change the default port 5008
	 */
	public void launch(String[] args) throws Exception {
		AsynchronousServerSocketChannel _open = AsynchronousServerSocketChannel.open();
		int _port = getPort(args);
		InetSocketAddress _inetSocketAddress = new InetSocketAddress("0.0.0.0", _port);
		final AsynchronousServerSocketChannel serverSocket = _open.bind(_inetSocketAddress);
		while (true) {
			final AsynchronousSocketChannel socketChannel = serverSocket.accept().get();
			final InputStream in = Channels.newInputStream(socketChannel);
			final OutputStream out = Channels.newOutputStream(socketChannel);
			final ExecutorService executorService = Executors.newCachedThreadPool();
			XMLLanguageServer languageServer = new XMLLanguageServer();
			final Launcher<LanguageClient> launcher = Launcher.createIoLauncher(languageServer, LanguageClient.class,
					in, out, executorService, (MessageConsumer it) -> {
						return it;
					});
			languageServer.setClient(launcher.getRemoteProxy());
			launcher.startListening();
		}
	}

	protected int getPort(final String... args) {
		for (int i = 0; (i < (args.length - 1)); i++) {
			String _get = args[i];
			boolean _equals = Objects.equals(_get, "--port");
			if (_equals) {
				return Integer.parseInt(args[(i + 1)]);
			}
		}
		return DEFAULT_PORT;
	}
}
