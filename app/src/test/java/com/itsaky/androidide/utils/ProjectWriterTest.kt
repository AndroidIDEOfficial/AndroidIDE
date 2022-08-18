package com.itsaky.androidide.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProjectWriterTest {

  @Test
  fun `test activity layout name creation from java file name`() {
    val javaName = "MainActivity.java"
    val layoutName = ProjectWriter.createLayoutName(javaName)
    assertThat(layoutName).isEqualTo("activity_main.xml")
  }
  
  @Test
  fun `test fragment layout name creation from java file name`() {
    val javaName = "MainFragment.java"
    val layoutName = ProjectWriter.createLayoutName(javaName)
    assertThat(layoutName).isEqualTo("fragment_main.xml")
  }
  
  @Test
  fun `test layout name creation from non-suffixed java file name`() {
    val javaName = "Main.java"
    val layoutName = ProjectWriter.createLayoutName(javaName)
    assertThat(layoutName).isEqualTo("layout_main.xml")
  }
  
  @Test
  fun `test layout name creation from lowercase non-suffixed java file name`() {
    val javaName = "main.java"
    val layoutName = ProjectWriter.createLayoutName(javaName)
    assertThat(layoutName).isEqualTo("layout_main.xml")
  }
}
