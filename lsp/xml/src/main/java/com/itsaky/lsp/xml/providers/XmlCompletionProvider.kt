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
import com.itsaky.androidide.utils.CharSequenceReader
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.api.AbstractServiceProvider
import com.itsaky.lsp.api.ICompletionProvider
import com.itsaky.lsp.api.IServerSettings
import com.itsaky.lsp.models.CompletionData
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionItemKind.CLASS
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.InsertTextFormat.PLAIN_TEXT
import com.itsaky.lsp.util.StringUtils
import com.itsaky.lsp.xml.utils.XmlUtils
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import com.itsaky.sdk.SDKInfo
import com.itsaky.widgets.models.Widget
import com.itsaky.xml.INamespace
import io.github.rosemoe.sora.text.ContentReference
import java.io.IOException
import java.io.Reader
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager

/**
 * Completion provider for XMl files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider(val sdkInfo: SDKInfo, settings: IServerSettings) :
    AbstractServiceProvider(), ICompletionProvider {

    init {
        super.applySettings(settings)
    }

    private val cachedResult: CompletionResult? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun complete(params: CompletionParams): CompletionResult {
        return try {
            if (params.module == null || params.module !is IdeAndroidModule) {
                log.warn("Cannot provide completions for file:", params.file)
                log.warn("Module provided in params is either null or is not an Android module")
                return CompletionResult()
            }

            val contents = toString(params.requireContents())
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

            val prefix =
                XmlUtils.getPrefix(document, params.position.requireIndex(), type)
                    ?: return CompletionResult()

            completeImpl(params, document, prefix, type)
        } catch (error: Throwable) {
            log.error("An error occurred while computing XML completions", error)
            CompletionResult()
        }
    }

    private fun toString(contents: CharSequence): String {
        val reader = getReader(contents)
        val text = reader.readText()
        try {
            reader.close()
        } catch (e: IOException) {
            log.warn("Unable to close char sequence reader", e)
        }
        return text
    }

    private fun getReader(contents: CharSequence): Reader =
        if (contents is ContentReference) {
            contents.createReader()
        } else {
            CharSequenceReader(contents)
        }

    private fun completeImpl(
        params: CompletionParams,
        document: DOMDocument,
        prefix: String,
        type: NodeType,
    ): CompletionResult {
        val index = params.position.requireIndex()
        return when (type) {
            TAG ->
                completeTags(
                    if (prefix.startsWith("<")) {
                        prefix.substring(1)
                    } else {
                        prefix
                    })
            //            ATTRIBUTE -> completeAttributes(document, prefix)
            //            ATTRIBUTE_VALUE -> completeAttributeValue(document, prefix)
            else -> CompletionResult()
        }
    }

    private fun completeTags(prefix: String): CompletionResult {
        val widgets = sdkInfo.widgetInfo.widgets
        var isIncomplete = false
        val result = mutableListOf<CompletionItem>()

        for (widget in widgets) {
            if (StringUtils.matchesPartialName(
                widget.name, prefix, settings.shouldMatchAllLowerCase()) ||
                StringUtils.matchesPartialName(
                    widget.name, prefix, settings.shouldMatchAllLowerCase())) {
                result.add(createTagCompletionItem(widget))

                if (result.size == CompletionResult.MAX_ITEMS) {
                    isIncomplete = true
                    break
                }
            }
        }

        return CompletionResult(isIncomplete, result)
    }

    private fun createTagCompletionItem(widget: Widget): CompletionItem =
        CompletionItem().apply {
            label = widget.simpleName
            detail = widget.name
            insertText = label as String
            insertTextFormat = PLAIN_TEXT
            sortText = "2$label"
            kind = CLASS
            data = CompletionData().apply { className = widget.name }
        }
}
