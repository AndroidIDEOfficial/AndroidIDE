import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
  id("kotlin-kapt")
}

groupConfig {
  groupIdSuffix.set("core")
}

android {
  namespace = "${BuildConfig.packageName}.projects"
}

kapt {
  arguments {
    arg("eventBusIndex", "${BuildConfig.packageName}.events.ProjectsApiEventsIndex")
  }
}

dependencies {

  kapt(projects.annotation.processors)
  kapt(libs.google.auto.service)

  api(projects.event.eventbus)
  api(projects.event.eventbusEvents)
  api(projects.tooling.api)

  implementation(projects.core.common)
  implementation(projects.java.javacServices)
  implementation(projects.logging.logger)
  implementation(projects.utilities.lookup)
  implementation(projects.utilities.shared)
  implementation(projects.xml.utils)

  implementation(libs.common.io)
  implementation(libs.common.kotlin.coroutines.android)
  implementation(libs.google.auto.service.annotations)
  implementation(libs.google.guava)

  testImplementation(projects.testing.gradleToolingTest)
}