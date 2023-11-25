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

package com.itsaky.androidide.editor.language.treesitter.internal

import com.itsaky.androidide.editor.language.treesitter.TSLanguageRegistry
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of [TSLanguageRegistry].
 *
 * @author Akash Yadav
 */
class TSLanguageRegistryImpl : TSLanguageRegistry {

  private val registry =
    ConcurrentHashMap<String, TreeSitterLanguage.Factory<out TreeSitterLanguage>>()

  override fun <T : TreeSitterLanguage> register(
    fileType: String,
    factory: TreeSitterLanguage.Factory<T>
  ) {
    val older = registry.put(fileType, factory)
    if (older != null) {
      registry[fileType] = older
      throw TSLanguageRegistry.AlreadyRegisteredException(fileType)
    }
  }

  override fun hasLanguage(fileType: String): Boolean {
    return registry.containsKey(fileType)
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : TreeSitterLanguage> getFactory(
    fileType: String
  ): TreeSitterLanguage.Factory<T> {
    return (registry[fileType] ?: throw TSLanguageRegistry.NotRegisteredException(fileType))
        as TreeSitterLanguage.Factory<T>
  }

  override fun destroy() {
    registry.clear()
  }
}
