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

    implementation(projects.common)
    implementation(projects.resources)
    implementation(libs.common.editor)
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
    implementation(libs.google.auto.service.annotations)
    
    implementation(libs.androidx.ktx)
    implementation(libs.google.material)
}