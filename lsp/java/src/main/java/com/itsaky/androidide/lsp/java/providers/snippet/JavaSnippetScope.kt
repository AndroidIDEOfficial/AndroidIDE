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

package com.itsaky.androidide.lsp.java.providers.snippet

import com.itsaky.androidide.lsp.snippets.ISnippetScope

/**
 * Scope for [JavaSnippet].
 *
 * @author Akash Yadav
 */
enum class JavaSnippetScope(override val filename: String) : ISnippetScope {

  /**
   * Snippets that can be used at the top level. This includes snippes such as class, interface,
   * enum templates.
   */
  TOP_LEVEL("top-level"),

  /** Snippets that can be used at the member level i.e. inside a class tree. */
  MEMBER("member"),

  /** Snippets that can be used at a local level. E.g. inside a method or constructor. */
  LOCAL("local"),

  /** Snippets that can be used anywhere in the code, irrespective of the current scope. */
  GLOBAL("global")
}
