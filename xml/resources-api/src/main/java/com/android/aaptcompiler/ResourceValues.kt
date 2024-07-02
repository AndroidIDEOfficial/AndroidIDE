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

import com.android.aapt.Resources
import com.android.aaptcompiler.AaptResourceType.MACRO
import com.android.aaptcompiler.Reference.Type.RESOURCE
import com.android.aaptcompiler.android.ResValue
import com.android.aaptcompiler.android.deviceToHost
import com.android.aaptcompiler.android.hostToDevice
import com.android.utils.ILogger
import java.io.File


open class Value {
  var source: Source = Source.EMPTY
  var comment = ""
  var weak = false
  var translatable = true
}

abstract class Item : Value() {
  abstract fun clone(newPool: StringPool): Item

  abstract fun flatten(): ResValue?
}

/**
 * An ID resource. Has no real value, just a place holder.
 */
class Id: Item() {
  init {
    weak = true
  }

  override fun equals(other: Any?) = other is Id

  override fun flatten(): ResValue? {
    return ResValue(ResValue.DataType.INT_BOOLEAN, 0.hostToDevice())
  }

  override fun clone(newPool: StringPool): Id {
    val newId = Id()
    newId.weak = weak
    newId.comment = comment
    newId.source = source
    return newId
  }
}

/**
 * A reference to another resource. This maps to android::Res_value::TYPE_REFERENCE.
 *
 * A reference can be symbolic (with the name set to a valid resource name) or be
 * numeric (the id is set to a valid resource ID).
 */

class Reference(var name: ResourceName = ResourceName.EMPTY): Item() {
  enum class Type {
    RESOURCE,
    ATTRIBUTE
  }

  var id : Int? = null
  var referenceType : Type = RESOURCE
  var isPrivate = false
  var isDynamic = false
  // Only used for macros, which can contain any format type
  var typeFlags: Int? = Resources.Attribute.FormatFlags.ANY_VALUE
  // Only used for macros, allos macros to have raw strings in them, not just references.
  var allowRaw = true

  override fun flatten(): ResValue? {
    if (name?.type == MACRO) {
        return null
    }
    val resId = id ?: 0
    val dynamic = resId.isValidDynamicId() && isDynamic

    val dataType = when {
      referenceType == RESOURCE ->
        if (dynamic) ResValue.DataType.DYNAMIC_REFERENCE else ResValue.DataType.REFERENCE
      else ->
        if (dynamic) ResValue.DataType.DYNAMIC_ATTRIBUTE else ResValue.DataType.ATTRIBUTE
    }

    return ResValue(dataType, resId.hostToDevice())
  }

  override fun equals(other: Any?): Boolean {
    if (other is Reference) {
      return referenceType == other.referenceType &&
        isPrivate == other.isPrivate &&
        id == other.id &&
        name == other.name &&
        allowRaw == other.allowRaw &&
        typeFlags == other.typeFlags
    }
    return false
  }

  override fun clone(newPool: StringPool): Reference {
    val newRef = Reference(this.name)
    newRef.id = id
    newRef.referenceType = referenceType
    newRef.isPrivate = isPrivate
    newRef.isDynamic = isDynamic
    newRef.comment = comment
    newRef.source = source
    return newRef
  }
}

class FileReference(val path: StringPool.Ref): Item() {
  // Handle to the file object from which this file can be read. This is only transient, and not
  // persisted in any format.
  var file: File? = null

  // FileType of the file pointed to by `file` This is used to know how to inflate the file, or
  // if to inflate at all (just copy)
  var type: ResourceFile.Type = ResourceFile.Type.Unknown

  override fun equals(other: Any?): Boolean {
    if (other is FileReference) {
      return path.value() == other.path.value() &&
        type == other.type
    }
    return false
  }

  override fun flatten(): ResValue? {
    return ResValue(ResValue.DataType.STRING, path.index().hostToDevice())
  }

  override fun clone(newPool: StringPool): FileReference {
    val newFileRef = FileReference(newPool.makeRef(path))
    newFileRef.file = file
    newFileRef.type = type
    newFileRef.comment = comment
    newFileRef.source = source
    return newFileRef
  }
}

