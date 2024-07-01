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
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.actions.newDialogBuilder
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.actions.requirePath
import com.itsaky.androidide.javac.services.util.JavaDiagnosticUtils
import com.itsaky.androidide.lsp.java.JavaCompilerProvider
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.lsp.java.models.DiagnosticCode
import com.itsaky.androidide.lsp.java.rewrite.AddImport
import com.itsaky.androidide.lsp.java.rewrite.Rewrite
import com.itsaky.androidide.lsp.models.CodeActionItem
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R
import jdkx.tools.Diagnostic
import jdkx.tools.JavaFileObject
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class AddImportAction : BaseJavaCodeAction() {

  override val id: String = "ide.editor.lsp.java.diagnostics.addImport"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.NOT_IMPORTED.id

  override val titleTextRes: Int = R.string.action_import_classes

  companion object {

    private val log = LoggerFactory.getLogger(AddImportAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !data.hasRequiredData(DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data.get(DiagnosticItem::class.java)!!
    if (diagnosticCode != diagnostic.code || diagnostic.extra !is Diagnostic<*>) {
      markInvisible()
      return
    }

    val file = data.requireFile()
    val module =
      IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false)
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

    val found =
      jcDiagnostic.args[1]?.toString()?.let { compiler.findQualifiedNames(it, true).isNotEmpty() }
        ?: false

    visible = found
    enabled = found
  }

  override suspend fun execAction(data: ActionData): Any {
    @Suppress("UNCHECKED_CAST")
    val diagnostic =
      JavaDiagnosticUtils.asUnwrapper(
        data.get(DiagnosticItem::class.java)!!.extra as Diagnostic<out JavaFileObject>
      )!!
    val file = data.requireFile()
    val module =
      IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false)
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
      rewrites.add(AddImport(data.requirePath(), klass))
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

    val file = data.requireFile()
    val module =
      IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false)
        ?: run {
          markInvisible()
          return
        }

    val compiler = JavaCompilerProvider.get(module)
    val client = data.getLanguageClient() ?: return
    val actions = mutableListOf<CodeActionItem>()
    val titles = result.first as List<String>
    val rewrites = result.second as List<Rewrite>

    for (index in rewrites.indices) {
      val name = titles[index]
      val rewrite = rewrites[index]
      rewrite.asCodeActions(compiler, name)?.let { actions.add(it) }
    }

    when (actions.size) {
      0 -> {
        log.warn("No rewrites found. Cannot perform action")
      }

      1 -> {
        client.performCodeAction(actions[0])
      }

      else -> {
        val builder = newDialogBuilder(data)
        builder.setTitle(label)
        builder.setItems(toArray(titles, String::class.java)) { d, w ->
          d.dismiss()
          client.performCodeAction(actions[w])
        }
        builder.show()
      }
    }
  }
}
