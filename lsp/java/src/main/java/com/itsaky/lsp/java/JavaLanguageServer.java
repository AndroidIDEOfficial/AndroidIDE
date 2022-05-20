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

package com.itsaky.lsp.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IDocumentHandler;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.internal.model.CachedCompletion;
import com.itsaky.lsp.java.actions.JavaCodeActionsMenu;
import com.itsaky.lsp.java.compiler.JavaCompilerService;
import com.itsaky.lsp.java.models.JavaServerConfiguration;
import com.itsaky.lsp.java.models.JavaServerSettings;
import com.itsaky.lsp.java.providers.CodeFormatProvider;
import com.itsaky.lsp.java.providers.CompletionProvider;
import com.itsaky.lsp.java.providers.DefinitionProvider;
import com.itsaky.lsp.java.providers.JavaDiagnosticProvider;
import com.itsaky.lsp.java.providers.JavaSelectionProvider;
import com.itsaky.lsp.java.providers.ReferenceProvider;
import com.itsaky.lsp.java.providers.SignatureProvider;
import com.itsaky.lsp.java.utils.AnalyzeTimer;
import com.itsaky.lsp.models.DefinitionParams;
import com.itsaky.lsp.models.DefinitionResult;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticResult;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;
import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.ReferenceParams;
import com.itsaky.lsp.models.ReferenceResult;
import com.itsaky.lsp.models.ServerCapabilities;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureHelpParams;
import com.itsaky.lsp.util.LSPEditorActions;
import com.itsaky.lsp.util.NoCompletionsProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JavaLanguageServer implements ILanguageServer, IDocumentHandler {

  private static final ILogger LOG = ILogger.newInstance("JavaLanguageServer");
  private final AnalyzeTimer analyzeTimer;
  private ILanguageClient client;
  private IServerSettings settings;
  private JavaCompilerService compiler;
  private JavaServerConfiguration configuration;
  private CachedCompletion cachedCompletion;
  private boolean initialized;
  private boolean createCompiler;
  private ServerCapabilities capabilities;
  private Path selectedFile;

  public JavaLanguageServer() {
    this.initialized = false;
    this.createCompiler = true;
    this.configuration = new JavaServerConfiguration();
    this.analyzeTimer = new AnalyzeTimer(this::analyzeSelected);
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
                if (diagnostics == null) {
                  diagnostics = new ArrayList<>(0);
                }

                client.publishDiagnostics(new DiagnosticResult(this.selectedFile, diagnostics));
              }
            }));
  }

  public IServerSettings getSettings() {
    if (settings == null) {
      settings = JavaServerSettings.getInstance();
    }

    return settings;
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public JavaCompilerService getCompiler() {
    if (createCompiler) {
      LOG.info("Creating new compiler instance...");
      compiler = createCompiler();
      createCompiler = false;
    }

    return compiler;
  }

  @NonNull
  private JavaCompilerService createCompiler() {
    return new JavaCompilerService(configuration.getClassPaths(), Collections.emptySet());
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
    if (compiler != null) {
      compiler.close();
      compiler = null;
      createCompiler = true;
    }

    FileStore.shutdown();
    this.analyzeTimer.shutdown();
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

    return new CompletionProvider(
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
  public List<DiagnosticItem> analyze(@NonNull Path file) {
    if (!settings.codeAnalysisEnabled()) {
      return Collections.emptyList();
    }

    return new JavaDiagnosticProvider(getCompiler()).analyze(file);
  }

  @NonNull
  @Override
  public CharSequence formatCode(CharSequence input) {
    return new CodeFormatProvider(getSettings()).format(input);
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
    FileStore.open(event);
  }

  @Override
  public void onContentChange(@NonNull DocumentChangeEvent event) {
    // If a file's content is changed, it is definitely visible to user.
    onFileSelected(event.getChangedFile());
    ensureAnalyzeTimerStarted();
    FileStore.change(event);
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

  private void ensureAnalyzeTimerStarted() {
    if (!this.analyzeTimer.isStarted()) {
      this.analyzeTimer.start();
    } else {
      this.analyzeTimer.restart();
    }
  }
}
