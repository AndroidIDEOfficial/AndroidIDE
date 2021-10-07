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
package org.eclipse.lemminx.extensions.contentmodel;

import java.util.Objects;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.commands.AssociateGrammarCommand;
import org.eclipse.lemminx.extensions.contentmodel.commands.CheckBoundGrammarCommand;
import org.eclipse.lemminx.extensions.contentmodel.commands.CheckFilePatternCommand;
import org.eclipse.lemminx.extensions.contentmodel.commands.XMLValidationAllFilesCommand;
import org.eclipse.lemminx.extensions.contentmodel.commands.XMLValidationFileCommand;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelCodeActionParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelCodeLensParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelCompletionParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelDocumentLinkParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelHoverParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelSymbolsProviderParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.ContentModelTypeDefinitionParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.DocumentTelemetryParticipant;
import org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics.ContentModelDiagnosticsParticipant;
import org.eclipse.lemminx.extensions.contentmodel.settings.ContentModelSettings;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.services.IXMLValidationService;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.services.extensions.IHoverParticipant;
import org.eclipse.lemminx.services.extensions.ITypeDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.InitializeParams;

/**
 * Content model plugin extension to provide:
 *
 * <ul>
 * <li>completion based on XML Schema, DTD...</li>
 * <li>hover based on XML Schema</li>
 * <li>diagnostics based on on XML Schema, DTD...</li>
 * </ul>
 */
public class ContentModelPlugin implements IXMLExtension {

	private final ICompletionParticipant completionParticipant;

	private final IHoverParticipant hoverParticipant;

	private final IDiagnosticsParticipant diagnosticsParticipant;

	private final ICodeActionParticipant codeActionParticipant;

	private IDocumentLinkParticipant documentLinkParticipant;

	private final ITypeDefinitionParticipant typeDefinitionParticipant;

	private ContentModelSymbolsProviderParticipant symbolsProviderParticipant;

	private ICodeLensParticipant codeLensParticipant;

	ContentModelManager contentModelManager;

	private ContentModelSettings cmSettings;

	private XMLValidationSettings currentValidationSettings;

	private DocumentTelemetryParticipant documentTelemetryParticipant;

	public ContentModelPlugin() {
		completionParticipant = new ContentModelCompletionParticipant();
		hoverParticipant = new ContentModelHoverParticipant();
		diagnosticsParticipant = new ContentModelDiagnosticsParticipant(this);
		codeActionParticipant = new ContentModelCodeActionParticipant();
		typeDefinitionParticipant = new ContentModelTypeDefinitionParticipant();
	}

	@Override
	public void doSave(ISaveContext context) {
		if (context.getType() == ISaveContext.SaveContextType.DOCUMENT) {
			// The save is done for a given XML file
			String documentURI = context.getUri();
			DOMDocument document = context.getDocument(documentURI);
			if (document != null && DOMUtils.isCatalog(document)) {
				// the XML document which has changed is a XML catalog.
				// 1) refresh catalogs
				contentModelManager.refreshCatalogs();
			}
			// 2) Validate all opened XML files except the catalog which have changed
			context.collectDocumentToValidate(d -> {
				DOMDocument xml = context.getDocument(d.getDocumentURI());
				xml.resetGrammar();
				return !documentURI.equals(d.getDocumentURI());
			});
		} else {
			// Settings
			updateSettings(context);
		}
	}

	private void updateSettings(ISaveContext saveContext) {
		Object initializationOptionsSettings = saveContext.getSettings();
		cmSettings = ContentModelSettings.getContentModelXMLSettings(initializationOptionsSettings);
		if (cmSettings != null) {
			updateSettings(cmSettings, saveContext);
		} else {
			currentValidationSettings = null;
		}
	}

