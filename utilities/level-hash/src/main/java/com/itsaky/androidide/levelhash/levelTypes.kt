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

/**
 * The result of a hashing operation. This is defined as a 64-bit unsigned long
 * as the value must always be positive in order to compute the bucket index
 * correctly.
 */
typealias HashT = ULong

/**
 * A function which computes the hash of a given key.
 */
typealias LevelHashFn<K> = (key: K, seed: HashT) -> HashT

/**
 * A function which generates two seed values for the random generator.
 */
typealias SeedGeneratorFn = () -> Pair<HashT, HashT>
