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

import org.slf4j.LoggerFactory
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

// TODO(itsaky): Automatically resize the pool based on the cache hit rate and the acess count

/**
 * An object pool allows to recycle and reuse objects of a specific type. Implmentations of this are
 * expected to be thread safe.
 *
 * @param capacity The maximum capacity of the pool.
 * @param fillFirst The number of items that should be created and put in the cache.
 * @param klass The class of the recyclable object. This is used for logging purposes.
 * @param objFactory The factory which will be used to create new instances of the object.
 * @author Akash Yadav
 */
class RecyclableObjectPool<RecyclableT : RecyclableObjectPool.Recyclable> @JvmOverloads constructor(
  private val capacity: Int = CAPACITY_DEFAULT,
  private val objFactory: Factory<RecyclableT>,
  private val metricsEnabled: Boolean = true,
  fillFirst: Int = 0,
  klass: Class<RecyclableT>
) {

  private val lastMetricsLog = AtomicLong(0)

  companion object {

    var DEBUG = false
    var LOG_METRICS_ON_N_ACCESS = 400

    private const val CACHE_HIT_WARNING_THRESHOLD = 50f

    private val log by lazy {
      LoggerFactory.getLogger(RecyclableObjectPool::class.java)
    }

    const val CAPACITY_DEFAULT: Int = 4096
    const val CAPACITY_LARGE: Int = CAPACITY_DEFAULT * 2
    const val CAPACITY_SMALL: Int = CAPACITY_DEFAULT / 2
    const val CAPACITY_MINI: Int = CAPACITY_SMALL / 2
  }

  private val objName = klass.name

  /**
   * Access count and cache hit of the pool. MSB 32 bits of this long represent the access count
   * while the LSB 32 bits represent the cache hit.
   */
  private val accCount = AtomicLong(0)

  /**
   * The recycle count of this pool.
   */
  private val recCount = AtomicInteger(0)

  private val cache = ArrayBlockingQueue<RecyclableT>(capacity)

  init {
    this.fillFirst(fillFirst)
  }

  private fun fillFirst(n: Int) {
    if (n < 1) {
      return
    }

    val count = min(n, capacity)
    for (i in 1..count) {
      cache.offer(objFactory.create())
    }
  }

  /**
   * Recycle the given object.
   */
  fun recycle(obj: RecyclableT): Boolean {
    if (obj.isRecycled) {
      log.warn("Trying to recyle already recycled object: {}", obj)
      return false
    }

    return cache.offer(obj).also { inserted ->
      if (inserted) {
        obj.isRecycled = true
        recCount.incrementAndGet()
      }
    }
  }

  /**
   * Obtain an object from the pool or a new object if the pool is empty.
   */
  fun obtain(): RecyclableT {
    val acc = accCount.get()
    var access = IntPair.getFirst(acc)
    var cacheHit = IntPair.getSecond(acc)

    // increment the access count
    accCount.set(IntPair.pack(++access, cacheHit))

    val result = this.cache.poll()?.also {
      it.isRecycled = false

      // increment the cache hit
      accCount.set(IntPair.pack(access, ++cacheHit))
    } ?: objFactory.create()

    if (DEBUG && access % LOG_METRICS_ON_N_ACCESS == 0) {
      logMetrics()
    }

    return result
  }

  fun cacheHitRate(): Float {
    val acc = accCount.get();
    val access = IntPair.getFirst(acc).toFloat()
    val cacheHit = IntPair.getSecond(acc).toFloat()
    return (cacheHit / access) * 100F
  }

  fun cacheMissRate(): Float {
    return 100F - cacheHitRate()
  }

  fun recycleRate(): Float {
    val rec = recCount.get().toFloat()
    val access = IntPair.getFirst(accCount.get()).toFloat()
    return (rec / access) * 100
  }

  fun cacheUtilization(): Float {
    val access = IntPair.getFirst(accCount.get()).toFloat()
    return (access / capacity) * 100
  }

  fun logMetrics() {
    if (!(metricsEnabled && DEBUG) || System.currentTimeMillis() - lastMetricsLog.get() < 1000) {
      return
    }

    val acc = accCount.get()
    val rec = recCount.get()
    val access = IntPair.getFirst(acc)
    val cacheHit = IntPair.getSecond(acc)
    val cacheHitRate = cacheHitRate()

    val simpleName = objName.let { if (it.contains('.')) it.substringAfterLast('.') else it }
    log.debug("{}: {}({})", javaClass.simpleName, simpleName, objName)
    log.debug("    Recycle count          : {}", rec)
    log.debug("    Access count           : {}", access)
    log.debug("    Cache hit count        : {}", cacheHit)
    log.debug("    Cache hit rate         : {}%", cacheHitRate)
    log.debug("    Cache miss rate        : {}%", cacheMissRate())
    log.debug("    Recycle rate           : {}%", recycleRate())
    log.debug("    Cache utilization rate : {}%", cacheUtilization())
    log.debug("    Objects in cache       : {}", cache.size)
    log.debug("    Total capacity         : {}", capacity)

    if (cacheHitRate < CACHE_HIT_WARNING_THRESHOLD) {
      // Cache hit rate is less than threshold
      // something might be wrong
      // log an error
      log.error("!!!!!!!!!!!!!!!!!!!!! CRITICAL ERROR !!!!!!!!!!!!!!!!!!!!!")
      log.error("Cache-hit rate for '{}' is less than {}%!!", simpleName, CACHE_HIT_WARNING_THRESHOLD)
      log.error("Make sure that instances of {} are obtained using the object pool", objName)
      log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    lastMetricsLog.set(System.currentTimeMillis())
  }

  /**
   * Any object that can be recycled.
   *
   * A [Recyclable] object implementation should prefer defining a default no-args constructor which
   * initializes the properties with the default values.
   */
  interface Recyclable {

    /**
     * Whether the object has been recycled. This property is supposed to be modified only by the pool.
     */
    var isRecycled: Boolean

    /**
     * Recycle the this object. Implementations should make sure that all the properties are resset
     * to their default values (as if the object was newly created).
     */
    fun recycle()
  }

  /**
   * A [Factory] creates [Recyclable] objects.
   */
  fun interface Factory<_RecyclableT : Recyclable> {

    /**
     * Create the [_RecyclableT] object.
     */
    fun create(): _RecyclableT
  }
}