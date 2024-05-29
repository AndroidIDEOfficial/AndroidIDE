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

package com.itsaky.androidide.templates.base.util

import com.itsaky.androidide.templates.Language
import com.itsaky.androidide.templates.SrcSet
import com.itsaky.androidide.templates.base.ModuleTemplateBuilder
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import jdkx.lang.model.element.Modifier

/**
 * Utility for building/writing source files.
 *
 * @author Akash Yadav
 */
class SourceWriter {

  /**
   * The type of Java source. A [SourceType] creates a [TypeSpec.Builder] and optionally
   * pre-configures it.
   */
  interface SourceType {

    /**
     * Create the [TypeSpec.Builder] for building this type of Java source file.
     */
    fun builder(klass: ClassName): TypeSpec.Builder
  }

  /**
   * A class type.
   */
  class ClassType : SourceType {

    override fun builder(klass: ClassName): TypeSpec.Builder {
      return TypeSpec.classBuilder(klass)
    }
  }

  /**
   * An interface type.
   */
  class InterfaceType : SourceType {

    override fun builder(klass: ClassName): TypeSpec.Builder {
      return TypeSpec.interfaceBuilder(klass)
    }
  }

  /**
   * An enum type.
   */
  class EnumType : SourceType {

    override fun builder(klass: ClassName): TypeSpec.Builder {
      return TypeSpec.enumBuilder(klass)
    }
  }

  /**
   * An annotation interface type.
   */
  class AnnotationType : SourceType {

    override fun builder(klass: ClassName): TypeSpec.Builder {
      return TypeSpec.annotationBuilder(klass)
    }
  }

  /**
   * Creates a new Java class.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param configure Function to configure the [TypeSpec.Builder].
   */
  inline fun ModuleTemplateBuilder.createClass(packageName: String, className: String,
                                        crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    return createJavaFile(packageName, className, ClassType(), configure)
  }

  /**
   * Creates a new Java enum class.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param configure Function to configure the [TypeSpec.Builder].
   */
  inline fun ModuleTemplateBuilder.createEnum(packageName: String, className: String,
                                       crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    return createJavaFile(packageName, className, EnumType(), configure)
  }

  /**
   * Creates a new Java interface.
   *
   * @param packageName The package name of the interface.
   * @param className The name of the interface.
   * @param configure Function to configure the [TypeSpec.Builder].
   */
  inline fun ModuleTemplateBuilder.createInterface(packageName: String, className: String,
                                            crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    return createJavaFile(packageName, className, InterfaceType(), configure)
  }

  /**
   * Creates a new Java annotation interface.
   *
   * @param packageName The package name of the interface.
   * @param className The name of the interface.
   * @param configure Function to configure the [TypeSpec.Builder].
   */
  inline fun ModuleTemplateBuilder.createAnnotation(packageName: String, className: String,
                                             crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    return createJavaFile(packageName, className, AnnotationType(), configure)
  }

  /**
   * Creates a new Java class.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param type The type of the class.
   * @param configure Function to configure the [TypeSpec.Builder].
   */
  inline fun ModuleTemplateBuilder.createJavaFile(packageName: String, className: String, type: SourceType,
                                           crossinline configure: TypeSpec.Builder.() -> Unit
  ) {
    val klass = ClassName.get(packageName, className)
    val builder = type.builder(klass).apply(configure).also {
      if (!it.modifiers.contains(Modifier.PUBLIC)) {
        it.addModifiers(Modifier.PUBLIC)
      }
    }
    val file = JavaFile.builder(packageName, builder.build())
    file.skipJavaLangImports(true)
    write(file.build())
  }

  /**
   * Writes the contents to a new Java file.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param source The source code for the file.
   */
  fun ModuleTemplateBuilder.writeJavaSrc(packageName: String, className: String,
                                         srcSet: SrcSet = SrcSet.Main, source: String
  ) {
    executor.save(source, srcFilePath(srcSet, packageName, className, Language.Java))
  }

  /**
   * Writes the contents to a new Kotlin file.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param source The source code for the file.
   */
  fun ModuleTemplateBuilder.writeKtSrc(packageName: String, className: String,
                                       srcSet: SrcSet = SrcSet.Main, source: String
  ) {
    executor.save(source, srcFilePath(srcSet, packageName, className, Language.Kotlin))
  }

  /**
   * Writes the contents to a new Java file.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param source A function which returns the source code for the file.
   */
  inline fun ModuleTemplateBuilder.writeJavaSrc(packageName: String, className: String,
                                         srcSet: SrcSet = SrcSet.Main, crossinline source: () -> String
  ) {
    writeJavaSrc(packageName, className, srcSet, source())
  }

  /**
   * Writes the contents to a new Kotlin file.
   *
   * @param packageName The package name of the class.
   * @param className The name of the class.
   * @param source A function which returns the source code for the file.
   */
  inline fun ModuleTemplateBuilder.writeKtSrc(packageName: String, className: String,
                                       srcSet: SrcSet = SrcSet.Main, crossinline source: () -> String
  ) {
    writeKtSrc(packageName, className, srcSet, source())
  }

  @PublishedApi
  internal fun ModuleTemplateBuilder.write(file: JavaFile) {
    file.writeTo(mainJavaSrc())
  }
}
