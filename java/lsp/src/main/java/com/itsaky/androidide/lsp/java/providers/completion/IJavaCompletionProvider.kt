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

package com.itsaky.androidide.lsp.java.providers.completion

import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.api.describeSnippet
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.edits.ClassImportEditHandler
import com.itsaky.androidide.lsp.java.models.JavaCompletionItem
import com.itsaky.androidide.lsp.java.providers.BaseJavaServiceProvider
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.lsp.models.ClassCompletionData
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItemKind
import com.itsaky.androidide.lsp.models.CompletionItemKind.ENUM_MEMBER
import com.itsaky.androidide.lsp.models.CompletionItemKind.FUNCTION
import com.itsaky.androidide.lsp.models.CompletionItemKind.KEYWORD
import com.itsaky.androidide.lsp.models.CompletionItemKind.MODULE
import com.itsaky.androidide.lsp.models.CompletionItemKind.NONE
import com.itsaky.androidide.lsp.models.CompletionItemKind.PROPERTY
import com.itsaky.androidide.lsp.models.CompletionItemKind.VARIABLE
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.FieldCompletionData
import com.itsaky.androidide.lsp.models.ICompletionData
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MethodCompletionData
import com.itsaky.androidide.lsp.snippets.ISnippet
import com.itsaky.androidide.preferences.utils.indentationString
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import jdkx.lang.model.element.Element
import jdkx.lang.model.element.ElementKind.ANNOTATION_TYPE
import jdkx.lang.model.element.ElementKind.CLASS
import jdkx.lang.model.element.ElementKind.CONSTRUCTOR
import jdkx.lang.model.element.ElementKind.ENUM
import jdkx.lang.model.element.ElementKind.ENUM_CONSTANT
import jdkx.lang.model.element.ElementKind.EXCEPTION_PARAMETER
import jdkx.lang.model.element.ElementKind.FIELD
import jdkx.lang.model.element.ElementKind.INSTANCE_INIT
import jdkx.lang.model.element.ElementKind.INTERFACE
import jdkx.lang.model.element.ElementKind.LOCAL_VARIABLE
import jdkx.lang.model.element.ElementKind.METHOD
import jdkx.lang.model.element.ElementKind.OTHER
import jdkx.lang.model.element.ElementKind.PACKAGE
import jdkx.lang.model.element.ElementKind.PARAMETER
import jdkx.lang.model.element.ElementKind.RESOURCE_VARIABLE
import jdkx.lang.model.element.ElementKind.STATIC_INIT
import jdkx.lang.model.element.ElementKind.TYPE_PARAMETER
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.TypeElement
import jdkx.lang.model.element.VariableElement
import jdkx.lang.model.type.TypeKind
import openjdk.source.tree.Tree
import openjdk.source.util.TreePath
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * Completion provider for Java source code.
 *
 * @author Akash Yadav
 */
