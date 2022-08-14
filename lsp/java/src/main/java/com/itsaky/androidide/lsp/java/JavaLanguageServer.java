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

import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentSelectedEvent;
import com.itsaky.androidide.javac.services.fs.CacheFSInfoSingleton;
import com.itsaky.androidide.javac.services.fs.CachingJarFileSystemProvider;
import com.itsaky.androidide.lsp.api.ILanguageClient;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.internal.model.CachedCompletion;
import com.itsaky.androidide.lsp.java.actions.JavaCodeActionsMenu;
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService;
import com.itsaky.androidide.lsp.java.compiler.SourceFileManager;
import com.itsaky.androidide.lsp.java.models.JavaServerSettings;
import com.itsaky.androidide.lsp.java.providers.CodeFormatProvider;
import com.itsaky.androidide.lsp.java.providers.CompletionProvider;
import com.itsaky.androidide.lsp.java.providers.DefinitionProvider;
import com.itsaky.androidide.lsp.java.providers.JavaDiagnosticProvider;
import com.itsaky.androidide.lsp.java.providers.JavaSelectionProvider;
import com.itsaky.androidide.lsp.java.providers.ReferenceProvider;
import com.itsaky.androidide.lsp.java.providers.SignatureProvider;
import com.itsaky.androidide.lsp.java.utils.AnalyzeTimer;
import com.itsaky.androidide.lsp.java.utils.CancelChecker;
import com.itsaky.androidide.lsp.models.CodeFormatResult;
import com.itsaky.androidide.lsp.models.CompletionParams;
import com.itsaky.androidide.lsp.models.CompletionResult;
import com.itsaky.androidide.lsp.models.DefinitionParams;
import com.itsaky.androidide.lsp.models.DefinitionResult;
import com.itsaky.androidide.lsp.models.DiagnosticResult;
import com.itsaky.androidide.lsp.models.ExpandSelectionParams;
import com.itsaky.androidide.lsp.models.FormatCodeParams;
import com.itsaky.androidide.lsp.models.LSPFailure;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.lsp.util.LSPEditorActions;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.FileManager;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.api.Project;
import com.itsaky.androidide.utils.DocumentUtils;
import com.itsaky.androidide.utils.VMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JavaLanguageServer implements ILanguageServer {

  public static final String SERVER_ID = "java";
  private final CompletionProvider completionProvider;
  private final JavaDiagnosticProvider diagnosticProvider;
  private ILanguageClient client;
  private IServerSettings settings;
  private Path selectedFile;
  private final AnalyzeTimer timer = new AnalyzeTimer(this::analyzeSelected);
  private CachedCompletion cachedCompletion;

  public JavaLanguageServer() {
    this.completionProvider = new CompletionProvider();
    this.diagnosticProvider = new JavaDiagnosticProvider();
    this.cachedCompletion = CachedCompletion.EMPTY;

    applySettings(getSettings());

    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
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
  public void shutdown() {
    JavaCompilerProvider.getInstance().destory();
    SourceFileManager.clearCache();
    CacheFSInfoSingleton.INSTANCE.clearCache();
    CachingJarFileSystemProvider.INSTANCE.clearCache();
    EventBus.getDefault().unregister(this);

    timer.cancel();
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
  public void setupWithProject(@NonNull final Project project) {

    LSPEditorActions.ensureActionsMenuRegistered(JavaCodeActionsMenu.class);

    // Once we have project initialized
    // Destory the NO_MODULE_COMPILER instance
    JavaCompilerService.NO_MODULE_COMPILER.destroy();

    // Clear cached file managers
    SourceFileManager.clearCache();

    // Clear cached module-specific compilers
    JavaCompilerProvider.getInstance().destory();

    // Cache classpath locations
    for (final Project subModule : project.getSubModules()) {
      if (!(subModule instanceof ModuleProject)) {
        continue;
      }

      SourceFileManager.forModule(((ModuleProject) subModule));
    }

    startOrRestartAnalyzeTimer();
  }

  @NonNull
  @Override
  public CompletionResult complete(final CompletionParams params) {
    final JavaCompilerService compiler = getCompiler(params.getFile());
    if (compiler == null
        || !settings.completionsEnabled()
        || !this.completionProvider.canComplete(params.getFile())) {
      return CompletionResult.EMPTY;
    }

    this.completionProvider.reset(
        compiler, this.settings, this.cachedCompletion, this::updateCachedCompletion);
    return this.completionProvider.complete(params);
  }

  @NonNull
  @Override
  public ReferenceResult findReferences(@NonNull ReferenceParams params) {
    final JavaCompilerService compiler = getCompiler(params.getFile());
    if (!settings.referencesEnabled() || compiler == null) {
      return new ReferenceResult(Collections.emptyList());
    }

    return new ReferenceProvider(compiler).findReferences(params);
  }

  @NonNull
  @Override
  public DefinitionResult findDefinition(@NonNull DefinitionParams params) {
    final JavaCompilerService compiler = getCompiler(params.getFile());
    if (!settings.definitionsEnabled() || compiler == null) {
      return new DefinitionResult(Collections.emptyList());
    }

    return new DefinitionProvider(compiler, getSettings()).findDefinition(params);
  }

  @NonNull
  @Override
  public Range expandSelection(@NonNull ExpandSelectionParams params) {
    final JavaCompilerService compiler = getCompiler(params.getFile());
    if (!settings.smartSelectionsEnabled() || compiler == null) {
      return params.getSelection();
    }

    return new JavaSelectionProvider(compiler).expandSelection(params);
  }

  @NonNull
  @Override
  public SignatureHelp signatureHelp(@NonNull SignatureHelpParams params) {
    final JavaCompilerService compiler = getCompiler(params.getFile());
    if (!settings.signatureHelpEnabled() || compiler == null) {
      return new SignatureHelp(Collections.emptyList(), -1, -1);
    }

    return new SignatureProvider(compiler).signatureHelp(params);
  }

  @NonNull
  @Override
  public DiagnosticResult analyze(@NonNull Path file) {
    if (!DocumentUtils.isJavaFile(file)) {
      return DiagnosticResult.NO_UPDATE;
    }
    
    final JavaCompilerService compiler = getCompiler(file);
    if (!settings.codeAnalysisEnabled() || compiler == null) {
      return DiagnosticResult.NO_UPDATE;
    }

    return this.diagnosticProvider.analyze(file);
  }

  @NonNull
  @Override
  public CodeFormatResult formatCode(FormatCodeParams params) {
    return new CodeFormatProvider(getSettings()).format(params);
  }

  @Override
  public boolean handleFailure(final LSPFailure failure) {
    //noinspection SwitchStatementWithTooFewBranches
    switch (failure.getType()) {
      case COMPLETION:
        if (CancelChecker.isCancelled(failure.getError())) {
          return true;
        }

        JavaCompilerProvider.getInstance().destory();
        return true;
    }

    return false;
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  public JavaCompilerService getCompiler(Path file) {
    if (!DocumentUtils.isJavaFile(file)) {
      return JavaCompilerService.NO_MODULE_COMPILER;
    }

    final Project root = ProjectManager.INSTANCE.getRootProject();
    if (root == null) {
      return JavaCompilerService.NO_MODULE_COMPILER;
    }

    final ModuleProject module = root.findModuleForFile(file);
    if (module == null) {
      return JavaCompilerService.NO_MODULE_COMPILER;
    }

    return JavaCompilerProvider.get(module);
  }

  private void updateCachedCompletion(CachedCompletion cachedCompletion) {
    Objects.requireNonNull(cachedCompletion);
    this.cachedCompletion = cachedCompletion;
  }

  private void startOrRestartAnalyzeTimer() {

    if (VMUtils.isJvm()) {
      return;
    }

    if (!this.timer.isStarted()) {
      this.timer.start();
    } else {
      this.timer.restart();
    }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @SuppressWarnings("unused")
  public void onContentChange(@NonNull DocumentChangeEvent event) {
    if (!DocumentUtils.isJavaFile(event.getChangedFile())) {
      return;
    }

    // TODO Find an alternative to efficiently update changeDelta in JavaCompilerService instance
    JavaCompilerService.NO_MODULE_COMPILER.onDocumentChange(event);

    final ModuleProject module = ProjectManager.INSTANCE.findModuleForFile(event.getChangedFile());
    if (module != null) {
      final JavaCompilerService compiler = JavaCompilerProvider.get(module);
      compiler.onDocumentChange(event);
    }

    startOrRestartAnalyzeTimer();
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @SuppressWarnings("unused")
  public void onFileSelected(@NonNull DocumentSelectedEvent event) {
    this.selectedFile = event.getSelectedFile();
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @SuppressWarnings("unused")
  public void onFileOpened(@NonNull DocumentOpenEvent event) {
    this.selectedFile = event.getOpenedFile();
    startOrRestartAnalyzeTimer();
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @SuppressWarnings("unused")
  public void onFileClosed(@NonNull DocumentCloseEvent event) {
    if (this.diagnosticProvider != null) {
      this.diagnosticProvider.clearTimestamp(event.getClosedFile());
    }

    if (FileManager.INSTANCE.getActiveDocumentCount() == 0) {
      this.selectedFile = null;
      this.timer.cancel();
    }
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
}
