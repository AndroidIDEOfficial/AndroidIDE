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

  override fun createView(context: Context, widget: Widget<*>): View {
    return when (widget) {
      is TextFieldWidget -> createTextField(context, widget)
      is CheckBoxWidget -> createCheckBox(context, widget)
      is SpinnerWidget -> createSpinner(context, widget)
      else -> throw IllegalArgumentException("Unknown widget type : $widget")
    }
  }

  private fun createCheckBox(context: Context, widget: CheckBoxWidget): View {
    return LayoutCheckboxBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as BooleanParameter
      root.setText(param.name)
      root.isChecked = param.default

      root.addOnCheckedStateChangedListener { checkbox, _ ->
        param.setValue(checkbox.isChecked)
      }
    }.root
  }

  private fun createTextField(context: Context, widget: TextFieldWidget): View {
    return LayoutTextfieldBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as StringParameter
      param.configureTextField(context, root) { value ->
        param.setValue(value)
        val err =
          ConstraintVerifier.verify(value, constraints = param.constraints)

        root.isErrorEnabled = err != null
        if (err != null) {
          root.error = err
        }
      }
      input.setText(param.default)
    }.root
  }

  private fun createSpinner(context: Context, widget: SpinnerWidget<*>): View {
    return LayoutSpinnerBinding.inflate(LayoutInflater.from(context)).apply {
      val param = widget.parameter as EnumParameter<Enum<*>>

      val items = mutableMapOf<String, Enum<*>>()
      param.default.javaClass.enumConstants?.forEach {
        val displayName = param.displayName?.invoke(it) ?: it.name
        items[displayName] = it
      }

      check(
        items.isNotEmpty()) { "Cannot retrive values for enum parameter $param with default value ${param.default}" }

      val array = items.keys.toTypedArray()

      input.setAdapter(ArrayAdapter(context,
        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
        array))

      val defaultName =
        param.displayName?.invoke(param.default) ?: param.default.name

      root.isEnabled = items.size > 1
      input.listSelection = array.indexOf(defaultName)
      input.setText(defaultName, false)

      param.configureTextField(context, root) {
        param.setValue(items[it] ?: param.default)
      }
    }.root
  }

  private fun TextFieldParameter<*>.configureTextField(context: Context,
                                                       root: TextInputLayout,
                                                       onTextChanged: (String) -> Unit = {}
  ) {
    root.setHint(name)

    startIcon?.let {
      root.startIconDrawable = ContextCompat.getDrawable(context, it)
      onStartIconClick?.let { onClick ->
        root.setStartIconOnClickListener { onClick() }
      }
    }

    endIcon?.let {
      root.endIconDrawable = ContextCompat.getDrawable(context, it)
      onEndIconClick?.let { onClick ->
        root.setEndIconOnClickListener { onClick() }
      }
    }

    root.editText!!.addTextChangedListener(object : SingleTextWatcher() {
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int,
                                 count: Int
      ) {
        onTextChanged(s?.toString() ?: "")
      }
    })
  }
}