import com.itsaky.androidide.build.config.BuildConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

groupConfig {
    groupIdSuffix.set("core")
}

android {
    namespace = "${BuildConfig.packageName}.lsp.models"
}

dependencies {
    implementation(libs.composite.fuzzysearch)

    implementation(projects.core.common)

    implementation(libs.common.editor)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
}