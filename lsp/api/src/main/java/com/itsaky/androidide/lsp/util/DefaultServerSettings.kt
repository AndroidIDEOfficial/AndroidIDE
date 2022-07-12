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

package com.itsaky.androidide.lsp.util

import com.itsaky.androidide.lsp.api.IServerSettings

/**
 * Default settings for a language server.
 *
 * @author Akash Yadav
 */
open class DefaultServerSettings : IServerSettings {
  override fun completionsEnabled(): Boolean = true
  override fun codeActionsEnabled(): Boolean = true
  override fun smartSelectionsEnabled(): Boolean = true
  override fun signatureHelpEnabled(): Boolean = true
  override fun referencesEnabled(): Boolean = true
  override fun definitionsEnabled(): Boolean = true
  override fun codeAnalysisEnabled(): Boolean = true

  override fun shouldMatchAllLowerCase(): Boolean = false
}
