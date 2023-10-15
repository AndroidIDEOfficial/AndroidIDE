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

import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSParser
import com.itsaky.androidide.treesitter.TSRange
import com.itsaky.androidide.treesitter.TSTree
import com.itsaky.androidide.treesitter.string.UTF16String
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterParser @JvmOverloads internal constructor(
  pointer: Long = 0
) : TSParser(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable(),
  TSSynchronized by DefaultSynchronized() {

  companion object {

    @JvmStatic
    fun obtain(
      pointer: Long
    ): TreeSitterParser {
      return obtainFromPool<TreeSitterParser>().apply {
        this.nativeObject = pointer
      }
    }
  }

  override fun close() {
    withLock {
      super.close()
      recycle()
    }
  }

  override fun setLanguage(language: TSLanguage?) {
    withLock { super.setLanguage(language) }
  }

  override fun parseString(oldTree: TSTree?, source: UTF16String?): TSTree {
    return withLock { super.parseString(oldTree, source) }
  }

  override fun setTimeout(microseconds: Long) {
    withLock { super.setTimeout(microseconds) }
  }

  override fun getTimeout(): Long {
    return withLock { super.getTimeout() }
  }

  override fun setIncludedRanges(ranges: Array<out TSRange>?): Boolean {
    return withLock { super.setIncludedRanges(ranges) }
  }

  override fun getIncludedRanges(): Array<TSRange> {
    return withLock { super.getIncludedRanges() }
  }

  override fun reset() {
    withLock { super.reset() }
  }

  override fun recycle() {
    this.nativeObject = 0
    returnToPool()
  }
}