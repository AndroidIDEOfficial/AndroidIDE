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

package com.itsaky.androidide.annotations.ksp.inflater

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.itsaky.androidide.annotations.inflater.ViewAdapter

/**
 * [SymbolProcessor] for [ViewAdapter] annotations.
 *
 * @author Akash Yadav
 */
class ViewAdapterSymbolProcessor(
  private val codeGenerator: CodeGenerator,
  private val logger: KSPLogger
) : SymbolProcessor {

  private val generator = ViewAdapterIndexGenerator(logger)

  init {
    generator.init()
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val symbols = resolver.getSymbolsWithAnnotation(ViewAdapter::class.java.name)

    if (!symbols.iterator().hasNext()) {
      return emptyList()
    }

    val unprocessed = mutableListOf<KSAnnotated>()
    val files = mutableListOf<KSFile>()
    symbols.forEach {
      it.containingFile?.also { file -> files.add(file) }
      if (!process(it)) {
        unprocessed.add(it)
      }
    }

    val file =
      codeGenerator.createNewFile(
        Dependencies(true, *files.toTypedArray()),
        INDEX_PACKAGE_NAME,
        INDEX_CLASS_NAME,
        "java"
      )

    generator.generate(file)

    file.flush()
    file.close()

    return unprocessed
  }

  private fun process(sym: KSAnnotated): Boolean {
    if (sym !is KSClassDeclaration) {
      logger.error("${ViewAdapter::class} must be applied to a class.", sym)
      return false
    }

    if (sym.parent !is KSFile) {
      logger.error(
        "${ViewAdapter::class} must be applied to a top level class. (${sym.parent})",
        sym
      )
      return false
    }

    if (sym.primaryConstructor?.parameters?.size != 0) {
      logger.error("A view adapter must have a single primary constructor", sym)
      return false
    }

    if (sym.typeParameters.size != 1) {
      logger.error("A view adapter must have exactly one type parameter", sym)
      return false
    }

    if (!sym.isOpen()) {
      logger.error("A view adapter implementation must be open.", sym)
      return false
    }

    return generator.addViewAdapter(sym)
  }
}
