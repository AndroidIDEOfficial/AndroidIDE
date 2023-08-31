@file:Suppress("UnstableApiUsage")

import androidx.navigation.safe.args.generator.ext.capitalize
import com.android.build.gradle.BaseExtension
import java.util.Base64
import java.util.Locale

plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("com.google.android.gms.oss-licenses-plugin")
  id("androidx.navigation.safeargs.kotlin")
}

android {
  namespace = BuildConfig.packageName

  defaultConfig {
    applicationId = BuildConfig.packageName
    vectorDrawables.useSupportLibrary = true
  }

  compileOptions { isCoreLibraryDesugaringEnabled = true }

  downloadSigningKey()

  // Keystore credentials
  val alias = getEnvOrProp(KEY_ALIAS)
  val storePass = getEnvOrProp(KEY_STORE_PASS)
  val keyPass = getEnvOrProp(KEY_PASS)

  if (alias != null && storePass != null && keyPass != null && signingKey.exists()) {
    signingConfigs.create("common") {
      storeFile = signingKey
      keyAlias = alias
      storePassword = storePass
      keyPassword = keyPass
    }

    buildTypes {
      debug { signingConfig = signingConfigs.getByName("common") }
      release { signingConfig = signingConfigs.getByName("common") }
    }
  } else {
    logger.warn(
      "Signing info not configured. keystoreFile=$signingKey[exists=${signingKey.exists()}]"
    )
  }

  buildTypes { release { isShrinkResources = true } }

  packaging {
    resources.excludes.addAll(
      arrayOf(
        "META-INF/eclipse.inf",
        "META-INF/CHANGES",
        "META-INF/README.md",
        "about_files/LICENSE-2.0.txt",
        "META-INF/AL2.0",
        "META-INF/LGPL2.1",
        "plugin.xml",
        "plugin.properties",
        "about.mappings",
        "about.properties",
        "about.ini",
        "modeling32.png"
      )
    )
  }

  lint {
    abortOnError = false
    disable.addAll(arrayOf("VectorPath", "NestedWeights", "ContentDescription", "SmallSp"))
  }
}

kapt { arguments { arg("eventBusIndex", "${BuildConfig.packageName}.events.AppEventsIndex") } }

dependencies {
  debugImplementation(libs.common.leakcanary)

  // Annotation processors
  kapt(libs.common.glide.ap)
  kapt(libs.google.auto.service)
  kapt(projects.annotationProcessors)

  implementation(libs.common.editor)
  implementation(libs.common.utilcode)
  implementation(libs.common.glide)
  implementation(libs.common.jsoup)

  implementation(libs.google.auto.service.annotations)
  implementation(libs.google.gson)
  implementation(libs.google.guava)

  // Git
  implementation(libs.git.jgit)

  // AndroidX
  implementation(libs.androidx.splashscreen)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.coordinatorlayout)
  implementation(libs.androidx.drawer)
  implementation(libs.androidx.grid)
  implementation(libs.androidx.nav.fragment)
  implementation(libs.androidx.nav.ui)
  implementation(libs.androidx.preference)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.transition)
  implementation(libs.androidx.vectors)
  implementation(libs.androidx.animated.vectors)
  implementation(libs.androidx.work)
  implementation(libs.androidx.work.ktx)
  implementation(libs.google.material)
  implementation(libs.google.flexbox)
  implementation(libs.google.oss.licenses)

  // Kotlin
  implementation(libs.androidx.ktx)
  implementation(libs.common.kotlin)

  // Local projects here
  implementation(projects.actions)
  implementation(projects.buildInfo)
  implementation(projects.common)
  implementation(projects.editor)
  implementation(projects.emulatorview)
  implementation(projects.eventbus)
  implementation(projects.eventbusAndroid)
  implementation(projects.eventbusEvents)
  implementation(projects.idestats)
  implementation(projects.subprojects.aaptcompiler)
  implementation(projects.subprojects.javacServices)
  implementation(projects.subprojects.javapoet)
  implementation(projects.subprojects.xmlUtils)
  implementation(projects.subprojects.projects)
  implementation(projects.subprojects.toolingApi)
  implementation(projects.logsender)
  implementation(projects.lsp.api)
  implementation(projects.lsp.java)
  implementation(projects.lsp.xml)
  implementation(projects.lexers)
  implementation(projects.lookup)
  implementation(projects.preferences)
  implementation(projects.resources)
  implementation(projects.treeview)
  implementation(projects.templatesApi)
  implementation(projects.templatesImpl)
  implementation(projects.uidesigner)
  implementation(projects.xmlInflater)

  coreLibraryDesugaring(libs.androidx.lib.desugaring)

  // This is to build the tooling-api-impl project before the app is built
  // So we always copy the latest JAR file to assets
  compileOnly(projects.subprojects.toolingApiImpl)

  testImplementation(libs.common.editor)
  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
  testImplementation(libs.tests.robolectric)
  androidTestImplementation(libs.tests.androidx.junit)
  androidTestImplementation(libs.tests.androidx.espresso)
  androidTestImplementation(libs.tests.google.truth)
}

