/**
 *  Copyright (c) 2018 Red Hat, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.settings.capabilities;

import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.CODE_ACTION_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.CODE_LENS_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.COMPLETION_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.DEFAULT_COMPLETION_OPTIONS;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.DEFAULT_LINK_OPTIONS;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.DEFINITION_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.DOCUMENT_HIGHLIGHT_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.DOCUMENT_SYMBOL_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.FOLDING_RANGE_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.FORMATTING_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.FORMATTING_RANGE_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.HOVER_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.LINKED_EDITING_RANGE_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.LINK_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.REFERENCES_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.RENAME_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.SELECTION_RANGE_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_CODE_ACTION;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_CODE_LENS;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_COMPLETION;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_DEFINITION;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_DOCUMENT_SYMBOL;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_FOLDING_RANGE;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_HIGHLIGHT;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_HOVER;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_LINK;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_LINKED_EDITING_RANGE;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_REFERENCES;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_RENAME;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_SELECTION_RANGE;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TEXT_DOCUMENT_TYPEDEFINITION;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.TYPEDEFINITION_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.WORKSPACE_EXECUTE_COMMAND;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.WORKSPACE_EXECUTE_COMMAND_ID;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.WORKSPACE_WATCHED_FILES;
import static org.eclipse.lemminx.settings.capabilities.ServerCapabilitiesConstants.WORKSPACE_WATCHED_FILES_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.lemminx.XMLTextDocumentService;
import org.eclipse.lemminx.client.ExtendedClientCapabilities;
import org.eclipse.lemminx.settings.XMLCodeLensSettings;
import org.eclipse.lemminx.settings.XMLFormattingOptions;
import org.eclipse.lemminx.settings.XMLSymbolSettings;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.DidChangeWatchedFilesRegistrationOptions;
import org.eclipse.lsp4j.ExecuteCommandOptions;
import org.eclipse.lsp4j.FileSystemWatcher;
import org.eclipse.lsp4j.Registration;
import org.eclipse.lsp4j.RegistrationParams;
import org.eclipse.lsp4j.Unregistration;
import org.eclipse.lsp4j.UnregistrationParams;
import org.eclipse.lsp4j.services.LanguageClient;

/**
 * Manager for capability related tasks
 *
 * A capability is a service (Formatting, Highlighting, ...) that the server is
 * able to provide. This server will tell the client about the services it is
 * capable of.
 */
public class XMLCapabilityManager {

	private final Set<String> registeredCapabilities = new HashSet<>(3);
	private final LanguageClient languageClient;
	private final XMLTextDocumentService textDocumentService;

	private ClientCapabilitiesWrapper clientWrapper;

	public XMLCapabilityManager(LanguageClient languageClient, XMLTextDocumentService textDocumentService) {
		this.languageClient = languageClient;
		this.textDocumentService = textDocumentService;
	}

	/**
	 * Creates and sets a {@link ClientCapabilitiesWrapper} instance formed from
	 * clientCapabilities
	 *
	 * @param clientCapabilities
	 * @param extendedClientCapabilities
	 */
	public void setClientCapabilities(ClientCapabilities clientCapabilities,
			ExtendedClientCapabilities extendedClientCapabilities) {
		this.clientWrapper = new ClientCapabilitiesWrapper(clientCapabilities, extendedClientCapabilities);
	}

	public ClientCapabilitiesWrapper getClientCapabilities() {
		if (this.clientWrapper == null) {
			this.clientWrapper = new ClientCapabilitiesWrapper();
		}
		return this.clientWrapper;
	}

	public void toggleCapability(boolean enabled, String id, String capability, Object options) {
		if (enabled) {
			registerCapability(id, capability, options);
		} else {
			unregisterCapability(id, capability);
		}
	}

	public void unregisterCapability(String id, String method) {
		if (registeredCapabilities.remove(id)) {
			Unregistration unregistration = new Unregistration(id, method);
			UnregistrationParams unregistrationParams = new UnregistrationParams(
					Collections.singletonList(unregistration));
			languageClient.unregisterCapability(unregistrationParams);
		}
	}

	public void registerCapability(String id, String method) {
		registerCapability(id, method, null);
	}

	public void registerCapability(String id, String method, Object options) {
		if (registeredCapabilities.add(id)) {
			Registration registration = new Registration(id, method, options);
			RegistrationParams registrationParams = new RegistrationParams(Collections.singletonList(registration));
			languageClient.registerCapability(registrationParams);
		}
	}

