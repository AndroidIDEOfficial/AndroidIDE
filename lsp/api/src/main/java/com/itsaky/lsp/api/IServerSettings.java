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

/**
 * Settings contain preferences for the language server. Clients can use settings to enable/disable
 * specific features of a server.
 *
 * @author Akash Yadav
 */
public interface IServerSettings {

  String KEY_COMPLETIONS_MATCH_LOWER = "idepref_editor_completions_matchLower";

  /**
   * Called by language server to check if the completions are enabled. If not enabled, the server
   * should not was time and memory computing completions.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean completionsEnabled();

  /**
   * Called by the language server to check if the code actions are enabled.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean codeActionsEnabled();

  /**
   * Called by language server to check if smart selections are enabled or not.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean smartSelectionsEnabled();

  /**
   * Called by the language server to check if the signature help is enabled.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean signatureHelpEnabled();

  /**
   * Called by the language server to check if finding references is enabled.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean referencesEnabled();

  /**
   * Called by the language server to check if finding definitions is enabled.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean definitionsEnabled();

  /**
   * Called by the language server to check if code analysis is enabled.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean codeAnalysisEnabled();

  /**
   * Called by the completions provider to check if it should match partial names in all lowercase.
   *
   * @return {@code true} if enabled, {@code false} otherwise.
   */
  boolean shouldMatchAllLowerCase();
}
