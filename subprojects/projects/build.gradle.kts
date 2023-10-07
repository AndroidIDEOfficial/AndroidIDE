plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
  id("kotlin-kapt")
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

  kapt(projects.annotationProcessors)
  kapt(libs.google.auto.service)

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
  implementation(libs.common.kotlin.coroutines.android)
  implementation(libs.google.auto.service.annotations)
  implementation(libs.google.guava)

  testImplementation(projects.testing.tooling)
}