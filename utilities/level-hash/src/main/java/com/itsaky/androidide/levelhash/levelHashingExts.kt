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

package com.itsaky.androidide.levelhash

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets
import kotlin.random.Random

private val seeds by lazy {
  //  val random = Random(System.currentTimeMillis())
  val random = Random(6248403840530382848)

  var seed1: HashT
  var seed2: HashT
  do {
    // its fine to perform unchecked sign conversion here
    seed1 = random.nextLong().toULong()
    seed2 = random.nextLong().toULong()

    // see docs for `shl` function for more details about why '63' is used
    seed1 = seed1 shl (random.nextLong().toULong() % 63UL).toInt()
    seed2 = seed2 shl (random.nextLong().toULong() % 63UL).toInt()

  } while (seed1 == seed2)

  seed1 to seed2
}

/**
 * Generate two randomized seeds for hash functions.
 */
internal fun levelHashDefaultSeeds() : Pair<HashT, HashT> {
  return seeds
}

/**
 * A simple [LevelHashFn] for hashing string keys using FarmHash's Fingerprint64, an open-source
 * algorithm.
 *
 * @param key The key to hash.
 * @param seed The seed to use for hashing. For this implementation, this value is simply hashed
 * along with the string key.
 */
@Suppress("UnstableApiUsage")
fun stringHash(key: String, seed: HashT) : HashT {
  val hasher = Hashing.farmHashFingerprint64().newHasher()
  return hasher
    .putLong(seed.toLong())
    .putString(key, StandardCharsets.UTF_8)
    .hash()
    .asLong()
    .toULong()
}