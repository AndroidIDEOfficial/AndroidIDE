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
package org.eclipse.lemminx.extensions.catalog;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.lemminx.client.InvalidPathWarner;
import org.eclipse.lemminx.client.PathFeature;
import org.eclipse.lemminx.extensions.contentmodel.settings.ContentModelSettings;
import org.eclipse.lemminx.services.IXMLNotificationService;
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lemminx.utils.FilesUtils;
import org.eclipse.lsp4j.InitializeParams;

/**
 * XMl Catalog plugin.
 */
public class XMLCatalogPlugin implements IXMLExtension {

	private XMLCatalogURIResolverExtension uiResolver;
	private final IDocumentLinkParticipant documentLinkParticipant;

	private InvalidPathWarner pathWarner;

	public XMLCatalogPlugin() {
		documentLinkParticipant = new XMLCatalogDocumentLinkParticipant();
	}

	@Override
	public void doSave(ISaveContext context) {
		Object initializationOptionsSettings = context.getSettings();
		ContentModelSettings cmSettings = ContentModelSettings.getContentModelXMLSettings(initializationOptionsSettings);
		if (cmSettings == null) {
			return;
		}
		validateCatalogPaths(cmSettings);
	}

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		uiResolver = new XMLCatalogURIResolverExtension(registry);
		registry.getResolverExtensionManager().registerResolver(uiResolver);
		IXMLNotificationService notificationService = registry.getNotificationService();
		if (notificationService != null) {
			this.pathWarner = new InvalidPathWarner(notificationService);
		}
		registry.registerDocumentLinkParticipant(documentLinkParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.getResolverExtensionManager().unregisterResolver(uiResolver);
	}

	private void validateCatalogPaths(ContentModelSettings cmSettings) {
		if (this.pathWarner == null) {
			return; // happen when notification service is not available
		}
		String[] catalogs = cmSettings.getCatalogs();
		Set<String> invalidCatalogs = Arrays.stream(catalogs).filter(c -> {
			return Files.notExists(FilesUtils.getPath(c));
		}).collect(Collectors.toSet());
		
		if (invalidCatalogs.size() > 0) {
			this.pathWarner.onInvalidFilePath(invalidCatalogs, PathFeature.CATALOGS);
		} else {
			this.pathWarner.evictKey(PathFeature.CATALOGS);
		}
	}
}
