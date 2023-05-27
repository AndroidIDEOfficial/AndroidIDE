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

/**
 * Represents an UI element.
 *
 * Each template has a collection of widgets which is rendered when user selects a template.
 */
interface Widget<T> {

  fun release()
}

/**
 * Widget for the given [parameter].
 */
sealed class ParameterWidget<T>(val parameter: Parameter<T>) : Widget<T> {

  override fun release() {
    parameter.release()
  }
}

/**
 * Widget for a [StringParameter]. Creates a text field for the parameter.
 */
class TextFieldWidget(p: StringParameter) : ParameterWidget<String>(p)

/**
 * Widget for a [BooleanParameter]. Creates a checkbox for the parameter.
 */
class CheckBoxWidget(p: BooleanParameter) : ParameterWidget<Boolean>(p)

/**
 * Widget for an [EnumParameter]. Creates a spinner for the parameter.
 */
class SpinnerWidget<T : Enum<*>>(p: EnumParameter<T>) : ParameterWidget<T>(p)