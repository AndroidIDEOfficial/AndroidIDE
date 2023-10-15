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

import com.itsaky.androidide.treesitter.TSTreeCursorNode
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterTreeCursorNode @JvmOverloads internal constructor(
  type: String? = null,
  name: String? = null,
  startByte: Int = 0,
  endByte: Int = 0
) : TSTreeCursorNode(type, name, startByte, endByte),
  RecyclableObjectPool.Recyclable by DefaultRecyclable() {

  companion object {

    @JvmStatic
    fun obtain(type: String?, name: String?, startByte: Int,
      endByte: Int): TreeSitterTreeCursorNode {
      return obtainFromPool<TreeSitterTreeCursorNode>().apply {
        this.type = type
        this.name = name
        this.startByte = startByte
        this.endByte = endByte
      }
    }
  }

  override fun recycle() {
    this.type = null
    this.name = null
    this.startByte = 0
    this.endByte = 0
    returnToPool()
  }
}