plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.itsaky.androidide.editor"
}

dependencies {
    
    api(libs.androidide.ts)
    api(libs.androidide.ts.java)
    api(libs.androidide.ts.xml)
    api(libs.common.editor)
    api(libs.common.editor.ts)
    
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    
    implementation(libs.common.utilcode)
    
    implementation(libs.google.material)
    
    implementation(projects.actions)
    implementation(projects.common)
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