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
package org.eclipse.lemminx;

import static org.eclipse.lsp4j.jsonrpc.CompletableFutures.computeAsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.lemminx.client.ExtendedClientCapabilities;
import org.eclipse.lemminx.client.LimitExceededWarner;
import org.eclipse.lemminx.client.LimitFeature;
import org.eclipse.lemminx.commons.ModelTextDocument;
import org.eclipse.lemminx.commons.ModelTextDocuments;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.commons.TextDocuments;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMParser;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.services.DocumentSymbolsResult;
import org.eclipse.lemminx.services.SymbolInformationResult;
import org.eclipse.lemminx.services.XMLLanguageService;
import org.eclipse.lemminx.services.extensions.save.AbstractSaveContext;
import org.eclipse.lemminx.settings.CompositeSettings;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.settings.XMLCodeLensSettings;
import org.eclipse.lemminx.settings.XMLCompletionSettings;
import org.eclipse.lemminx.settings.XMLFormattingOptions;
import org.eclipse.lemminx.settings.XMLPreferences;
import org.eclipse.lemminx.settings.XMLSymbolSettings;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.ConfigurationItem;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentLink;
import org.eclipse.lsp4j.DocumentLinkParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.LinkedEditingRangeParams;
import org.eclipse.lsp4j.LinkedEditingRanges;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SelectionRange;
import org.eclipse.lsp4j.SelectionRangeParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextDocumentClientCapabilities;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.TypeDefinitionParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.eclipse.lsp4j.services.TextDocumentService;

import com.google.gson.JsonPrimitive;

/**
 * XML text document service.
 *
 */
public class XMLTextDocumentService implements TextDocumentService {

	private static final Logger LOGGER = Logger.getLogger(XMLTextDocumentService.class.getName());

	private final XMLLanguageServer xmlLanguageServer;
	private final TextDocuments<ModelTextDocument<DOMDocument>> documents;
	private SharedSettings sharedSettings;
	private LimitExceededWarner limitExceededWarner;

	/**
	 * Enumeration for Validation triggered by.
	 *
	 */
	private static enum TriggeredBy {
		didOpen, //
		didChange, //
		Other;
	}

	/**
	 * Save context.
	 */
	class SaveContext extends AbstractSaveContext {

		private final Collection<ModelTextDocument<DOMDocument>> documentsToValidate;

		public SaveContext(Object settings) {
			super(settings);
			this.documentsToValidate = new ArrayList<>();
		}

		public SaveContext(String uri) {
			super(uri);
			this.documentsToValidate = new ArrayList<>();
		}

		@Override
		public void collectDocumentToValidate(Predicate<DOMDocument> validateDocumentPredicate) {
			documents.all().stream().forEach(document -> {
				DOMDocument xmlDocument = document.getModel().getNow(null);
				if (xmlDocument != null && !documentsToValidate.contains(document)
						&& validateDocumentPredicate.test(xmlDocument)) {
					documentsToValidate.add(document);
				}
			});
		}

		@Override
		public DOMDocument getDocument(String uri) {
			return xmlLanguageServer.getDocument(uri);
		}

		public void triggerValidationIfNeeded() {
			triggerValidationFor(documentsToValidate);
		}
	}

	final ScheduledExecutorService delayer = Executors.newScheduledThreadPool(2);
	private boolean codeActionLiteralSupport;
	private boolean hierarchicalDocumentSymbolSupport;
	private boolean definitionLinkSupport;
	private boolean typeDefinitionLinkSupport;

	public XMLTextDocumentService(XMLLanguageServer xmlLanguageServer) {
		this.xmlLanguageServer = xmlLanguageServer;
		DOMParser parser = DOMParser.getInstance();
		this.documents = new ModelTextDocuments<DOMDocument>((document, cancelChecker) -> {
			return parser.parse(document, getXMLLanguageService().getResolverExtensionManager(), true, cancelChecker);
		});
		this.sharedSettings = new SharedSettings();
		this.limitExceededWarner = null;
	}

