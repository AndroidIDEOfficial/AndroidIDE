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
package com.itsaky.androidide.tooling.impl.sync

import com.android.builder.model.v2.models.Versions
import com.itsaky.androidide.tooling.impl.Main
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.LogUtils
import org.gradle.tooling.BuildController
import org.gradle.tooling.model.Model

/**
 * Abstract class for [IModelBuilder] implementations.
 *
 * @property androidVariant The name of the variant for which the Android models will be built.
 * @author Akash Yadav
 */
abstract class AbstractModelBuilder<P, R>(protected val androidVariant: String = VARIANT_DEBUG) :
  IModelBuilder<P, R> {

  companion object {

    /**
     * Default variant name used for [AbstractModelBuilder.androidVariant].
     */
    const val VARIANT_DEBUG = "debug"

    /**
     * Checks the Android Gradle Plugin version from the given [Versions] model and compares
     * it with [Main.MIN_SUPPORTED_AGP_VERSION]. If the version is less than the [Main.MIN_SUPPORTED_AGP_VERSION],
     * throws an [UnsupportedOperationException].
     *
     * @param versions The [Versions] model.
     */
    @JvmStatic
    protected fun assertMinimumAgp(versions: Versions) {
      if (versions.agp < Main.MIN_SUPPORTED_AGP_VERSION) {
        throw ModelBuilderException(
          "Android Gradle Plugin version "
              + versions.agp
              + " is not supported by AndroidIDE. "
              + "Please update your project to use at least v"
              + Main.MIN_SUPPORTED_AGP_VERSION
              + " of Android Gradle Plugin to build this project.")
      }
    }

    /**
     * Get the [Versions] information about Android projects. This returns `null` if
     * the project is not an Android project.
     *
     * @param model      The model element, usually a project.
     * @param controller The build controller that is used for finding the model.
     * @return The [Versions] model if available, `null` otherwise.
     */
    @JvmStatic
    protected fun getAndroidVersions(model: Model, controller: BuildController): Versions? {
      return controller.findModel(model, Versions::class.java)
    }

    @JvmStatic
    protected fun log(vararg objects: Any?) {
      buildLog(*objects)
    }

    /**
     * Logs the given objects to the error stream.
     *
     * @param objects The objects to log.
     */
    protected fun buildLog(vararg objects: Any?) {
      System.err.println(generateMessage(*objects))
    }

    /**
     * Generates the log message for the given objects. This works similar to
     * [ generateMessage(Object...)][com.itsaky.androidide.utils.ILogger.generateMessage] in [ILogger][com.itsaky.androidide.utils.ILogger].
     *
     * @param objects The objects to print in the message.
     * @return The generated message.
     */
    protected fun generateMessage(vararg objects: Any?): String {
      val sb = StringBuilder()
      for (msg in objects) {
        sb.append(if (msg is Throwable) "\n" else ILogger.MSG_SEPARATOR)
        sb.append(if (msg is Throwable) LogUtils.getFullStackTrace(
          msg as Throwable?) else msg)
      }
      return sb.toString()
    }
  }
}