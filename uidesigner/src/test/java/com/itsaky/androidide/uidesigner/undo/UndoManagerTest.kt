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

import android.view.View
import android.widget.LinearLayout
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewGroupImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.uidesigner.requiresActivity
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class UndoManagerTest {

  @Test
  fun `test single view addition undo redo`() {
    requiresActivity {
      val parent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val child = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val undoManager = UndoManager()

      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isFalse()

      parent.addChild(child)
      undoManager.push(ViewAddedAction(child, parent))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(1)
      assertThat(parent[0]).isEqualTo(child)

      // --------------------------------------------------
      // Test undo/redo for view addition

      // undo add child
      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(0)

      // redo add child
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(1)
      assertThat(parent[0]).isEqualTo(child)
    }
  }

  @Test
  fun `test single view removal undo redo`() {
    requiresActivity {
      val parent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val child = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val undoManager = UndoManager()

      parent.addChild(child)

      // -----------------------------------------------------
      // Test undo/redo for view removal

      parent.removeChild(child)
      undoManager.push(ViewRemovedAction(child, parent, 0))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(0)

      // add child again
      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(1)
      assertThat(parent[0]).isEqualTo(child)

      // remove child again
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(0)
    }
  }
  
  @Test
  fun `test undo redo when view moved from one parent to another`() {
    requiresActivity {
      val firstParent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val secondParent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val child = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val undoManager = UndoManager()
      
      // view initially added to first parent
      firstParent.addChild(child)
      assertThat(firstParent.childCount).isEqualTo(1)
      assertThat(secondParent.childCount).isEqualTo(0)
      assertThat(firstParent[0]).isEqualTo(child)
      
      
      // view moved to second parent
      child.removeFromParent()
      secondParent.addChild(child)
      undoManager.push(ViewMovedAction(child, firstParent, secondParent, 0, 0))
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(firstParent.childCount).isEqualTo(0)
      assertThat(secondParent.childCount).isEqualTo(1)
      assertThat(secondParent[0]).isEqualTo(child)
      
      // undo the last action
      // this should move child from secondParent to firstParent
      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(firstParent.childCount).isEqualTo(1)
      assertThat(secondParent.childCount).isEqualTo(0)
      assertThat(firstParent[0]).isEqualTo(child)
  
      // redo the last action
      // this should move child from firstParent to secondParent
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(firstParent.childCount).isEqualTo(0)
      assertThat(secondParent.childCount).isEqualTo(1)
      assertThat(secondParent[0]).isEqualTo(child)
    }
  }

  @Test
  fun `test actions not redoable after new action push`() {
    requiresActivity {
      val parent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val child = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val undoManager = UndoManager()

      // --------------------------------------------------------
      // When the user undoes an action and then performs another action then there should not be
      // anything to redo

      parent.addChild(child)
      undoManager.push(ViewAddedAction(child, parent))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()

      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()

      child.removeFromParent()
      undoManager.push(ViewRemovedAction(child, parent, 0))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
    }
  }

  @Test
  fun `test multiple view undo redo`() {
    requiresActivity {
      val parent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val firstChild = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val secondChild = ViewImpl(LayoutFile(File(""), ""), "", View(this))
      val undoManager = UndoManager()

      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isFalse()

      parent.addChild(firstChild)
      undoManager.push(ViewAddedAction(firstChild, parent))

      parent.addChild(secondChild)
      undoManager.push(ViewAddedAction(secondChild, parent))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(2)
      assertThat(parent[0]).isEqualTo(firstChild)
      assertThat(parent[1]).isEqualTo(secondChild)

      // remove secondChild
      undoManager.undo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(1)
      assertThat(parent[0]).isEqualTo(firstChild)

      // remove firstChild
      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(0)

      // add firstChild again
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(1)
      assertThat(parent[0]).isEqualTo(firstChild)

      // add secondChild again
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(2)
      assertThat(parent[0]).isEqualTo(firstChild)
      assertThat(parent[1]).isEqualTo(secondChild)
    }
  }

  @Test
  fun `test indexed child addition and removal`() {
    requiresActivity {
      val parent = ViewGroupImpl(LayoutFile(File(""), ""), "", LinearLayout(this))
      val undoManager = UndoManager()

      for (i in 1..5) {
        parent.addChild(ViewImpl(LayoutFile(File(""), ""), "", View(this)))
      }

      assertThat(parent.childCount).isEqualTo(5)

      val child = ViewImpl(LayoutFile(File(""), ""), "", View(this))

      parent.addChild(3, child)
      undoManager.push(ViewAddedAction(child, parent))

      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(6)
      assertThat(parent[3]).isEqualTo(child)

      // remove added child
      undoManager.undo()
      assertThat(undoManager.canUndo()).isFalse()
      assertThat(undoManager.canRedo()).isTrue()
      assertThat(parent.childCount).isEqualTo(5)
      assertThat(parent[3]).isNotEqualTo(child)

      // add child again
      undoManager.redo()
      assertThat(undoManager.canUndo()).isTrue()
      assertThat(undoManager.canRedo()).isFalse()
      assertThat(parent.childCount).isEqualTo(6)
      assertThat(parent[3]).isEqualTo(child)
    }
  }
}
