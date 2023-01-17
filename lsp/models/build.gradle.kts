plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.itsaky.androidide.lsp.models"
}

dependencies {
    implementation(libs.common.editor)
    implementation(projects.common)
    implementation(projects.subprojects.fuzzysearch)
    
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.ktx)
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
}