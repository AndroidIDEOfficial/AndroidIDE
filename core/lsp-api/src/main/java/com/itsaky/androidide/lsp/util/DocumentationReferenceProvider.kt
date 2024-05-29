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

import com.itsaky.androidide.lsp.models.ClassCompletionData
import com.itsaky.androidide.lsp.models.ICompletionData
import com.itsaky.androidide.lsp.models.MemberCompletionData
import com.itsaky.androidide.lsp.models.MethodCompletionData
import org.slf4j.LoggerFactory

/**
 * Provides the documentation URL for classes, methods, fields, etc.
 *
 * @author Akash Yadav
 */
object DocumentationReferenceProvider {

  private val log = LoggerFactory.getLogger(DocumentationReferenceProvider::class.java)

  const val DOCS_BASE_URL = "https://developer.android.com/reference/"

  /**
   * Package names whose documentation is most likely to be available on the Android Developers
   * website.
   */
  private val availablePackages =
    setOf(
      "android", // Android APIs
      "androidx", // AndroidX libraries
      "com.google.android.material", // Material Components
      "java" // Java APIs
    )

  /**
   * Get the documentation URL for given completion data.
   *
   * @return The URL or `null` if cannot be determined.
   */
  @JvmStatic
  fun getUrl(data: ICompletionData): String? {
    val klass =
      when (data) {
        is ClassCompletionData -> data
        is MemberCompletionData -> data.classInfo
        else -> return null
      }
    val url = StringBuilder(DOCS_BASE_URL)
    val baseName =
      if (klass.isNested) {
        klass.topLevelClass
      } else klass.className

    if (availablePackages.find { baseName.startsWith("$it.") } == null) {
      // This package is probably not listed on Android Developers documentation
      return null
    }

    url.append(baseName.replace('.', '/'))

    if (klass.isNested) {
      url.append('.')
      url.append(klass.nameWithoutTopLevel)
    }

    if (data is MemberCompletionData) {
      url.append('#')
      url.append(data.memberName)
    }

    if (data is MethodCompletionData) {
      url.append('(')
      url.append(data.parameterTypes.joinToString(separator = ", "))
      url.append(')')
    }

    log.debug("Documentation URL for {}#{} is {}", klass.className,
      ((data as? MemberCompletionData?)?.memberName ?: "<self>"), url)

    return url.toString()
  }
}
