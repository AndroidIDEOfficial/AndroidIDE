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

import com.itsaky.androidide.treesitter.TSInputEdit
import com.itsaky.androidide.treesitter.TSPoint
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

class TreeSitterInputEdit @JvmOverloads internal constructor(
  startByte: Int = 0,
  oldEndByte: Int = 0,
  newEndByte: Int = 0,
  startPoint: TSPoint? = null,
  oldEndPoint: TSPoint? = null,
  newEndPoint: TSPoint? = null
) : TSInputEdit(startByte, oldEndByte, newEndByte, startPoint, oldEndPoint, newEndPoint),
  RecyclableObjectPool.Recyclable by DefaultRecyclable() {

  companion object {

    /**
     * Obtain an instance of [TreeSitterInputEdit].
     */
    @JvmStatic
    fun obtain(startByte: Int,
      oldEndByte: Int,
      newEndByte: Int,
      startPoint: TSPoint,
      oldEndPoint: TSPoint,
      newEndPoint: TSPoint): TreeSitterInputEdit {

      return obtainFromPool<TreeSitterInputEdit>().apply {
        this.startByte = startByte
        this.oldEndByte = oldEndByte
        this.newEndByte = newEndByte
        this.startPoint = startPoint
        this.oldEndPoint = oldEndPoint
        this.newEndPoint = newEndPoint
      }
    }
  }

  override fun recycle() {
    this.startByte = 0
    this.oldEndByte = 0
    this.newEndByte = 0
    this.startPoint = null
    this.oldEndPoint = null
    this.newEndPoint = null

    returnToPool()
  }
}