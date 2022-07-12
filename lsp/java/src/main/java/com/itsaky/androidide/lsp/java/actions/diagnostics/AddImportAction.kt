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
package com.itsaky.androidide.lsp.java.actions.diagnostics

import com.google.common.collect.Iterables.toArray
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.javac.services.util.JavaDiagnosticUtils
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.lsp.java.R
import com.itsaky.androidide.lsp.java.actions.BaseCodeAction
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.rewrite.AddImport
import com.itsaky.androidide.lsp.java.rewrite.Rewrite
import com.itsaky.androidide.lsp.models.CodeActionItem
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.utils.ILogger
import javax.tools.Diagnostic
import javax.tools.JavaFileObject

/** @author Akash Yadav */
class AddImportAction : BaseCodeAction() {

  override val id: String = "lsp_java_addImport"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.NOT_IMPORTED.id
  private val log = ILogger.newInstance("AddImportAction")

  override val titleTextRes: Int = R.string.action_import_classes

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !hasRequiredData(data, DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data.get(DiagnosticItem::class.java)!!
    if (diagnosticCode != diagnostic.code || diagnostic.extra !is Diagnostic<*>) {
      markInvisible()
      return
    }

    val file = requireFile(data)
    val module =
      ProjectManager.findModuleForFile(file)
        ?: run {
          markInvisible()
          return
        }

    val compiler = JavaCompilerProvider.get(module)

    @Suppress("UNCHECKED_CAST")
    val jcDiagnostic =
      JavaDiagnosticUtils.asJCDiagnostic(diagnostic.extra as Diagnostic<out JavaFileObject>)
    if (jcDiagnostic == null) {
      markInvisible()
      return
    }

    var found = false
    val simpleName = jcDiagnostic.args[1]
    for (name in compiler.publicTopLevelTypes()) {
      var klass = name

      // This will be true in a test environment
      if (klass.contains("/")) {
        klass = name.replace('/', '.')
      }

      if (klass.endsWith(".$simpleName")) {
        found = true
        // There is at least one class to import
        break
      }
    }

    visible = found
    enabled = found
  }

  override fun execAction(data: ActionData): Any {
    @Suppress("UNCHECKED_CAST")
    val diagnostic =
      JavaDiagnosticUtils.asUnwrapper(
        data.get(DiagnosticItem::class.java)!!.extra as Diagnostic<out JavaFileObject>
      )!!
    val file = requireFile(data)
    val module =
      ProjectManager.findModuleForFile(file)
        ?: run {
          markInvisible()
          return Any()
        }

    val compiler = JavaCompilerProvider.get(module)

    val titles = mutableListOf<String>()
    val rewrites = mutableListOf<AddImport>()
    val simpleName = diagnostic.d.args[1]
    for (name in compiler.publicTopLevelTypes()) {
      var klass = name
      if (klass.contains('/')) {
        klass = klass.replace('/', '.')
      }

      if (!klass.endsWith(".$simpleName")) {
        continue
      }

      titles.add(klass)
      rewrites.add(AddImport(requirePath(data), klass))
    }

    if (rewrites.isEmpty()) {
      return false
    }

    return Pair(titles, rewrites)
  }

  @Suppress("UNCHECKED_CAST")
  override fun postExec(data: ActionData, result: Any) {

    if (result !is Pair<*, *>) {
      return
    }

    val file = requireFile(data)
    val module =
      ProjectManager.findModuleForFile(file)
        ?: run {
          markInvisible()
          return
        }

    val compiler = JavaCompilerProvider.get(module)
    val server =
      ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
        as JavaLanguageServer
    val client = server.client ?: return
    val actions = mutableListOf<CodeActionItem>()
    val titles = result.first as List<String>
    val rewrites = result.second as List<Rewrite>

    for (index in rewrites.indices) {
      val name = titles[index]
      val rewrite = rewrites[index]
      val action = rewrite.asCodeActions(compiler, name)
      actions.add(action)
    }

    when (actions.size) {
      0 -> {
        log.warn("No rewrites found. Cannot perform action")
      }
      1 -> {
        client.performCodeAction(file, actions[0])
      }
      else -> {
        val builder = newDialogBuilder(data)
        builder.setTitle(label)
        builder.setItems(toArray(titles, String::class.java)) { d, w ->
          d.dismiss()
          client.performCodeAction(file, actions[w])
        }
        builder.show()
      }
    }
  }
}
