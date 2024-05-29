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

package com.itsaky.androidide.templates.base.root

import com.itsaky.androidide.templates.base.ProjectTemplateBuilder
import java.io.File

internal fun ProjectTemplateBuilder.gradleWrapperProps() {
  val name = "gradle-wrapper.properties"
  val wrapperProps = File(data.projectDir, "gradle/wrapper/${name}")
  wrapperProps.parentFile!!.mkdirs()

  executor.save(gradleWrapperPropsSrc(), wrapperProps)
}

internal fun ProjectTemplateBuilder.gradleWrapperPropsSrc(): String {
  return """
    distributionBase=GRADLE_USER_HOME
    distributionPath=wrapper/dists
    distributionUrl=https\://services.gradle.org/distributions/gradle-${data.version.gradle}-bin.zip
    networkTimeout=10000
    zipStoreBase=GRADLE_USER_HOME
    zipStorePath=wrapper/dists
  """.trimIndent()
}