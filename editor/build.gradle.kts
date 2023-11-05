plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version libs.versions.ksp
}

android {
    namespace = "${BuildConfig.packageName}.editor"
}

kapt {
    arguments {
        arg ("eventBusIndex", "${BuildConfig.packageName}.events.EditorEventsIndex")
    }
}

dependencies {
    ksp(projects.annotationProcessorsKsp)
    kapt(projects.annotationProcessors)
    
    api(libs.androidide.ts)
    api(libs.androidide.ts.java)
    api(libs.androidide.ts.json)
    api(libs.androidide.ts.kotlin)
    api(libs.androidide.ts.log)
    api(libs.androidide.ts.xml)
    api(libs.androidx.collection)
    api(libs.common.editor)
    
    api(projects.editorApi)
    api(projects.editorTreesitter)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.tracing)
    implementation(libs.androidx.tracing.ktx)

    implementation(libs.common.utilcode)
    
    implementation(libs.google.material)
    
    implementation(projects.actions)
    implementation(projects.annotations)
    implementation(projects.common)
    implementation(projects.eventbusAndroid)
    implementation(projects.eventbusEvents)
    implementation(projects.lexers)
    implementation(projects.shared)
    implementation(projects.resources)
    
    implementation(projects.lsp.api)
    implementation(projects.lsp.java)
    implementation(projects.lsp.xml)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
