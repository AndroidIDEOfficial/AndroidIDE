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
import com.android.aaptcompiler.AaptResourceType.ANIM
import com.android.aaptcompiler.AaptResourceType.ANIMATOR
import com.android.aaptcompiler.AaptResourceType.DRAWABLE
import com.android.aaptcompiler.AaptResourceType.LAYOUT
import com.android.aaptcompiler.AaptResourceType.MENU
import com.android.aaptcompiler.AaptResourceType.TRANSITION
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.extractPathData
import com.itsaky.androidide.lsp.api.AbstractServiceProvider
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.xml.providers.completion.AttrValueCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.IXmlCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.canCompleteManifest
import com.itsaky.androidide.lsp.xml.providers.completion.common.CommonAttrCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.etc.InheritingAttrCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.layout.LayoutAttrCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.layout.LayoutTagCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.manifest.ManifestAttrCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.manifest.ManifestAttrValueCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.manifest.ManifestTagCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.AnimTagTransformer
import com.itsaky.androidide.lsp.xml.utils.AnimatorTagTransformer
import com.itsaky.androidide.lsp.xml.utils.DrawableTagTransformer
import com.itsaky.androidide.lsp.xml.utils.ITagTransformer
import com.itsaky.androidide.lsp.xml.utils.MenuTagTransformer
import com.itsaky.androidide.lsp.xml.utils.NoOpTagTransformer
import com.itsaky.androidide.lsp.xml.utils.TransitionTagTransformer
import com.itsaky.androidide.lsp.xml.utils.XmlUtils
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import com.itsaky.androidide.lsp.xml.utils.forTransitionAttr
import com.itsaky.androidide.utils.CharSequenceReader
import com.itsaky.androidide.utils.StopWatch
import io.github.rosemoe.sora.text.ContentReference
import org.eclipse.lemminx.dom.DOMParser
import org.eclipse.lemminx.uriresolver.URIResolverExtensionManager
import org.slf4j.LoggerFactory
import java.io.Reader
import kotlin.io.path.name

/**
 * Completion provider for XML files.
 *
 * @author Akash Yadav
 */
class XmlCompletionProvider(settings: IServerSettings) :
  AbstractServiceProvider(), ICompletionProvider {

  companion object {

    private val log = LoggerFactory.getLogger(XmlCompletionProvider::class.java)
  }

  init {
    super.applySettings(settings)
  }

  override fun complete(params: CompletionParams): CompletionResult {
    return try {
      val watch =
        StopWatch(
          "Complete at ${params.file.name}:${params.position.line}:${params.position.column}"
        )
      doComplete(params).also { watch.log() }
    } catch (error: Throwable) {
      log.error("An error occurred while computing XML completions", error)
      EMPTY
    }
  }

  private fun doComplete(params: CompletionParams): CompletionResult {
    val contents = toString(contents = params.requireContents())
    val document =
      DOMParser.getInstance().parse(contents, "http://schemas.android.com/apk/res/android",
        URIResolverExtensionManager())
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
            "No completer available for resource type '{}' and node type '{}'", pathData.type, type
          )
          return EMPTY
        }

    return completer.complete(params, pathData, document, type, prefix)
  }

  private fun toString(contents: CharSequence): String {
    return getReader(contents).use { it.readText() }
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
      TRANSITION -> createTransitionCompleter(type)
      null -> createNullTypeCompleter(pathData, type)
      else -> createCommonCompleter(pathData, type)
    }
  }

  private fun createTransitionCompleter(type: NodeType): IXmlCompletionProvider? {
    return when (type) {
      ATTRIBUTE ->
        InheritingAttrCompletionProvider(::forTransitionAttr, TransitionTagTransformer, this)

      ATTRIBUTE_VALUE -> AttrValueCompletionProvider(this)
      else -> null
    }
  }

  private fun createCommonCompleter(
    pathData: ResourcePathData,
    type: NodeType
  ): IXmlCompletionProvider? {
    return when (type) {
      ATTRIBUTE -> CommonAttrCompletionProvider(tagTransformerFor(pathData), this)
      ATTRIBUTE_VALUE -> AttrValueCompletionProvider(this)
      else -> null
    }
  }

  private fun tagTransformerFor(pathData: ResourcePathData): ITagTransformer {
    return when (pathData.type) {
      ANIM -> AnimTagTransformer
      ANIMATOR -> AnimatorTagTransformer
      DRAWABLE -> DrawableTagTransformer
      MENU -> MenuTagTransformer
      else -> NoOpTagTransformer
    }
  }

  private fun createNullTypeCompleter(
    pathData: ResourcePathData,
    type: NodeType
  ): IXmlCompletionProvider? {

    // In test cases
    if (canCompleteManifest(pathData, type)) {
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
      ATTRIBUTE -> ManifestAttrCompletionProvider(this)
      ATTRIBUTE_VALUE -> ManifestAttrValueCompletionProvider(this)
      else -> null
    }
  }

  private fun createLayoutCompleter(type: NodeType): IXmlCompletionProvider? {
    return when (type) {
      TAG -> LayoutTagCompletionProvider(this)
      ATTRIBUTE -> LayoutAttrCompletionProvider(this)
      ATTRIBUTE_VALUE -> AttrValueCompletionProvider(this)
      else -> null
    }
  }
}
