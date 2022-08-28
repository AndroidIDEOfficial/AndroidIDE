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

package com.itsaky.androidide.lsp.xml.providers.completion.manifest

import com.android.SdkConstants
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.IXmlCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.VMUtils

/**
 * Base class for completers which provide completions in Android manifest.
 *
 * @author Akash Yadav
 */
abstract class ManifestCompletionProvider(provider: ICompletionProvider) :
  IXmlCompletionProvider(provider) {

  companion object {
    @JvmStatic
    fun canComplete(pathData: ResourcePathData, type: NodeType) : Boolean {
      return pathData.file.name == SdkConstants.ANDROID_MANIFEST_XML ||
          (VMUtils.isJvm() &&
            pathData.file.name.startsWith("Manifest") &&
            pathData.file.name.endsWith("_template.xml"))
    }
  }
  
  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && canComplete(pathData, type)
  }
}
