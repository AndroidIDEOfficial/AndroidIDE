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

import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;

import org.eclipse.lemminx.services.IXMLNotificationService;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.MessageType;

/**
 * LimitExceededWarner sends a warning to the client via a jsonrpc notification
 * 
 * Cache is a Map<String, Set<String>>
 * 
 * where the keys are the feature names and the values are the file paths for
 * which notifications have been sent already
 */
public class LimitExceededWarner extends AbstractXMLNotifier {

	public LimitExceededWarner(IXMLNotificationService notificationService) {
		super(notificationService);
	}

	/**
	 * Sends the limit exceeded warning to the client only if the provided
	 * uri does not exist in the <code>feature</code>'s cache set
	 * 
	 * After sending the warning, the uri is added to the <code>feature</code>'s cache set
	 * 
	 * @param uri            the file uri
	 * @param featureName    the feature name
	 */
	public void onResultLimitExceeded(String uri, LimitFeature feature) {
		int resultLimit = 0;
		switch(feature) {
			case SYMBOLS:
				resultLimit = getSharedSettings().getSymbolSettings().getMaxItemsComputed();
		}
		onResultLimitExceeded(uri, resultLimit, feature);
	}
	
	/**
	 * Sends the limit exceeded warning to the client only if the provided
	 * uri does not exist in the cache
	 * 
	 * After sending the warning, the uri is added to the cache
	 * 
	 * @param uri         the file uri
	 * @param resultLimit the result limit
	 * @param feature     the feature
	 */
	public void onResultLimitExceeded(String uri, int resultLimit, LimitFeature feature) {
		if (existsInCache(feature, uri)) {
			return;
		}
		sendLimitExceededWarning(uri, resultLimit, feature);
		addToCache(feature, uri);
	}

	/**
	 * Send limit exceeded warning to client
	 * 
	 * @param uri         the file uri
	 * @param resultLimit the result limit
	 * @param feature     the feature 
	 */
	private void sendLimitExceededWarning(String uri, int resultLimit, LimitFeature feature) {
		String filename = Paths.get(URI.create(uri)).getFileName().toString();
		String message = filename != null ? filename + ": " : "";
		message += "For performance reasons, " + feature.getName() + " have been limited to " + resultLimit
				+ " items.\nIf a new limit is set, please close and reopen this file to recompute " + feature.getName() + ".";

		// create command that opens the settings UI on the client side, in order to
		// quickly edit the setting
		Command command = new Command("Configure limit", ClientCommands.OPEN_SETTINGS,
				Collections.singletonList(feature.getSettingId()));
		
		super.sendNotification(message, MessageType.Info ,command);
	}
}