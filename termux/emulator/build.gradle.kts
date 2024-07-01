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


import com.itsaky.androidide.build.config.BuildConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}



android {
    namespace = "com.termux.emulator"
    ndkVersion = BuildConfig.ndkVersion

    defaultConfig {
        externalNativeBuild {
            ndkBuild {
                cFlags += arrayOf("-std=c11", "-Wall", "-Wextra", "-Werror", "-Os", "-fno-stack-protector", "-Wl,--gc-sections")
            }
        }
    }

    externalNativeBuild {
        ndkBuild {
            path = file("src/main/jni/Android.mk")
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

tasks.withType(Test::class.java) {
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    testImplementation(projects.testing.unitTest)
}
