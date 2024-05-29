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

package com.itsaky.androidide.tooling.api.util

import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.UNKNOWN_PACKAGE
import com.itsaky.androidide.tooling.api.IAndroidProject
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager
import java.io.File

/**
 * Find the package name for this library. If this library is not an [ANDROID_LIBRARY] or if error
 * occurs while extracting the package name, [UNKNOWN_PACKAGE] is returned.
 */
fun DefaultLibrary.findPackageName(): String {
  if (!lookupPackage) {
    return this.packageName
  }

  if (type != ANDROID_LIBRARY) {
    this.lookupPackage = false
    return UNKNOWN_PACKAGE
  }

  val manifestFile = androidLibraryData!!.manifest
  if (!manifestFile.exists()) {
    this.lookupPackage = false
    return UNKNOWN_PACKAGE
  }

  this.lookupPackage = false
  this.packageName = extractPackageName(manifestFile) ?: UNKNOWN_PACKAGE
  return this.packageName
}

/** Extracts package name from the given `AndroidManifest.xml` file. */
fun extractPackageName(manifestFile: File): String? {
  val content = manifestFile.readText()
  val document =
    DOMParser.getInstance()
      .parse(content, IAndroidProject.ANDROID_NAMESPACE, URIResolverExtensionManager())
  val manifest = document.children.first { it.nodeName == "manifest" } ?: return null
  val packageAttr = manifest.attributes.getNamedItem("package")
  if (packageAttr != null) {
    return packageAttr.nodeValue
  }
  return null
}