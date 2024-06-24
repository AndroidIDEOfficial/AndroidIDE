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

package com.itsaky.androidide.lsp.models

/** Contains information about a completion item. */
interface ICompletionData

/**
 * Information about a class-related completion item.
 *
 * @property className The fully qualified name of the class. Example: `pck.outer.inner`.
 * @property flatName The flat name of the class. Example: `pck.outer$inner`.
 * @property isCompleteData Whether the data provided by this [ClassCompletionData] is complete.
 * @property isNested Whether the given class is a nested class or not.
 * @property topLevelClass If [isNested] is true, then this must be set to the fully qualified name
 * of the top-level class.
 * @property simpleName The simple name of the class.
 * @property nameWithoutTopLevel The name of this class without the fully qualified name of its top
 * level class. For example, the value of this property for class name
 * `com.my.pck.MyClass.Inner.InnerInner` will be `Inner.InnerInner`
 */
data class ClassCompletionData
@JvmOverloads
constructor(
  val className: String,
  val isCompleteData: Boolean = false,
  val flatName: String = className,
  val simpleName: String = className.substringAfterLast(delimiter = '.'),
  val isNested: Boolean = false,
  val topLevelClass: String = ""
) : ICompletionData {

  val nameWithoutTopLevel: String = if (isNested) {
    className.substring(topLevelClass.length + 1)
  } else {
    className
  }
}

/**
 * Information about a member of a class.
 *
 * @property memberName The simple name of the class member.
 * @property classInfo Information about the class [memberName] is a member of.
 */
interface MemberCompletionData : ICompletionData {
  val memberName: String
  val classInfo: ClassCompletionData
}

/** Information about a field-related completion item. */
data class FieldCompletionData(
  override val memberName: String,
  override val classInfo: ClassCompletionData
) : MemberCompletionData

/**
 * Information about a method-related completion item.
 *
 * @property erasedParameterTypes The erased parameter types of the method.
 * @property plusOverloads The number of existing overloaded versions of this method.
 */
data class MethodCompletionData(
  override val memberName: String,
  override val classInfo: ClassCompletionData,
  val parameterTypes: List<String>,
  val erasedParameterTypes: List<String>,
  val plusOverloads: Int
) : MemberCompletionData
