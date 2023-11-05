plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "${BuildConfig.packageName}.common"
}

dependencies {
    api(libs.common.editor)
    api(libs.common.lang3)
    api(libs.common.utilcode)
    api(libs.google.guava)
    api(libs.google.material)
    
    api(libs.androidx.appcompat)
    api(libs.androidx.collection)
    api(libs.androidx.preference)
    api(libs.androidx.vectors)
    api(libs.androidx.animated.vectors)
    
    api(libs.androidx.ktx)
    api(libs.common.kotlin)
    
    api(projects.buildInfo)
    api(projects.eventbusEvents)
    api(projects.lexers)
    api(projects.resources)
    
    api(projects.shared)
    api(projects.logger)
    api(projects.subprojects.flashbar)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
