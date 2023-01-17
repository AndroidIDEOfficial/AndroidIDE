plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.itsaky.androidide.projects"
}

kapt {
    arguments {
        arg ("eventBusIndex", "com.itsaky.androidide.events.ProjectsApiEventsIndex")
    }
}

dependencies {
    
    kapt(libs.common.eventbus.ap)
    
    api(projects.eventbus)
    api(projects.eventbusEvents)
    api(projects.subprojects.toolingApi)
    
    implementation(projects.common)
    implementation(projects.logger)
    implementation(projects.lookup)
    implementation(projects.shared)
    implementation(projects.subprojects.javacServices)
    implementation(projects.subprojects.xmlUtils)
    
    implementation(libs.common.io)
    implementation(libs.google.guava)
    
    testImplementation(projects.subprojects.toolingApiTesting)
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
    androidTestImplementation(libs.tests.androidx.junit)
    androidTestImplementation(libs.tests.androidx.espresso)
    androidTestImplementation(libs.tests.google.truth)
}