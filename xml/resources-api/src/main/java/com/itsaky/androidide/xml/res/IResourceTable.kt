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

package com.itsaky.androidide.xml.res

import com.android.aaptcompiler.ResourceName

/**
 * @author Akash Yadav
 */
interface IResourceTable {

  val packages: Collection<IResourceTablePackage>

  fun findResource(name: ResourceName): ISearchResult?

  /**
   * Returns the package struct with the given name, or null if such a package does not
   * exist. The empty string is a valid package and typically is used to represent the
   * 'current' package before it is known to the ResourceTable.
   *
   * @param name the name of the package.
   * @return the [IResourceTablePackage] with the requested name or {@code null} if that package
   *   does not exist in the table.
   */
  fun findPackage(name: String): IResourceTablePackage?
}