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
import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSNode
import com.itsaky.androidide.treesitter.TSRange
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool
import java.util.concurrent.TimeUnit

/**
 * @author Akash Yadav
 */
class TreeSitterTree @JvmOverloads internal constructor(
  pointer: Long = 0,
  private val synchronized: DefaultSynchronized = DefaultSynchronized()
) : TSTree(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable(),
  TSSynchronized by synchronized {

  private var ownerThread: Thread? = null

  companion object {

    @JvmStatic
    fun obtain(pointer: Long): TreeSitterTree {
      return obtainFromPool<TreeSitterTree>().apply {
        this.nativeObject = pointer
        this.ownerThread = Thread.currentThread()
      }
    }
  }

  override fun getRootNode(): TSNode {
    return withLock { super.getRootNode() }
  }

  override fun getChangedRanges(oldTree: TSTree?): Array<TSRange> {
    return withLock { super.getChangedRanges(oldTree) }
  }

  override fun copy(): TSTree {
    return withLock { super.copy() }
  }

  override fun edit(edit: TSInputEdit?) {
    withLock { super.edit(edit) }
  }

  override fun getLanguage(): TSLanguage {
    return withLock { super.getLanguage() }
  }

  override fun <T> withLock(timeout: Long, unit: TimeUnit, action: () -> T): T {
//    check(ownerThread != null && Thread.currentThread() == ownerThread) {
//      "TSTree instances are not thread-safe. Copy the tree if you need to use it on multiple threads." +
//          "currentThread=${Thread.currentThread()} initialThread=$ownerThread"
//    }

    return synchronized.withLock(timeout, unit, action)
  }

  override fun close() {
    withLock {
      super.close()
      this.nativeObject = 0
      recycle()
    }
  }

  override fun recycle() {
    this.ownerThread = null
    if (!canAccess()) {
      returnToPool()
    }
  }
}