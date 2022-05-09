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

package com.itsaky.lsp.xml.providers

import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.api.ICompletionProvider
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.xml.utils.XmlUtils
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import com.itsaky.xml.INamespace
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager

/**
 * Completion provider for XMl files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider : ICompletionProvider {

    private val cachedResult: CompletionResult? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun complete(params: CompletionParams): CompletionResult {
        return try {
            if (params.module == null || params.module !is IdeAndroidModule) {
                log.warn("Cannot provide completions for file:", params.file)
                log.warn("Module provide in params is either null or is not an Android module")
                return CompletionResult()
            }

            val contents = params.requireContents().toString()
            val namespace =
                INamespace.forPackageName((params.module as IdeAndroidModule).packageName)
            val document =
                DOMParser.getInstance()
                    .parse(contents, namespace.uri, URIResolverExtensionManager())
            val type = XmlUtils.getNodeType(document, params.position.requireIndex())

            if (type == UNKNOWN) {
                log.warn("Unknown node type. CompletionParams:", params)
                return CompletionResult()
            }

            CompletionResult()
        } catch (error: Throwable) {
            log.error("An error occurred while computing XML completions", error)
            CompletionResult()
        }
    }
}
