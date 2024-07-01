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
package com.itsaky.androidide.lsp.java

import androidx.annotation.RestrictTo
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentSelectedEvent
import com.itsaky.androidide.javac.services.fs.CacheFSInfoSingleton
import com.itsaky.androidide.javac.services.fs.CachingJarFileSystemProvider.clearCache
import com.itsaky.androidide.javac.services.fs.CachingJarFileSystemProvider.clearCachesForPaths
import com.itsaky.androidide.lsp.api.ILanguageClient
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.internal.model.CachedCompletion
import com.itsaky.androidide.lsp.java.actions.JavaCodeActionsMenu
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.compiler.SourceFileManager
import com.itsaky.androidide.lsp.java.models.JavaServerSettings
import com.itsaky.androidide.lsp.java.providers.CodeFormatProvider
import com.itsaky.androidide.lsp.java.providers.CompletionProvider
import com.itsaky.androidide.lsp.java.providers.DefinitionProvider
import com.itsaky.androidide.lsp.java.providers.JavaDiagnosticProvider
import com.itsaky.androidide.lsp.java.providers.JavaSelectionProvider
import com.itsaky.androidide.lsp.java.providers.ReferenceProvider
import com.itsaky.androidide.lsp.java.providers.SignatureProvider
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippetRepository.init
import com.itsaky.androidide.lsp.java.utils.AnalyzeTimer
import com.itsaky.androidide.lsp.java.utils.CancelChecker.Companion.isCancelled
import com.itsaky.androidide.lsp.models.CodeFormatResult
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.DefinitionParams
import com.itsaky.androidide.lsp.models.DefinitionResult
import com.itsaky.androidide.lsp.models.DiagnosticResult
import com.itsaky.androidide.lsp.models.ExpandSelectionParams
import com.itsaky.androidide.lsp.models.FailureType
import com.itsaky.androidide.lsp.models.FormatCodeParams
import com.itsaky.androidide.lsp.models.LSPFailure
import com.itsaky.androidide.lsp.models.ReferenceParams
import com.itsaky.androidide.lsp.models.ReferenceResult
import com.itsaky.androidide.lsp.models.SignatureHelp
import com.itsaky.androidide.lsp.models.SignatureHelpParams
import com.itsaky.androidide.lsp.util.LSPEditorActions
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.projects.FileManager.getActiveDocumentCount
import com.itsaky.androidide.projects.IProjectManager.Companion.getInstance
import com.itsaky.androidide.projects.IWorkspace
import com.itsaky.androidide.projects.ModuleProject
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.utils.VMUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.Objects

class JavaLanguageServer : ILanguageServer {

  private val completionProvider: CompletionProvider = CompletionProvider()
  private val diagnosticProvider: JavaDiagnosticProvider?
  override var client: ILanguageClient? = null
    private set

  private var _settings: IServerSettings? = null
  private var selectedFile: Path? = null
  private val timer = AnalyzeTimer { analyzeSelected() }
  private var cachedCompletion: CachedCompletion

  val settings: IServerSettings
    get() {
      return _settings ?: JavaServerSettings.getInstance()
        .also { _settings = it }
    }

  override val serverId: String = SERVER_ID

  companion object {

    const val SERVER_ID = "ide.lsp.java"
    private val log = LoggerFactory.getLogger(JavaLanguageServer::class.java)
  }

