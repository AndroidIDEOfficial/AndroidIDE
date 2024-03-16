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

package com.itsaky.androidide.uidesigner.undo

/**
 * Handles undo and redo actions in the UI designer.
 *
 * @author Akash Yadav
 */
class UndoManager @JvmOverloads constructor(private var maxStackSize: Int = DEFAULT_STACK_SIZE) {

  internal var enabled = true

  private var undoStack = ArrayDeque<IUiAction>()
  private var redoStack = ArrayDeque<IUiAction>()

  companion object {
    const val DEFAULT_STACK_SIZE = 30
  }

  fun enable() {
    this.enabled = true
  }

  fun disable() {
    this.enabled = false
  }

  fun canUndo(): Boolean {
    return enabled && undoStack.isNotEmpty()
  }

  fun canRedo(): Boolean {
    return enabled && redoStack.isNotEmpty()
  }

  fun undo() {
    if (!canUndo()) {
      return
    }

    disable()
    val action = undoStack.removeLast()
    action.undo()

    redoStack.addLast(action)
    trimStacks()
    enable()
  }

  fun redo() {
    if (!canRedo()) {
      return
    }

    disable()
    val action = redoStack.removeLast()
    action.redo()

    undoStack.addLast(action)
    trimStacks()
    enable()
  }

  /**
   * Push the given action to the action stack.
   *
   * @param action The action to push.
   */
  fun push(action: IUiAction) {
    if (!enabled) {
      return
    }

    redoStack.clear()
    undoStack.addLast(action)
    trimStacks()
  }

  fun peekUndo(): IUiAction? {
    return undoStack.lastOrNull()
  }

  fun peekRedo(): IUiAction? {
    return redoStack.lastOrNull()
  }

  fun popUndo(): IUiAction? {
    return undoStack.removeLastOrNull()
  }

  fun popRedo(): IUiAction? {
    return redoStack.removeLastOrNull()
  }

  private fun trimStacks() {
    while (undoStack.size > maxStackSize) {
      undoStack.removeFirst()
    }
    while (redoStack.size > maxStackSize) {
      redoStack.removeFirst()
    }
  }
}
