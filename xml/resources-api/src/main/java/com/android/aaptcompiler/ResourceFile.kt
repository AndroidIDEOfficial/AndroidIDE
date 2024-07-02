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

/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.aaptcompiler

import com.android.aaptcompiler.AaptResourceType.RAW

data class ResourceFile(
  val name: ResourceName,
  val configuration: ConfigDescription,
  val source: Source,
  val type: Type,
  val exportedSymbols: MutableList<SourcedResourceName> = mutableListOf()) {

  override fun equals(other: Any?): Boolean {
    if (other !is ResourceFile) return false
    return name == other.name
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }

  enum class Type {
      Unknown,
      Png,
      BinaryXml,
      ProtoXml
  }
}

data class ResourceName(
  val pck: String?,
  val type: AaptResourceType = RAW,
  val entry: String? = null): Comparable<ResourceName> {

  override fun compareTo(other: ResourceName): Int {
    val pckCompare = when {
      pck === other.pck -> 0
      pck == null -> -1
      other.pck == null -> 1
      else -> pck.compareTo(other.pck)
    }
    if (pckCompare != 0) {
      return pckCompare
    }

    val typeCompare = type.compareTo(other.type)
    if (typeCompare != 0) {
      return typeCompare
    }

    return when {
      entry === other.entry -> 0
      entry == null -> -1
      other.entry == null -> 1
      else -> entry.compareTo(other.entry)
    }
  }

  override fun toString() : String {
        val maybePck = if (!pck.isNullOrEmpty()) "$pck:" else ""
        return "$maybePck${type.tagName}/$entry"
    }

  companion object {
    val EMPTY = ResourceName("", RAW, "")
  }
}

data class SourcedResourceName(val name: ResourceName, val line: Int)

fun Int.isValidId(): Boolean = (this and 0xff000000.toInt()) != 0 && this.isValidDynamicId()

fun Int.isValidDynamicId(): Boolean = (this and 0x00ff0000) != 0

fun Int.getPackageId(): Byte = (this shr 24).toByte()

fun Int.getTypeId(): Byte = (this shr 16).toByte()

fun Int.getEntryId(): Short = this.toShort()

fun resourceIdFromParts(packageId: Byte, typeId: Byte, entryId: Short): Int =
  (packageId.toInt() shl 24) or (typeId.toInt() shl 16) or (entryId.toInt())
