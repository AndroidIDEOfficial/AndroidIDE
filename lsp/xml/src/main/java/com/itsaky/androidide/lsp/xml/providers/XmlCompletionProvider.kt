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

package com.itsaky.androidide.lsp.xml.providers

import com.android.SdkConstants.ANDROID_MANIFEST_XML
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.lsp.api.AbstractServiceProvider
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.xml.providers.completion.IXmlCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.common.AttrValueCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.layout.LayoutAttributeCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.layout.LayoutTagCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.manifest.ManifestCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.manifest.ManifestTagCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import com.itsaky.androidide.utils.CharSequenceReader
import com.itsaky.androidide.utils.ILogger
import com.itsaky.xml.INamespace
import io.github.rosemoe.sora.text.ContentReference
import java.io.IOException
import java.io.Reader
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager

/**
 * Completion provider for XML files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider(settings: IServerSettings) :
  AbstractServiceProvider(), ICompletionProvider {

  init {
    super.applySettings(settings)
  }

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun complete(params: CompletionParams): CompletionResult {
    return try {
      doComplete(params)
    } catch (error: Throwable) {
      log.error("An error occurred while computing XML completions", error)
      EMPTY
    }
  }

  private fun doComplete(params: CompletionParams): CompletionResult {
    val namespace = INamespace.ANDROID
    val contents = toString(contents = params.requireContents())
    val document =
      DOMParser.getInstance().parse(contents, namespace.uri, URIResolverExtensionManager())
    val type = XmlUtils.getNodeType(document, params.position.requireIndex())

    if (type == UNKNOWN) {
      log.warn("Unknown node type. Aborting completion.")
      return EMPTY
    }

    val prefix = XmlUtils.getPrefix(document, params.position.requireIndex(), type) ?: return EMPTY
    if (prefix.isBlank() && type != ATTRIBUTE_VALUE) {
      return EMPTY
    }

    val pathData = extractPathData(params.file.toFile())

    val completer =
      getCompleter(pathData, type)
        ?: run {
          log.error(
            "No completer available for resource type '${pathData.type}' and node type '$type'"
          )
          return EMPTY
        }

    return completer.complete(params, pathData, document, type, prefix)
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

  private fun getCompleter(pathData: ResourcePathData, type: NodeType): IXmlCompletionProvider? {
    return when (pathData.type) {
      LAYOUT -> createLayoutCompleter(type)
      null -> createNullTypeCompleter(pathData, type)
      else -> null
    }
  }

  private fun createNullTypeCompleter(
    pathData: ResourcePathData,
    type: NodeType
  ): IXmlCompletionProvider? {

    // In test cases
    if (ManifestCompletionProvider.canComplete(pathData, type)) {
      return createManifestCompleter(type)
    }

    return when (pathData.file.name) {
      ANDROID_MANIFEST_XML -> createManifestCompleter(type)
      else -> null
    }
  }

  private fun createManifestCompleter(type: NodeType): IXmlCompletionProvider? {
    return when (type) {
      TAG -> ManifestTagCompletionProvider(this)
      else -> null
    }
  }

  private fun createLayoutCompleter(type: NodeType): IXmlCompletionProvider? {
    return when (type) {
      TAG -> LayoutTagCompletionProvider(this)
      ATTRIBUTE -> LayoutAttributeCompletionProvider(this)
      ATTRIBUTE_VALUE -> AttrValueCompletionProvider(this)
      else -> null
    }
  }
}
