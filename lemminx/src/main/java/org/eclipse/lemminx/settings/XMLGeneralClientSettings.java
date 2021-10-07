/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.settings;

import org.eclipse.lemminx.utils.JSONUtility;

/**
 * Class to hold all settings from the client side.
 *
 * See https://github.com/eclipse/lemminx/blob/master/docs/Configuration.md for more
 * information.
 *
 * This class is created through the deseralization of a JSON object. Each
 * internal setting must be represented by a class and have:
 *
 * 1) A constructor with no parameters
 *
 * 2) The JSON key/parent for the settings must have the same name as a varible.
 *
 * eg: {"format" : {...}, "completion" : {...}}
 *
 * In this class must exist both a "format" and "completion" variable with the
 * appropriate Class to represent the value of each key
 *
 */
public class XMLGeneralClientSettings {

	private LogsSettings logs;

	private XMLFormattingOptions format;

	private XMLCompletionSettings completion;

	private ServerSettings server;

	private XMLSymbolSettings symbols;

	private XMLCodeLensSettings codeLens;

	private XMLPreferences preferences;

	private XMLTelemetrySettings telemetry;

	public void setLogs(LogsSettings logs) {
		this.logs = logs;
	}

	public LogsSettings getLogs() {
		return logs;
	}

	public XMLSymbolSettings getSymbols() {
		return symbols;
	}

	public void setSymbols(XMLSymbolSettings symbols) {
		this.symbols = symbols;
	}

	/**
	 * Returns the code lens settings.
	 *
	 * @return the code lens settings.
	 */
	public XMLCodeLensSettings getCodeLens() {
		return codeLens;
	}

	/**
	 * Sets the code lens settings.
	 *
	 * @param codeLens
	 */
	public void setCodeLens(XMLCodeLensSettings codeLens) {
		this.codeLens = codeLens;
	}

	/**
	 * Sets the formatting options
	 *
	 * @param format
	 */
	public void setFormat(XMLFormattingOptions format) {
		this.format = format;
	}

	/**
	 * Returns the formatting options
	 *
	 * @return the formatting options
	 */
	public XMLFormattingOptions getFormat() {
		return format;
	}

	/**
	 * Sets the completion settings
	 *
	 * @param completion
	 */
	public void setCompletion(XMLCompletionSettings completion) {
		this.completion = completion;
	}

	/**
	 * Returns the completion settings
	 *
	 * @return the completion settings
	 */
	public XMLCompletionSettings getCompletion() {
		return completion;
	}

	/**
	 * Returns the XML preferences
	 *
	 * @return the XML preferences
	 */
	public XMLPreferences getPreferences() {
		return preferences;
	}

	/**
	 * Sets the XML preferences
	 *
	 * @param preferences the XML preferences
	 */
	public void setPreferences(XMLPreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Returns the server
	 *
	 * @return the server
	 */
	public ServerSettings getServer() {
		return server;
	}

	/**
	 * Sets the server
	 *
	 * @param server
	 */
	public void setServer(ServerSettings server) {
		this.server = server;
	}

	/**
	 * Returns the telemetry settings
	 *
	 * @return the telemetry settings
	 */
	public XMLTelemetrySettings getTelemetry() {
		return telemetry;
	}

	/**
	 * Sets the telemetry settings
	 *
	 * @param telemetry the telemetry settings
	 */
	public void setTelemetry(XMLTelemetrySettings telemetry) {
		this.telemetry = telemetry;
	}

	/**
	 * Returns a new instance of <code>XMLGeneralClientSettings</code>
	 * with contents from <code>initializationOptionsSettings</code>
	 *
	 * @param initializationOptionsSettings
	 * @return a new instance of <code>XMLGeneralClientSettings</code>
	 * with contents from <code>initializationOptionsSettings</code>
	 */
	public static XMLGeneralClientSettings getGeneralXMLSettings(Object initializationOptionsSettings) {
		return JSONUtility.toModel(initializationOptionsSettings, XMLGeneralClientSettings.class);
	}
}