plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
}

android {
  namespace = "${BuildConfig.packageName}.uidesigner"
}

dependencies {
  
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.nav.fragment)
  implementation(libs.androidx.nav.ui)
  implementation(libs.common.editor)
  implementation(libs.common.kotlin)
  implementation(libs.common.utilcode)
  implementation(libs.google.material)

  implementation(projects.actions)
  implementation(projects.annotations)
  implementation(projects.common)
  implementation(projects.editor)
  implementation(projects.logger)
  implementation(projects.lookup)
  implementation(projects.lsp.api)
  implementation(projects.lsp.xml)
  implementation(projects.resources)
  implementation(projects.xmlInflater)

  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.robolectric)
  testImplementation(libs.tests.mockito.kotlin)
}
