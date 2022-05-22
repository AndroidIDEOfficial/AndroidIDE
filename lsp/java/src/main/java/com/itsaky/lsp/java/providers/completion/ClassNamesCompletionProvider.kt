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

package com.itsaky.lsp.java.providers.completion

import com.itsaky.lsp.api.IServerSettings
import com.itsaky.lsp.java.compiler.CompileTask
import com.itsaky.lsp.java.compiler.CompilerProvider
import com.itsaky.lsp.java.providers.CompletionProvider
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.MatchLevel.NO_MATCH
import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.util.TreePath
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Completes class names.
 *
 * @author Akash Yadav
 */
class ClassNamesCompletionProvider(
    completingFile: Path,
    cursor: Long,
    compiler: CompilerProvider,
    settings: IServerSettings,
    val root: CompilationUnitTree
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

    override fun doComplete(
        task: CompileTask,
        path: TreePath,
        partial: String,
        endsWithParen: Boolean,
    ): CompletionResult {
        val list = mutableListOf<CompletionItem>()
        val packageName = Objects.toString(root.packageName, "")
        val uniques: MutableSet<String> = HashSet()
        val previousSize: Int = list.size

        val file: Path = Paths.get(root.sourceFile.toUri())
        val imports: Set<String> =
            root.imports.map { it.qualifiedIdentifier }.mapNotNull { it.toString() }.toSet()

        for (className in compiler.packagePrivateTopLevelTypes(packageName)) {
            val matchLevel = matchLevel(className, partial)
            if (matchLevel == NO_MATCH) {
                continue
            }

            list.add(classItem(imports, file, className, partial, matchLevel))
            uniques.add(className)
        }

        for (className in compiler.publicTopLevelTypes()) {
            val matchLevel = matchLevel(simpleName(className), partial)
            if (matchLevel == NO_MATCH) {
                continue
            }

            if (uniques.contains(className)) {
                continue
            }
            
            if (list.size > CompletionProvider.MAX_COMPLETION_ITEMS) {
                break
            }
            
            list.add(classItem(imports, file, className, partial, matchLevel))
            uniques.add(className)
        }

        for (t in root.typeDecls) {
            if (t !is ClassTree) {
                continue
            }
            val candidate = if (t.simpleName == null) "" else t.simpleName

            val matchLevel = matchLevel(candidate, partial)
            if (matchLevel == NO_MATCH) {
                continue
            }

            val name = packageName + "." + t.simpleName
            list.add(classItem(name, partial, matchLevel))

            if (list.size > CompletionProvider.MAX_COMPLETION_ITEMS) {
                break
            }
        }

        log.info("...found " + (list.size - previousSize) + " class names")

        return CompletionResult(list)
    }
}
