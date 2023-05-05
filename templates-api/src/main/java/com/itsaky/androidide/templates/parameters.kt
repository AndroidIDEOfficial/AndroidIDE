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

package com.itsaky.androidide.templates

import androidx.annotation.StringRes

enum class ParameterConstraint {

  /**
   * Value must be unique.
   */
  UNIQUE,

  /**
   * Value must be a valid Java package name.
   */
  PACKAGE,

  /**
   * Value must a valid fully qualified Java class name.
   */
  CLASS,

  /**
   * Value must be a valid Java class name.
   */
  CLASS_NAME,

  /**
   * Value must be a valid Gradle module name.
   */
  MODULE_NAME,

  /**
   * Value must not be empty or blank.
   */
  NONEMPTY,

  /**
   * Value must be a valid layout file name.
   */
  LAYOUT,

  /**
   * Value must path to a file.
   */
  FILE,

  /**
   * Value must path to a directory.
   */
  DIRECTORY,

  /**
   * Used with [FILE] and [DIRECTORY]. Asserts that the file/directory at the given path exists.
   */
  EXISTS
}

typealias ValueSuggestion<T> = Parameter<T>.() -> T?

abstract class Parameter<T>(@StringRes val name: Int, @StringRes val description: Int?,
                            val default: T, var constraints: List<ParameterConstraint>,
                            var suggest: ValueSuggestion<T>
) {

  var value: T? = null
    get() = field ?: default
}

abstract class ParameterBuilder<T>() {

  @StringRes
  var name: Int? = null

  @StringRes
  var description: Int? = null
  var default: T? = null

  var constraints: List<ParameterConstraint> = emptyList()

  var suggest: ValueSuggestion<T> = { null }

  protected open fun validate() {
    checkNotNull(name) { "Parameter must have a name" }
    checkNotNull(default) { "Parameter must have a default value" }
  }

  abstract fun build(): Parameter<T>
}

class StringParameter(@StringRes name: Int, @StringRes description: Int?, default: String,
                      constraints: List<ParameterConstraint>, suggest: ValueSuggestion<String>
) : Parameter<String>(name, description, default, constraints, suggest)

class StringParameterBuilder : ParameterBuilder<String>() {

  override fun build(): StringParameter {
    return StringParameter(name!!, description, default!!, constraints, suggest)
  }
}

class BooleanParameter(@StringRes name: Int, @StringRes description: Int?, default: Boolean,
                       constraints: List<ParameterConstraint>, suggest: ValueSuggestion<Boolean>
) : Parameter<Boolean>(name, description, default, constraints, suggest)

class BooleanParameterBuilder : ParameterBuilder<Boolean>() {

  override fun build(): BooleanParameter {
    return BooleanParameter(name!!, description, default!!, constraints, suggest)
  }
}

class EnumParameter<T : Enum<*>>(@StringRes name: Int, @StringRes description: Int?, default: T,
                                 constraints: List<ParameterConstraint>, suggest: ValueSuggestion<T>
) : Parameter<T>(name, description, default, constraints, suggest)

class EnumParameterBuilder<T : Enum<*>> : ParameterBuilder<T>() {

  override fun build(): EnumParameter<T> {
    return EnumParameter(name!!, description, default!!, constraints, suggest)
  }
}

/**
 * Create a new [StringParameter] for accepting string input.
 */
fun stringParameter(block: StringParameterBuilder.() -> Unit
): StringParameter = StringParameterBuilder().apply(block).build()

/**
 * Create a new [BooleanParameter] for accepting boolean input.
 */
fun booleanParameter(block: BooleanParameterBuilder.() -> Unit
): BooleanParameter = BooleanParameterBuilder().apply(block).build()

fun <T : Enum<*>> enumParameter(block: EnumParameterBuilder<T>.() -> Unit): EnumParameter<T> =
  EnumParameterBuilder<T>().apply(block).build()