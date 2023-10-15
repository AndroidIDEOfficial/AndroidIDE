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
import com.itsaky.androidide.treesitter.TSTreeCursor
import com.itsaky.androidide.treesitter.TSTreeCursorNode
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterTreeCursor @JvmOverloads internal constructor(
  pointer: Long = 0
) : TSTreeCursor(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable(),
  TSSynchronized by DefaultSynchronized() {

  companion object {

    @JvmStatic
    fun obtain(pointer: Long): TreeSitterTreeCursor {
      return obtainFromPool<TreeSitterTreeCursor>().apply {
        this.nativeObject = pointer
      }
    }
  }

  override fun getCurrentNode(): TSNode {
    return withLock { super.getCurrentNode() }
  }

  override fun getCurrentFieldName(): String {
    return withLock { super.getCurrentFieldName() }
  }

  override fun getCurrentFieldId(): Short {
    return withLock { super.getCurrentFieldId() }
  }

  override fun getCurrentTreeCursorNode(): TSTreeCursorNode {
    return withLock { super.getCurrentTreeCursorNode() }
  }

  override fun gotoFirstChild(): Boolean {
    return withLock { super.gotoFirstChild() }
  }

  override fun gotoFirstChildForByte(byteIndex: Int): Long {
    return withLock { super.gotoFirstChildForByte(byteIndex) }
  }

  override fun gotoFirstChildForPoint(point: TSPoint?): Boolean {
    return withLock { super.gotoFirstChildForPoint(point) }
  }

  override fun gotoLastChild(): Boolean {
    return withLock { super.gotoLastChild() }
  }

  override fun gotoNextSibling(): Boolean {
    return withLock { super.gotoNextSibling() }
  }

  override fun gotoPreviousSibling(): Boolean {
    return withLock { super.gotoPreviousSibling() }
  }

  override fun gotoParent(): Boolean {
    return withLock { super.gotoParent() }
  }

  override fun gotoDescendant(descendantIndex: Int) {
    withLock { super.gotoDescendant(descendantIndex) }
  }

  override fun getCurrentDescendantIndex(): Int {
    return withLock { super.getCurrentDescendantIndex() }
  }

  override fun getDepth(): Int {
    return withLock { super.getDepth() }
  }

  override fun reset(node: TSNode?) {
    withLock { super.reset(node) }
  }

  override fun resetTo(another: TSTreeCursor?) {
    withLock { super.resetTo(another) }
  }

  override fun copy(): TSTreeCursor {
    return withLock { super.copy() }
  }

  override fun close() {
    withLock {
      super.close()
      recycle()
    }
  }

  override fun recycle() {
    this.nativeObject = 0
    this.context0 = 0
    this.context1 = 0
    this.id = 0
    this.tree = 0
    returnToPool()
  }
}