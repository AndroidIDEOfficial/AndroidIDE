import com.itsaky.androidide.build.config.BuildConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

groupConfig {
    groupIdSuffix.set("java")
}

android {
    namespace = "${BuildConfig.packageName}.javac.services"
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    api(libs.composite.javac)

    implementation(libs.common.kotlin)
    implementation(libs.common.utilcode)
    implementation(libs.google.guava)

    implementation(projects.core.common)
    implementation(projects.logging.logger)
    
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}