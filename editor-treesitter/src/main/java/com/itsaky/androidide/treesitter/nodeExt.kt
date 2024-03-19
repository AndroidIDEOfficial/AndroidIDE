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

package com.itsaky.androidide.treesitter

/**
 * Get the first node at the line and column.
 *
 * @param line The 0-based line number.
 * @param colBytes The 0-based column number in bytes.
 */
fun TSNode.getNodeAt(line: Int, colBytes: Int): TSNode? {
  return getDescendantForPointRange(TSPoint.create(line, colBytes),
    TSPoint.create(line, colBytes + 1))
}