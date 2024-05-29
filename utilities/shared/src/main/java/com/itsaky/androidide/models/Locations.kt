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

package com.itsaky.androidide.models

import com.google.gson.annotations.SerializedName
import java.nio.file.Path

data class Location(var file: Path, var range: Range)

data class Position @JvmOverloads constructor(
  @SerializedName("line") var line: Int,
  @SerializedName("column") var column: Int,
  @SerializedName("index") var index: Int = -1
) : Comparable<Position> {

  fun requireIndex(): Int {
    if (index == -1) {
      throw IllegalArgumentException("No index provided")
    }
    return index
  }

  /** Makes the indices 0 if they are negative. */
  fun zeroIfNegative() {
    if (line < 0) {
      line = 0
    }

    if (column < 0) {
      column = 0
    }
  }

  companion object {

    @JvmField
    val NONE = Position(-1, -1)
  }

  override fun compareTo(other: Position): Int {

    val byLine =
      when {
        line < other.line -> -1
        line > other.line -> 1
        else -> 0
      }

    if (byLine != 0) {
      return byLine
    }

    return when {
      column < other.column -> -1
      column > other.column -> 1
      else -> 0
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Position) return false

    if (line != other.line) return false
    if (column != other.column) return false

    return true
  }

  override fun hashCode(): Int {
    var result = line
    result = 31 * result + column
    return result
  }
}

open class Range
@JvmOverloads
constructor(
  @SerializedName("start") var start: Position = Position(0, 0),
  @SerializedName("end") var end: Position = Position(0, 0)
) : Comparable<Range> {

  constructor(src: Range) : this(Position(src.start.line, src.start.column),
    Position(src.end.line, src.end.column))

  companion object {

    @JvmField
    val NONE = Range(Position.NONE, Position.NONE)

    @JvmStatic
    fun pointRange(line: Int, column: Int): Range {
      return pointRange(Position(line, column))
    }

    @JvmStatic
    fun pointRange(position: Position): Range {
      return Range(position, position)
    }
  }

  /**
   * Validate the start and end positions.
   * @see Position.zeroIfNegative()
   */
  fun validate() {
    start.zeroIfNegative()
    end.zeroIfNegative()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Range) return false

    if (start != other.start) return false
    if (end != other.end) return false

    return true
  }

  override fun hashCode(): Int {
    var result = start.hashCode()
    result = 31 * result + end.hashCode()
    return result
  }

  override fun compareTo(other: Range): Int = start.compareTo(other.start)

  fun compareByEnd(other: Range): Int = end.compareTo(other.end)

  fun contains(position: Position): Boolean {
    if (position.line < start.line || position.line > end.line) {
      return false
    }

    if (start.line == end.line) {
      return position.column >= start.column && position.column <= end.column
    }

    return false
  }

  /**
   * Check if this range contains the given position or not. Return an integer result indicating
   * where the index of the range containing this position might be.
   *
   * The return value will be used in a binary search.
   */
  fun containsForBinarySearch(position: Position): Int {

    // The position might appear before this range
    if (position.line < start.line) {
      return -1
    }

    // The position might appear after this range
    if (position.line > end.line) {
      return 1
    }

    // If start and end lines are same, compare by column indexes
    if (start.line == end.line) {

      if (position.column < start.column) {
        return -1
      }

      if (position.column > end.column) {
        return 1
      }
    }

    // This range definitely contains the position.
    return 0
  }

  fun containsLine(line: Int): Boolean {
    return start.line <= line && end.line >= line
  }

  fun containsColumn(column: Int): Boolean {
    return start.column <= column && end.column >= column
  }

  fun containsRange(other: Range): Boolean {
    if (!containsLine(other.start.line) || !containsLine(other.end.line)) {
      return false
    }

    return containsColumn(other.start.column) && containsColumn(other.end.column)
  }

  fun isSmallerThan(other: Range): Boolean {
    return other.isBiggerThan(this)
  }

  fun isBiggerThan(other: Range): Boolean {

    if (equals(other)) {
      return false
    }

    if (start.line < other.start.line && end.line > other.end.line) {
      return true
    }

    if (start.line == other.start.line && end.line == other.end.line) {
      if (start.column <= other.start.column && end.column >= other.end.column) {
        return true
      }
    }

    return false
  }

  override fun toString(): String {
    return "Range(start=$start, end=$end)"
  }
}
