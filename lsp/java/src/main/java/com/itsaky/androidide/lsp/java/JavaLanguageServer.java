/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.lsp.api.ICompletionProvider;
import com.itsaky.androidide.lsp.api.IDocumentHandler;
import com.itsaky.androidide.lsp.api.ILanguageClient;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.internal.model.CachedCompletion;
import com.itsaky.androidide.lsp.java.actions.JavaCodeActionsMenu;
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService;
import com.itsaky.androidide.lsp.java.models.JavaServerConfiguration;
import com.itsaky.androidide.lsp.java.models.JavaServerSettings;
import com.itsaky.androidide.lsp.java.providers.CodeFormatProvider;
import com.itsaky.androidide.lsp.java.providers.CompletionProvider;
import com.itsaky.androidide.lsp.java.providers.DefinitionProvider;
import com.itsaky.androidide.lsp.java.providers.JavaDiagnosticProvider;
import com.itsaky.androidide.lsp.java.providers.JavaSelectionProvider;
import com.itsaky.androidide.lsp.java.providers.ReferenceProvider;
import com.itsaky.androidide.lsp.java.providers.SignatureProvider;
import com.itsaky.androidide.lsp.java.utils.AnalyzeTimer;
import com.itsaky.androidide.lsp.models.DefinitionParams;
import com.itsaky.androidide.lsp.models.DefinitionResult;
import com.itsaky.androidide.lsp.models.DiagnosticResult;
import com.itsaky.androidide.lsp.models.DocumentChangeEvent;
import com.itsaky.androidide.lsp.models.DocumentCloseEvent;
import com.itsaky.androidide.lsp.models.DocumentOpenEvent;
import com.itsaky.androidide.lsp.models.DocumentSaveEvent;
import com.itsaky.androidide.lsp.models.ExpandSelectionParams;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.lsp.models.InitializeParams;
import com.itsaky.androidide.lsp.models.LSPFailure;
import com.itsaky.androidide.lsp.models.Range;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.lsp.models.ServerCapabilities;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.lsp.util.LSPEditorActions;
import com.itsaky.androidide.lsp.util.NoCompletionsProvider;

