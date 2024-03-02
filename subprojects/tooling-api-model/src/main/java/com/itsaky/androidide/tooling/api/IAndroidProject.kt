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

package com.itsaky.androidide.tooling.api

import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.models.AndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.BasicAndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * Model for an Android project/module.
 *
 * @author Akash Yadav
 */
@JsonSegment("android")
interface IAndroidProject : IModuleProject {

  /**
   * Get the variant that was configured/selected while building the model for this project.
   */
  @JsonRequest
  fun getConfiguredVariant(): CompletableFuture<String>

  /**
   * Get the metadata about all variants of this Android project.
   */
  @JsonRequest
  fun getVariants(): CompletableFuture<List<BasicAndroidVariantMetadata>>

  /**
   * Get the metadata about the variant with the given name.
   */
  @JsonRequest
  fun getVariant(param: StringParameter): CompletableFuture<AndroidVariantMetadata?>

  /**
   * Get the boot classpaths for this Android project.
   */
  @JsonRequest
  fun getBootClasspaths(): CompletableFuture<Collection<File>>

  /**
   * Get the map of libraries. Each entry is a unique key representing the library, and allowing
   * to match it with GraphItem instances.
   *
   * @param variant The name of the variant for which the libraries will be fetched.
   */
  @JsonRequest
  fun getLibraryMap(): CompletableFuture<Map<String, DefaultLibrary>>

  /**
   * Get the main source set container for this project.
   *
   * @return The main source set container or `null` if it is unavailable.
   */
  @JsonRequest
  fun getMainSourceSet(): CompletableFuture<DefaultSourceSetContainer?>

  /**
   * Get the lint check jars for this project.
   */
  @JsonRequest
  fun getLintCheckJars(): CompletableFuture<List<File>>

  @Suppress("unused")
  companion object {

    /**
     * The name of the Android project build variant that is used by default.
     */
    const val DEFAULT_VARIANT = "debug"

    const val ANDROID_NAMESPACE = "http://schemas.android.com/res/android"

    //  Injectable properties to use with -P
    // Sent by Studio 4.2+
    const val PROPERTY_BUILD_MODEL_ONLY = "android.injected.build.model.v2"

    // Sent by Studio 2.2+ and Android Support plugin running with IDEA from 4.1+
    // This property will enable compatibility checks between Android Support plugin and the
    // Android
    // Gradle plugin.
    // A use case for this property is that by restricting which versions are compatible
    // with the plugin, we could safely remove deprecated methods in the builder-model
    // interfaces.
    const val PROPERTY_ANDROID_SUPPORT_VERSION = "android.injected.studio.version"

    // Sent in when external native projects models requires a refresh.
    const val PROPERTY_REFRESH_EXTERNAL_NATIVE_MODEL =
      "android.injected.refresh.external.native.model"

    // Sent by Studio 2.2+
    // This property is sent when a run or debug is invoked.  APK built with this property
    // should
    // be marked with android:testOnly="true" in the AndroidManifest.xml such that it will be
    // rejected by the Play store.
    const val PROPERTY_TEST_ONLY = "android.injected.testOnly"

    // Sent by Studio 1.5+
    // The version api level of the target device.
    const val PROPERTY_BUILD_API = "android.injected.build.api"

    // The version codename of the target device. Null for released versions,
    const val PROPERTY_BUILD_API_CODENAME = "android.injected.build.codename"
    const val PROPERTY_BUILD_ABI = "android.injected.build.abi"
    const val PROPERTY_BUILD_DENSITY = "android.injected.build.density"

    // Has the effect of telling the Gradle plugin to
    //   1) Generate machine-readable errors
    //   2) Generate build metadata JSON files
    const val PROPERTY_INVOKED_FROM_IDE = "android.injected.invoked.from.ide"
    const val PROPERTY_SIGNING_STORE_FILE = "android.injected.signing.store.file"
    const val PROPERTY_SIGNING_STORE_PASSWORD = "android.injected.signing.store.password"
    const val PROPERTY_SIGNING_KEY_ALIAS = "android.injected.signing.key.alias"
    const val PROPERTY_SIGNING_KEY_PASSWORD = "android.injected.signing.key.password"
    const val PROPERTY_SIGNING_STORE_TYPE = "android.injected.signing.store.type"
    const val PROPERTY_SIGNING_V1_ENABLED = "android.injected.signing.v1-enabled"
    const val PROPERTY_SIGNING_V2_ENABLED = "android.injected.signing.v2-enabled"
    const val PROPERTY_DEPLOY_AS_INSTANT_APP = "android.injected.deploy.instant-app"
    const val PROPERTY_SIGNING_COLDSWAP_MODE = "android.injected.coldswap.mode"
    const val PROPERTY_APK_SELECT_CONFIG = "android.inject.apkselect.config"
    const val PROPERTY_EXTRACT_INSTANT_APK = "android.inject.bundle.extractinstant"

    /** Version code to be used in the built APK. */
    const val PROPERTY_VERSION_CODE = "android.injected.version.code"

    /** Version name to be used in the built APK. */
    const val PROPERTY_VERSION_NAME = "android.injected.version.name"

    /**
     * Location for APKs. If defined as a relative path, then it is resolved against the project's
     * path.
     */
    const val PROPERTY_APK_LOCATION = "android.injected.apk.location"

    /**
     * Location of the build attribution file produced by the gradle plugin to be deserialized and
     * used in the IDE build attribution.
     */
    const val PROPERTY_ATTRIBUTION_FILE_LOCATION = "android.injected.attribution.file.location"

    /**
     * Comma separated list of on-demand dynamic modules or instant app modules names that are
     * selected by the user for installation on the device during deployment.
     */
    const val PROPERTY_INJECTED_DYNAMIC_MODULES_LIST = "android.injected.modules.install.list"

    const val FD_INTERMEDIATES = "intermediates"
    const val FD_LOGS = "logs"
    const val FD_OUTPUTS = "outputs"
    const val FD_GENERATED = "generated"
  }
}