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

plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}



android {
    namespace = "${BuildConfig.packageName}.actions"
}

dependencies {
    kapt(libs.google.auto.service)

    api(libs.androidx.nav.fragment)
    api(libs.androidx.nav.ui)

    implementation(projects.core.common)
    implementation(projects.core.resources)
    implementation(libs.common.editor)
    implementation(libs.common.kotlin)
    implementation(libs.common.kotlin.coroutines.android)
    implementation(libs.common.utilcode)
    implementation(libs.google.auto.service.annotations)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.material)
}