fun downloadSigningKey() {
  if (signingKey.exists()) {
    logger.info("Skipping download as ${signingKey.name} file already exists.")
    return
  }

  getEnvOrProp(KEY_BIN)?.let { bin ->
    logger.info("Using $KEY_BIN for writing signing key")
    val contents = Base64.getDecoder().decode(bin)
    signingKey.writeBytes(contents)
    return
  }

  // URL to download the signing key
  val url = getEnvOrProp(KEY_URL) ?: return

  // Username and password required to download the keystore
  val user = getEnvOrProp(AUTH_USER) ?: return
  val pass = getEnvOrProp(AUTH_PASS) ?: return

  logger.info("Downloading signing key...")
  val result = exec {
    workingDir(rootProject.projectDir)
    commandLine("bash", "./.tools/download_key.sh", signingKey.absolutePath, url, user, pass)
  }

  result.assertNormalExitValue()
}

fun getEnvOrProp(key: String): String? {
  var value: String? = System.getenv(key)
  if (value.isNullOrBlank()) {
    value = project.properties[key] as? String?
  }
  if (value.isNullOrBlank()) {
    logger.warn("$key is not set. Debug key will be used to sign the APK")
    return null
  }
  return value
}

tasks.create("generateInitScript") {
  val out = file("src/main/assets/data/common/androidide.init.gradle")

  if (out.exists()) {
    out.delete()
  }

  doLast {
    out.bufferedWriter().use {
      it.write(
        """
      initscript {
          repositories {
              maven {
                  mavenCentral()
                  google()

                  // Add snapshots repository for AndroidIDE CI builds
                  url "https://s01.oss.sonatype.org/content/repositories/snapshots/"
              }
          }

          dependencies {
              classpath '${BuildConfig.packageName}:gradle-plugin:${downloadVersion}'
          }
      }
      
      apply plugin: com.itsaky.androidide.gradle.AndroidIDEInitScriptPlugin
    """
          .trimIndent()
      )
    }
  }
}

afterEvaluate {
  extensions.getByType<BaseExtension>().apply {
    val flavorNames = productFlavors.map { it.name.capitalize(Locale.getDefault()) }
    val dependents =
      listOf(
        "merge@@DebugAssets",
        "merge@@ReleaseAssets",
        "lintAnalyze@@Debug",
        "lintAnalyze@@Release",
        "lintVitalAnalyze@@Release"
      )

    for (dependent in dependents) {
      for (flavorName in flavorNames) {
        tasks.getByName(dependent.replace("@@", flavorName)).apply {
          dependsOn(":subprojects:tooling-api-impl:copyJarToAssets")
          dependsOn("generateInitScript")
        }
      }
    }
  }
}
