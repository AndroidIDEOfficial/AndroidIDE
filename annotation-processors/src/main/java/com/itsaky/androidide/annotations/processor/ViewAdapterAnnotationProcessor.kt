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

import com.google.auto.service.AutoService
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.ElementKind.PACKAGE
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR

@Suppress("unused")
@AutoService(Processor::class)
class ViewAdapterAnnotationProcessor : AbstractProcessor() {

  private val adapterElement by lazy { processingEnv.erasure(processingEnv.adapterElement()) }
  private val viewElement by lazy { processingEnv.erasure(processingEnv.viewElement()) }

  private val generator = ViewAdapterIndexGenerator()

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(ViewAdapter::class.java.name, IncludeInDesigner::class.java.name)
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
    
    generator.init(processingEnv)

    roundEnv.getElementsAnnotatedWith(ViewAdapter::class.java).forEach {
      if (it.kind != CLASS) {
        processingEnv.messager.printMessage(
          ERROR,
          "@ViewAdapter can only be used with classes. Used on $it"
        )
        return true
      }
      process(it)
    }

    generator.generate(
      processingEnv.options[KAPT_KOTLIN_GENERATED]
        ?: throw IllegalStateException("Cannot find kapt output directory path")
    )

    return false
  }

  private fun process(element: Element) {
    if (element.enclosingElement?.kind != PACKAGE || element !is TypeElement) {
      processingEnv.messager.printMessage(
        ERROR,
        "@ViewAdapter can only be used on top-level classes. Used on $element"
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
        "A view adapter must have only default constructor"
      )
      return
    }

    if ((constructors.first() as ExecutableElement).parameters.isNotEmpty()) {
      processingEnv.messager.printMessage(
        ERROR,
        "View adapter constructor should not have any parameters"
      )
      return
    }

    if (
      !processingEnv.typeUtils.isAssignable(
        processingEnv.typeUtils.erasure(element.asType()),
        adapterElement
      )
    ) {
      processingEnv.messager.printMessage(
        ERROR,
        "Class must be a subtype of $ADAPTER_BASE_CLASS $element $adapterElement"
      )
      return
    }

    if (element.typeParameters.size != 1) {
      processingEnv.messager.printMessage(
        ERROR,
        "A view adapter must have exactly one type parameter"
      )
    }

    val typeParam = element.typeParameters[0]
    if (!processingEnv.typeUtils.isAssignable(typeParam.asType(), viewElement)) {
      processingEnv.messager.printMessage(
        ERROR,
        "The type parameter of view adapter must be assignable from android.view.View"
      )
    }

    generator.addViewAdapter(processingEnv, element)
  }
}
