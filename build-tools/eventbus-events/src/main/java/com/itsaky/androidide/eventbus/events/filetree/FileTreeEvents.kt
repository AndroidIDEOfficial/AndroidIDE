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

package com.itsaky.androidide.eventbus.events.filetree

import com.itsaky.androidide.eventbus.events.Event
import java.io.File

/**
 * Event dispatched when a file is clicked in the file tree.
 *
 * @param file The clicked file.
 * @author Akash Yadav
 */
data class FileClickEvent(val file: File) : Event()

/**
 * Event dispatched when a file is long clicked in the file tree.
 *
 * @param file The file that was long clicked.
 * @author Akash Yadav
 */
class FileLongClickEvent(val file: File) : Event()
