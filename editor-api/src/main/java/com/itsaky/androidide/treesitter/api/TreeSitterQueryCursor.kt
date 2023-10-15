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
import com.itsaky.androidide.treesitter.TSPoint
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSQueryMatch
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterQueryCursor(
  pointer: Long = 0
) : TSQueryCursor(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable(),
  TSSynchronized by DefaultSynchronized() {

  companion object {

    @JvmStatic
    fun obtain(pointer: Long): TreeSitterQueryCursor {
      return obtainFromPool<TreeSitterQueryCursor>().apply {
        this.nativeObject = pointer
      }
    }
  }

  override fun exec(query: TSQuery?, node: TSNode?) {
    withLock { super.exec(query, node) }
  }

  override fun didExceedMatchLimit(): Boolean {
    return withLock { super.didExceedMatchLimit() }
  }

  override fun getMatchLimit(): Int {
    return withLock { super.getMatchLimit() }
  }

  override fun setMatchLimit(newLimit: Int) {
    withLock { super.setMatchLimit(newLimit) }
  }

  override fun setByteRange(start: Int, end: Int) {
    withLock { super.setByteRange(start, end) }
  }

  override fun setPointRange(start: TSPoint?, end: TSPoint?) {
    withLock { super.setPointRange(start, end) }
  }

  override fun nextMatch(): TSQueryMatch? {
    return withLock { super.nextMatch() }
  }

  override fun removeMatch(id: Int) {
    withLock { super.removeMatch(id) }
  }

  override fun close() {
    withLock {
      super.close()
      recycle()
    }
  }

  override fun recycle() {
    this.nativeObject = 0
    returnToPool()
  }
}