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
package org.eclipse.lemminx.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.lemminx.services.IXMLNotificationService;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.MessageType;

/**
 * This class sends a warning to the client via a jsonrpc notification
 * 
 * The cache is a Map<String, Set<String>>
 * 
 * where the keys are the feature names and the value is a set of file paths for
 * which notifications have been sent already
 */
public class InvalidPathWarner extends AbstractXMLNotifier {

	public InvalidPathWarner(IXMLNotificationService notificationService ) {
		super(notificationService);
	}

	/**
	 * Sends the invalid path warning to the client only if the provided
	 * <code>invalidPaths</code> set does not match the <code>feature</code>'s cache set
	 * 
	 * After sending the warning, the <code>feature</code>'s cache set is replaced
	 * with the provided <code>invalidPaths</code> set
	 * 
	 * @param invalidPaths the set of invalid paths
	 * @param feature      the feature
	 */
	public void onInvalidFilePath(Set<String> invalidPaths, PathFeature feature) {
		if (existsInCache(feature, invalidPaths)) {
			return;
		}
		sendInvalidFilePathWarning(invalidPaths, feature);
		setCacheValues(feature, new HashSet<String>(invalidPaths));
	}

	private void sendInvalidFilePathWarning(Set<String> invalidPaths, PathFeature feature) {
		String message = createWarningMessage(feature.getSettingId(), invalidPaths);

		Command command = new Command("Configure setting", ClientCommands.OPEN_SETTINGS,
					Collections.singletonList(feature.getSettingId()));

		super.sendNotification(message, MessageType.Error, command);
	}

	private static String createWarningMessage(String settingId, Set<String> invalidPaths) {
		StringBuilder message = new StringBuilder("Invalid path");
		if (invalidPaths.size() > 1) {
			message.append('s');
		}
		message.append(" for setting '").append(settingId).append("': ");

		List<String> list = new ArrayList<>(invalidPaths); 
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				message.append(", ");
			}
			message.append("'").append(list.get(i)).append("'");
		}
		return message.toString();
	}
}