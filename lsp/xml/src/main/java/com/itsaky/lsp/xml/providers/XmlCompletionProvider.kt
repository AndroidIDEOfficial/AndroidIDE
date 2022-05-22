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

import com.itsaky.androidide.utils.CharSequenceReader
import com.itsaky.androidide.utils.ILogger
import com.itsaky.attrinfo.models.Attr
import com.itsaky.lsp.api.AbstractServiceProvider
import com.itsaky.lsp.api.ICompletionProvider
import com.itsaky.lsp.api.IServerSettings
import com.itsaky.lsp.models.Command
import com.itsaky.lsp.models.CompletionData
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionItemKind.CLASS
import com.itsaky.lsp.models.CompletionItemKind.FIELD
import com.itsaky.lsp.models.CompletionItemKind.VALUE
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.lsp.models.MatchLevel
import com.itsaky.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.lsp.models.Position
import com.itsaky.lsp.xml.utils.XmlUtils
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import com.itsaky.sdk.SDKInfo
import com.itsaky.widgets.models.Widget
import com.itsaky.xml.INamespace
import io.github.rosemoe.sora.text.ContentReference
import java.io.IOException
import java.io.Reader
import kotlin.math.max
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager

/**
 * Completion provider for XMl files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider(private val sdkInfo: SDKInfo, settings: IServerSettings) :
    AbstractServiceProvider(), ICompletionProvider {

    init {
        super.applySettings(settings)
    }

    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun complete(params: CompletionParams): CompletionResult {
        return try {
            // TODO When the completion will be namespace-aware, we will then need to use
            //   'params.module'

            // val namespace =
            // INamespace.forPackageName((params.module as IdeAndroidModule).packageName)

            val namespace = INamespace.ANDROID
            val contents = toString(params.requireContents())
            val document =
                DOMParser.getInstance()
                    .parse(contents, namespace.uri, URIResolverExtensionManager())
            val type = XmlUtils.getNodeType(document, params.position.requireIndex())

            if (type == UNKNOWN) {
                log.warn("Unknown node type. CompletionParams:", params)
                return EMPTY
            }

            val prefix =
                XmlUtils.getPrefix(document, params.position.requireIndex(), type) ?: return EMPTY

            completeImpl(params, document, prefix, type)
        } catch (error: Throwable) {
            log.error("An error occurred while computing XML completions", error)
            EMPTY
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
        return when (type) {
            TAG ->
                completeTags(
                    if (prefix.startsWith("<")) {
                        prefix.substring(1)
                    } else {
                        prefix
                    })
            ATTRIBUTE -> completeAttributes(document, params.position)
            ATTRIBUTE_VALUE -> completeAttributeValue(document, prefix, params.position)
            else -> EMPTY
        }
    }

    private fun completeTags(prefix: String): CompletionResult {
        val widgets = sdkInfo.widgetInfo.widgets
        val result = mutableListOf<CompletionItem>()

        for (widget in widgets) {
            val simpleNameMatchLevel = matchLevel(widget.simpleName, prefix)
            val nameMatchLevel = matchLevel(widget.name, prefix)
            if (simpleNameMatchLevel == NO_MATCH && nameMatchLevel == NO_MATCH) {
                continue
            }

            val matchLevel =
                MatchLevel.values()[max(simpleNameMatchLevel.ordinal, nameMatchLevel.ordinal)]

            result.add(createTagCompletionItem(widget, matchLevel))
        }

        return CompletionResult(result)
    }

    private fun completeAttributes(document: DOMDocument, position: Position): CompletionResult {
        // TODO Provide attributes based on current node and it's direct parent node
        //   For example, if the current node is a 'TextView', provide attributes applicable to
        //   TextView only. Also, if the parent of this TextView is a LinearLayout, then add
        //   attributes related to LinearLayout LayoutParams.

        // TODO Provided attributes from declared namespaces only
        val attr = document.findAttrAt(position.requireIndex())
        val list = mutableListOf<CompletionItem>()
        for (attribute in sdkInfo.attrInfo.attributes.values) {
            val matchLevel = matchLevel(attribute.name, attr.name)
            if (matchLevel == NO_MATCH) {
                continue
            }
            
            list.add(createAttrCompletionItem(attribute, matchLevel))
        }

        return CompletionResult(list)
    }

    private fun completeAttributeValue(
        document: DOMDocument,
        prefix: String,
        position: Position
    ): CompletionResult {
        val attr = document.findAttrAt(position.requireIndex())

        // TODO Provide attribute values based on namespace URI
        //   For example, if the package name of the namespace of this attribute refers to a library
        //   dependency/module, check for values in the respective dependency
        //   Currently, only the attributes from the 'android' package name are suggested

        val name = attr.localName ?: return EMPTY
        val attribute = sdkInfo.attrInfo.getAttribute(name) ?: return EMPTY
        val items = mutableListOf<CompletionItem>()
        for (value in attribute.possibleValues) {
            val matchLevel = matchLevel(value, prefix)

            // It might happen that the completion request is triggered but the prefix is empty
            // For example, a completion request is triggered when the user selects an attribute
            // completion item.
            // In such cases, 'prefix' is an empty string.
            // So, we still have to provide completions
            if (prefix.isEmpty() || matchLevel != NO_MATCH) {
                items.add(createAttrValueCompletionItem(attr.name, value, matchLevel))
            }
        }

        return CompletionResult(items)
    }

    private fun createTagCompletionItem(widget: Widget, matchLevel: MatchLevel): CompletionItem =
        CompletionItem().apply {
            this.label = widget.simpleName
            this.detail = widget.name
            this.sortText = label.toString()
            this.matchLevel = matchLevel
            this.kind = CLASS
            this.data = CompletionData().apply { className = widget.name }
        }

    private fun createAttrCompletionItem(attr: Attr, matchLevel: MatchLevel): CompletionItem =
        CompletionItem().apply {
            this.label = attr.name
            this.kind = FIELD
            this.detail = "From package '${attr.namespace.packageName}'"
            this.insertText = "${attr.namespace.prefix}:${attr.name}=\"$0\""
            this.insertTextFormat = SNIPPET
            this.sortText = label.toString()
            this.matchLevel = matchLevel
            this.command = Command("Trigger completion request", Command.TRIGGER_COMPLETION)
        }

    private fun createAttrValueCompletionItem(
        attrName: String,
        value: String,
        matchLevel: MatchLevel
    ): CompletionItem {
        return CompletionItem().apply {
            this.label = value
            this.detail = "Value for '$attrName'"
            this.kind = VALUE
            this.sortText = label.toString()
            this.matchLevel = matchLevel
        }
    }
}
