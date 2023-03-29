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

package com.itsaky.androidide.projects.models

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.eventbus.events.editor.ChangeType
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import java.nio.file.Paths
import java.time.Instant
import org.junit.Test

/** @author Akash Yadav */
class ActiveDocumentTest {

  @Test
  fun `test new text set patch`() {
    val file = Paths.get("/")
    val doc = ActiveDocument(file, 0, Instant.now(), "this is a string")
    doc.patch(
      DocumentChangeEvent(
        file,
        "this is a new text",
        2,
        ChangeType.NEW_TEXT,
        -1,
        Range.pointRange(Position(0, doc.content.length).apply { index = column })
      )
    )

    assertThat(doc.content).isEqualTo("this is a new text")
  }

  @Test
  fun `test single line insert patch`() {
    val file = Paths.get("/")
    val doc = ActiveDocument(file, 0, Instant.now(), "this is a string")
    doc.patch(
      DocumentChangeEvent(
        file,
        " added",
        2,
        ChangeType.INSERT,
        -1,
        Range.pointRange(Position(0, doc.content.length).apply { index = column })
      )
    )

    assertThat(doc.content).isEqualTo("this is a string added")
  }

  @Test
  fun `test single line delete patch`() {
    val file = Paths.get("/")
    val content = "this is a string added"
    val doc = ActiveDocument(file, 0, Instant.now(), content)
    doc.patch(
      DocumentChangeEvent(
        file,
        " added",
        2,
        ChangeType.DELETE,
        -1,
        Range(
          Position(0, content.length - " added".length).apply { index = column },
          Position(0, content.length).apply { index = column }
        )
      )
    )

    assertThat(doc.content).isEqualTo("this is a string")
  }

  @Test
  fun `test multi line insert patch`() {
    val file = Paths.get("/")
    val doc = ActiveDocument(file, 0, Instant.now(), "this is an string")
    doc.patch(
      DocumentChangeEvent(
        file,
        "added\nand added\nand added ",
        2,
        ChangeType.INSERT,
        -1,
        Range.pointRange(Position(0, doc.content.length - 6).apply { index = column })
      )
    )

    assertThat(doc.content).isEqualTo("this is an added\nand added\nand added string")
  }

  @Test
  fun `test multi line delete patch`() {
    val file = Paths.get("/")
    val content = "this is an added\nand added\nand added string"
    val doc = ActiveDocument(file, 0, Instant.now(), content)
    doc.patch(
      DocumentChangeEvent(
        file,
        " added",
        2,
        ChangeType.DELETE,
        -1,
        Range(
          Position(0, 9).apply { index = column },
          Position(0, content.length - 7).apply { index = column }
        )
      )
    )

    assertThat(doc.content).isEqualTo("this is a string")
  }
}
