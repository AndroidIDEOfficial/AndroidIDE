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
package com.itsaky.lsp.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.lsp.models.DefinitionParams;
import com.itsaky.lsp.models.DefinitionResult;
import com.itsaky.lsp.models.DiagnosticResult;
import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.FormatCodeParams;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.LSPFailure;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.ReferenceParams;
import com.itsaky.lsp.models.ReferenceResult;
import com.itsaky.lsp.models.ServerCapabilities;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureHelpParams;

import java.nio.file.Path;

/**
 * A language server provides API for providing functions related to a specific file type.
 *
 * @author Akash Yadav
 */
public interface ILanguageServer {

  /**
   * Get the unique language server ID.
   *
   * @return The server ID.
   */
  String getServerId();

  /**
   * Initialize this language server with the given params. Subclasses are expected to throw {@link
   * AlreadyInitializedException} if the language server was already initialized.
   *
   * @param params The params used to initialize the language server.
   * @throws AlreadyInitializedException If the language server was already initialized.
   */
  void initialize(@NonNull InitializeParams params) throws AlreadyInitializedException;

  /**
   * Checks if this language server has been initialized.
   *
   * @return <code>true</code> if the server has been initialized, <code>false</code> otherwise.
   */
  boolean isInitialized();

  /**
   * Returns the capabilities that the LSP implementation provides.
   *
   * @return The capabilities of the server.
   */
  @NonNull
  ServerCapabilities getCapabilities();

  /**
   * Called by client to notify the server to shutdown. Language servers must release all the
   * resources in use.
   *
   * <p>After this is called, clients must re-initialize the server.
   */
  void shutdown();

  /**
   * Set the client to whom notifications and events must be sent.
   *
   * @param client The client to set.
   */
  void connectClient(@Nullable ILanguageClient client);

  /**
   * Get the instance of the language client connected to this server.
   *
   * @return The language client.
   */
  @Nullable
  ILanguageClient getClient();

  /**
   * Apply settings to the language server. Its up to the language server how it applies these
   * settings to the language service providers.
   *
   * @param settings The new settings to use. Pass {@code null} to use default settings.
   */
  void applySettings(@Nullable IServerSettings settings);

  /**
   * Notify the language server that the project's configuration was changed. Language servers
   * decide what type of object they want to receive as configuration.
   *
   * @param newConfiguration The new configuration object. Only a specific type of object might be
   *     accepted by observers.
   */
  void configurationChanged(Object newConfiguration);

  /**
   * Get the completion provider associated with this language server. Should never be null.
   *
   * @return The completion provider.
   */
  @NonNull
  ICompletionProvider getCompletionProvider();

  /**
   * Find references using the given params.
   *
   * @param params The params to use for computing references.
   * @return The result of the computation.
   */
  @NonNull
  ReferenceResult findReferences(@NonNull ReferenceParams params);

  /**
   * Find definition using the given params.
   *
   * @param params The params to use for computing the definition.
   * @return The result of the computation.
   */
  @NonNull
  DefinitionResult findDefinition(@NonNull DefinitionParams params);

  /**
   * Request the server to provide an expanded selection range for the current selection.
   *
   * @param params The params for computing the expanded selection range.
   * @return The expanded range or same selection range if computation was failed.
   */
  @NonNull
  Range expandSelection(@NonNull ExpandSelectionParams params);

  /**
   * Compute signature help with the given params.
   *
   * @param params The params to compute signature help.
   * @return The signature help.
   */
  @NonNull
  SignatureHelp signatureHelp(@NonNull SignatureHelpParams params);

  /**
   * Analyze the given file and provide diagnostics from the analyze result.
   *
   * @param file The file to analyze.
   * @return The diagnostic result. Points to {@link DiagnosticResult#NO_UPDATE} if no diagnotic
   *     items are available.
   */
  @NonNull
  DiagnosticResult analyze(@NonNull Path file);

  /**
   * Format the given source code input.
   *
   * @param params The code formatting parameters.
   * @return The formatted source.
   */
  @NonNull
  default CharSequence formatCode(FormatCodeParams params) {
    return params.getContent();
  }

  /**
   * The document handler associated with this language server instance.
   *
   * @return The document handler. Must not be null.
   */
  @NonNull
  IDocumentHandler getDocumentHandler();

  /**
   * Handle failure caused by LSP
   *
   * @param failure {@link LSPFailure} describing the failure.
   * @return <code>true</code> if the failure was handled. <code>false</code> otherwise.
   */
  default boolean handleFailure(LSPFailure failure) {
    return false;
  }

  /**
   * Thrown to indicate that a language server received an initialize notification but was already
   * initialized.
   *
   * @author Akash Yadav
   */
  class AlreadyInitializedException extends IllegalStateException {}
}
