import com.itsaky.androidide.build.config.BuildConfig

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
}

groupConfig {
  groupIdSuffix.set("utils")
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

  implementation(projects.annotation.annotations)
  implementation(projects.core.actions)
  implementation(projects.core.common)
  implementation(projects.core.lspApi)
  implementation(projects.core.resources)
  implementation(projects.editor.impl)
  implementation(projects.logging.logger)
  implementation(projects.utilities.lookup)
  implementation(projects.utilities.xmlInflater)
  implementation(projects.xml.lsp)

  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.robolectric)
  testImplementation(libs.tests.mockito.kotlin)
}