  init {
    diagnosticProvider = JavaDiagnosticProvider()
    cachedCompletion = CachedCompletion.EMPTY

    applySettings(JavaServerSettings.getInstance())

    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this)
    }

    init()
  }

  override fun shutdown() {
    JavaCompilerProvider.getInstance().destroy()
    SourceFileManager.clearCache()
    CacheFSInfoSingleton.clearCache()
    clearCache()
    EventBus.getDefault().unregister(this)
    timer.cancel()
  }

  override fun connectClient(client: ILanguageClient?) {
    this.client = client
  }

  override fun applySettings(settings: IServerSettings?) {
    this._settings = settings
  }

  override fun setupWorkspace(workspace: IWorkspace) {
    LSPEditorActions.ensureActionsMenuRegistered(JavaCodeActionsMenu)

    // Once we have workspace initialized
    // Destory the NO_MODULE_COMPILER instance
    JavaCompilerService.NO_MODULE_COMPILER.destroy()

    // Clear cached file managers
    SourceFileManager.clearCache()

    // Clear cached JAR file system for R.jar
    // Using the cached instance will result in completions not being updated for updated resources
    // TODO Clearing caches for JAR files ending with '/R.jar' is probably not a good idea
    //    Maybe this could be improved by using data from the AndroidModule workspace model
    clearCachesForPaths { path: String -> path.endsWith("/R.jar") }

    // Clear cached module-specific compilers
    JavaCompilerProvider.getInstance().destroy()

    // Cache classpath locations
    for (subModule in workspace.getSubProjects()) {
      if (subModule !is ModuleProject || subModule.path == workspace.getRootProject().path) {
        continue
      }
      SourceFileManager.forModule(subModule)
    }
    startOrRestartAnalyzeTimer()
  }

  override fun complete(params: CompletionParams?): CompletionResult {
    val compiler = getCompiler(params!!.file)
    if (!settings.completionsEnabled() || !completionProvider.canComplete(params.file)
    ) {
      return CompletionResult.EMPTY
    }

    if (diagnosticProvider!!.isAnalyzing()) {
      log.warn("Cancelling source code analysis due to completion request")
      diagnosticProvider.cancel()
    }

    completionProvider.reset(
      compiler, settings, cachedCompletion
    ) { cachedCompletion: CachedCompletion ->
      updateCachedCompletion(cachedCompletion)
    }

    return completionProvider.complete(params)
  }

  override suspend fun findReferences(params: ReferenceParams): ReferenceResult {
    val compiler = getCompiler(params.file)
    return if (!settings.referencesEnabled()) {
      ReferenceResult(emptyList())
    } else ReferenceProvider(compiler, params.cancelChecker).findReferences(params)
  }

  override suspend fun findDefinition(params: DefinitionParams): DefinitionResult {
    val compiler = getCompiler(params.file)
    return if (!settings.definitionsEnabled()) {
      DefinitionResult(emptyList())
    } else DefinitionProvider(compiler, settings, params.cancelChecker).findDefinition(params)
  }

  override suspend fun expandSelection(params: ExpandSelectionParams): Range {
    val compiler = getCompiler(params.file)
    return if (!settings.smartSelectionsEnabled()) {
      params.selection
    } else JavaSelectionProvider(compiler).expandSelection(params)
  }

  override suspend fun signatureHelp(params: SignatureHelpParams): SignatureHelp {
    val compiler = getCompiler(params.file)
    return if (!settings.signatureHelpEnabled()) {
      SignatureHelp(emptyList(), -1, -1)
    } else SignatureProvider(compiler, params.cancelChecker).signatureHelp(params)
  }

  override suspend fun analyze(file: Path): DiagnosticResult {
    if (!settings.diagnosticsEnabled() || !DocumentUtils.isJavaFile(file)) {
      return DiagnosticResult.NO_UPDATE
    }

    return if (!settings.codeAnalysisEnabled()) {
      DiagnosticResult.NO_UPDATE
    } else diagnosticProvider!!.analyze(file)
  }

  override fun formatCode(params: FormatCodeParams?): CodeFormatResult {
    return CodeFormatProvider(settings).format(params)
  }

  override fun handleFailure(failure: LSPFailure?): Boolean {
    return when (failure!!.type) {
      FailureType.COMPLETION -> {
        if (isCancelled(failure.error)) {
          return true
        }
        JavaCompilerProvider.getInstance().destroy()
        true
      }
    }
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
  fun getCompiler(file: Path?): JavaCompilerService {
    if (!DocumentUtils.isJavaFile(file)) {
      return JavaCompilerService.NO_MODULE_COMPILER
    }
    val workspace = getInstance().getWorkspace()
      ?: return JavaCompilerService.NO_MODULE_COMPILER
    val module = workspace.findModuleForFile(file!!) ?: return JavaCompilerService.NO_MODULE_COMPILER
    return JavaCompilerProvider.get(module)
  }

  private fun updateCachedCompletion(cachedCompletion: CachedCompletion) {
    Objects.requireNonNull(cachedCompletion)
    this.cachedCompletion = cachedCompletion
  }

  private fun startOrRestartAnalyzeTimer() {
    if (VMUtils.isJvm()) {
      return
    }
    if (!timer.isStarted) {
      timer.start()
    } else {
      timer.restart()
    }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @Suppress("unused")
  fun onContentChange(event: DocumentChangeEvent) {
    if (!DocumentUtils.isJavaFile(event.changedFile)) {
      return
    }

    // TODO Find an alternative to efficiently update changeDelta in JavaCompilerService instance
    JavaCompilerService.NO_MODULE_COMPILER.onDocumentChange(event)
    val module = getInstance()
      .getWorkspace()?.findModuleForFile(event.changedFile, true)
    if (module != null) {
      val compiler = JavaCompilerProvider.get(module)
      compiler.onDocumentChange(event)
    }
    startOrRestartAnalyzeTimer()
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @Suppress("unused")
  fun onFileSelected(event: DocumentSelectedEvent) {
    selectedFile = event.selectedFile
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @Suppress("unused")
  fun onFileOpened(event: DocumentOpenEvent) {
    selectedFile = event.openedFile
    startOrRestartAnalyzeTimer()
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  @Suppress("unused")
  fun onFileClosed(event: DocumentCloseEvent) {
    diagnosticProvider?.clearTimestamp(event.closedFile)

    if (getActiveDocumentCount() == 0) {
      selectedFile = null
      timer.cancel()
    }
  }

  private fun analyzeSelected() {
    if (selectedFile == null || client == null) {
      return
    }

    CoroutineScope(Dispatchers.Default).launch {
      val result = analyze(selectedFile!!)
      withContext(Dispatchers.Main) {
        client?.publishDiagnostics(result)
      }
    }
  }
}