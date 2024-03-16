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

package com.itsaky.androidide.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itsaky.androidide.preferences.utils.indentationString
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import jdkx.lang.model.element.Modifier.PROTECTED
import jdkx.lang.model.element.Modifier.PUBLIC

object ClassBuilder {
  @JvmStatic
  fun createClass(packageName: String, className: String): String {
    return toJavaFile(packageName, newClassSpec(className)).toString()
  }

  private fun toJavaFile(
    packageName: String,
    type: TypeSpec,
    block: JavaFile.Builder.() -> Unit = {}
  ): JavaFile {
    return JavaFile.builder(packageName, type)
      .indent(indentationString)
      .apply { block(this) }
      .build()
  }

  private fun newClassSpec(className: String): TypeSpec {
    return TypeSpec.classBuilder(className).addModifiers(PUBLIC).build()
  }

  @JvmStatic
  fun createInterface(packageName: String, className: String): String {
    return toJavaFile(packageName, newInterfaceSpec(className)).toString()
  }

  private fun newInterfaceSpec(className: String): TypeSpec {
    return TypeSpec.interfaceBuilder(className).addModifiers(PUBLIC).build()
  }

  @JvmStatic
  fun createEnum(packageName: String, className: String): String {
    return toJavaFile(packageName, newEnumSpec(className)).toString()
  }

  private fun newEnumSpec(className: String): TypeSpec {
    return TypeSpec.enumBuilder(className)
      .addModifiers(PUBLIC)
      .addEnumConstant("ENUM_DECLARED")
      .build()
  }

  @JvmStatic
  fun createActivity(packageName: String, className: String): String {
    val onCreate =
      MethodSpec.methodBuilder("onCreate")
        .addAnnotation(Override::class.java)
        .addModifiers(PROTECTED)
        .addParameter(Bundle::class.java, "savedInstanceState")
        .addStatement("super.onCreate(savedInstanceState)")
        .build()
    val activity =
      newClassSpec(className)
        .toBuilder()
        .superclass(AppCompatActivity::class.java)
        .addMethod(onCreate)
    return toJavaFile(packageName, activity.build()) { skipJavaLangImports(true) }.toString()
  }
}
