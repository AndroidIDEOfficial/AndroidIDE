/*******************************************************************************
 * Copyright (c) 2016-2017 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.lemminx.customservice;

import java.util.List;

import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * An ActionableNotification class.
 * 
 * An instance of this class is sent to the client as a parameter
 * for the xml/actionableNotification notification.
 * 
 * See {@link org.eclipse.lemminx.customservice.XMLLanguageClientAPI}
 */
public class ActionableNotification {

	/**
	 * The message type. See {@link MessageType}.
	 *
	 */
	@SerializedName("severity")
	@Expose
	private MessageType severity;
	/**
	 * The actual message
	 *
	 */
	@SerializedName("message")
	@Expose
	private String message;

	/**
	 * Optional data
	 *
	 */
	@SerializedName("data")
	@Expose
	private Object data;


	/**
	 * Optional commands
	 *
	 */
	@SerializedName("commands")
	@Expose
	private List<Command> commands;

	/**
	 * The message severity. See {@link MessageType}.
	 *
	 * @return
	 *     The severity
	 */
	public MessageType getSeverity() {
		return severity;
	}

	/**
	 * The message severity. See {@link MessageType}.
	 *
	 * @param severity
	 *     The message severity
	 */
	public void setSeverity(MessageType severity) {
		this.severity = severity;
	}

	public ActionableNotification withSeverity(MessageType severity) {
		this.severity = severity;
		return this;
	}

	/**
	 * The actual message
	 *
	 * @return
	 *     The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The actual message
	 *
	 * @param message
	 *     The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public ActionableNotification withMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	public ActionableNotification withData(Object data) {
		this.data = data;
		return this;
	}

	/**
	 * @return the commands
	 */
	public List<Command> getCommands() {
		return commands;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	public ActionableNotification withCommands(List<Command> commands) {
		this.commands = commands;
		return this;
	}

	@Override
	public String toString() {
		return MessageJsonHandler.toString(this);
	}
}