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

package com.itsaky.lsp.models

import java.nio.file.Path

data class Location (var file: Path, var range: Range)

data class Position(var line: Int, var column: Int) {
    
    /**
     * Validate the line and column positions if necessary.
     * This checks if the values are negative or not. If they are,
     * sets them to 0.
     */
    fun validate () {
        if (line < 0) {
            line = 0
        }
        
        if (column < 0) {
            column = 0
        }
    }
    
    companion object {
        @JvmField val NONE = Position (-1, -1)
    }
}

open class Range(var start: Position, var end: Position) {
    
    constructor() : this (Position(0, 0), Position(0, 0));
    
    companion object {
        @JvmField val NONE = Range (Position.NONE, Position.NONE);
    }
    
    /**
     * Validate the start and end positions.
     * @see Position.validate()
     */
    fun validate () {
        start.validate()
        end.validate()
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
}