	/**
	 * Registers all dynamic capabilities that the server does not support client
	 * side preferences turning on/off
	 */
	public void initializeCapabilities() {
		if (this.getClientCapabilities().isCodeActionDynamicRegistered()) {
			registerCapability(CODE_ACTION_ID, TEXT_DOCUMENT_CODE_ACTION);
		}
		if (this.getClientCapabilities().isCompletionDynamicRegistrationSupported()) {
			registerCapability(COMPLETION_ID, TEXT_DOCUMENT_COMPLETION, DEFAULT_COMPLETION_OPTIONS);
		}
		if (this.getClientCapabilities().isDocumentHighlightDynamicRegistered()) {
			registerCapability(DOCUMENT_HIGHLIGHT_ID, TEXT_DOCUMENT_HIGHLIGHT);
		}
		if (this.getClientCapabilities().isRangeFoldingDynamicRegistrationSupported()) {
			registerCapability(FOLDING_RANGE_ID, TEXT_DOCUMENT_FOLDING_RANGE);
		}
		if (this.getClientCapabilities().isHoverDynamicRegistered()) {
			registerCapability(HOVER_ID, TEXT_DOCUMENT_HOVER);
		}
		if (this.getClientCapabilities().isLinkDynamicRegistrationSupported()) {
			registerCapability(LINK_ID, TEXT_DOCUMENT_LINK, DEFAULT_LINK_OPTIONS);
		}
		if (this.getClientCapabilities().isRenameDynamicRegistrationSupported()) {
			registerCapability(RENAME_ID, TEXT_DOCUMENT_RENAME);
		}
		if (this.getClientCapabilities().isDefinitionDynamicRegistered()) {
			registerCapability(DEFINITION_ID, TEXT_DOCUMENT_DEFINITION);
		}
		if (this.getClientCapabilities().isDefinitionDynamicRegistered()) {
			registerCapability(TYPEDEFINITION_ID, TEXT_DOCUMENT_TYPEDEFINITION);
		}
		if (this.getClientCapabilities().isReferencesDynamicRegistrationSupported()) {
			registerCapability(REFERENCES_ID, TEXT_DOCUMENT_REFERENCES);
		}
		if (this.getClientCapabilities().isLinkedEditingRangeDynamicRegistered()) {
			registerCapability(LINKED_EDITING_RANGE_ID, TEXT_DOCUMENT_LINKED_EDITING_RANGE);
		}
		if (this.getClientCapabilities().isDidChangeWatchedFilesRegistered()) {
			registerWatchedFiles();
		}

		syncDynamicCapabilitiesWithPreferences();
	}

	private void registerWatchedFiles() {
		List<FileSystemWatcher> watchers = new ArrayList<>(2);
		watchers.add(new FileSystemWatcher("**/*.xsd"));
		watchers.add(new FileSystemWatcher("**/*.dtd"));
		DidChangeWatchedFilesRegistrationOptions options = new DidChangeWatchedFilesRegistrationOptions(watchers);
		registerCapability(WORKSPACE_WATCHED_FILES_ID, WORKSPACE_WATCHED_FILES, options);
	}

	public void registerExecuteCommand(List<String> commands) {
		registerCapability(WORKSPACE_EXECUTE_COMMAND_ID, WORKSPACE_EXECUTE_COMMAND,
				new ExecuteCommandOptions(commands));
	}

	/**
	 * Registers(indicates the servers ability to support the service) all
	 * capabilities that have the ability to be turned on/off on the client side
	 * through preferences.
	 *
	 * In the case the preference is set to off/false this server will tell the
	 * cliet it does not support this capability.
	 *
	 * If a capability is not dynamic, it's handled by
	 * {@link ServerCapabilitiesInitializer}
	 */
	public void syncDynamicCapabilitiesWithPreferences() {
		XMLFormattingOptions formattingPreferences = this.textDocumentService.getSharedFormattingSettings();

		if (this.getClientCapabilities().isFormattingDynamicRegistrationSupported()) {
			toggleCapability(formattingPreferences.isEnabled(), FORMATTING_ID,
					ServerCapabilitiesConstants.TEXT_DOCUMENT_FORMATTING, null);
		}

		if (this.getClientCapabilities().isRangeFormattingDynamicRegistrationSupported()) {
			toggleCapability(formattingPreferences.isEnabled(), FORMATTING_RANGE_ID,
					ServerCapabilitiesConstants.TEXT_DOCUMENT_RANGE_FORMATTING, null);
		}

		if (this.getClientCapabilities().isSelectionRangeDynamicRegistered()) {
			toggleCapability(true, SELECTION_RANGE_ID, TEXT_DOCUMENT_SELECTION_RANGE, null);
		}

		XMLSymbolSettings symbolSettings = this.textDocumentService.getSharedSymbolSettings();
		if (this.getClientCapabilities().isDocumentSymbolDynamicRegistrationSupported()) {
			toggleCapability(symbolSettings.isEnabled(), DOCUMENT_SYMBOL_ID, TEXT_DOCUMENT_DOCUMENT_SYMBOL, null);
		}

		XMLCodeLensSettings codeLensSettings = this.textDocumentService.getSharedCodeLensSettings();
		if (this.getClientCapabilities().isCodeLensDynamicRegistrationSupported()) {
			toggleCapability(codeLensSettings.isEnabled(), CODE_LENS_ID, TEXT_DOCUMENT_CODE_LENS, null);
		}
	}

	public Set<String> getRegisteredCapabilities() {
		return this.registeredCapabilities;
	}

}