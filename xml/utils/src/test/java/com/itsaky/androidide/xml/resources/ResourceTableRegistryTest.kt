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
package com.itsaky.androidide.xml.resources

import android.graphics.Color
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.COLOR
import com.android.aaptcompiler.AaptResourceType.STRING
import com.android.aaptcompiler.AaptResourceType.STYLEABLE
import com.android.aaptcompiler.BasicString
import com.android.aaptcompiler.BinaryPrimitive
import com.android.aaptcompiler.ResourceName
import com.android.aaptcompiler.android.ResValue.DataType
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.xml.findAndroidJar
import com.itsaky.androidide.xml.internal.resources.DefaultResourceTableRegistry
import com.itsaky.androidide.xml.resources.ResourceTableRegistry.Companion.PCK_ANDROID
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

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
    val platform = androidJar.parentFile!!
    val registry = ResourceTableRegistry.getInstance()
    val resDir = File(androidJar.parentFile, "data/res")
    val resourceTable = registry.forPackage(PCK_ANDROID, resDir)

    assertThat(resourceTable).isNotNull()

    // Should return same instance unless updated
    assertThat(registry.forPackage(PCK_ANDROID, resDir)).isEqualTo(resourceTable)

    // It should also create the manifest attrs table by default
    registry.getManifestAttrTable(platform = platform).apply {
      assertThat(this).isNotNull()
      assertThat(this!!.packages).hasSize(1)
      assertThat(this.packages.first().name).isEqualTo(PCK_ANDROID)
      assertThat(findPackage(PCK_ANDROID)!!.findGroup(ATTR)).isNotNull()
      assertThat(findPackage(PCK_ANDROID)!!.findGroup(STYLEABLE)).isNotNull()
    }

    registry.getActivityActions(platform).apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeastElementsIn(
          arrayOf(
            "android.app.action.SET_NEW_PASSWORD",
            "android.app.action.START_ENCRYPTION",
            "android.intent.action.CARRIER_SETUP",
            "android.intent.action.CHOOSER",
            "android.intent.action.MAIN",
            "android.intent.action.MANAGE_APP_PERMISSION",
            "android.intent.action.MANAGE_APP_PERMISSIONS"
          )
        )
      assertThat(this.size).isGreaterThan(250) // actual size is 300+
    }

    registry.getBroadcastActions(platform).apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeastElementsIn(
          arrayOf(
            "android.telephony.action.SUBSCRIPTION_PLANS_CHANGED",
            "android.telephony.action.SUBSCRIPTION_SPECIFIC_CARRIER_IDENTITY_CHANGED",
            "android.telephony.euicc.action.NOTIFY_CARRIER_SETUP_INCOMPLETE",
            "android.telephony.euicc.action.OTA_STATUS_CHANGED",
            "android.app.action.USER_ADDED",
            "android.app.action.USER_REMOVED"
          )
        )
    }

    registry.getServiceActions(platform).apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeastElementsIn(
          arrayOf(
            "android.telephony.data.DataService",
            "android.view.InputMethod",
            "android.intent.action.TTS_SERVICE",
            "android.media.MediaRoute2ProviderService",
            "android.service.voice.VoiceInteractionService",
            "android.service.vr.VrListenerService"
          )
        )
    }

    registry.getCategories(platform).apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeastElementsIn(
          arrayOf(
            "android.intent.category.HOME",
            "android.intent.category.HOME_MAIN",
            "android.intent.category.INFO",
            "android.intent.category.LAUNCHER",
            "android.intent.category.LAUNCHER_APP",
            "android.net.category.EVENT_IKE_ERROR",
            "android.net.category.EVENT_NETWORK_ERROR",
            "android.service.quicksettings.action.QS_TILE_PREFERENCES"
          )
        )
    }

    registry.getFeatures(platform).apply {
      assertThat(this).isNotEmpty()
      assertThat(this)
        .containsAtLeastElementsIn(
          arrayOf(
            "android.software.vulkan.deqp.level",
            "android.software.webview",
            "android.software.window_magnification",
            "android.sofware.nfc.beam",
            "android.hardware.strongbox_keystore",
            "android.hardware.telephony",
            "android.hardware.telephony.calling",
            "android.hardware.telephony.carrierlock",
            "android.hardware.ethernet",
            "android.hardware.faketouch"
          )
        )
    }

    resourceTable.apply {
      assertThat(this).isNotNull()
      assertThat(this!!.packages).hasSize(1)
      this.packages.first().apply { assertThat(this.name).isEqualTo("android") }
    }

    resourceTable!!.findResource(
      ResourceName(
        pck = "android",
        type = STRING,
        entry = "ok"
      )
    ).apply {
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

    resourceTable
      .findResource(
        ResourceName(
          pck = "android",
          type = STRING,
          entry = "cancel"
        )
      )
      .apply {
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
      .findResource(
        ResourceName(
          pck = "android",
          type = COLOR,
          entry = "holo_red_dark"
        )
      )
      .apply {
        assertThat(this).isNotNull()
        assertThat(this!!.entry).isNotNull()
        assertThat(this.entry.values).isNotEmpty()
        this.entry.values.firstOrNull().apply {
          assertThat(this).isNotNull()
          assertThat(this!!.value).isNotNull()
          this.value.apply {
            assertThat(this).isNotNull()
            assertThat(this!!).isInstanceOf(com.android.aaptcompiler.BinaryPrimitive::class.java)
            (this as com.android.aaptcompiler.BinaryPrimitive).resValue.apply {
              assertThat(this).isNotNull()
              assertThat(this.dataType).isEqualTo(DataType.INT_COLOR_ARGB8)
              assertThat(this.data)
                .isEqualTo(Color.parseColor(/*from colors_holo.xml*/ "#ffcc0000"))
            }
          }
        }
      }
  }
}
