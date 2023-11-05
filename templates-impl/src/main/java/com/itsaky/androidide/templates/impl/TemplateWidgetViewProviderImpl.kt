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

package com.itsaky.androidide.templates.impl

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.auto.service.AutoService
import com.itsaky.androidide.templates.BooleanParameter
import com.itsaky.androidide.templates.CheckBoxWidget
import com.itsaky.androidide.templates.EnumParameter
import com.itsaky.androidide.templates.ITemplateWidgetViewProvider
import com.itsaky.androidide.templates.Parameter
import com.itsaky.androidide.templates.Parameter.DefaultObserver
import com.itsaky.androidide.templates.ParameterWidget
import com.itsaky.androidide.templates.SpinnerWidget
import com.itsaky.androidide.templates.StringParameter
import com.itsaky.androidide.templates.TextFieldParameter
import com.itsaky.androidide.templates.TextFieldWidget
import com.itsaky.androidide.templates.Widget
import com.itsaky.androidide.templates.impl.databinding.LayoutCheckboxBinding
import com.itsaky.androidide.templates.impl.databinding.LayoutSpinnerBinding
import com.itsaky.androidide.templates.impl.databinding.LayoutTextfieldBinding
import com.itsaky.androidide.utils.ServiceLoader
import com.itsaky.androidide.utils.SingleTextWatcher


/**
 * Default implementation of [ITemplateWidgetViewProvider].
 *
 * @author Akash Yadav
 */
@AutoService(ITemplateWidgetViewProvider::class)
class TemplateWidgetViewProviderImpl : ITemplateWidgetViewProvider {

  companion object {

    @JvmStatic
    private var service: ITemplateWidgetViewProvider? = null

    @JvmStatic
    fun getInstance(reload: Boolean = false): ITemplateWidgetViewProvider {
      if (reload) {
        service = null
      }
      return ServiceLoader.load(ITemplateWidgetViewProvider::class.java)
        .findFirstOrThrow()
        .also { service = it }
    }
  }

  override fun <T> createView(context: Context, widget: Widget<T>): View {
    if (widget is ParameterWidget<T>) {
      widget.parameter.apply {
        beforeCreateView()
        setValue(value, false)
      }
    }

    return when (widget) {
      is TextFieldWidget -> createTextField(context, widget)
      is CheckBoxWidget -> createCheckBox(context, widget)
      is SpinnerWidget -> createSpinner(context, widget)
      else -> throw IllegalArgumentException("Unknown widget type : $widget")
    }.also {
      if (widget is ParameterWidget<T>) {
        widget.parameter.afterCreateView()
      }
    }
  }

  private fun createCheckBox(context: Context, widget: CheckBoxWidget): View {
    return LayoutCheckboxBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as BooleanParameter
      root.setText(param.name)
      root.isChecked = param.value

      val observer = object : DefaultObserver<Boolean>() {
        override fun onChanged(parameter: Parameter<Boolean>) {
          disableAndRun {
            root.isChecked = param.value
          }
        }
      }

      root.addOnCheckedStateChangedListener { checkbox, _ ->
        observer.disableAndRun {
          param.setValue(checkbox.isChecked)
        }
      }

      param.observe(observer)
    }.root
  }

  private fun createTextField(context: Context, widget: TextFieldWidget): View {
    return LayoutTextfieldBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as StringParameter
      val observer = object : DefaultObserver<String>() {
        override fun onChanged(parameter: Parameter<String>) {
          disableAndRun {
            input.setText(param.value)
          }
        }
      }

      param.configureTextField(context, root) { value ->
        observer.disableAndRun {
          param.setValue(value)
          param.resetStartAndEndIcons(root.context, root)
        }

        val err =
          ConstraintVerifier.verify(value, constraints = param.constraints)

        root.isErrorEnabled = err != null
        if (err != null) {
          root.error = err
        }

      }

      input.setText(param.value)
      param.observe(observer)

    }.root
  }

  private fun createSpinner(context: Context, widget: SpinnerWidget<*>): View {
    return LayoutSpinnerBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as EnumParameter<Enum<*>>

      val nameToEnum = mutableMapOf<String, Enum<*>>()
      val enumToName = mutableMapOf<Enum<*>, String>()
      param.value.javaClass.enumConstants?.forEach {

        // remove the elements for which the filter fails
        if (param.filter?.invoke(it) == false) {
          return@forEach
        }

        val displayName = param.displayName?.invoke(it) ?: it.name
        nameToEnum[displayName] = it
        enumToName[it] = displayName
      }

      check(
        nameToEnum.isNotEmpty()) { "Cannot retrive values for enum parameter $param with default value ${param.default}" }

      val array = nameToEnum.keys.toTypedArray()

      input.setAdapter(ArrayAdapter(context,
        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
        array))

      val defaultName = enumToName[param.default] ?: param.default.name

      root.isEnabled = nameToEnum.size > 1
      input.listSelection = array.indexOf(defaultName)
      input.setText(defaultName, false)

      val observer = object : DefaultObserver<Enum<*>>() {
        override fun onChanged(parameter: Parameter<Enum<*>>) {
          (parameter as EnumParameter<*>).apply {
            disableAndRun {
              input.setText(enumToName[value])
            }
          }
        }
      }

      if (param.default != nameToEnum[defaultName]) {
        // the default value may have been filtered
        // reset the value to the first item
        val first = checkNotNull(
          nameToEnum.values.firstOrNull()) { "No entries available for enum parameter (all entries filtered out?)." }
        param.setValue(first)
      }

      param.configureTextField(context, root) {
        observer.disableAndRun {
          param.setValue(nameToEnum[it] ?: param.default)
          param.resetStartAndEndIcons(root.context, root)
        }
      }

      param.observe(observer)
    }.root
  }

  private inline fun TextFieldParameter<*>.configureTextField(context: Context,
                                                       root: TextInputLayout,
                                                       crossinline onTextChanged: (String) -> Unit = {}
  ) {
    root.setHint(name)
    resetStartAndEndIcons(context, root)
    root.editText!!.addTextChangedListener(object : SingleTextWatcher() {
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int,
                                 count: Int
      ) {
        onTextChanged(s?.toString() ?: "")
      }
    })
  }

  private fun <T> TextFieldParameter<T>.resetStartAndEndIcons(context: Context,
                                                          root: TextInputLayout
  ) {
    startIcon?.let {
      root.startIconDrawable = ContextCompat.getDrawable(context, it(this))
      onStartIconClick?.let(root::setStartIconOnClickListener)
    }

    endIcon?.let {
      root.endIconDrawable = ContextCompat.getDrawable(context, it(this))
      onEndIconClick?.let(root::setEndIconOnClickListener)
    }
  }
}