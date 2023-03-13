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

package com.itsaky.androidide.eventbus.events.file

import com.itsaky.androidide.eventbus.events.Event
import java.io.File

/** Base class for file events. */
abstract class FileEvent : Event() {
  abstract val file: File
}

/**
 * Event dispatched when a new file is created in the file tree.
 *
 * @author Akash Yadav
 */
data class FileCreationEvent(override val file: File) : FileEvent()

/**
 * Event dispatched when a file is deleted in the file tree.
 *
 * @author Akash Yadav
 */
data class FileDeletionEvent(override val file: File) : FileEvent()

/**
 * Event dispatched when a file is renamed in the file tree.
 *
 * @author Akash Yadav
 */
class FileRenameEvent(override val file: File, val newFile: File) : FileEvent()