abstract class IJavaCompletionProvider(
  protected val cursor: Long,
  completingFile: Path,
  compiler: JavaCompilerService,
  settings: IServerSettings,
) : BaseJavaServiceProvider(completingFile, compiler, settings) {
  protected lateinit var filePackage: String
  protected lateinit var fileImports: Set<String>

  companion object {
    @JvmStatic
    protected val log: Logger = LoggerFactory.getLogger(IJavaCompletionProvider::class.java)
  }

  open fun complete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    val root = task.root(file)
    filePackage = root.`package`?.packageName?.toString() ?: ""
    fileImports = root.imports.map { it.qualifiedIdentifier.toString() }.toSet()
    abortIfCancelled()
    abortCompletionIfCancelled()
    return doComplete(task, path, partial, endsWithParen)
  }

  /**
   * Provide completions with the given data.
   *
   * @param task The compilation task. Subclasses are expected to use this compile task instead of
   *   starting another compilation process.
   * @param path The [TreePath] defining the [Tree] at the current position.
   * @param partial The partial identifier.
   * @param endsWithParen `true` if the statement at cursor ends with a parenthesis. `false`
   *   otherwise.
   */
  protected abstract fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult

  protected open fun matchLevel(candidate: CharSequence, partial: CharSequence): MatchLevel {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return CompletionItem.matchLevel(candidate.toString(), partial.toString())
  }

  protected open fun putMethod(
    method: ExecutableElement,
    methods: MutableMap<String, MutableList<ExecutableElement>>,
  ) {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val name = method.simpleName.toString()
    if (!methods.containsKey(name)) {
      methods[name] = ArrayList()
    }
    methods[name]!!.add(method)
  }

  protected open fun keyword(
    keyword: String,
    partial: CharSequence,
    matchRatio: Int,
  ): CompletionItem =
    keyword(keyword, partial, CompletionItem.matchLevel(keyword, partial.toString()))

  protected open fun keyword(
    keyword: String,
    partialName: CharSequence,
    matchLevel: MatchLevel,
  ): CompletionItem {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val item = JavaCompletionItem()
    item.ideLabel = keyword
    item.completionKind = KEYWORD
    item.detail = "keyword"
    item.ideSortText = keyword
    item.matchLevel = matchLevel
    return item
  }

  protected open fun method(
    task: CompileTask,
    overloads: List<ExecutableElement>,
    addParens: Boolean,
    matchLevel: MatchLevel,
    partial: String
  ): CompletionItem {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val first = overloads[0]
    val item = JavaCompletionItem()
    item.ideLabel = first.simpleName.toString()
    item.completionKind = CompletionItemKind.METHOD
    item.detail = printMethodDetail(first)
    item.ideSortText = item.ideLabel
    item.matchLevel = matchLevel
    item.overrideTypeText = EditHelper.printType(first.returnType)
    val data = data(task, first, overloads.size)
    item.data = data

    abortIfCancelled()
    abortCompletionIfCancelled()
    if (addParens) {
      if (overloads.size == 1 && first.parameters.isEmpty()) {
        item.insertText = first.simpleName.toString() + "()$0"
      } else {
        item.insertText = first.simpleName.toString() + "($0)"
        item.command = Command("Trigger Parameter Hints", Command.TRIGGER_PARAMETER_HINTS)
      }
      item.insertTextFormat = SNIPPET // DefaultSnippet
      item.snippetDescription = describeSnippet(prefix = partial, allowCommandExecution = true)
    }
    return item
  }

  protected open fun printMethodDetail(first: ExecutableElement): String {
    val sb = StringBuilder()
    sb.append(first.simpleName)
    sb.append("(")
    if (first.parameters.isNotEmpty()) {
      for (index in first.parameters.indices) {
        val parameter = first.parameters[index]
        sb.append(EditHelper.printType(parameter.asType()))
        if (index != first.parameters.lastIndex) {
          sb.append(", ")
        }
      }
    }
    sb.append(")")
    return sb.toString()
  }

  protected open fun item(
    task: CompileTask,
    element: Element,
    matchLevel: MatchLevel,
  ): CompletionItem {
    if (element.kind == METHOD) throw RuntimeException("method")

    abortIfCancelled()
    abortCompletionIfCancelled()
    val item = JavaCompletionItem()
    item.ideLabel = element.simpleName.toString()
    item.completionKind = kind(element)
    item.detail = element.toString()
    item.data = data(task, element, 1)
    item.ideSortText = item.ideLabel
    item.matchLevel = matchLevel

    if (element is VariableElement) {
      if (element.constantValue != null) {
        item.detail = "Constant: ${element.constantValue}"
      }
      item.overrideTypeText = EditHelper.printType(element.asType())
    }

    return item
  }

  protected open fun classItem(className: String, matchLevel: MatchLevel): CompletionItem {
    return classItem(emptySet(), null, className, matchLevel)
  }

  protected open fun classItem(
    imports: Set<String>,
    file: Path?,
    className: String,
    matchLevel: MatchLevel,
  ): CompletionItem {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val item = JavaCompletionItem()
    item.ideLabel = simpleName(className).toString()
    item.completionKind = CompletionItemKind.CLASS
    item.detail = packageName(className).toString()
    item.ideSortText = item.ideLabel
    item.matchLevel = matchLevel

    // TODO(itsaky): This will result in incorrect flatName if 'className' is a nested or local class
    item.data = ClassCompletionData(className)

    // If file is not provided, we are probably completing an import path
    item.additionalEditHandler = if (file == null) null else ClassImportEditHandler(imports, file)
    return item
  }

  protected open fun simpleName(name: String): CharSequence {
    return if (name.contains(".")) {
      name.subSequence(name.lastIndexOf('.') + 1, name.length)
    } else name
  }

  private fun packageName(name: CharSequence): CharSequence {
    return if (name.contains(".")) {
      name.subSequence(0, name.lastIndexOf('.'))
    } else name
  }

  protected open fun packageItem(name: String, matchLevel: MatchLevel): CompletionItem {
    abortIfCancelled()
    abortCompletionIfCancelled()
    val simpleName = simpleName(name).toString()
    var packageName = packageName(name).toString()
    if (packageName == name) {
      packageName = " "
    }
    return JavaCompletionItem().apply {
      this.ideLabel = simpleName
      this.detail = packageName
      this.insertText = simpleName
      this.completionKind = MODULE
      this.ideSortText = name
      this.matchLevel = matchLevel
    }
  }

  protected open fun snippetItem(
    snippet: ISnippet,
    matchLevel: MatchLevel,
    partial: String,
    indent: Int
  ): CompletionItem {
    return JavaCompletionItem().apply {
      this.ideLabel = snippet.prefix
      this.detail = snippet.description
      this.completionKind = CompletionItemKind.SNIPPET
      this.matchLevel = matchLevel
      this.ideSortText = "00000${snippet.prefix}"
      this.snippetDescription = describeSnippet(partial)

      val indentation = indentationString(indent)
      this.insertTextFormat = SNIPPET
      this.insertText =
        snippet.body.joinToString(separator = "\n").also {
          it.replace("\t", indentationString).replace("\n", "\n${indentation}")
        }
    }
  }

  protected open fun kind(e: Element): CompletionItemKind {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return when (e.kind) {
      ANNOTATION_TYPE -> CompletionItemKind.ANNOTATION_TYPE
      CLASS -> CompletionItemKind.CLASS
      CONSTRUCTOR -> CompletionItemKind.CONSTRUCTOR
      ENUM -> CompletionItemKind.ENUM
      ENUM_CONSTANT -> ENUM_MEMBER
      EXCEPTION_PARAMETER,
      PARAMETER,
      -> PROPERTY

      FIELD -> CompletionItemKind.FIELD
      STATIC_INIT,
      INSTANCE_INIT,
      -> FUNCTION

      INTERFACE -> CompletionItemKind.INTERFACE
      LOCAL_VARIABLE,
      RESOURCE_VARIABLE,
      -> VARIABLE

      METHOD -> CompletionItemKind.METHOD
      PACKAGE -> MODULE
      TYPE_PARAMETER -> CompletionItemKind.TYPE_PARAMETER
      OTHER -> NONE
      else -> NONE
    }
  }

  protected open fun data(task: CompileTask, element: Element, overloads: Int): ICompletionData? {
    abortIfCancelled()
    abortCompletionIfCancelled()
    return when {
      element is TypeElement -> getClassCompletionData(task, element)
      element.kind == FIELD -> getFieldCompletionData(task, element)
      element is ExecutableElement -> getMethodCompletionData(task, element, overloads)
      else -> return null
    }
  }

  protected open fun getMethodCompletionData(
    task: CompileTask,
    element: ExecutableElement,
    overloads: Int
  ): MethodCompletionData {
    val types = task.task.types
    val elements = task.task.elements
    val type = element.enclosingElement as TypeElement
    val parameterTypes = Array(element.parameters.size) { "" }
    val erasedParameterTypes = Array(parameterTypes.size) { "" }
    val plusOverloads = overloads - 1

    for (i in element.parameters.indices) {
      val p = element.parameters[i].asType()
      parameterTypes[i] = p.toString()

      if (p.kind == TypeKind.DECLARED) {
        erasedParameterTypes[i] =
          elements.getBinaryName(types.asElement(p) as TypeElement).toString()
      } else {
        erasedParameterTypes[i] = types.erasure(p).toString()
      }
    }

    return MethodCompletionData(
      element.simpleName.toString(),
      getClassCompletionData(task, type),
      parameterTypes.toList(),
      erasedParameterTypes.toList(),
      plusOverloads
    )
  }

  protected open fun getFieldCompletionData(
    task: CompileTask,
    element: Element
  ): FieldCompletionData {
    val field = element as VariableElement
    val type = field.enclosingElement as TypeElement
    return FieldCompletionData(field.simpleName.toString(), getClassCompletionData(task, type))
  }

  protected open fun getClassCompletionData(
    task: CompileTask,
    element: TypeElement
  ): ClassCompletionData {
    val elements = task.task.elements
    return ClassCompletionData(
      className = element.qualifiedName.toString(),
      isCompleteData = true,
      flatName = elements.getBinaryName(element).toString(),
      simpleName = element.simpleName.toString(),
      isNested = element.enclosingElement.kind != PACKAGE,
      topLevelClass = element.findTopLevelElement().qualifiedName.toString()
    )
  }

  protected open fun TypeElement.findTopLevelElement(): TypeElement {
    if (enclosingElement.kind == PACKAGE) {
      return this
    }

    var element: TypeElement? = this
    while (true) {
      if (element == null || element.enclosingElement?.kind == PACKAGE) {
        break
      }

      element = element.enclosingElement as? TypeElement
    }

    return element!!
  }
}