data class BinaryPrimitive(val resValue: ResValue): Item() {
  override fun flatten(): ResValue? {
    return ResValue(resValue.dataType, resValue.data.hostToDevice())
  }

  override fun clone(newPool: StringPool): BinaryPrimitive {
    val newPrimitive = BinaryPrimitive(resValue)
    newPrimitive.comment = comment
    newPrimitive.source = source
    return newPrimitive
  }
}

data class AttributeResource(var typeMask: Int = 0, val logger: ILogger? = null): Value() {
  data class Symbol(val symbol: Reference, val value: Int, val type: Byte)

  var minInt = Int.MIN_VALUE
  var maxInt = Int.MAX_VALUE
  val symbols = mutableListOf<Symbol>()

  fun matches(item: Item): Boolean {
    val value = item.flatten()!!
    val flattenedData = value.data.deviceToHost()

    // Always allow references
    val actualType = androidTypeToAttributeTypeMask(value.dataType)

    // Only one type must match between the actual and the expected.
    if ((actualType and (typeMask or Resources.Attribute.FormatFlags.REFERENCE_VALUE) == 0)) {
      val errorMsg = "%s, Expected %s but got %s"
      logger?.error(null, errorMsg, blameSource(source), maskToString(typeMask), item)
      return false
    }

    // Enums and flags are encoded as integers, so check them first before doing any range checks.
    if ((typeMask and actualType and Resources.Attribute.FormatFlags.ENUM_VALUE) != 0) {

      for (symbol in symbols) {
        if (flattenedData == symbol.value) {
          return true
        }
      }

      // If the attribute accepts integers, we can't fail here.
      if ((typeMask and Resources.Attribute.FormatFlags.INTEGER_VALUE) == 0) {
        val errorMsg = "%s, %s is not a valid enum."
        logger?.error(null, errorMsg, blameSource(source), item)
        return false
      }
    }

    if ((typeMask and actualType and Resources.Attribute.FormatFlags.FLAGS_VALUE) != 0) {

      var allFlags = 0
      for (symbol in symbols) {
        allFlags = allFlags or symbol.value
      }

      // Check if the flattened data is covered by the flag bit mask.
      if ((allFlags and flattenedData) == flattenedData) {
        return true
      }

      // If the attribute accepts integers, we can't fail here.
      if ((typeMask and Resources.Attribute.FormatFlags.INTEGER_VALUE) == 0) {
        val errorMsg = "%s, %s is not a valid flag."
        logger?.error(null, errorMsg, blameSource(source), item)
        return false
      }
    }

    // If the value is an integer, we can't out of range.
    return true
  }

  fun isCompatibleWith(other: AttributeResource): Boolean {
    // if the high bits are set on any of these attribute type masks, then they are incompatible.
    // We don't check that flags and enums are identical.
    if ((typeMask and Resources.Attribute.FormatFlags.ANY_VALUE.inv()) != 0 ||
      (other.typeMask and Resources.Attribute.FormatFlags.ANY_VALUE.inv()) != 0) {
      return false
    }

    // Every attribute accepts a reference.
    val thisTypeMask = typeMask or Resources.Attribute.FormatFlags.REFERENCE_VALUE
    val otherTypeMask = other.typeMask or Resources.Attribute.FormatFlags.REFERENCE_VALUE

    return thisTypeMask == otherTypeMask
  }
}

private fun maskToString(typeMask: Int, target: Int, success: String) =
  if ((typeMask and target) == target) success else ""

