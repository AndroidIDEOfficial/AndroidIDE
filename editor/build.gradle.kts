import com.itsaky.androidide.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
}

android {
  namespace = "com.itsaky.androidide.editor"
  compileSdk = Versions.compileSdk
  buildToolsVersion = Versions.buildTools
  
  defaultConfig {
    minSdk = Versions.minSdk
    targetSdk = Versions.targetSdk
  }
  
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  
  compileOptions {
    sourceCompatibility = Versions.javaVersion
    targetCompatibility = Versions.javaVersion
  }
  
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {
  implementation(project(path = ":common"))
  implementation(project(path = ":shared"))
  implementation(project(path = ":lsp:api"))
}