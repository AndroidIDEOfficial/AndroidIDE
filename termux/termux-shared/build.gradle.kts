plugins {
    id ("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.termux.shared"
    ndkVersion = BuildConfig.ndkVersion

    externalNativeBuild {
        ndkBuild {
            path = file("src/main/cpp/Android.mk")
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.androidx.window.v1alpha9)
    implementation(libs.common.markwon.core)
    implementation(libs.common.markwon.extStrikethrough)
    implementation(libs.common.markwon.linkify)
    implementation(libs.common.markwon.recycler)
    implementation(libs.google.material)
    implementation(libs.google.guava)
    implementation(libs.common.hiddenApiBypass)

    // Do not increment version higher than 1.0.0-alpha09 since it will break ViewUtils and needs to be looked into
    // noinspection GradleDependency
    implementation(libs.common.io)
    implementation(libs.common.termuxAmLib)

    implementation(projects.common)
    implementation(projects.termux.termuxView)
    implementation(projects.buildInfo)
    implementation(projects.preferences)
    implementation(projects.resources)

    testImplementation(projects.testing.unit)
    testImplementation(projects.testing.android)
}
