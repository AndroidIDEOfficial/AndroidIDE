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

package com.itsaky.androidide.editor.language.treesitter

import com.itsaky.androidide.editor.language.treesitter.internal.TSLanguageRegistryImpl

/**
 * Registry for managing [TreeSitterLanguage factories][TreeSitterLanguage.Factory].
 *
 * @author Akash Yadav
 */
interface TSLanguageRegistry {

  companion object {

    @JvmStatic
    val instance by lazy { TSLanguageRegistryImpl() }
  }

  /**
   * Registers the given [factory] for the given file types.
   *
   * @param fileType The file extension for which the given factory should be used.
   * @param factory The factory which will create the [TreeSitterLanguage] instance.
   * @throws AlreadyRegisteredException If an instance of [TreeSitterLanguage.Factory] is already
   *   registered for the given file type.
   */
  fun <T : TreeSitterLanguage> register(fileType: String, factory: TreeSitterLanguage.Factory<T>)

  /**
   * Checks whether a [TreeSitterLanguage] has been registered for the given [file type][fileType].
   *
   * @return `true` if a [TreeSitterLanguage] has been registered for [fileType], `false` otherwise.
   */
  fun hasLanguage(fileType: String): Boolean

  /**
   * Returns the instance of the [TreeSitterLanguage.Factory] for the given file type.
   *
   * @param fileType The file type (extension) to create the language factory instance for.
   * @return The [TreeSitterLanguage.Factory] implmementation.
   * @throws NotRegisteredException If no [TreeSitterLanguage.Factory] is registered for the given
   *   file type.
   */
  fun <T : TreeSitterLanguage> getFactory(fileType: String): TreeSitterLanguage.Factory<T>

  /**
   * Destroys the language registry, removing all the registered language factory. This must be
   * called only when the application is exiting.
   */
  fun destroy()

  class AlreadyRegisteredException(type: String) :
    IllegalStateException(
      "An instance of TreeSitterLanguage.Factory is already registered for file type '$type'"
    )

  class NotRegisteredException(type: String) :
    RuntimeException("No TreeSitterLanguage.Factory registered for file type '$type'")
}
