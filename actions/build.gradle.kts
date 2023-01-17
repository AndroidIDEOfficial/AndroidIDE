plugins{
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.itsaky.androidide.actions"
}

dependencies {
    implementation(projects.common)
    implementation(projects.resources)
    implementation(libs.common.editor)
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
    
    implementation(libs.androidx.ktx)
    implementation(libs.google.material)
}