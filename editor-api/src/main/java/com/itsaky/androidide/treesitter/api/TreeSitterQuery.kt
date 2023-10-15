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

import com.itsaky.androidide.treesitter.TSQuantifier
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryPredicateStep
import com.itsaky.androidide.utils.DefaultRecyclable
import com.itsaky.androidide.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterQuery @JvmOverloads internal constructor(
  pointer: Long = 0
) : TSQuery(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable(),
  TSSynchronized by DefaultSynchronized() {

  companion object {

    @JvmStatic
    fun obtain(pointer: Long): TreeSitterQuery {
      return obtainFromPool<TreeSitterQuery>().apply {
        this.nativeObject = pointer
      }
    }
  }

  override fun getCaptureCount(): Int {
    return withLock { super.getCaptureCount() }
  }

  override fun getPatternCount(): Int {
    return withLock { super.getPatternCount() }
  }

  override fun getStringCount(): Int {
    return withLock { super.getStringCount() }
  }

  override fun getCaptureNames(): Array<String> {
    return withLock { super.getCaptureNames() }
  }

  override fun getStartByteForPattern(pattern: Int): Int {
    return withLock { super.getStartByteForPattern(pattern) }
  }

  override fun getPredicatesForPattern(pattern: Int): Array<TSQueryPredicateStep> {
    return withLock { super.getPredicatesForPattern(pattern) }
  }

  override fun isPatternRooted(pattern: Int): Boolean {
    return withLock { super.isPatternRooted(pattern) }
  }

  override fun isPatternNonLocal(pattern: Int): Boolean {
    return withLock { super.isPatternNonLocal(pattern) }
  }

  override fun isPatternGuaranteedAtStep(offset: Int): Boolean {
    return withLock { super.isPatternGuaranteedAtStep(offset) }
  }

  override fun getCaptureNameForId(id: Int): String {
    return withLock { super.getCaptureNameForId(id) }
  }

  override fun getStringValueForId(id: Int): String {
    return withLock { super.getStringValueForId(id) }
  }

  override fun getCaptureQuantifierForId(pattern: Int, capture: Int): TSQuantifier {
    return withLock { super.getCaptureQuantifierForId(pattern, capture) }
  }

  override fun close() {
    withLock {
      super.close()
      recycle()
    }
  }

  override fun recycle() {
    this.nativeObject = 0
    this.captureNames = null
    this.errorOffset = 0
    this.errorType = 0
    returnToPool()
  }
}