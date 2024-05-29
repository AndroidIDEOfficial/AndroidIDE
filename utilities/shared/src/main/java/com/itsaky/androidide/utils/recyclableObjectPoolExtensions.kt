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

package com.itsaky.androidide.utils

/**
 * Create a new [RecyclableObjectPool] with the given [capacity] and the object [factory].
 */
inline fun <reified T : RecyclableObjectPool.Recyclable> newRecyclableObjectPool(
  capacity: Int = RecyclableObjectPool.CAPACITY_DEFAULT,
  fillFirst: Int = 0,
  metricsEnabled: Boolean = true,
  factory: RecyclableObjectPool.Factory<T>
): RecyclableObjectPool<T> {
  return RecyclableObjectPool(
    capacity = capacity,
    fillFirst = fillFirst,
    metricsEnabled = metricsEnabled,
    klass = T::class.java,
    objFactory = factory
  )
}

/**
 * Recycle all the recyclable objects in this iterable.
 */
fun <T : RecyclableObjectPool.Recyclable> Iterable<T>.recycleAll() {
  forEach { it.recycle() }
}

/**
 * Recycle all the recyclable objects in this array.
 */
fun <T : RecyclableObjectPool.Recyclable> Array<T>.recycleAll() {
  forEach { it.recycle() }
}
