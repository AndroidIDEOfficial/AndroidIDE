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

  private var stackPointer = 0
  private var stack = mutableListOf<IUiAction>()

  companion object {
    const val DEFAULT_STACK_SIZE = 30
  }

  fun canUndo(): Boolean {
    return stackPointer > 0
  }

  fun canRedo(): Boolean {
    return stackPointer < stack.size
  }

  fun undo() {
    if (!canUndo()) {
      return
    }

    stack[stackPointer - 1].undo()
    --stackPointer
  }

  fun redo() {
    if (!canRedo()) {
      return
    }

    stack[stackPointer].redo()
    ++stackPointer
  }

  /**
   * Push the given action to the action stack.
   *
   * @param action The action to push.
   */
  fun push(action: IUiAction) {
    if (stack.isEmpty()) {
      stack.add(action)
      ++stackPointer
      return
    }
    
    removeRedoable()

    stack.add(action)
    ++stackPointer

    trimStack()
  }
  
  private fun removeRedoable() {
    while(stackPointer < stack.size) {
      stack.removeAt(stack.size - 1)
    }
  }

  private fun trimStack() {
    while (stackPointer > 1 && stack.size > maxStackSize) {
      stack.removeAt(0)
      --stackPointer
    }
  }
}