private fun maskToString(typeMask: Int): String {
  val result = StringBuilder()
  result.append(maskToString(typeMask, Resources.Attribute.FormatFlags.BOOLEAN_VALUE, "| boolean"))
  result.append(maskToString(typeMask, Resources.Attribute.FormatFlags.COLOR_VALUE, "| color"))
  result.append(
    maskToString(typeMask, Resources.Attribute.FormatFlags.DIMENSION_VALUE, "| dimension")
  )
  result.append(maskToString(typeMask, Resources.Attribute.FormatFlags.ENUM_VALUE, "| enum"))
  result.append(maskToString(typeMask, Resources.Attribute.FormatFlags.FLAGS_VALUE, "| flags"))
  result.append(maskToString(typeMask, Resources.Attribute.FormatFlags.FLOAT_VALUE, "| float"))
  result.append(
    maskToString(typeMask, Resources.Attribute.FormatFlags.FRACTION_VALUE, "| fraction")
  )
  result.append(
    maskToString(typeMask, Resources.Attribute.FormatFlags.INTEGER_VALUE, "| integer")
  )
  result.append(
    maskToString(typeMask, Resources.Attribute.FormatFlags.REFERENCE_VALUE, "| reference")
  )
  result.append(
    maskToString(typeMask, Resources.Attribute.FormatFlags.STRING_VALUE, "| string")
  )
  return if (result.isNotEmpty()) result.substring(2) else ""
}

data class UntranslatableSection(var startIndex: Int, var endIndex: Int = startIndex) {
  fun shift(offset : Int): UntranslatableSection {
    return UntranslatableSection(startIndex + offset, endIndex + offset)
  }
}


/**
 * A raw, unprocessed string. This may contain quotations, escape sequences, and whitespace. This
 * shall *NOT* end up in the final resource table.
 */
class RawString(val value: StringPool.Ref) : Item() {
  override fun equals(other: Any?): Boolean {
    if (other is RawString) {
      return value.value() == other.value.value()
    }
    return false
  }

  override fun clone(newPool: StringPool): RawString {
    val newRaw = RawString(newPool.makeRef(value))
    newRaw.source = source
    newRaw.comment = comment
    return newRaw
  }

  override fun flatten(): ResValue? {
    return ResValue(ResValue.DataType.STRING, value.index().hostToDevice())
  }
}

/**
 * A processed string resource. Unlike [StyledString], the string does not contain any spans, and
 * is represented a single string.
 *
 * @param ref The reference to this basic string in the associated [StringPool].
 * @param untranslatables The list of indexed sections of this string that should not be translated.
 */
class BasicString(
  val ref: StringPool.Ref, val untranslatables: List<UntranslatableSection> = listOf()) : Item() {

  override fun toString(): String {
    return ref.value()
  }

  override fun equals(other: Any?): Boolean {
    if (other is BasicString) {
      if (toString() != other.toString()) {
        return false
      }

      return untranslatables == other.untranslatables
    }
    return false
  }

  override fun clone(newPool: StringPool): BasicString {
    val newString = BasicString(newPool.makeRef(ref), untranslatables)
    newString.comment = comment
    newString.source = source
    return newString
  }

  override fun flatten(): ResValue? {
    return ResValue(ResValue.DataType.STRING, ref.index().hostToDevice())
  }
}

/**
 * A processed string resource with xml spans. For example: "Hello <b>world!</b>"
 *
 * @param ref The reference to this StyledString in the associated [StringPool]. Use
 * [spans] to find the spans associated with this string.
 * @param untranslatableSections The list of indexed sections of this string that should not be
 * translated.
 */
class StyledString(
  val ref: StringPool.StyleRef,
  val untranslatableSections: List<UntranslatableSection>) : Item() {

  override fun equals(other: Any?): Boolean {
    if (other is StyledString) {
      return ref.value() == other.ref.value() &&
        ref.spans() == other.ref.spans()
    }
    return false
  }

  override fun toString(): String {
    return ref.value()
  }

  override fun clone(newPool: StringPool): StyledString {
    val newStyledString = StyledString(newPool.makeRef(ref), untranslatableSections)
    newStyledString.comment = comment
    newStyledString.source = source
    return newStyledString
  }

  override fun flatten(): ResValue? {
    return ResValue(ResValue.DataType.STRING, ref.index().hostToDevice())
  }

  fun spans() = ref.spans()
}

class ArrayResource: Value() {
  val elements = mutableListOf<Item>()


  override fun equals(other: Any?): Boolean {
    if (other is ArrayResource) {
      return elements == other.elements
    }
    return false
  }

  fun clone(newPool: StringPool): ArrayResource {
    val newArray = ArrayResource()
    newArray.source = source
    newArray.comment = comment
    for (item in elements) {
      newArray.elements.add(item.clone(newPool))
    }
    return newArray
  }
}

