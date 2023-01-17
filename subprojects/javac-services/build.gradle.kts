import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.itsaky.androidide.javac.services"
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    
    // Must be set to Java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
    implementation(libs.google.guava)
    implementation(projects.common)
    implementation(projects.logger)
    
    api(projects.subprojects.javac)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}