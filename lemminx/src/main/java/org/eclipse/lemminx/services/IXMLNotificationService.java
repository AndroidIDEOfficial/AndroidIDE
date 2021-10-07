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
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.MessageType;

/**
 * Notification service API
 */
public interface IXMLNotificationService {

	/**
	 * Sends a notification to the client with the provided {@code message}
	 * 
	 * If the client supports actionable notifications, the provided {@code commands}
	 * will be provided alongside the notification
	 * 
	 * See {@link org.eclipse.lemminx.customservice.ActionableNotification} and
	 * {@link org.eclipse.lemminx.customservice.XMLLanguageClientAPI#actionableNotification}
	 * 
	 * @param message     the message to send
	 * @param messageType the message type
	 * @param commands    the commands to send alongside the notification
	 */
	void sendNotification(String message, MessageType messageType, Command... commands);

	/**
	 * Returns the current SharedSettings instance
	 * @return the current SharedSettings instance
	 */
	SharedSettings getSharedSettings();
}
