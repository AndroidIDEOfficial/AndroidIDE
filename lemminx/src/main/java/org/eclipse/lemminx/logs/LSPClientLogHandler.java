/**
 *  Copyright (c) 2018 Red Hat, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Nikolas Komonen <nikolaskomonen@gmail.com>, Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.logs;

import static java.lang.System.lineSeparator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * LSP client JUL {@link Handler}
 */
public class LSPClientLogHandler extends Handler {

	private LanguageClient languageClient;

	public LSPClientLogHandler(LanguageClient languageClient) {
		this.languageClient = languageClient;
	}

	public LanguageClient getLanguageClient() {
		return this.languageClient;
	}

	@Override
	public void publish(LogRecord record) {
		if (languageClient == null) {
			return;
		}

		String msg = formatRecord(record, Locale.getDefault());
		MessageType messageType = getMessageType(record.getLevel());
		MessageParams mp = new MessageParams(messageType, msg);
		languageClient.logMessage(mp);

	}

	public static String formatRecord(LogRecord record, Locale locale) {
		DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss", locale);
		long date = record.getMillis();
		String formattedDate = formatter.format(date);
		StringBuilder sb = new StringBuilder();
		sb.append(formattedDate).append(" ").append(record.getSourceClassName()).append(" ")
				.append(record.getSourceMethodName()).append("()").append(lineSeparator())
				.append("Message: " + record.getMessage());
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			sb.append(sw);
		}

		return sb.toString();
	}

	private static MessageType getMessageType(Level level) {
		if (level == Level.WARNING) {
			return MessageType.Warning;
		}
		if (level == Level.SEVERE) {
			return MessageType.Error;
		}
		return MessageType.Info;
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LSPClientLogHandler)) {
			return false;
		}
		LSPClientLogHandler c = (LSPClientLogHandler) o;
		return this.languageClient == c.getLanguageClient();
	}

}