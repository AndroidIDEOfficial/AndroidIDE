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

import com.android.aaptcompiler.ResourceTable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.xml.registry.XmlRegistry
import com.itsaky.androidide.xml.resources.internal.DefaultResourceTableRegistry
import java.io.File

/**
 * Handles resource tables for multiple modules/dependencies and the framework resources.
 *
 * @author Akash Yadav
 */
interface ResourceTableRegistry : XmlRegistry<ResourceTable> {

  companion object {

    @JvmStatic val COMPLETION_MODULE_RES_LOOKUP_KEY = Lookup.Key<Set<ResourceTable>>()
    @JvmStatic val COMPLETION_FRAMEWORK_RES_LOOKUP_KEY = Lookup.Key<ResourceTable>()

    @JvmStatic fun getInstance(): ResourceTableRegistry = DefaultResourceTableRegistry
  }

  /**
   * Get the resource table for the given resource directory. Creates a resource table if not
   * already created.
   *
   * @param dir The directory to create resource table for.
   */
  fun forResourceDir(dir: File): ResourceTable?

  /**
   * Remove the resource table entry for the given resource directory.
   *
   * @param dir The resource directory to remove the resource table entry for.
   */
  fun removeTable(dir: File)

  override fun forPlatformDir(platform: File): ResourceTable? {
    return forResourceDir(File(platform, "data/res"))
  }
}
