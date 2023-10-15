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

import com.itsaky.androidide.utils.RecyclableObjectPool
import com.itsaky.androidide.utils.RecyclableObjectPool.Companion.CAPACITY_DEFAULT
import com.itsaky.androidide.utils.RecyclableObjectPool.Companion.CAPACITY_MINI
import com.itsaky.androidide.utils.newRecyclableObjectPool
import com.itsaky.androidide.utils.uncheckedCast
import java.util.concurrent.ConcurrentHashMap

/**
 * Provides instances of [RecyclableObjectPool] for tree sitter objects.
 *
 * @author Akash Yadav
 */
internal object TreeSitterObjectPoolProvider {

  private val caches = ConcurrentHashMap<Class<out RecyclableObjectPool.Recyclable>, RecyclableObjectPool<*>>()

  init {
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterInputEdit)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterLookaheadIterator)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterNativeLanguage)
    putPool(capacity = 1_00_000, fillFirst = 1000, factory = ::TreeSitterNode)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterParser)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterPoint)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterQuery)
    putPool(capacity = 1_00_000, fillFirst = 1000, factory = ::TreeSitterQueryCapture)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterQueryCursor)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterQueryMatch)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterQueryPredicateStep)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterRange)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterTree)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterTreeCursor)
    putPool(capacity = CAPACITY_MINI, factory = ::TreeSitterTreeCursorNode)
  }

  fun logMetrics() {
    caches.forEach { (_, pool) ->
      pool.logMetrics()
    }
  }

  inline fun <reified T : RecyclableObjectPool.Recyclable> getPool(): RecyclableObjectPool<T>? {
    return getPool(T::class.java)
  }

  inline fun <reified T : RecyclableObjectPool.Recyclable> requirePool(): RecyclableObjectPool<T> {
    return requireNotNull(getPool())
  }

  fun <T : RecyclableObjectPool.Recyclable> getPool(klass: Class<T>): RecyclableObjectPool<T>? {
    return caches[klass]?.let { uncheckedCast(it) }
  }

  fun <T : RecyclableObjectPool.Recyclable> requirePool(klass: Class<T>): RecyclableObjectPool<T> {
    return requireNotNull(getPool(klass))
  }

  private inline fun <reified T : RecyclableObjectPool.Recyclable> putPool(
    capacity: Int = CAPACITY_DEFAULT,
    fillFirst: Int = 0,
    factory: RecyclableObjectPool.Factory<T>
  ) {
    if (caches[T::class.java] != null) {
      throw IllegalArgumentException("Attempt to replace object pool for ${T::class.java}")
    }

    // disable metrics logging for tree sitter objects
    // this is required because the log views also use tree sitter to highlight the lines
    // if we enable metrics, it would result in an infite loop and 'analyze and log' in some cases
    caches[T::class.java] = newRecyclableObjectPool(capacity, fillFirst, false, factory)
  }
}