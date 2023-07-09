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
package com.itsaky.androidide.lsp.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.progress.ICancelChecker;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.Project;

import java.nio.file.Path;
import java.util.Collections;

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
   * Setup this language server with the given project. Servers are not expected to keep a reference
   * to the provided project. Instead, use {@link ProjectManager#getRootProject()} to obtain the
   * project instance.
   *
   * @param project The initialized project.
   */
  void setupWithProject(@NonNull Project project);

  /**
   * Compute code completions for the given completion params.
   *
   * @param params        The completion params.
   * @param cancelChecker
   * @return The completion provider.
   */
  @NonNull
  CompletionResult complete(CompletionParams params, ICancelChecker cancelChecker);

  /**
   * Find references using the given params.
   *
   * @param params        The params to use for computing references.
   * @param cancelChecker
   * @return The result of the computation.
   */
  @NonNull
  ReferenceResult findReferences(@NonNull ReferenceParams params, ICancelChecker cancelChecker);

  /**
   * Find definition using the given params.
   *
   * @param params        The params to use for computing the definition.
   * @param cancelChecker
   * @return The result of the computation.
   */
  @NonNull
  DefinitionResult findDefinition(@NonNull DefinitionParams params, ICancelChecker cancelChecker);

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
  default CodeFormatResult formatCode(FormatCodeParams params) {
    return new CodeFormatResult(false, Collections.emptyList());
  }

  /**
   * Handle failure caused by LSP
   *
   * @param failure {@link LSPFailure} describing the failure.
   * @return <code>true</code> if the failure was handled. <code>false</code> otherwise.
   */
  default boolean handleFailure(LSPFailure failure) {
    return false;
  }
}
