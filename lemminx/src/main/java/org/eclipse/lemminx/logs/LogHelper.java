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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.lemminx.settings.LogsSettings;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * LogHelper
 */
public class LogHelper {

	private static final String ROOT_LOGGER = "";

	// This will apply to all child loggers
	public static void initializeRootLogger(LanguageClient newLanguageClient, LogsSettings settings) {
		if (newLanguageClient == null) {
			return;
		}

		Logger logger = Logger.getLogger(ROOT_LOGGER);
		unregisterAllHandlers(logger.getHandlers());
		logger.setLevel(getLogLevel());
		logger.setUseParentHandlers(false);// Stops output to console

		// Configure logging LSP client handler
		if (settings != null) {
			if (settings.isClient()) {
				try {
					logger.addHandler(LogHelper.getClientHandler(newLanguageClient));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			// Configure logging for file
			String path = settings.getFile();
			if (!StringUtils.isBlank(path)) {
				createDirectoryPath(path);
				try {
					FileHandler fh = LogHelper.getFileHandler(path);
					logger.addHandler(fh);
				} catch (SecurityException | IOException e) {
					logger.warning("Error at creation of FileHandler for logging");
				}
			} else {
				logger.info("Log file could not be created, path not provided");
			}
		}
	}

	private static Level getLogLevel() {
		String logLevel = System.getProperty("log.level", "info").toLowerCase();
		switch (logLevel) {
		case "info":
			return Level.INFO;
		case "off":
			return Level.OFF;
		case "all":
		case "debug":
		case "fine":
		case "finer":
		case "finest":
			return Level.FINEST;
		case "warn":
		case "warning":
			return Level.WARNING;
		case "error":
		case "fatal":
			return Level.SEVERE;
		}
		return Level.INFO;
	}

	private static void createDirectoryPath(String path) {
		Path parentPath = Paths.get(path).normalize().getParent();
		if (parentPath != null) {
			try {
				Files.createDirectories(parentPath);
			} catch (IOException e) {

			}
		}
	}

	public static LSPClientLogHandler getClientHandler(LanguageClient languageClient) {
		if (languageClient == null) {
			return null;
		}
		return new LSPClientLogHandler(languageClient);
	}

	public static FileHandler getFileHandler(String filePath) throws SecurityException, IOException {
		if (filePath == null || filePath.isEmpty()) {
			throw new IllegalArgumentException("Incorrect file path provided");
		}
		File f = new File(filePath);
		if (f.isDirectory()) {
			throw new IllegalArgumentException("Provided path was a directory");
		}
		if (!f.exists() || f.canWrite()) {
			FileHandler fh = null;
			fh = new FileHandler(filePath, true);
			fh.setFormatter(new SimpleFormatter());
			fh.setLevel(Level.INFO);
			return fh;
		}
		throw new IOException("Cannot write file since it cannot be written to");
	}

	public static void unregisterHandler(Handler handler) {
		if (handler == null) {
			return;
		}
		handler.close();
		Logger.getLogger(ROOT_LOGGER).removeHandler(handler);
	}

	public static void unregisterAllHandlers(Handler[] handlers) {
		if (handlers == null) {
			return;
		}
		for (Handler h : handlers) {
			unregisterHandler(h);
		}
	}
}