plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.itsaky.androidide.build")
}

android {
    namespace = "com.github.appintro"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.fragment.ktx)
}