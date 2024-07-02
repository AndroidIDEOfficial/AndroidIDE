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

package com.itsaky.androidide.xml.resources

import com.android.aaptcompiler.ResourceGroup
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.utils.ServiceLoader
import com.itsaky.androidide.xml.registry.XmlRegistry
import com.itsaky.androidide.xml.res.IResourceTable
import java.io.File

/**
 * Handles resource tables for multiple modules/dependencies and the framework resources.
 *
 * @author Akash Yadav
 */
interface ResourceTableRegistry : XmlRegistry<IResourceTable> {

  companion object {

    const val PCK_ANDROID = "android"

    @JvmStatic
    val COMPLETION_MODULE_RES = Lookup.Key<Set<IResourceTable>>()

    @JvmStatic
    val COMPLETION_DEP_RES = Lookup.Key<Set<IResourceTable>>()

    @JvmStatic
    val COMPLETION_FRAMEWORK_RES = Lookup.Key<IResourceTable>()

    @JvmStatic
    val COMPLETION_MANIFEST_ATTR_RES = Lookup.Key<IResourceTable>()

    private var sInstance: ResourceTableRegistry? = null

    @JvmStatic
    fun getInstance(): ResourceTableRegistry {
      val klass = ResourceTableRegistry::class.java
      return sInstance ?: ServiceLoader.load(klass, klass.classLoader).findFirstOrThrow()
        .also { sInstance = it }
    }
  }

  /**
   * Find the resource table by package name. Should not be used for platform resource tables.
   *
   * @param name The package name for the resource table.
   */
  fun forPackage(name: String, vararg resDirs: File): IResourceTable?

  /**
   * Remove the resource table entry for the given package name.
   *
   * @param packageName The package name to remove the resource table entry for.
   */
  fun removeTable(packageName: String)

  /**
   * Get the resource group which corresponds to the attributes for `AndroidManifest.xml`.
   *
   * @return The [ResourceGroup] or null if not found or not available.
   */
  fun getManifestAttrTable(platform: File): IResourceTable?

  /** Get the list of all activity actions for the given [platform] directory. */
  fun getActivityActions(platform: File): List<String>

  /** Get the list of all broadcast actions for the given [platform] directory. */
  fun getBroadcastActions(platform: File): List<String>

  /** Get the list of all service actions for the given [platform] directory. */
  fun getServiceActions(platform: File): List<String>

  /** Get the list of all categories for the given [platform] directory. */
  fun getCategories(platform: File): List<String>

  /** Get the list of all features for the given [platform] directory. */
  fun getFeatures(platform: File): List<String>

  override fun forPlatformDir(platform: File): IResourceTable? {
    return forPackage(PCK_ANDROID, File(platform, "data/res"))
  }
}