	public void updateClientCapabilities(ClientCapabilities capabilities,
			ExtendedClientCapabilities extendedClientCapabilities) {
		if (capabilities != null) {
			TextDocumentClientCapabilities textDocumentClientCapabilities = capabilities.getTextDocument();
			if (textDocumentClientCapabilities != null) {
				sharedSettings.getCompletionSettings().setCapabilities(textDocumentClientCapabilities.getCompletion());
				sharedSettings.getFoldingSettings().setCapabilities(textDocumentClientCapabilities.getFoldingRange());
				sharedSettings.getHoverSettings().setCapabilities(textDocumentClientCapabilities.getHover());
				sharedSettings.getValidationSettings()
						.setCapabilities(textDocumentClientCapabilities.getPublishDiagnostics());
				codeActionLiteralSupport = textDocumentClientCapabilities.getCodeAction() != null
						&& textDocumentClientCapabilities.getCodeAction().getCodeActionLiteralSupport() != null;
				hierarchicalDocumentSymbolSupport = textDocumentClientCapabilities.getDocumentSymbol() != null
						&& textDocumentClientCapabilities.getDocumentSymbol()
								.getHierarchicalDocumentSymbolSupport() != null
						&& textDocumentClientCapabilities.getDocumentSymbol().getHierarchicalDocumentSymbolSupport();
				definitionLinkSupport = textDocumentClientCapabilities.getDefinition() != null
						&& textDocumentClientCapabilities.getDefinition().getLinkSupport() != null
						&& textDocumentClientCapabilities.getDefinition().getLinkSupport();
				typeDefinitionLinkSupport = textDocumentClientCapabilities.getTypeDefinition() != null
						&& textDocumentClientCapabilities.getTypeDefinition().getLinkSupport() != null
						&& textDocumentClientCapabilities.getTypeDefinition().getLinkSupport();
			}
			// Workspace settings
			sharedSettings.getWorkspaceSettings().setCapabilities(capabilities.getWorkspace());
		}
		if (extendedClientCapabilities != null) {
			// Extended client capabilities
			sharedSettings.getCodeLensSettings().setCodeLens(extendedClientCapabilities.getCodeLens());
			sharedSettings
					.setActionableNotificationSupport(extendedClientCapabilities.isActionableNotificationSupport());
			sharedSettings.setOpenSettingsCommandSupport(extendedClientCapabilities.isOpenSettingsCommandSupport());
			sharedSettings.setBindingWizardSupport(extendedClientCapabilities.isBindingWizardSupport());
		}

	}

	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			CompletionList list = getXMLLanguageService().doComplete(xmlDocument, params.getPosition(), sharedSettings,
					cancelChecker);
			return Either.forRight(list);
		});
	}

	@Override
	public CompletableFuture<Hover> hover(HoverParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().doHover(xmlDocument, params.getPosition(), sharedSettings, cancelChecker);
		});
	}

	/**
	 * Returns the indentation settings (`xml.format.tabSize` and
	 * `xml.format.insertSpaces`) for the document with the given URI.
	 *
	 * @param uri the uri of the document to get the indentation settings for
	 * @return the indentation settings (`xml.format.tabSize` and
	 *         `xml.format.insertSpaces`) for the document with the given URI
	 */
	private CompletableFuture<XMLFormattingOptions> getIndentationSettings(@NonNull String uri) {
		ConfigurationItem insertSpaces = new ConfigurationItem();
		insertSpaces.setScopeUri(uri);
		insertSpaces.setSection("xml.format.insertSpaces");

		ConfigurationItem tabSize = new ConfigurationItem();
		tabSize.setScopeUri(uri);
		tabSize.setSection("xml.format.tabSize");

		return xmlLanguageServer.getLanguageClient().configuration(new ConfigurationParams(Arrays.asList( //
				insertSpaces, tabSize //
		))).thenApply(indentationSettings -> {
			XMLFormattingOptions newOptions = new XMLFormattingOptions();
			if (indentationSettings.get(0) != null) {
				newOptions.setInsertSpaces(((JsonPrimitive) indentationSettings.get(0)).getAsBoolean());
			}
			if (indentationSettings.get(1) != null) {
				newOptions.setTabSize(((JsonPrimitive) indentationSettings.get(1)).getAsInt());
			}
			return newOptions;
		});
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().findDocumentHighlights(xmlDocument, params.getPosition(), cancelChecker);
		});
	}

	@Override
	public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
			DocumentSymbolParams params) {

		TextDocument document = getDocument(params.getTextDocument().getUri());
		if (document == null) {
			return CompletableFuture.completedFuture(null);
		}
		XMLSymbolSettings symbolSettings = sharedSettings.getSymbolSettings();

		if (!symbolSettings.isEnabled() || symbolSettings.isExcluded(document.getUri())) {
			return CompletableFuture.completedFuture(Collections.emptyList());
		}

		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			boolean resultLimitExceeded = false;
			List<Either<SymbolInformation, DocumentSymbol>> symbols = null;

			if (hierarchicalDocumentSymbolSupport) {
				DocumentSymbolsResult result = getXMLLanguageService().findDocumentSymbols(xmlDocument, symbolSettings,
						cancelChecker);
				resultLimitExceeded = result.isResultLimitExceeded();
				symbols = result //
						.stream() //
						.map(s -> {
							Either<SymbolInformation, DocumentSymbol> e = Either.forRight(s);
							return e;
						}) //
						.collect(Collectors.toList());
			} else {
				SymbolInformationResult result = getXMLLanguageService().findSymbolInformations(xmlDocument,
						symbolSettings, cancelChecker);
				resultLimitExceeded = result.isResultLimitExceeded();
				symbols = result.stream() //
						.map(s -> {
							Either<SymbolInformation, DocumentSymbol> e = Either.forLeft(s);
							return e;
						}) //
						.collect(Collectors.toList());
			}
			if (resultLimitExceeded) {
				// send warning
				getLimitExceededWarner().onResultLimitExceeded(xmlDocument.getTextDocument().getUri(),
						LimitFeature.SYMBOLS);
			}
			return symbols;
		});
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
		return computeAsync((cancelChecker) -> {
			String uri = params.getTextDocument().getUri();
			TextDocument document = getDocument(uri);
			if (document == null) {
				return null;
			}
			CompositeSettings settings = new CompositeSettings(getSharedSettings(), params.getOptions());
			return getXMLLanguageService().format(document, null, settings);
		});
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
		return computeAsync((cancelChecker) -> {
			String uri = params.getTextDocument().getUri();
			TextDocument document = getDocument(uri);
			if (document == null) {
				return null;
			}
			CompositeSettings settings = new CompositeSettings(getSharedSettings(), params.getOptions());
			return getXMLLanguageService().format(document, params.getRange(), settings);
		});
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().doRename(xmlDocument, params.getPosition(), params.getNewName());
		});
	}

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		TextDocument document = documents.onDidOpenTextDocument(params);
		triggerValidationFor(document, TriggeredBy.didOpen);
	}

	/**
	 * This method is triggered when the user types on an XML document.
	 */
	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		TextDocument document = documents.onDidChangeTextDocument(params);
		triggerValidationFor(document, TriggeredBy.didChange, params.getContentChanges());
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		TextDocumentIdentifier identifier = params.getTextDocument();
		String uri = identifier.getUri();
		DOMDocument xmlDocument = getNowDOMDocument(uri);
		// Remove the document from the cache
		documents.onDidCloseTextDocument(params);
		// Publish empty errors from the document
		xmlLanguageServer.getLanguageClient()
				.publishDiagnostics(new PublishDiagnosticsParams(uri, Collections.emptyList()));
		getLimitExceededWarner().evictValue(uri);
		// Manage didClose document lifecycle participants
		if (xmlDocument != null) {
			getXMLLanguageService().getDocumentLifecycleParticipants().forEach(participant -> {
				try {
					participant.didClose(xmlDocument);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Error while processing didClose for the participant '"
							+ participant.getClass().getName() + "'.", e);
				}
			});
		}
	}

	private DOMDocument getNowDOMDocument(String uri) {
		TextDocument document = documents.get(uri);
		if (document != null) {
			return ((ModelTextDocument<DOMDocument>) document).getModel().getNow(null);
		}
		return null;
	}

	@Override
	public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().getFoldingRanges(xmlDocument, sharedSettings.getFoldingSettings(),
					cancelChecker);
		});
	}

	@Override
	public CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().findDocumentLinks(xmlDocument);
		});
	}

	@Override
	public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(
			DefinitionParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			if (definitionLinkSupport) {
				return Either.forRight(
						getXMLLanguageService().findDefinition(xmlDocument, params.getPosition(), cancelChecker));
			}
			List<? extends Location> locations = getXMLLanguageService()
					.findDefinition(xmlDocument, params.getPosition(), cancelChecker) //
					.stream() //
					.map(locationLink -> XMLPositionUtility.toLocation(locationLink)) //
					.collect(Collectors.toList());
			return Either.forLeft(locations);
		});
	}

	@Override
	public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(
			TypeDefinitionParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			if (typeDefinitionLinkSupport) {
				return Either.forRight(
						getXMLLanguageService().findTypeDefinition(xmlDocument, params.getPosition(), cancelChecker));
			}
			List<? extends Location> locations = getXMLLanguageService()
					.findTypeDefinition(xmlDocument, params.getPosition(), cancelChecker) //
					.stream() //
					.map(locationLink -> XMLPositionUtility.toLocation(locationLink)) //
					.collect(Collectors.toList());
			return Either.forLeft(locations);
		});
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().findReferences(xmlDocument, params.getPosition(), params.getContext(),
					cancelChecker);
		});
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
		if (!sharedSettings.getCodeLensSettings().isEnabled()) {
			return CompletableFuture.completedFuture(Collections.emptyList());
		}
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().getCodeLens(xmlDocument, sharedSettings.getCodeLensSettings(),
					cancelChecker);
		});
	}

	@Override
	public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
		String uri = params.getTextDocument().getUri();
		return getIndentationSettings(uri) //
				.handle((XMLFormattingOptions indentationSettings, Throwable err) -> {
					if (indentationSettings != null) {
						sharedSettings.getFormattingSettings().merge(indentationSettings);
					}
					return null;
				}) //
				.thenCombine(computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
					return xmlDocument;
				}), (void_, xmlDocument) -> {
					return (List<Either<Command, CodeAction>>) getXMLLanguageService()
							.doCodeActions(params.getContext(), params.getRange(), xmlDocument, sharedSettings) //
							.stream() //
							.map(ca -> {
								if (codeActionLiteralSupport) {
									Either<Command, CodeAction> e = Either.forRight(ca);
									return e;
								} else {
									List<Object> arguments = Arrays.asList(uri,
											xmlDocument.getTextDocument().getVersion(),
											ca.getEdit().getDocumentChanges().get(0).getLeft().getEdits());
									Command command = new Command(ca.getTitle(), "_xml.applyCodeAction", arguments);
									Either<Command, CodeAction> e = Either.forLeft(command);
									return e;
								}
							}) //
							.collect(Collectors.toList());
				});
	}

	@Override
	public CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().getSelectionRanges(xmlDocument, params.getPositions(), cancelChecker);
		});
	}

	public CompletableFuture<LinkedEditingRanges> linkedEditingRange(LinkedEditingRangeParams params) {
		return computeDOMAsync(params.getTextDocument(), (cancelChecker, xmlDocument) -> {
			return getXMLLanguageService().findLinkedEditingRanges(xmlDocument, params.getPosition(), cancelChecker);
		});
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		computeAsync((monitor) -> {
			// A document was saved, collect documents to revalidate
			SaveContext context = new SaveContext(params.getTextDocument().getUri());
			doSave(context);

			// Manage didSave document lifecycle participants
			final DOMDocument xmlDocument = getNowDOMDocument(params.getTextDocument().getUri());
			if (xmlDocument != null) {
				getXMLLanguageService().getDocumentLifecycleParticipants().forEach(participant -> {
					try {
						participant.didSave(xmlDocument);
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, "Error while processing didSave for the participant '"
								+ participant.getClass().getName() + "'.", e);
					}
				});
			}
			return null;
		});
	}

	/**
	 * Update settings of the language service.
	 *
	 * @param settings
	 */
	public void updateSettings(Object settings) {
		SaveContext context = new SaveContext(settings);
		doSave(context);
	}

	void doSave(String uri) {
		SaveContext context = new SaveContext(uri);
		doSave(context);
	}

	/**
	 * Save settings or XML file.
	 *
	 * @param context
	 */
	void doSave(SaveContext context) {
		getXMLLanguageService().doSave(context);
		context.triggerValidationIfNeeded();
	}

	private void triggerValidationFor(Collection<ModelTextDocument<DOMDocument>> documents) {
		if (!documents.isEmpty()) {
			xmlLanguageServer.schedule(() -> {
				documents.forEach(document -> {
					try {
						validate(document.getModel().getNow(null));
					} catch (CancellationException e) {
						// Ignore the error and continue to validate other documents
					}
				});
			}, 500, TimeUnit.MILLISECONDS);
		}
	}

	private void triggerValidationFor(TextDocument document, TriggeredBy triggeredBy) {
		triggerValidationFor(document, triggeredBy, null);
	}

	@SuppressWarnings("unchecked")
	private void triggerValidationFor(TextDocument document, TriggeredBy triggeredBy,
			List<TextDocumentContentChangeEvent> changeEvents) {
		((ModelTextDocument<DOMDocument>) document).getModel()//
				.thenAcceptAsync(xmlDocument -> {
					// Validate the DOM document
					validate(xmlDocument);
					// Manage didOpen, didChange document lifecycle participants
					switch (triggeredBy) {
					case didOpen:
						getXMLLanguageService().getDocumentLifecycleParticipants().forEach(participant -> {
							try {
								participant.didOpen(xmlDocument);
							} catch (Exception e) {
								LOGGER.log(Level.SEVERE, "Error while processing didOpen for the participant '"
										+ participant.getClass().getName() + "'.", e);
							}
						});
						break;
					case didChange:
						getXMLLanguageService().getDocumentLifecycleParticipants().forEach(participant -> {
							try {
								participant.didChange(xmlDocument);
							} catch (Exception e) {
								LOGGER.log(Level.SEVERE, "Error while processing didChange for the participant '"
										+ participant.getClass().getName() + "'.", e);
							}
						});
						break;
					default:
						// Do nothing
					}
				});
	}

	void validate(DOMDocument xmlDocument) throws CancellationException {
		CancelChecker cancelChecker = xmlDocument.getCancelChecker();
		cancelChecker.checkCanceled();
		getXMLLanguageService().publishDiagnostics(xmlDocument,
				params -> xmlLanguageServer.getLanguageClient().publishDiagnostics(params),
				(doc) -> triggerValidationFor(doc, TriggeredBy.Other), sharedSettings.getValidationSettings(),
				cancelChecker);
	}

	private XMLLanguageService getXMLLanguageService() {
		return xmlLanguageServer.getXMLLanguageService();
	}

	public void updateCompletionSettings(XMLCompletionSettings newCompletion) {
		sharedSettings.getCompletionSettings().merge(newCompletion);
	}

	public void updateSymbolSettings(XMLSymbolSettings newSettings) {
		sharedSettings.getSymbolSettings().merge(newSettings);
	}

	public void updateCodeLensSettings(XMLCodeLensSettings newSettings) {
		sharedSettings.getCodeLensSettings().merge(newSettings);
	}

	public void updatePreferences(XMLPreferences newPreferences) {
		sharedSettings.getPreferences().merge(newPreferences);
	}

	public XMLSymbolSettings getSharedSymbolSettings() {
		return sharedSettings.getSymbolSettings();
	}

	public XMLCodeLensSettings getSharedCodeLensSettings() {
		return sharedSettings.getCodeLensSettings();
	}

	public boolean isIncrementalSupport() {
		return documents.isIncremental();
	}

	public XMLFormattingOptions getSharedFormattingSettings() {
		return sharedSettings.getFormattingSettings();
	}

	public XMLValidationSettings getValidationSettings() {
		return sharedSettings.getValidationSettings();
	}

	public XMLPreferences getPreferences() {
		return sharedSettings.getPreferences();
	}

	public SharedSettings getSharedSettings() {
		return this.sharedSettings;
	}

	/**
	 * Returns the text document from the given uri.
	 *
	 * @param uri the uri
	 * @return the text document from the given uri.
	 */
	public ModelTextDocument<DOMDocument> getDocument(String uri) {
		return documents.get(uri);
	}

	public Collection<ModelTextDocument<DOMDocument>> allDocuments() {
		return documents.all();
	}

	public boolean documentIsOpen(String uri) {
		ModelTextDocument<DOMDocument> document = getDocument(uri);
		return document != null;
	}

	/**
	 * Compute the DOM Document for a given uri in a future and then apply the given
	 * function.
	 *
	 * @param <R>
	 * @param documentIdentifier the document indetifier.
	 * @param code               a bi function that accepts a {@link CancelChecker}
	 *                           and parsed {@link DOMDocument} and returns the to
	 *                           be computed value
	 * @return the DOM Document for a given uri in a future and then apply the given
	 *         function.
	 */
	public <R> CompletableFuture<R> computeDOMAsync(TextDocumentIdentifier documentIdentifier,
			BiFunction<CancelChecker, DOMDocument, R> code) {
		ModelTextDocument<DOMDocument> document = getDocument(documentIdentifier.getUri());
		if (document != null) {
			return computeModelAsync(document.getModel(), code);
		}
		return CompletableFuture.completedFuture(null);
	}

	private static <R, M> CompletableFuture<R> computeModelAsync(CompletableFuture<M> loadModel,
			BiFunction<CancelChecker, M, R> code) {
		CompletableFuture<CancelChecker> start = new CompletableFuture<>();
		CompletableFuture<R> result = start.thenCombineAsync(loadModel, code);
		CancelChecker cancelIndicator = () -> {
			if (result.isCancelled())
				throw new CancellationException();
		};
		start.complete(cancelIndicator);
		return result;
	}

	public LimitExceededWarner getLimitExceededWarner() {
		if (this.limitExceededWarner == null) {
			this.limitExceededWarner = new LimitExceededWarner(this.xmlLanguageServer);
		}
		return this.limitExceededWarner;
	}
}
