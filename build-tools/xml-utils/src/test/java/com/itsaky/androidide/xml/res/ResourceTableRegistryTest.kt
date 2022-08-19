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

package com.itsaky.androidide.xml.res

import android.graphics.Color
import com.android.aaptcompiler.AaptResourceType.COLOR
import com.android.aaptcompiler.AaptResourceType.STRING
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.android.ResValue.DataType
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.xml.res.internal.DefaultResourceTableRegistry
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class ResourceTableRegistryTest {

  @Test
  fun `test instance of registry`() {
    val instance = ResourceTableRegistry.getInstance()
    assertThat(instance).isInstanceOf(DefaultResourceTableRegistry::class.java)
  }

  @Test
  fun `test with simple framework resource parsing`() {
    val androidJar = findAndroidJar()
    val registry = ResourceTableRegistry.getInstance()
    val resourceTable = registry.forResourceDir(File(androidJar.parentFile, "data/res"))

    assertThat(resourceTable).isNotNull()
    
    resourceTable.apply {
      assertThat(this!!.packages).hasSize(1)
      this.packages.first().apply {
        assertThat(this.name).isEqualTo("")
      }
    }

    resourceTable!!.findResource(ResourceName(pck = "", type = STRING, entry = "ok")).apply {
      assertThat(this).isNotNull()
      assertThat(this!!.entry).isNotNull()
      assertThat(this.entry.values).isNotEmpty()
      this.entry.values.firstOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this!!.value).isNotNull()
        this.value.apply {
          assertThat(this).isInstanceOf(BasicString::class.java)
          assertThat(this.toString()).isEqualTo("OK")
        }
      }
    }

    resourceTable.findResource(ResourceName(pck = "", type = STRING, entry = "cancel")).apply {
      assertThat(this).isNotNull()
      assertThat(this!!.entry).isNotNull()
      assertThat(this.entry.values).isNotEmpty()
      this.entry.values.firstOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this!!.value).isNotNull()
        this.value.apply {
          assertThat(this).isInstanceOf(BasicString::class.java)
          assertThat(this.toString()).isEqualTo("Cancel")
        }
      }
    }

    resourceTable
      .findResource(ResourceName(pck = "", type = COLOR, entry = "holo_red_dark"))
      .apply {
        assertThat(this).isNotNull()
        assertThat(this!!.entry).isNotNull()
        assertThat(this.entry.values).isNotEmpty()
        this.entry.values.firstOrNull().apply {
          assertThat(this).isNotNull()
          assertThat(this!!.value).isNotNull()
          this.value.apply {
            assertThat(this).isNotNull()
            assertThat(this!!).isInstanceOf(BinaryPrimitive::class.java)
            (this as BinaryPrimitive).resValue.apply {
              assertThat(this).isNotNull()
              assertThat(this.dataType).isEqualTo(DataType.INT_COLOR_ARGB8)
              assertThat(this.data)
                .isEqualTo(Color.parseColor(/*from colors_holo.xml*/ "#ffcc0000"))
            }
          }
        }
      }
  }

  private fun findAndroidJar(): File {
    val androidHome = findAndroidHome()
    return run {
      for (platform in intArrayOf(33, 32, 31)) {
        val f = File(androidHome, "platforms/android-$platform/android.jar")
        if (f.exists() && !f.isDirectory) {
          return@run f
        }
      }

      throw RuntimeException("Cannot find android.jar")
    }
  }

  private fun findAndroidHome(): String {
    var androidHome = System.getenv("ANDROID_HOME")
    if (androidHome != null && androidHome.isNotBlank()) {
      return androidHome
    }

    androidHome = System.getenv("ANDROID_SDK_ROOT")
    if (androidHome != null && androidHome.isNotBlank()) {
      return androidHome
    }

    val os = System.getProperty("os.name")
    val home = System.getProperty("user.home")
    return if (os.contains("Linux")) {
      "$home/Android/Sdk"
    } else {
      "$home\\AppData\\Local\\Android\\Sdk"
    }
  }
}