import com.itsaky.androidide.javac.services.CancelAbort;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JavaLanguageServer implements ILanguageServer, IDocumentHandler {

  private static final ILogger LOG = ILogger.newInstance("JavaLanguageServer");
  public static final String SERVER_ID = "java";
  private final AnalyzeTimer timer = new AnalyzeTimer(this::analyzeSelected);
  private final CompletionProvider completionProvider;
  private final JavaDiagnosticProvider diagnosticProvider;
  private ILanguageClient client;
  private IServerSettings settings;
  private Path selectedFile;
  private JavaCompilerService compilerService;
  private JavaServerConfiguration configuration;
  private CachedCompletion cachedCompletion;
  private ServerCapabilities capabilities;
  private boolean initialized;
  private boolean createCompiler;

  public JavaLanguageServer() {
    this.initialized = false;
    this.createCompiler = true;
    this.configuration = new JavaServerConfiguration();
    this.completionProvider = new CompletionProvider();
    this.diagnosticProvider = new JavaDiagnosticProvider(this.completionProvider::isCompleting);
    this.cachedCompletion = CachedCompletion.EMPTY;

    applySettings(getSettings());
  }

  private void analyzeSelected() {
    if (this.selectedFile == null) {
      return;
    }

    CompletableFuture.supplyAsync(() -> analyze(selectedFile))
        .whenComplete(
            ((diagnostics, throwable) -> {
              if (client != null) {
                client.publishDiagnostics(diagnostics);
              }
            }));
  }

  public IServerSettings getSettings() {
    if (settings == null) {
      settings = JavaServerSettings.getInstance();
    }

    return settings;
  }

  @Override
  public String getServerId() {
    return SERVER_ID;
  }

  @Override
  public void initialize(@NonNull InitializeParams params) throws AlreadyInitializedException {

    if (initialized) {
      throw new AlreadyInitializedException();
    }

    FileStore.setWorkspaceRoots(params.getWorkspaceRoots());

    capabilities = new ServerCapabilities();
    capabilities.setCompletionsAvailable(true);
    capabilities.setCodeActionsAvailable(true);
    capabilities.setDefinitionsAvailable(true);
    capabilities.setReferencesAvailable(true);
    capabilities.setSignatureHelpAvailable(true);
    capabilities.setCodeAnalysisAvailable(true);
    capabilities.setSmartSelectionsEnabled(true);

    LSPEditorActions.ensureActionsMenuRegistered(JavaCodeActionsMenu.class);

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @NonNull
  @Override
  public ServerCapabilities getCapabilities() {
    return capabilities;
  }

  @Override
  public void shutdown() {
    if (compilerService != null) {
      compilerService.close();
      compilerService = null;
      createCompiler = true;
    }

    FileStore.shutdown();
    timer.shutdown();
    initialized = false;
  }

  @Override
  public void connectClient(@Nullable ILanguageClient client) {
    this.client = client;
  }

  @Nullable
  @Override
  public ILanguageClient getClient() {
    return this.client;
  }

  @Override
  public void applySettings(@Nullable IServerSettings settings) {
    this.settings = settings;
  }

  @Override
  public void configurationChanged(Object newConfiguration) {
    if (!(newConfiguration instanceof JavaServerConfiguration)) {
      LOG.error("Invalid configuration passed to server.", newConfiguration);
      LOG.error("Configuration change event will be ignored.");
      return;
    }

    this.configuration = (JavaServerConfiguration) newConfiguration;
    LOG.info("Java language server configuration changed.");
    LOG.info(
        this.configuration.getClassPaths().size(),
        "class paths and",
        this.configuration.getSourceDirs().size(),
        "source directories were provided in the configuration");
    // Compiler must be recreated on a configuration change
    this.createCompiler = true;

    FileStore.configurationChanged(this.configuration.getSourceDirs());
  }

  @NonNull
  @Override
  public ICompletionProvider getCompletionProvider() {
    if (!settings.completionsEnabled()) {
      return new NoCompletionsProvider();
    }

    return this.completionProvider.reset(
        getCompiler(), this.settings, this.cachedCompletion, this::updateCachedCompletion);
  }

  @NonNull
  @Override
  public ReferenceResult findReferences(@NonNull ReferenceParams params) {
    if (!settings.referencesEnabled()) {
      return new ReferenceResult(Collections.emptyList());
    }

    return new ReferenceProvider(getCompiler()).findReferences(params);
  }

  @NonNull
  @Override
  public DefinitionResult findDefinition(@NonNull DefinitionParams params) {
    if (!settings.definitionsEnabled()) {
      return new DefinitionResult(Collections.emptyList());
    }

    return new DefinitionProvider(getCompiler()).findDefinition(params);
  }

  @NonNull
  @Override
  public Range expandSelection(@NonNull ExpandSelectionParams params) {
    if (!settings.smartSelectionsEnabled()) {
      return params.getSelection();
    }

    return new JavaSelectionProvider(getCompiler()).expandSelection(params);
  }

  @NonNull
  @Override
  public SignatureHelp signatureHelp(@NonNull SignatureHelpParams params) {
    if (!settings.signatureHelpEnabled()) {
      return new SignatureHelp(Collections.emptyList(), -1, -1);
    }

    return new SignatureProvider(getCompiler()).signatureHelp(params);
  }

  @NonNull
  @Override
  public DiagnosticResult analyze(@NonNull Path file) {
    if (!settings.codeAnalysisEnabled()) {
      return DiagnosticResult.NO_UPDATE;
    }

    return this.diagnosticProvider.analyze(getCompiler(), file);
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public JavaCompilerService getCompiler() {
    if (createCompiler) {
      LOG.info("Creating new compiler instance...");
      compilerService = createCompiler();
      createCompiler = false;
    }

    return compilerService;
  }

  @NonNull
  private JavaCompilerService createCompiler() {
    return new JavaCompilerService(configuration.getClassPaths(), Collections.emptySet());
  }

  @NonNull
  @Override
  public CharSequence formatCode(FormatCodeParams params) {
    return new CodeFormatProvider(getSettings()).format(params);
  }

  @NonNull
  @Override
  public IDocumentHandler getDocumentHandler() {
    return this;
  }

  private void updateCachedCompletion(CachedCompletion cachedCompletion) {
    Objects.requireNonNull(cachedCompletion);
    this.cachedCompletion = cachedCompletion;
  }

  @Override
  public boolean accepts(Path file) {
    return FileStore.isJavaFile(file);
  }

  @Override
  public void onFileOpened(DocumentOpenEvent event) {
    onFileSelected(event.getOpenedFile());
    FileStore.open(event);
    startOrRestartAnalyzeTimer();
  }

  @Override
  public void onContentChange(@NonNull DocumentChangeEvent event) {
    // If a file's content is changed, it is definitely visible to user.
    onFileSelected(event.getChangedFile());
    FileStore.change(event);
    if (compilerService != null) {
      compilerService.onContentChanged(event);
    }
    startOrRestartAnalyzeTimer();
  }

  @Override
  public void onFileSaved(DocumentSaveEvent event) {
    // TODO Run a lint check (or a simple compilation)
  }

  @Override
  public void onFileClosed(DocumentCloseEvent event) {
    FileStore.close(event);
  }

  @Override
  public void onFileSelected(@NonNull Path path) {
    this.selectedFile = path;
  }

  @Override
  public boolean handleFailure(final LSPFailure failure) {
    //noinspection SwitchStatementWithTooFewBranches
    switch (failure.getType()) {
      case COMPLETION:
        if (CancelAbort.isCancelled(failure.getError())
            || CompilationCancellationException.isCancelled(failure.getError())) {
          return true;
        }

        if (compilerService != null) {
          compilerService.close();
        }
        return true;
    }

    return false;
  }

  private void startOrRestartAnalyzeTimer() {
    if (!this.timer.isStarted()) {
      this.timer.start();
    } else {
      this.timer.restart();
    }
  }
}
