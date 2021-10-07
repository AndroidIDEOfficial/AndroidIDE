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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.lemminx.services.IXMLNotificationService;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.MessageType;

/** Sends a notification to the client via a jsonrpc notification
 * while managing cache
 */
public abstract class AbstractXMLNotifier {

	private final IXMLNotificationService notificationService;

	protected final Map<IXMLSettingFeature /* feature name */, Set<String>> cache;

	public AbstractXMLNotifier(IXMLNotificationService notificationService) {
		this.notificationService = notificationService;
		this.cache = new HashMap<>();
	}

	/**
	 * Returns true if <code>value</code> exists in the cache set
	 * identified by <code>key</code> and false otherwise
	 * 
	 * @param key   the key for the cache set
	 * @param value the value to check
	 * @return true if <code>value</code> exists in the cache set
	 * identified by <code>key</code> and false otherwise
	 */
	public boolean existsInCache(IXMLSettingFeature key, String value) {
		return this.cache.get(key) != null && this.cache.get(key).contains(value);
	}

	/**
	 * Returns true if <code>values</code> is equal to the cache set
	 * identified by <code>key</code> and false otherwise
	 * 
	 * @param key   the key for the cache set
	 * @param values the values set to check
	 * @return true if <code>values</code> is equal to the cache set
	 * identified by <code>key</code> and false otherwise
	 */
	public boolean existsInCache(IXMLSettingFeature key, Set<String> values) {
		return this.cache.get(key) != null && this.cache.get(key).equals(values);
	}

	/**
	 * Adds <code>value</code> to the cache set identified by <code>key</code>
	 * 
	 * If such cache set does not exist, this method will initialize it with
	 * <code>value</code> added
	 * 
	 * @param key   the key for the cache set to add to
	 * @param value the value to add
	 */
	public void addToCache(IXMLSettingFeature key, String value) {
		if (this.cache.get(key) == null) {
			this.cache.put(key, new HashSet<String>());
		}
		this.cache.get(key).add(value);
	}

	/**
	 * Sets the cache set identified by <code>key</code>
	 * 
	 * @param key    the key for the cache set
	 * @param values the cache set
	 */
	public void setCacheValues(IXMLSettingFeature key, Set<String> values) {
		evictKey(key);
		this.cache.put(key, new HashSet<String>(values));
	}


	/**
	 * Evicts the set identified by the provided key, from the cache
	 * 
	 * @param key
	 */
	public void evictKey(IXMLSettingFeature key) {
		Set<String> setToEvict = this.cache.get(key);
		if (setToEvict != null) {
			setToEvict.clear();
		}
	}

	/**
	 * Evicts the provided value from all cached sets
	 * 
	 * @param value
	 */
	public void evictValue(String value) {
		for (Set<String> set: this.cache.values()) {
			set.remove(value);
		}
	}

	protected void sendNotification(String message, MessageType messageType, Command... commands) {
		notificationService.sendNotification(message, messageType, commands);
	}

	protected SharedSettings getSharedSettings() {
		return notificationService.getSharedSettings();
	}
}