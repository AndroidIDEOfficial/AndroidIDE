plugins {
    id("com.android.library")
}

android {
    namespace = "com.itsaky.terminal.view"
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(projects.resources)
}
