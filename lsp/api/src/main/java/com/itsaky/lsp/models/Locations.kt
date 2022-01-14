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
    companion object {
        @JvmField val NONE = Position (0, 0)
    }
}

data class Range(var start: Position, var end: Position) {
    companion object {
        @JvmField val NONE = Range (Position.NONE, Position.NONE);
    }
}