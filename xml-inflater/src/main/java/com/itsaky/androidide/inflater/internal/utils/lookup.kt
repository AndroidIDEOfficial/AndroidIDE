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

package com.itsaky.androidide.inflater.internal.utils

import com.android.SdkConstants
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourceEntry
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.ResourceTablePackage
import com.itsaky.androidide.inflater.utils.module
import com.itsaky.androidide.utils.ILogger

private val log: ILogger = ILogger.newInstance("ParseLookupUtils")

internal data class LookupResult(
  val table: ResourceTable,
  val group: ResourceGroup,
  val pack: ResourceTablePackage,
  val entry: ResourceEntry
)

internal fun lookupUnqualifedResource(
  type: AaptResourceType,
  name: String,
  value: String?
): LookupResult? {
  if (name.isBlank()) {
    throw IllegalArgumentException("Cannot parse resource reference: '$value'")
  }
  val (table, group, pack, entry) =
    findUnqualifiedResourceEntry(type, name)
      ?: run {
        log.warn("Unable to find resource entry '$value'")
        return null
      }

  return LookupResult(table, group, pack, entry)
}

internal fun findUnqualifiedResourceEntry(type: AaptResourceType, name: String): LookupResult? {
  var resTable: ResourceTable? = null
  var resGrp: ResourceGroup? = null
  var resPck: ResourceTablePackage? = null
  var resEntry: ResourceEntry? = null
  for (t in module.getAllResourceTables()) {
    val entries =
      t.packages.mapNotNull {
        if (it.name == SdkConstants.ANDROID_PKG) {
          // Do not look in 'android' package
          return@mapNotNull null
        }

        val group = it.findGroup(type) ?: return@mapNotNull null
        val entry = group.findEntry(name) ?: return@mapNotNull null
        Triple(it, group, entry)
      }
    if (entries.isEmpty()) {
      continue
    }
    val result = entries.first()
    resTable = t
    resPck = result.first
    resGrp = result.second
    resEntry = result.third
    break
  }
  if (resTable == null) {
    return null
  }
  return LookupResult(resTable, resGrp!!, resPck!!, resEntry!!)
}

internal fun findQualifedResourceEntry(
  pack: String,
  type: AaptResourceType,
  name: String
): ResourceEntry? {
  return module
    .findResourceTableForPackage(pack, type)
    ?.findResource(ResourceName(pack, type, name))
    ?.entry
}

internal fun findAttributeResource(
  pck: String?,
  type: AaptResourceType,
  name: String
): AttributeResource? {
  val entry =
    if (pck == null) {
      (findUnqualifiedResourceEntry(type, name) ?: return null).entry
    } else findQualifedResourceEntry(pck, type, name)

  return entry?.findValue(ConfigDescription())?.value as? AttributeResource
}
