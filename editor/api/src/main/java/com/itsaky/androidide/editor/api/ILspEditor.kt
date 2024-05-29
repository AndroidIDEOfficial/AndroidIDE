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

package com.itsaky.androidide.editor.api

import com.itsaky.androidide.lsp.api.ILanguageClient
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.SignatureHelp

/**
 * LSP functions for the editor.
 *
 * @author Akash Yadav
 */
interface ILspEditor {
  /**
   * Set the language server that this editor will connect with. If the language client is not set,
   * it'll be set to [ILanguageClient] from the language server.
   *
   * @param server The server to set. Provide `null` to disable all the language server features.
   */
  fun setLanguageServer(server: ILanguageServer?)

  /**
   * Set the language client to this editor.
   *
   * @param client The client to set.
   */
  fun setLanguageClient(client: ILanguageClient?)

  /**
   * Execute the given LSP command in the editor.
   *
   * @param command The command to execute.
   */
  fun executeCommand(command: Command?)

  /**
   * If any language server is set, requests signature help at the cursor's position. On a valid
   * response, shows the signature help in a popup window.
   */
  fun signatureHelp()

  /**
   * Shows the given signature help in the editor.
   *
   * @param help The signature help data to show.
   */
  fun showSignatureHelp(help: SignatureHelp?)

  /**
   * If any language server is set, asks the language server to find the definition of token at the
   * cursor position.
   *
   * If the server returns a valid response, and the file specified in the response is same the file
   * in this editor, the range specified in the response will be selected.
   */
  fun findDefinition()

  /**
   * If any language server instance is set, finds the references to of the token at the current
   * cursor position.
   *
   * If the server returns a valid response, that response is forwarded to the [ ].
   */
  fun findReferences()

  /**
   * Requests the language server to provided a semantically larger selection than the current
   * selection. If a valid response is received, that range will be selected.
   */
  fun expandSelection()

  /** Ensures that all the windows are dismissed. */
  fun ensureWindowsDismissed()
}