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
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterNode @JvmOverloads internal constructor(
  context0: Int = 0,
  context1: Int = 0,
  context2: Int = 0,
  context3: Int = 0,
  id: Long = 0,
  tree: Long = 0
) : TSNode(context0, context1, context2, context3, id, tree),
  RecyclableObjectPool.Recyclable by DefaultRecyclable() {

  companion object {

    @JvmStatic
    fun obtain(
      context0: Int, context1: Int, context2: Int, context3: Int, id: Long, tree: Long
    ): TreeSitterNode {
      return obtainFromPool<TreeSitterNode>().apply {
        this.context0 = context0
        this.context1 = context1
        this.context2 = context2
        this.context3 = context3
        this.id = id
        this.tree = tree
      }
    }
  }

  override fun recycle() {
    this.context0 = 0
    this.context1 = 0
    this.context2 = 0
    this.context3 = 0
    this.id = 0
    this.tree = 0
    this.mTree = null
    returnToPool()
  }
}