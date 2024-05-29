import com.itsaky.androidide.build.config.BuildConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version libs.versions.ksp
}

groupConfig {
    groupIdSuffix.set("core")
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
    ksp(projects.annotation.processorsKsp)
    kapt(projects.annotation.processors)
    
    api(libs.androidide.ts)
    api(libs.androidide.ts.java)
    api(libs.androidide.ts.json)
    api(libs.androidide.ts.kotlin)
    api(libs.androidide.ts.log)
    api(libs.androidide.ts.xml)
    api(libs.androidx.collection)
    api(libs.common.editor)
    
    api(projects.editor.api)
    api(projects.editor.treesitter)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.tracing)
    implementation(libs.androidx.tracing.ktx)

    implementation(libs.common.utilcode)
    
    implementation(libs.google.material)

    implementation(projects.annotation.annotations)
    implementation(projects.core.actions)
    implementation(projects.core.common)
    implementation(projects.core.lspApi)
    implementation(projects.core.resources)
    implementation(projects.editor.lexers)
    implementation(projects.event.eventbusAndroid)
    implementation(projects.event.eventbusEvents)
    implementation(projects.java.lsp)
    implementation(projects.utilities.shared)
    implementation(projects.xml.lsp)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
