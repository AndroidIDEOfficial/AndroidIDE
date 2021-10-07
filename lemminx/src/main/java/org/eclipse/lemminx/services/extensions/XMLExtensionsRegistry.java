/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.services.extensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.services.IXMLNotificationService;
import org.eclipse.lemminx.services.IXMLValidationService;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lemminx.services.extensions.format.IFormatterParticipant;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lemminx.services.extensions.save.ISaveContext.SaveContextType;
import org.eclipse.lemminx.telemetry.TelemetryManager;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lsp4j.InitializeParams;

/**
 * XML extensions registry.
 *
 */
public class XMLExtensionsRegistry implements IComponentProvider {

	private static final Logger LOGGER = Logger.getLogger(XMLExtensionsRegistry.class.getName());

	private final URIResolverExtensionManager resolverExtensionManager;
	private final Collection<IXMLExtension> extensions;
	private final List<ICompletionParticipant> completionParticipants;
	private final List<IHoverParticipant> hoverParticipants;
	private final List<IDiagnosticsParticipant> diagnosticsParticipants;
	private final List<ICodeActionParticipant> codeActionsParticipants;
	private final List<IDocumentLinkParticipant> documentLinkParticipants;
	private final List<IDefinitionParticipant> definitionParticipants;
	private final List<ITypeDefinitionParticipant> typeDefinitionParticipants;
	private final List<IReferenceParticipant> referenceParticipants;
	private final List<ICodeLensParticipant> codeLensParticipants;
	private final List<IHighlightingParticipant> highlightingParticipants;
	private final List<IRenameParticipant> renameParticipants;
	private final List<IFormatterParticipant> formatterParticipants;
	private final List<ISymbolsProviderParticipant> symbolsProviderParticipants;
	private final List<IWorkspaceServiceParticipant> workspaceServiceParticipants;
	private final List<IDocumentLifecycleParticipant> documentLifecycleParticipants;
	private IXMLDocumentProvider documentProvider;
	private IXMLValidationService validationService;
	private IXMLCommandService commandService;

	private InitializeParams params;

	private ISaveContext initialSaveContext;

	private boolean initialized;

	private IXMLNotificationService notificationService;

	private final Map<Class<?>, Object> components;

	private TelemetryManager telemetryManager;

	public XMLExtensionsRegistry() {
		extensions = new ArrayList<>();
		completionParticipants = new ArrayList<>();
		hoverParticipants = new ArrayList<>();
		diagnosticsParticipants = new ArrayList<>();
		codeActionsParticipants = new ArrayList<>();
		documentLinkParticipants = new ArrayList<>();
		definitionParticipants = new ArrayList<>();
		typeDefinitionParticipants = new ArrayList<>();
		referenceParticipants = new ArrayList<>();
		codeLensParticipants = new ArrayList<>();
		highlightingParticipants = new ArrayList<>();
		renameParticipants = new ArrayList<>();
		formatterParticipants = new ArrayList<>();
		symbolsProviderParticipants = new ArrayList<>();
		workspaceServiceParticipants = new ArrayList<>();
		documentLifecycleParticipants = new ArrayList<>();
		resolverExtensionManager = new URIResolverExtensionManager();
		components = new HashMap<>();
		telemetryManager = new TelemetryManager(null);
		registerComponent(resolverExtensionManager);
	}

	public void registerComponent(Object component) {
		this.components.put(component.getClass(), component);
	}

	@Override
	public <T> T getComponent(Class clazz) {
		return (T) components.get(clazz);
	}

