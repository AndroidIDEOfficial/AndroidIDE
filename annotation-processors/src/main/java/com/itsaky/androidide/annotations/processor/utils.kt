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

package com.itsaky.androidide.annotations.processor

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"

const val VIEW_CLASS = "android.view.View"

const val ADAPTER_BASE_CLASS_PCK = "com.itsaky.androidide.inflater"
const val ADAPTER_BASE_CLASS_NAME = "IViewAdapter"
const val ADAPTER_BASE_CLASS = "$ADAPTER_BASE_CLASS_PCK.$ADAPTER_BASE_CLASS_NAME"
const val ADAPTER_FUNC_CREATE_WIDGET = "createUiWidgets"

const val INDEX_PACKAGE_NAME = "com.itsaky.androidide.inflater.internal"
const val INDEX_CLASS_NAME = "ViewAdapterIndexImpl"
const val INDEX_MAP_FIELD = "adapterMap"
const val INDEX_PROVIDER_MAP_FIELD = "widgetProviders"

const val METHOD_SET_SUPERCLASS_HIERARCHY = "setSuperclassHierarchy"
const val METHOD_SET_MODULE = "setModuleNamespace"

val viewAdapterInterface: ClassName =
  ClassName.get("com.itsaky.androidide.inflater", "IViewAdapterIndex")

private var adapterElement: TypeElement? = null
private var viewElement: TypeElement? = null

fun ProcessingEnvironment.adapterElement(): TypeElement {
  return adapterElement
    ?: elementUtils.getTypeElement(ADAPTER_BASE_CLASS)!!.also { adapterElement = it }
}

fun ProcessingEnvironment.viewElement(): TypeElement {
  return viewElement ?: elementUtils.getTypeElement(VIEW_CLASS)!!.also { viewElement = it }
}

fun ProcessingEnvironment.erasure(typeElement: TypeElement): TypeMirror? {
  return typeUtils.erasure(typeElement.asType())
}
