plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.termux.view"
    ndkVersion = BuildConfig.ndkVersion
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(projects.resources)
    api(projects.termux.termuxEmulator)

    testImplementation(projects.testing.unit)
}