	private void updateSettings(ContentModelSettings settings, ISaveContext context) {
		if (settings.getCatalogs() != null) {
			// Update XML catalog settings
			boolean catalogPathsChanged = contentModelManager.setCatalogs(settings.getCatalogs());
			if (catalogPathsChanged) {
				// Validate all opened XML files
				context.collectDocumentToValidate(d -> {
					DOMDocument xml = context.getDocument(d.getDocumentURI());
					if (xml == null) {
						return false;
					}
					xml.resetGrammar();
					return true;
				});
			}
		}
		if (settings.getFileAssociations() != null) {
			// Update XML file associations
			boolean fileAssociationsChanged = contentModelManager.setFileAssociations(settings.getFileAssociations());
			if (fileAssociationsChanged) {
				// Validate all opened XML files
				context.collectDocumentToValidate(d -> {
					DOMDocument xml = context.getDocument(d.getDocumentURI());
					xml.resetGrammar();
					return true;
				});
			}
		}
		// Update use cache, only if it is set in the settings.
		Boolean useCache = settings.isUseCache();
		if (useCache != null) {
			contentModelManager.setUseCache(useCache);
		}
		// Update symbols
		boolean showReferencedGrammars = settings.isShowReferencedGrammars();
		symbolsProviderParticipant.setEnabled(showReferencedGrammars);
		// Track if validation settings has changed
		XMLValidationSettings oldValidationSettings = currentValidationSettings;
		currentValidationSettings = cmSettings.getValidation();
		if (oldValidationSettings != null && !Objects.equals(oldValidationSettings, currentValidationSettings)) {
			context.collectDocumentToValidate(d -> true);
		}
	}

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		URIResolverExtensionManager resolverManager = registry.getComponent(URIResolverExtensionManager.class);
		contentModelManager = new ContentModelManager(resolverManager);
		registry.registerComponent(contentModelManager);
		if (params != null) {
			contentModelManager.setRootURI(params.getRootUri());
		}
		documentLinkParticipant = new ContentModelDocumentLinkParticipant(resolverManager);
		registry.registerCompletionParticipant(completionParticipant);
		registry.registerHoverParticipant(hoverParticipant);
		registry.registerDiagnosticsParticipant(diagnosticsParticipant);
		registry.registerCodeActionParticipant(codeActionParticipant);
		registry.registerDocumentLinkParticipant(documentLinkParticipant);
		registry.registerTypeDefinitionParticipant(typeDefinitionParticipant);
		symbolsProviderParticipant = new ContentModelSymbolsProviderParticipant(contentModelManager);
		registry.registerSymbolsProviderParticipant(symbolsProviderParticipant);
		codeLensParticipant = new ContentModelCodeLensParticipant(contentModelManager);
		registry.registerCodeLensParticipant(codeLensParticipant);
		documentTelemetryParticipant = new DocumentTelemetryParticipant(registry.getTelemetryManager(), contentModelManager);
		registry.registerDocumentLifecycleParticipant(documentTelemetryParticipant);

		// Register custom commands to re-validate XML files
		IXMLCommandService commandService = registry.getCommandService();
		if (commandService != null) {
			IXMLValidationService validationService = registry.getValidationService();
			IXMLDocumentProvider documentProvider = registry.getDocumentProvider();
			commandService.registerCommand(XMLValidationFileCommand.COMMAND_ID,
					new XMLValidationFileCommand(contentModelManager, documentProvider, validationService));
			commandService.registerCommand(XMLValidationAllFilesCommand.COMMAND_ID,
					new XMLValidationAllFilesCommand(contentModelManager, documentProvider, validationService));
			commandService.registerCommand(AssociateGrammarCommand.COMMAND_ID,
					new AssociateGrammarCommand(documentProvider));
			commandService.registerCommand(CheckBoundGrammarCommand.COMMAND_ID,
					new CheckBoundGrammarCommand(documentProvider));
			commandService.registerCommand(CheckFilePatternCommand.COMMAND_ID,
					new CheckFilePatternCommand());
		}
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.unregisterCompletionParticipant(completionParticipant);
		registry.unregisterHoverParticipant(hoverParticipant);
		registry.unregisterDiagnosticsParticipant(diagnosticsParticipant);
		registry.unregisterCodeActionParticipant(codeActionParticipant);
		registry.unregisterDocumentLinkParticipant(documentLinkParticipant);
		registry.unregisterTypeDefinitionParticipant(typeDefinitionParticipant);
		registry.unregisterSymbolsProviderParticipant(symbolsProviderParticipant);
		registry.unregisterCodeLensParticipant(codeLensParticipant);
		registry.unregisterDocumentLifecycleParticipant(documentTelemetryParticipant);

		// Un-register custom commands to re-validate XML files
		IXMLCommandService commandService = registry.getCommandService();
		if (commandService != null) {
			commandService.unregisterCommand(XMLValidationFileCommand.COMMAND_ID);
			commandService.unregisterCommand(XMLValidationAllFilesCommand.COMMAND_ID);
			commandService.unregisterCommand(AssociateGrammarCommand.COMMAND_ID);
			commandService.unregisterCommand(CheckBoundGrammarCommand.COMMAND_ID);
			commandService.unregisterCommand(CheckFilePatternCommand.COMMAND_ID);
		}
	}

	public ContentModelSettings getContentModelSettings() {
		return cmSettings;
	}

	public ContentModelManager getContentModelManager() {
		return contentModelManager;
	}

}