class Style: Value() {
  data class Entry(val key: Reference, val value: Item?)

  var parent: Reference? = null

  // If set to true, the parent was auto inferred from the style's name
  var parentInferred = false

  val entries = mutableListOf<Entry>()

  fun clone(pool: StringPool): Style {
    val newStyle = Style()
    newStyle.parent = parent
    newStyle.parentInferred = parentInferred
    newStyle.comment = comment
    newStyle.source = source
    for (entry in entries){
      newStyle.entries.add(
        Entry(entry.key, entry.value?.clone(pool))
      )
    }
    return newStyle
  }

  override fun equals(other: Any?): Boolean {
    if (other is Style) {
      if (parent != other.parent) {
        return false
      }

      val sortedEntriess = entries.sortedBy { it.key.name }
      val otherSortedEntries = other.entries.sortedBy { it.key.name }

      return sortedEntriess == otherSortedEntries
    }
    return false
  }

  fun mergeWith(other: Style, pool: StringPool) {
    if (other.parent != null) {
      parent = other.parent
    }

    val sortedEntries = entries.sortedBy { it.key.name }
    val otherSortedEntries = other.entries.sortedBy { it.key.name }

    val entryIterator = sortedEntries.iterator()
    val otherIterator = otherSortedEntries.iterator()

    val mergedEntries = mutableListOf<Entry>()

    var carry = if (otherIterator.hasNext()) otherIterator.next() else null
    while (entryIterator.hasNext()) {
      val entry = entryIterator.next()
      var entryOverridden = false
      while (carry != null && carry.key.name <= entry.key.name) {
        when {
          carry.key.name < entry.key.name -> mergedEntries.add(carry)
          carry.key.name == entry.key.name -> {
            // The other overrides, when the keys are the same.
            mergedEntries.add(carry)
            entryOverridden = true
          }
        }
        carry = if (otherIterator.hasNext()) otherIterator.next() else null
      }

      if (!entryOverridden) {
        mergedEntries.add(entry)
      }
    }

    if (carry != null) {
      mergedEntries.add(carry)
      while (otherIterator.hasNext()) {
        mergedEntries.add(otherIterator.next())
      }
    }

    entries.clear()
    for (entry in mergedEntries) {
      entries.add(Entry(entry.key, entry.value?.clone(pool)))
    }
  }

  override fun toString(): String {
    val sb = StringBuilder(parent?.name.toString() +"\n")
    for (entry in entries) {
      sb.appendLine(entry.key.name.toString() + "    " + entry.value?.toString())
    }
    return sb.toString()
  }
}

class Plural: Value() {
  enum class Type{
    ZERO,
    ONE,
    TWO,
    FEW,
    MANY,
    OTHER;

    companion object {
      val TYPES = Type.values()
      val NUM_TYPES = TYPES.size
    }
  }

  val values = arrayOfNulls<Item?>(Type.NUM_TYPES)

  fun setValue(type: Type, item: Item) {
    values[type.ordinal] = item
  }

  override fun equals(other: Any?): Boolean {
    if (other is Plural) {
      return values contentEquals other.values
    }
    return false
  }

  fun clone(newPool: StringPool): Plural {
    val newPlural = Plural()
    newPlural.comment = comment
    newPlural.source = source
    for (i in 0.until(Type.NUM_TYPES)) {
      newPlural.values[i] = values[i]?.clone(newPool)
    }
    return newPlural
  }
}

class Styleable: Value() {
  val entries = mutableListOf<Reference>()

  override fun equals(other: Any?): Boolean {
    if (other is Styleable) {
      return entries == other.entries
    }
    return false
  }
}

class Macro(
  var rawValue: String? = null,
  var styleString: StyleString? = null,
  var untranslatables: List<UntranslatableSection> = listOf(),
  var aliasNamespaces: List<Namespace> = listOf()): Value() {

    class Namespace(
        var alias: String? = null,
        var packageName: String? = null,
        var isPrivate: Boolean = false) {

        override fun equals(other: Any?): Boolean {
            if (other is Namespace) {
                return alias == other.alias && packageName == other.packageName
                        && isPrivate == isPrivate
            }
            return false
        }
    }
}
