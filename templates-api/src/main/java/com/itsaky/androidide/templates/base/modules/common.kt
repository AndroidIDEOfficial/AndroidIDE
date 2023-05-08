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

package com.itsaky.androidide.templates.base.modules

import com.itsaky.androidide.templates.base.ModuleTemplateBuilder
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import jdkx.lang.model.element.Modifier

internal fun ModuleTemplateBuilder.dependencies(): String {
  if (dependencies.isEmpty()) {
    return ""
  }

  return StringBuilder().apply {
    append("dependencies {")
    for (dep in dependencies) {
      append("    ${dep.configuration.configName}(\"${dep.group}:${dep.artifact}:${dep.version}\")")
    }
    append("}")
  }.toString()
}

/**
 * Creates a new [FieldSpec] for a static constant and adds it to this [TypeSpec].
 *
 * @param name The name of the field.
 * @param configure A function to configure the field.
 */
fun TypeSpec.Builder.createConstant(type: TypeName, name: String, isPublic: Boolean,
                                    configure: FieldSpec.Builder.() -> Unit
) {
  val accessMod = if (isPublic) Modifier.PUBLIC else Modifier.PRIVATE
  FieldSpec.builder(type, name, accessMod, Modifier.STATIC, Modifier.FINAL)
    .apply(configure)
    .build()
    .also { addField(it) }
}

/**
 * Creates a new [FieldSpec] and adds it to this [TypeSpec].
 *
 * @param name The name of the field.
 * @param configure A function to configure the field.
 */
fun TypeSpec.Builder.createField(type: TypeName, name: String, vararg modifiers: Modifier,
                                 configure: FieldSpec.Builder.() -> Unit
) {
  FieldSpec.builder(type, name, *modifiers).apply(configure).build().also { addField(it) }
}

/**
 * Creates a new [MethodSpec] for a constructor and adds it to this [TypeSpec].
 *
 * @param configure A function to configure the method.
 */
fun TypeSpec.Builder.createConstructor(configure: MethodSpec.Builder.() -> Unit) {
  MethodSpec.constructorBuilder().apply(configure).build().also { addMethod(it) }
}

/**
 * Creates a new [MethodSpec] and adds it to this [TypeSpec].
 *
 * @param name The name of the method.
 * @param configure A function to configure the method.
 */
fun TypeSpec.Builder.createMethod(name: String, configure: MethodSpec.Builder.() -> Unit) {
  MethodSpec.methodBuilder(name).apply(configure).build().also { addMethod(it) }
}