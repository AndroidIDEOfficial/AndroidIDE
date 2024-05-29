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

package com.itsaky.androidide.templates.base.util

import com.android.SdkConstants.ANDROID_NS_NAME
import com.android.SdkConstants.ANDROID_URI
import com.android.SdkConstants.TAG_ACTION
import com.android.SdkConstants.TAG_ACTIVITY
import com.android.SdkConstants.TAG_APPLICATION
import com.android.SdkConstants.TAG_CATEGORY
import com.android.SdkConstants.TAG_INTENT_FILTER
import com.android.SdkConstants.TAG_MANIFEST
import com.android.SdkConstants.TAG_USES_PERMISSION
import com.android.SdkConstants.XMLNS
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.base.modules.android.ManifestActivity
import com.itsaky.androidide.templates.base.modules.android.ManifestIcon
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder.ConfigurationType.APPLICATION_ATTR
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder.ConfigurationType.APPLICATION_CONTENT
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder.ConfigurationType.MANIFEST_ATTR
import com.itsaky.androidide.templates.base.util.AndroidManifestBuilder.ConfigurationType.MANIFEST_CONTENT
import com.itsaky.androidide.xml.permissions.Permission
import org.eclipse.lemminx.dom.builder.IndentedXmlBuilder
import org.eclipse.lemminx.dom.builder.IndentedXmlConfigurator
import java.io.File

/**
 * Builder for building `AndroidManifest.xml` file for an Android module.
 *
 * @author Akash Yadav
 */
class AndroidManifestBuilder {

  enum class ConfigurationType {

    /**
     * For configuring attributes for the `<manifest>` tag.
     */
    MANIFEST_ATTR,

    /**
     * For configuring elements inside the `<manifest>` tag.
     */
    MANIFEST_CONTENT,

    /**
     * For configuring attributes for the `<application>` tag.
     */
    APPLICATION_ATTR,

    /**
     * For configuring elements inside the `<application>` tag.
     */
    APPLICATION_CONTENT
  }

  private val configurators =
    hashMapOf<ConfigurationType, HashSet<IndentedXmlConfigurator>>()
  private val permissions = hashSetOf<Permission>()
  private val activities = hashSetOf<ManifestActivity>()

  /**
   * The name of the string resource to use in the `android:label` attribute of `<application>` tag.
   */
  var appLabelRes: String = "app_name"

  /**
   * The package name for the manifest. If the value is `null`, then the package attribute is not defined in the manifest.
   */
  var packageName: String? = null

  /**
   * The icon for the application. Not defined in manifest if value is `null`.
   */
  var icon: ManifestIcon = ManifestIcon("ic_launcher", "mipmap")

  /**
   * The round icon for the application. Not defined in manifest if value is `null`.
   */
  var roundIcon: ManifestIcon? = icon

  /**
   * The name of the theme resource that will be used in the `<application>` tag.
   */
  var themeRes: String = "AppTheme"

  /**
   * Whether the RTL flag should be set or not.
   */
  var rtl = true

  /**
   * Whether the manifest is for a library. When set to `true`, some flags
   * like [icon], [roundIcon], [rtl] are ignored.
   */
  var isLibrary = false

  /**
   * Adds the given permission to the manifest.
   */
  fun addPermission(permission: Permission) {
    permissions.add(permission)
  }

  /**
   * Adds the given activity to the manifest.
   */
  fun addActivity(activity: ManifestActivity) {
    activities.add(activity)
  }

  fun configure(type: ConfigurationType, configurator: IndentedXmlConfigurator
  ) {
    configurators.computeIfAbsent(type) { hashSetOf() }.add(configurator)
  }

  /**
   * Generates the manifest and saves the content to the given file.
   */
  fun RecipeExecutor.generate(manifest: File) {
    save(manifestSrc(), manifest)
  }

  private fun manifestSrc(): String {
    return IndentedXmlBuilder(autoIndent = true).apply {
      buildManifest()
    }.withXmlDecl()
  }

  private fun IndentedXmlBuilder.buildManifest() {
    createElement(TAG_MANIFEST) {
      attr(name = ANDROID_NS_NAME, value = ANDROID_URI, ns = XMLNS)
      configurators[MANIFEST_ATTR]?.forEach { configurator -> configurator() }
      closeStartElement()

      permissions()

      configurators[MANIFEST_CONTENT]?.forEach { configurator -> configurator() }

      application()
    }
  }

  private fun IndentedXmlBuilder.permissions() {
    if (permissions.isEmpty()) {
      return
    }

    for (permission in permissions) {
      createElement(TAG_USES_PERMISSION, selfClose = true) {
        androidAttr("name", permission.constant)
      }
    }
  }

  private fun IndentedXmlBuilder.application() {
    if (isLibrary) {
      return
    }

    createElement(TAG_APPLICATION) {
      androidAttr("allowBackup", "true")
      androidAttr("icon", icon.value())
      androidAttr("roundIcon", (roundIcon ?: icon).value())
      androidAttr("label", "@string/${appLabelRes}")
      androidAttr("supportsRtl", rtl.toString())

      if (themeRes.isNotBlank()) {
        androidAttr("theme", "@style/${themeRes}")
      }

      configurators[APPLICATION_ATTR]?.forEach { configurator -> configurator() }
      closeStartElement()

      activities()

      configurators[APPLICATION_CONTENT]?.forEach { configurator -> configurator() }
    }
  }

  private fun IndentedXmlBuilder.activities() {
    if (activities.isEmpty()) {
      return
    }

    for (activity in activities) {
      createElement(TAG_ACTIVITY) {
        androidAttr("name", activity.name(packageName))
        if (activity.isLauncher || activity.isExported) {
          androidAttr("exported", "true")
        }

        activity.configureAttrs?.invoke(this@activities)
        closeStartElement()

        if (activity.isLauncher) {
          intentFilter()
        }

        activity.configureInside?.invoke(this@activities)
      }
    }
  }

  private fun IndentedXmlBuilder.intentFilter() {
    createElement(TAG_INTENT_FILTER, closeStartTag = true) {
      // action
      createElement(TAG_ACTION, selfClose = true) {
        androidAttr("name", "android.intent.action.MAIN")
      }
      // category
      createElement(TAG_CATEGORY, selfClose = true) {
        androidAttr("name", "android.intent.category.LAUNCHER")
      }
    }
  }

  private fun IndentedXmlBuilder.androidAttr(name: String, value: String) {
    androidAttribute(name, value)
  }

  private fun IndentedXmlBuilder.attr(name: String, value: String,
                                      ns: String = ""
  ) {
    if (ns.isNotEmpty()) {
      addSingleAttribute("${ns}:${name}", value)
    } else {
      addSingleAttribute(name, value)
    }
  }

  private fun ManifestActivity.name(packageName: String?): String {
    packageName ?: return this.name
    return if (this.name.indexOf(packageName) == 0) {
      this.name.substring(packageName.length)
    } else {
      this.name
    }
  }
}
