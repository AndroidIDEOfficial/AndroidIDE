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

package com.itsaky.androidide.annotations.inflater

import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.ElementKind.PACKAGE
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic.Kind.ERROR

@Suppress("unused")
@AutoService(Processor::class)
class AttrAdapterAnnotationProcessor : AbstractProcessor() {

  private val adapterElement by lazy {
    processingEnv.elementUtils.getTypeElement(ADAPTER_BASE_CLASS)
  }

  private val classBuilder = TypeSpec.classBuilder(INDEX_CLASS_NAME).addModifiers(PUBLIC, FINAL)
  private val addStatements = CodeBlock.builder()
  init {
    val adpaterType = ClassName.get(ADAPTER_BASE_CLASS_PCK, ADAPTER_BASE_CLASS_NAME)
    val type =
      ParameterizedTypeName.get(
        ClassName.get(java.util.Map::class.java),
        ClassName.get(String::class.java),
        adpaterType
      )
    classBuilder.addField(
      FieldSpec.builder(type, INDEX_MAP_FIELD, PRIVATE, STATIC, FINAL)
        .initializer("new \$T<>();", java.util.HashMap::class.java)
        .build()
    )

    classBuilder.addMethod(
      MethodSpec.constructorBuilder()
        .addModifiers(PRIVATE)
        .addStatement(
          "throw new \$T(\$S)",
          UnsupportedOperationException::class.java,
          "This class cannot be instantiated."
        )
        .build()
    )

    classBuilder.addMethod(
      MethodSpec.methodBuilder("getAdapter")
        .addAnnotation(ClassName.get("androidx.annotation", "Nullable"))
        .addModifiers(PUBLIC, STATIC)
        .addParameter(String::class.java, "view", FINAL)
        .returns(adpaterType)
        .addStatement("return \$L.get(\$L)", INDEX_MAP_FIELD, "view")
        .build()
    )
  }

  companion object {
    const val ADAPTER_BASE_CLASS_PCK = "com.itsaky.androidide.inflater"
    const val ADAPTER_BASE_CLASS_NAME = "IAttributeAdapter"
    const val ADAPTER_BASE_CLASS = "$ADAPTER_BASE_CLASS_PCK.$ADAPTER_BASE_CLASS_NAME"
    const val INDEX_PACKAGE_NAME = "com.itsaky.androidide.inflater.internal"
    const val INDEX_CLASS_NAME = "AttributeAdapterIndex"
    const val INDEX_MAP_FIELD = "adapterMap"

    const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
  }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(AttributeAdapter::class.java.name)
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latestSupported()
  }

  override fun process(
    annotations: MutableSet<out TypeElement>?,
    roundEnv: RoundEnvironment
  ): Boolean {
    if (roundEnv.processingOver()) {
      return true
    }
    roundEnv.getElementsAnnotatedWith(AttributeAdapter::class.java).forEach {
      if (it.kind != CLASS) {
        processingEnv.messager.printMessage(
          ERROR,
          "@AttributeAdapter can only be used with classes. Used on $it"
        )
        return true
      }
      process(it)
    }

    classBuilder.addStaticBlock(addStatements.build())
    val file = JavaFile.builder(INDEX_PACKAGE_NAME, classBuilder.build())
    file
      .build()
      .writeTo(
        File(
          processingEnv.options[KAPT_KOTLIN_GENERATED]
            ?: throw IllegalStateException("Cannot find kapt output directory path")
        )
      )

    return false
  }

  private fun process(element: Element) {
    if (element.enclosingElement?.kind != PACKAGE || element !is TypeElement) {
      processingEnv.messager.printMessage(
        ERROR,
        "@AttributeAdapter can only be used on top-level classes. Used on $element"
      )
      return
    }

    val constructors =
      processingEnv.elementUtils.getAllMembers(element).filter {
        "<init>".contentEquals(it.simpleName)
      }
    if (constructors.size != 1) {
      processingEnv.messager.printMessage(
        ERROR,
        "An attribute adapter must have only default constructor"
      )
      return
    }

    if ((constructors.first() as ExecutableElement).parameters.isNotEmpty()) {
      processingEnv.messager.printMessage(
        ERROR,
        "Attribute adapter constructor should not have any parameters"
      )
      return
    }

    if (!processingEnv.typeUtils.isSubtype(element.asType(), adapterElement.asType())) {
      processingEnv.messager.printMessage(ERROR, "Class must be a subtype of $ADAPTER_BASE_CLASS")
      return
    }

    val annotation =
      element.getAnnotation(AttributeAdapter::class.java) ?: throw IllegalStateException()
    addStatements.addStatement(
      "\$L.put(\$S, new \$T())",
      INDEX_MAP_FIELD,
      getViewName(annotation),
      TypeName.get(element.asType())
    )
  }

  private fun getViewName(annotation: AttributeAdapter): String {
    return try {
      annotation.forView.qualifiedName
        ?: throw IllegalStateException("Cannot find type of $annotation")
    } catch (err: MirroredTypeException) {
      (processingEnv.typeUtils.asElement(err.typeMirror) as TypeElement).qualifiedName.toString()
    }
  }
}
