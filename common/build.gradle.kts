plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "${BuildConfig.packageName}.common"
}

dependencies {
    implementation(libs.common.editor)
    implementation(libs.common.lang3)
    implementation(libs.common.utilcode)
    implementation(libs.google.guava)
    implementation(libs.google.material)
    
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.vectors)
    implementation(libs.androidx.animated.vectors)
    
    implementation(libs.androidx.ktx)
    implementation(libs.common.kotlin)
    
    implementation(projects.buildInfo)
    implementation(projects.eventbusEvents)
    implementation(projects.lexers)
    implementation(projects.resources)
    
    api(projects.shared)
    api(projects.logger)
    api(projects.subprojects.flashbar)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
