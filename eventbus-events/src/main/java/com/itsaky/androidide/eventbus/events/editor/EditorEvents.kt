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

package com.itsaky.androidide.eventbus.events.editor

import com.itsaky.androidide.eventbus.events.Event

/**
 * Dispatched when the color schemes has been reloaded. Listeners should update any UI which use
 * color schemes.
 *
 * For example, on receiving this event, the editors in AndroidIDE invalidate themselves to redraw
 * the content with the updated color schemes.
 */
class ColorSchemeInvalidatedEvent : Event()