	public void initializeParams(InitializeParams params) {
		if (initialized) {
			extensions.stream().forEach(extension -> {
				try {
					extension.start(params, this);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Error while starting extension <" + extension.getClass().getName() + ">",
							e);
				}
			});
		} else {
			this.params = params;
		}
	}

	public void doSave(ISaveContext saveContext) {
		if (initialized) {
			extensions.stream().forEach(extension -> extension.doSave(saveContext));
		} else if (this.initialSaveContext == null
				|| (saveContext != null && saveContext.getType() == SaveContextType.SETTINGS)) {
			// capture initial configuration iff:
			// 1. the saveContext is for configuration, not document save
			// 2. we haven't captured settings before
			this.initialSaveContext = saveContext;
		}
	}

	public Collection<IXMLExtension> getExtensions() {
		initializeIfNeeded();
		return extensions;
	}

	public Collection<ICompletionParticipant> getCompletionParticipants() {
		initializeIfNeeded();
		return completionParticipants;
	}

	public Collection<IHoverParticipant> getHoverParticipants() {
		initializeIfNeeded();
		return hoverParticipants;
	}

	public Collection<IDiagnosticsParticipant> getDiagnosticsParticipants() {
		initializeIfNeeded();
		return diagnosticsParticipants;
	}

	public List<ICodeActionParticipant> getCodeActionsParticipants() {
		initializeIfNeeded();
		return codeActionsParticipants;
	}

	public Collection<IDocumentLinkParticipant> getDocumentLinkParticipants() {
		initializeIfNeeded();
		return documentLinkParticipants;
	}

	public Collection<IDefinitionParticipant> getDefinitionParticipants() {
		initializeIfNeeded();
		return definitionParticipants;
	}

	public Collection<ITypeDefinitionParticipant> getTypeDefinitionParticipants() {
		initializeIfNeeded();
		return typeDefinitionParticipants;
	}

	public Collection<IReferenceParticipant> getReferenceParticipants() {
		initializeIfNeeded();
		return referenceParticipants;
	}

	public Collection<ICodeLensParticipant> getCodeLensParticipants() {
		initializeIfNeeded();
		return codeLensParticipants;
	}

	public Collection<IHighlightingParticipant> getHighlightingParticipants() {
		initializeIfNeeded();
		return highlightingParticipants;
	}

	public Collection<IRenameParticipant> getRenameParticipants() {
		initializeIfNeeded();
		return renameParticipants;
	}

	public Collection<IFormatterParticipant> getFormatterParticipants() {
		initializeIfNeeded();
		return formatterParticipants;
	}

	public Collection<ISymbolsProviderParticipant> getSymbolsProviderParticipants() {
		initializeIfNeeded();
		return symbolsProviderParticipants;
	}

	/**
	 * Return the registered workspace service participants.
	 * 
	 * @return the registered workspace service participants.
	 * @since 0.14.2
	 */
	public Collection<IWorkspaceServiceParticipant> getWorkspaceServiceParticipants() {
		initializeIfNeeded();
		return workspaceServiceParticipants;
	}

	/**
	 * Return the registered document lifecycle participants.
	 * 
	 * @return the registered document lifecycle participants.
	 * @since 0.18.0
	 */
	public List<IDocumentLifecycleParticipant> getDocumentLifecycleParticipants() {
		initializeIfNeeded();
		return documentLifecycleParticipants;
	}

	public void initializeIfNeeded() {
		if (initialized) {
			return;
		}
		initialize();
	}

	private synchronized void initialize() {

		if (initialized) {
			return;
		}

		if (commandService != null) {
			commandService.beginCommandsRegistration();
		}
		Iterator<IXMLExtension> extensions = ServiceLoader.load(IXMLExtension.class).iterator();
		while (extensions.hasNext()) {
			try {
				registerExtension(extensions.next());
			} catch (ServiceConfigurationError e) {
				LOGGER.log(Level.SEVERE, "Error while instantiating extension", e);
			}
		}
		initialized = true;
		if (commandService != null) {
			commandService.endCommandsRegistration();
		}
	}

	void registerExtension(IXMLExtension extension) {
		try {
			extensions.add(extension);
			extension.start(params, this);
			if (initialSaveContext != null) {
				extension.doSave(initialSaveContext);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while initializing extension <" + extension.getClass().getName() + ">", e);
		}
	}

	void unregisterExtension(IXMLExtension extension) {
		try {
			extensions.remove(extension);
			extension.stop(this);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while stopping extension <" + extension.getClass().getName() + ">", e);
		}
	}

	/**
	 * Unregisters all registered extensions.
	 */
	public void dispose() {
		// Copy the list of extensions to avoid ConcurrentModificationError
		List<IXMLExtension> extensionReferences = new ArrayList<>();
		extensions.forEach(extensionReferences::add);
		extensionReferences.forEach(this::unregisterExtension);
	}

	public void registerCompletionParticipant(ICompletionParticipant completionParticipant) {
		completionParticipants.add(completionParticipant);
	}

	public void unregisterCompletionParticipant(ICompletionParticipant completionParticipant) {
		completionParticipants.remove(completionParticipant);
	}

	public void registerHoverParticipant(IHoverParticipant hoverParticipant) {
		hoverParticipants.add(hoverParticipant);
	}

	public void unregisterHoverParticipant(IHoverParticipant hoverParticipant) {
		hoverParticipants.remove(hoverParticipant);
	}

	public void registerDiagnosticsParticipant(IDiagnosticsParticipant diagnosticsParticipant) {
		diagnosticsParticipants.add(diagnosticsParticipant);
	}

	public void unregisterDiagnosticsParticipant(IDiagnosticsParticipant diagnosticsParticipant) {
		diagnosticsParticipants.remove(diagnosticsParticipant);
	}

	public void registerCodeActionParticipant(ICodeActionParticipant codeActionsParticipant) {
		codeActionsParticipants.add(codeActionsParticipant);
	}

	public void unregisterCodeActionParticipant(ICodeActionParticipant codeActionsParticipant) {
		codeActionsParticipants.remove(codeActionsParticipant);
	}

	public void registerDocumentLinkParticipant(IDocumentLinkParticipant documentLinkParticipant) {
		documentLinkParticipants.add(documentLinkParticipant);
	}

	public void unregisterDocumentLinkParticipant(IDocumentLinkParticipant documentLinkParticipant) {
		documentLinkParticipants.remove(documentLinkParticipant);
	}

	public void registerDefinitionParticipant(IDefinitionParticipant definitionParticipant) {
		definitionParticipants.add(definitionParticipant);
	}

	public void unregisterDefinitionParticipant(IDefinitionParticipant definitionParticipant) {
		definitionParticipants.remove(definitionParticipant);
	}

	public void registerTypeDefinitionParticipant(ITypeDefinitionParticipant typeDefinitionParticipant) {
		typeDefinitionParticipants.add(typeDefinitionParticipant);
	}

	public void unregisterTypeDefinitionParticipant(ITypeDefinitionParticipant typeDefinitionParticipant) {
		typeDefinitionParticipants.remove(typeDefinitionParticipant);
	}

	public void registerReferenceParticipant(IReferenceParticipant referenceParticipant) {
		referenceParticipants.add(referenceParticipant);
	}

	public void unregisterReferenceParticipant(IReferenceParticipant referenceParticipant) {
		referenceParticipants.remove(referenceParticipant);
	}

	public void registerCodeLensParticipant(ICodeLensParticipant codeLensParticipant) {
		codeLensParticipants.add(codeLensParticipant);
	}

	public void unregisterCodeLensParticipant(ICodeLensParticipant codeLensParticipant) {
		codeLensParticipants.remove(codeLensParticipant);
	}

	public void registerHighlightingParticipant(IHighlightingParticipant highlightingParticipant) {
		highlightingParticipants.add(highlightingParticipant);
	}

	public void unregisterHighlightingParticipant(IHighlightingParticipant highlightingParticipant) {
		highlightingParticipants.remove(highlightingParticipant);
	}

	public void registerRenameParticipant(IRenameParticipant renameParticipant) {
		renameParticipants.add(renameParticipant);
	}

	public void unregisterRenameParticipant(IRenameParticipant renameParticipant) {
		renameParticipants.remove(renameParticipant);
	}

	public void registerFormatterParticipant(IFormatterParticipant formatterParticipant) {
		formatterParticipants.add(formatterParticipant);
	}

	public void unregisterFormatterParticipant(IFormatterParticipant formatterParticipant) {
		formatterParticipants.remove(formatterParticipant);
	}

	public void registerSymbolsProviderParticipant(ISymbolsProviderParticipant symbolsProviderParticipant) {
		symbolsProviderParticipants.add(symbolsProviderParticipant);
	}

	public void unregisterSymbolsProviderParticipant(ISymbolsProviderParticipant symbolsProviderParticipant) {
		symbolsProviderParticipants.remove(symbolsProviderParticipant);
	}

	/**
	 * Register a new workspace service participant
	 * 
	 * @param workspaceServiceParticipant the participant to register
	 * @since 0.14.2
	 */
	public void registerWorkspaceServiceParticipant(IWorkspaceServiceParticipant workspaceServiceParticipant) {
		workspaceServiceParticipants.add(workspaceServiceParticipant);
	}

	/**
	 * Unregister a new workspace service participant.
	 * 
	 * @param workspaceServiceParticipant the participant to unregister
	 * @since 0.14.2
	 */
	public void unregisterWorkspaceServiceParticipant(IWorkspaceServiceParticipant workspaceServiceParticipant) {
		workspaceServiceParticipants.remove(workspaceServiceParticipant);
	}

	/**
	 * Register a new document lifecycle participant
	 * 
	 * @param documentLifecycleParticipant the participant to register
	 * @since 0.18.0
	 */
	public void registerDocumentLifecycleParticipant(IDocumentLifecycleParticipant documentLifecycleParticipant) {
		documentLifecycleParticipants.add(documentLifecycleParticipant);
	}

	/**
	 * Unregister a new document lifecycle participant.
	 * 
	 * @param documentLifecycleParticipant the participant to unregister
	 * @since 0.18.0
	 */
	public void unregisterDocumentLifecycleParticipant(IDocumentLifecycleParticipant documentLifecycleParticipant) {
		documentLifecycleParticipants.remove(documentLifecycleParticipant);
	}

	/**
	 * Returns the XML Document provider and null otherwise.
	 * 
	 * @return the XML Document provider and null otherwise.
	 */
	public IXMLDocumentProvider getDocumentProvider() {
		return documentProvider;
	}

	/**
	 * Set the XML Document provider
	 * 
	 * @param documentProvider XML Document provider
	 */
	public void setDocumentProvider(IXMLDocumentProvider documentProvider) {
		this.documentProvider = documentProvider;
	}

	public URIResolverExtensionManager getResolverExtensionManager() {
		return resolverExtensionManager;
	}

	/**
	 * Returns the notification service
	 * 
	 * @return the notification service
	 */
	public IXMLNotificationService getNotificationService() {
		return notificationService;
	}

	/**
	 * Sets the notification service
	 * 
	 * @param notificationService the new notification service
	 */
	public void setNotificationService(IXMLNotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * Returns the XML document validation service
	 * 
	 * @return the validation service
	 */
	public IXMLValidationService getValidationService() {
		return validationService;
	}

	/**
	 * Sets the XML document validation service
	 * 
	 * @param validationService
	 */
	public void setValidationService(IXMLValidationService validationService) {
		this.validationService = validationService;
	}

	/**
	 * Returns the LS command service
	 * 
	 * @return the command service
	 */
	public IXMLCommandService getCommandService() {
		return commandService;
	}

	/**
	 * Sets the LS command service
	 * 
	 * @param commandService
	 */
	public void setCommandService(IXMLCommandService commandService) {
		this.commandService = commandService;
	}

	public TelemetryManager getTelemetryManager() {
		return telemetryManager;
	}

	public void setTelemetryManager(TelemetryManager telemetryManager) {
		this.telemetryManager = telemetryManager;
	}

}