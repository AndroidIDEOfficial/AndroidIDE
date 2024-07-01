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
package com.itsaky.androidide.lsp.api

import com.itsaky.androidide.lsp.models.CodeFormatResult
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.DefinitionParams
import com.itsaky.androidide.lsp.models.DefinitionResult
import com.itsaky.androidide.lsp.models.DiagnosticResult
import com.itsaky.androidide.lsp.models.ExpandSelectionParams
import com.itsaky.androidide.lsp.models.FormatCodeParams
import com.itsaky.androidide.lsp.models.LSPFailure
import com.itsaky.androidide.lsp.models.ReferenceParams
import com.itsaky.androidide.lsp.models.ReferenceResult
import com.itsaky.androidide.lsp.models.SignatureHelp
import com.itsaky.androidide.lsp.models.SignatureHelpParams
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.projects.IWorkspace
import java.nio.file.Path

/**
 * A language server provides API for providing functions related to a specific file type.
 *
 * @author Akash Yadav
 */
interface ILanguageServer {

  val serverId: String?

  /**
   * Called by client to notify the server to shutdown. Language servers must release all the
   * resources in use.
   *
   *
   * After this is called, clients must re-initialize the server.
   */
  fun shutdown()

  /**
   * Set the client to whom notifications and events must be sent.
   *
   * @param client The client to set.
   */
  fun connectClient(client: ILanguageClient?)

  /**
   * Get the instance of the language client connected to this server.
   *
   * @return The language client.
   */
  val client: ILanguageClient?

  /**
   * Apply settings to the language server. Its up to the language server how it applies these
   * settings to the language service providers.
   *
   * @param settings The new settings to use. Pass `null` to use default settings.
   */
  fun applySettings(settings: IServerSettings?)

  /**
   * Setup this language server with the given workspace. Servers are not expected to keep a reference
   * to the provided workspace. Instead, use
   * [getRootWorkspace()][com.itsaky.androidide.projects.IProjectManager.workspace] to
   * obtain the workspace instance.
   *
   * @param workspace The initialized workspace.
   */
  fun setupWorkspace(workspace: IWorkspace)

  /**
   * Compute code completions for the given completion params.
   *
   * @param params        The completion params.
   * @param cancelChecker
   * @return The completion provider.
   */
  fun complete(params: CompletionParams?): CompletionResult

  /**
   * Find references using the given params.
   *
   * @param params        The params to use for computing references.
   * @param cancelChecker
   * @return The result of the computation.
   */
  suspend fun findReferences(params: ReferenceParams): ReferenceResult

  /**
   * Find definition using the given params.
   *
   * @param params        The params to use for computing the definition.
   * @param cancelChecker
   * @return The result of the computation.
   */
  suspend fun findDefinition(params: DefinitionParams): DefinitionResult

  /**
   * Request the server to provide an expanded selection range for the current selection.
   *
   * @param params The params for computing the expanded selection range.
   * @return The expanded range or same selection range if computation was failed.
   */
  suspend fun expandSelection(params: ExpandSelectionParams): Range

  /**
   * Compute signature help with the given params.
   *
   * @param params The params to compute signature help.
   * @return The signature help.
   */
  suspend fun signatureHelp(params: SignatureHelpParams): SignatureHelp

  /**
   * Analyze the given file and provide diagnostics from the analyze result.
   *
   * @param file The file to analyze.
   * @return The diagnostic result. Points to [DiagnosticResult.NO_UPDATE] if no diagnotic
   * items are available.
   */
  suspend fun analyze(file: Path): DiagnosticResult

  /**
   * Format the given source code input.
   *
   * @param params The code formatting parameters.
   * @return The formatted source.
   */
  fun formatCode(params: FormatCodeParams?): CodeFormatResult {
    return CodeFormatResult(false, mutableListOf())
  }

  /**
   * Handle failure caused by LSP
   *
   * @param failure [LSPFailure] describing the failure.
   * @return `true` if the failure was handled. `false` otherwise.
   */
  fun handleFailure(failure: LSPFailure?): Boolean {
    return false
  }
}