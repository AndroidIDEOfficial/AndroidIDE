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
import com.itsaky.lsp.java.providers.CompletionProvider.MAX_COMPLETION_ITEMS
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.MatchLevel.NO_MATCH
import com.sun.source.util.TreePath
import java.nio.file.Path

/**
 * Provides completions for imports.
 *
 * @author Akash Yadav
 */
class ImportCompletionProvider(
    completingFile: Path,
    cursor: Long,
    compiler: CompilerProvider,
    settings: IServerSettings,
) : IJavaCompletionProvider(completingFile, cursor, compiler, settings) {

    lateinit var importPath: String

    override fun doComplete(
        task: CompileTask,
        path: TreePath,
        partial: String,
        endsWithParen: Boolean,
    ): CompletionResult {
        log.info("...complete import")

        val names: MutableSet<String> = HashSet()
        val list = mutableListOf<CompletionItem>()
        for (className in compiler.publicTopLevelTypes()) {
            val matchLevel = matchLevel(className, partial)
            if (matchLevel == NO_MATCH) {
                continue
            }

            val start = importPath.lastIndexOf('.')
            var end = className.indexOf('.', importPath.length)
            if (end == -1) {
                end = className.length
            }
            val segment = className.substring(start + 1, end)
            if (names.contains(segment)) {
                continue
            }
            names.add(segment)
            val isClass = end == importPath.length
            if (isClass) {
                list.add(classItem(className, importPath, matchLevel))
            } else {
                list.add(packageItem(segment, importPath, matchLevel))
            }

            if (list.size > MAX_COMPLETION_ITEMS) {
                break
            }
        }

        return CompletionResult(list)
    }
}
