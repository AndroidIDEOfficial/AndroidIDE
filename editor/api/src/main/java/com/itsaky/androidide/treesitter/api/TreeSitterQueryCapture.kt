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

package com.itsaky.androidide.treesitter.api

import com.itsaky.androidide.treesitter.TSNode
import com.itsaky.androidide.treesitter.TSQueryCapture
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterQueryCapture @JvmOverloads internal constructor(
  node: TSNode? = null,
  index: Int = 0
) : TSQueryCapture(node, index), RecyclableObjectPool.Recyclable by DefaultRecyclable() {

  companion object {

    @JvmStatic
    fun obtain(node: TSNode?, index: Int): TreeSitterQueryCapture {
      return obtainFromPool<TreeSitterQueryCapture>().apply {
        this.node = node
        this.index = index
      }
    }
  }

  override fun recycle() {
    (this.node as? TreeSitterNode?)?.apply {
      if (!isRecycled) {
        recycle()
      }
    }
    this.index = 0
    this.node = null
    returnToPool()
  }
}