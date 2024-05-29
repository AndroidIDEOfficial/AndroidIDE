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

package com.itsaky.androidide.templates.base.modules.android

import com.itsaky.androidide.templates.Language.Kotlin
import com.itsaky.androidide.templates.ModuleType
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.ModuleTemplateBuilder
import com.itsaky.androidide.templates.base.modules.dependencies

private const val compose_kotlinCompilerExtensionVersion = "1.3.2"

private val AndroidModuleTemplateBuilder.androidPlugin: String
  get() {
    return if (data.type == ModuleType.AndroidLibrary) "com.android.library"
    else "com.android.application"
  }

fun AndroidModuleTemplateBuilder.buildGradleSrc(isComposeModule: Boolean
): String {
  return if (data.useKts) buildGradleSrcKts(
    isComposeModule) else buildGradleSrcGroovy(isComposeModule)
}

private fun AndroidModuleTemplateBuilder.buildGradleSrcKts(
  isComposeModule: Boolean
): String {
  return """
plugins {
    id("$androidPlugin")
    ${ktPlugin()}
}

android {
    namespace = "${data.packageName}"
    compileSdk = ${data.versions.compileSdk.api}
    
    defaultConfig {
        applicationId = "${data.packageName}"
        minSdk = ${data.versions.minSdk.api}
        targetSdk = ${data.versions.targetSdk.api}
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = ${data.versions.javaSource()}
        targetCompatibility = ${data.versions.javaTarget()}
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        ${if (!isComposeModule) "viewBinding = true" else ""}
        ${if (isComposeModule) "compose = true" else ""}
    }
    ${if(isComposeModule) composeConfigKts() else ""}
}
${ktJvmTarget()}
${dependencies()}
"""
}

private fun AndroidModuleTemplateBuilder.buildGradleSrcGroovy(
  isComposeModule: Boolean
): String {
  return """
plugins {
    id '$androidPlugin'
    ${ktPlugin()}
}

android {
    namespace '${data.packageName}'
    compileSdk ${data.versions.compileSdk.api}
    
    defaultConfig {
        applicationId "${data.packageName}"
        minSdk ${data.versions.minSdk.api}
        targetSdk ${data.versions.targetSdk.api}
        versionCode 1
        versionName "1.0"
        
        vectorDrawables { 
            useSupportLibrary true
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility ${data.versions.javaSource()}
        targetCompatibility ${data.versions.javaTarget()}
    }

    buildFeatures {
        ${if (!isComposeModule) "viewBinding true" else ""}
        ${if (isComposeModule) "compose true" else ""}
    }
    ${if(isComposeModule) composeConfigGroovy() else ""}
}
${ktJvmTarget()}
${dependencies()}
"""
}

fun composeConfigGroovy(): String
= """
    composeOptions {
        kotlinCompilerExtensionVersion '$compose_kotlinCompilerExtensionVersion'
    }
    packagingOptions {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
""".trim()

fun composeConfigKts(): String
  = """
    composeOptions {
        kotlinCompilerExtensionVersion = "$compose_kotlinCompilerExtensionVersion"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
""".trim()

private fun ModuleTemplateBuilder.ktJvmTarget(): String {
  if (data.language != Kotlin) {
    return ""
  }

  return if (data.useKts) ktJvmTargetKts() else ktJvmTargetGroovy()
}

private fun ModuleTemplateBuilder.ktJvmTargetKts(): String {
  return """
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "${data.versions.javaTarget}"
}
"""
}

private fun ModuleTemplateBuilder.ktJvmTargetGroovy(): String {
  return """
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
  kotlinOptions {
    jvmTarget = "${data.versions.javaTarget}"
  }
}
"""
}

private fun AndroidModuleTemplateBuilder.ktPlugin(): String {
  if (data.language != Kotlin) {
    return ""
  }

  return if (data.useKts) ktPluginKts() else ktPluginGroovy()
}

private fun ktPluginKts(): String {
  return """id("kotlin-android")"""
}

private fun ktPluginGroovy(): String {
  return "id 'kotlin-android'"
}
