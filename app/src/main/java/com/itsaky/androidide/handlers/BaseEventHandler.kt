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

package com.itsaky.androidide.handlers

import android.content.Context
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.eventbus.events.Event
import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.utils.ILogger

/**
 * Base class for event handlers.
 *
 * @author Akash Yadav
 */
abstract class BaseEventHandler : EventReceiver {

  protected val log = ILogger.newInstance(javaClass.simpleName)

  protected open fun checkIsEditorActivity(event: Event): Boolean {
    return event.get(Context::class.java) is EditorActivity
  }

  protected open fun logCannotHandle(event: Event) {
    log.warn("Context is not EditorActivity. Cannot handle ${event.javaClass.simpleName} event.")
  }
}
