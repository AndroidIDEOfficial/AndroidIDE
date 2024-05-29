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

package com.itsaky.androidide.lsp.xml.providers.completion.layout

import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.AttrCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.canCompleteLayout
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType

/**
 * Attribute completion provider for layout files.
 *
 * @author Akash Yadav
 */
class LayoutAttrCompletionProvider(provider: ICompletionProvider) :
  AttrCompletionProvider(provider) {

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && canCompleteLayout(pathData, type)
  }
}
