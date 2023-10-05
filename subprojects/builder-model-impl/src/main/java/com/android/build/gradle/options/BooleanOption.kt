/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.build.gradle.options

import com.android.build.gradle.options.DeprecationTarget.BUILD_CONFIG_GLOBAL_PROPERTY
import com.android.build.gradle.options.DeprecationTarget.VERSION_9_0
import com.android.build.gradle.options.Version.VERSION_3_5
import com.android.build.gradle.options.Version.VERSION_3_6
import com.android.build.gradle.options.Version.VERSION_4_0
import com.android.build.gradle.options.Version.VERSION_4_1
import com.android.build.gradle.options.Version.VERSION_4_2
import com.android.build.gradle.options.Version.VERSION_7_0
import com.android.build.gradle.options.Version.VERSION_7_2
import com.android.build.gradle.options.Version.VERSION_7_3
import com.android.build.gradle.options.Version.VERSION_BEFORE_4_0
import com.android.builder.model.AndroidProject
import com.android.builder.model.AndroidProject.PROPERTY_BUILD_MODEL_ONLY
import com.android.builder.model.PROPERTY_BUILD_MODEL_V2_ONLY
import com.android.builder.model.PROPERTY_BUILD_WITH_STABLE_IDS
import com.android.builder.model.PROPERTY_DEPLOY_AS_INSTANT_APP
import com.android.builder.model.PROPERTY_EXTRACT_INSTANT_APK
import com.android.builder.model.PROPERTY_INVOKED_FROM_IDE
import com.android.builder.model.PROPERTY_REFRESH_EXTERNAL_NATIVE_MODEL

enum class BooleanOption(
    override val propertyName: String,
    override val defaultValue: Boolean,
    val stage: Stage
) : Option<Boolean> {

    /* ----------
     * STABLE API
     */

    // IDE properties
    IDE_INVOKED_FROM_IDE(PROPERTY_INVOKED_FROM_IDE, false, ApiStage.Stable),
    IDE_BUILD_MODEL_ONLY_V2(PROPERTY_BUILD_MODEL_V2_ONLY, false, ApiStage.Stable),
    @Deprecated("This is for model v1 only. Please also use IDE_BUILD_MODEL_ONLY_V2")
    IDE_BUILD_MODEL_ONLY(PROPERTY_BUILD_MODEL_ONLY, false, ApiStage.Stable),
    @Deprecated("Use IDE_BUILD_MODEL_ONLY_V2")
    IDE_BUILD_MODEL_ONLY_ADVANCED(AndroidProject.PROPERTY_BUILD_MODEL_ONLY_ADVANCED, false, ApiStage.Stable),
    @Deprecated("Use IDE_BUILD_MODEL_ONLY_V2")
    IDE_BUILD_MODEL_FEATURE_FULL_DEPENDENCIES(AndroidProject.PROPERTY_BUILD_MODEL_FEATURE_FULL_DEPENDENCIES, false, ApiStage.Stable),
    IDE_REFRESH_EXTERNAL_NATIVE_MODEL(PROPERTY_REFRESH_EXTERNAL_NATIVE_MODEL, false, ApiStage.Stable),
    //IDE_GENERATE_SOURCES_ONLY(AndroidProject.PROPERTY_GENERATE_SOURCES_ONLY, false, ApiStage.Stable),

    // tell bundletool to only extract instant APKs.
    IDE_EXTRACT_INSTANT(PROPERTY_EXTRACT_INSTANT_APK, false, ApiStage.Stable),

    // Flag used to indicate a "deploy as instant" run configuration.
    IDE_DEPLOY_AS_INSTANT_APP(PROPERTY_DEPLOY_AS_INSTANT_APP, false, ApiStage.Stable),

    ENABLE_STUDIO_VERSION_CHECK("android.injected.studio.version.check", true, ApiStage.Stable),
    ENABLE_STABLE_IDS(PROPERTY_BUILD_WITH_STABLE_IDS, false, ApiStage.Stable),

    // Features' default values
    BUILD_FEATURE_DATABINDING("android.defaults.buildfeatures.databinding", false, ApiStage.Stable),
    BUILD_FEATURE_RESVALUES("android.defaults.buildfeatures.resvalues", true, ApiStage.Stable),
    BUILD_FEATURE_SHADERS("android.defaults.buildfeatures.shaders", true, ApiStage.Stable),
    BUILD_FEATURE_VIEWBINDING("android.defaults.buildfeatures.viewbinding", false, ApiStage.Stable),
    BUILD_FEATURE_ANDROID_RESOURCES("android.library.defaults.buildfeatures.androidresources", true, ApiStage.Stable),

    // DSLs default values
    ENABLE_DATABINDING_KTX("android.defaults.databinding.addKtx", true, ApiStage.Stable),

    // AndroidX & Jetifier
    USE_ANDROID_X("android.useAndroidX", false, ApiStage.Stable),
    ENABLE_JETIFIER("android.enableJetifier", false, ApiStage.Stable),

    DEBUG_OBSOLETE_API("android.debug.obsoleteApi", false, ApiStage.Stable),

    // Disabled by default due to low usage.
    GENERATE_MANIFEST_CLASS("android.generateManifestClass", false, ApiStage.Stable),

    USE_NON_FINAL_RES_IDS("android.nonFinalResIds", true, ApiStage.Stable),
    NON_TRANSITIVE_R_CLASS("android.nonTransitiveRClass", true, ApiStage.Stable),

    /**
     * Setting this field to false indicates that in the current
     * project, all the APKs installed during test will be uninstalled
     * after test finishes. Setting it to true means that the AGP
     * will leave the test APKs untouched after test.
     *
     * Default is false
     */
    ANDROID_TEST_LEAVE_APKS_INSTALLED_AFTER_RUN("android.injected.androidTest.leaveApksInstalledAfterRun", false, ApiStage.Stable),

    /**
     * When this option is enabled, dexing transforms will use the full classpath (if desugaring
     * requires a classpath). This classpath consists of all external artifacts
     * ([com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactScope.EXTERNAL])
     * in addition to the input artifact's dependencies provided by Gradle through
     * [org.gradle.api.artifacts.transform.InputArtifactDependencies].
     *
     * This option is useful when some dependencies are missing from the input artifact's
     * dependencies (see bug 230454566), so the full classpath is needed.
     *
     * If dexing transforms do not require a classpath, this option is not used.
     */
    USE_FULL_CLASSPATH_FOR_DEXING_TRANSFORM(
        "android.useFullClasspathForDexingTransform",
        false,
        ApiStage.Stable
    ),

    /* ------------------
     * SUPPORTED FEATURES
     */

    // Used by Studio as workaround for b/71054106, b/75955471
    ENABLE_SDK_DOWNLOAD("android.builder.sdkDownload", true, FeatureStage.Supported),

    ENFORCE_UNIQUE_PACKAGE_NAMES("android.uniquePackageNames", false, FeatureStage.Supported),

    // Flag added to work around b/130596259.
    FORCE_JACOCO_OUT_OF_PROCESS("android.forceJacocoOutOfProcess", false, FeatureStage.Supported),

    PRECOMPILE_DEPENDENCIES_RESOURCES("android.precompileDependenciesResources", true, FeatureStage.Supported),

    INCLUDE_DEPENDENCY_INFO_IN_APKS("android.includeDependencyInfoInApks", true, FeatureStage.Supported),

    // FIXME switch to false once we know we don't use these getters internally.
    ENABLE_LEGACY_API("android.compatibility.enableLegacyApi", true, FeatureStage.Supported),
    FULL_R8("android.enableR8.fullMode", true, FeatureStage.Supported),

    /* ---------------------
     * EXPERIMENTAL FEATURES
     */

    BUILD_FEATURE_MLMODELBINDING("android.defaults.buildfeatures.mlmodelbinding", false, ApiStage.Experimental),
    ENABLE_DEFAULT_DEBUG_SIGNING_CONFIG("android.experimental.useDefaultDebugSigningConfigForProfileableBuildtypes", false, ApiStage.Experimental),
    ENABLE_PROFILE_JSON("android.enableProfileJson", false, FeatureStage.Experimental),
    DISALLOW_DEPENDENCY_RESOLUTION_AT_CONFIGURATION("android.dependencyResolutionAtConfigurationTime.disallow", false, FeatureStage.Experimental),
    VERSION_CHECK_OVERRIDE_PROPERTY("android.overrideVersionCheck", false, FeatureStage.Experimental),
    OVERRIDE_PATH_CHECK_PROPERTY("android.overridePathCheck", false, FeatureStage.Experimental),
    DISABLE_RESOURCE_VALIDATION("android.disableResourceValidation", false, FeatureStage.Experimental),
    CONSUME_DEPENDENCIES_AS_SHARED_LIBRARIES("android.consumeDependenciesAsSharedLibraries", false, FeatureStage.Experimental),
    DISABLE_EARLY_MANIFEST_PARSING("android.disableEarlyManifestParsing", false, FeatureStage.Experimental),
    ENABLE_RESOURCE_NAMESPACING_DEFAULT("android.enableResourceNamespacingDefault", false, FeatureStage.Experimental),
    CONDITIONAL_KEEP_RULES("android.useConditionalKeepRules", false, FeatureStage.Experimental),
    KEEP_SERVICES_BETWEEN_BUILDS("android.keepWorkerActionServicesBetweenBuilds", false, FeatureStage.Experimental),
    ENABLE_PARTIAL_R_INCREMENTAL_BUILDS("android.enablePartialRIncrementalBuilds", false, FeatureStage.Experimental),
    ENABLE_NEW_RESOURCE_SHRINKER_PRECISE("android.experimental.enableNewResourceShrinker.preciseShrinking", false, FeatureStage.Experimental),
    ENABLE_LOCAL_TESTING("android.bundletool.enableLocalTesting", false, FeatureStage.Experimental),
    DISABLE_MINSDKLIBRARY_CHECK("android.unsafe.disable.minSdkLibraryCheck", false, FeatureStage.Experimental),
    ENABLE_INSTRUMENTATION_TEST_DESUGARING("android.experimental.library.desugarAndroidTest", false, FeatureStage.Experimental),
    ENABLE_EMULATOR_CONTROL("android.experimental.androidTest.enableEmulatorControl", false, FeatureStage.Experimental),
    ENABLE_SCREENSHOT_TEST("android.experimental.enableScreenshotTest", false, FeatureStage.Experimental),
    /**
     * When enabled, incompatible APKs installed on a testing device will be uninstalled automatically
     * during an instrumentation test run (e.g. When INSTALL_FAILED_UPDATE_INCOMPATIBLE error happens
     * after attempting to install APKs for testing).
     */
    UNINSTALL_INCOMPATIBLE_APKS("android.experimental.testOptions.uninstallIncompatibleApks", false, FeatureStage.Experimental),

    /**
     * When enabled, "-show-kernel" and "-verbose" flags are used when running an Android emulator
     * for Gradle Managed devices.
     */
    GRADLE_MANAGED_DEVICE_EMULATOR_SHOW_KERNEL_LOGGING("android.experimental.testOptions.managedDevices.emulator.showKernelLogging", false, FeatureStage.Experimental),

    /**
     * Gradle Managed devices officially supports Android devices on API level 27 and higher because
     * using the API level 26 and lower devices increase instability. When a user tries to use those
     * old API devices, GMD task throws an exception and task fails by default.
     *
     * When this flag is enabled, it allows a user to use any old API level devices regardless of
     * its instability.
     */
    GRADLE_MANAGED_DEVICE_ALLOW_OLD_API_LEVEL_DEVICES("android.experimental.testOptions.managedDevices.allowOldApiLevelDevices", false, FeatureStage.Experimental),

    /**
     * When enabled, Gradle Managed Device allows a custom managed device type that can be provided
     * by a plugin by implementing ManagedDeviceTestRunner APIs.
     */
    GRADLE_MANAGED_DEVICE_CUSTOM_DEVICE("android.experimental.testOptions.managedDevices.customDevice", false, FeatureStage.Experimental),

    /** When set R classes are treated as compilation classpath in libraries, rather than runtime classpath, with values set to 0. */
    ENABLE_ADDITIONAL_ANDROID_TEST_OUTPUT("android.enableAdditionalTestOutput", true, FeatureStage.Experimental),

    ENABLE_APP_COMPILE_TIME_R_CLASS("android.enableAppCompileTimeRClass", false, FeatureStage.Experimental),
    ENABLE_EXTRACT_ANNOTATIONS("android.enableExtractAnnotations", true, FeatureStage.Experimental),

    // Marked as stable to avoid reporting deprecation twice.
    CONVERT_NON_NAMESPACED_DEPENDENCIES("android.convertNonNamespacedDependencies", true, FeatureStage.Experimental),

    /** Set to true to build native .so libraries only for the device it will be run on. */
    BUILD_ONLY_TARGET_ABI("android.buildOnlyTargetAbi", true, FeatureStage.Experimental),

    ENABLE_PARALLEL_NATIVE_JSON_GEN("android.enableParallelJsonGen", false, FeatureStage.Experimental),
    ENABLE_SIDE_BY_SIDE_CMAKE("android.enableSideBySideCmake", true, FeatureStage.Experimental),
    ENABLE_NATIVE_COMPILER_SETTINGS_CACHE("android.enableNativeCompilerSettingsCache", false, FeatureStage.Experimental),
    ENABLE_CMAKE_BUILD_COHABITATION("android.enableCmakeBuildCohabitation", false, FeatureStage.Experimental),
    ENABLE_PROGUARD_RULES_EXTRACTION("android.proguard.enableRulesExtraction", true, FeatureStage.Experimental),
    USE_DEPENDENCY_CONSTRAINTS("android.dependency.useConstraints", true, FeatureStage.Experimental),
    EXCLUDE_LIBRARY_COMPONENTS_FROM_CONSTRAINTS("android.experimental.dependency.excludeLibraryComponentsFromConstraints", false, FeatureStage.Experimental),
    ENABLE_DUPLICATE_CLASSES_CHECK("android.enableDuplicateClassesCheck", true, FeatureStage.Experimental),
    MINIMAL_KEEP_RULES("android.useMinimalKeepRules", true, FeatureStage.Experimental),
    EXCLUDE_RES_SOURCES_FOR_RELEASE_BUNDLES("android.bundle.excludeResSourcesForRelease", true, FeatureStage.Experimental),
    ENABLE_BUILD_CONFIG_AS_BYTECODE("android.enableBuildConfigAsBytecode", false, FeatureStage.Experimental),
    /** Whether lint should be run in process; the default is true. */
    RUN_LINT_IN_PROCESS("android.experimental.runLintInProcess", true, FeatureStage.Experimental),

    ENABLE_TEST_FIXTURES("android.experimental.enableTestFixtures", false, FeatureStage.Experimental),

    USE_NEW_DSL_INTERFACES("android.experimental.newDslInterfaces", false, FeatureStage.Experimental),

    /** Whether to force the APK to be deterministic. */
    FORCE_DETERMINISTIC_APK("android.experimental.forceDeterministicApk", false, FeatureStage.Experimental),

    MISSING_LINT_BASELINE_IS_EMPTY_BASELINE(
        "android.experimental.lint.missingBaselineIsEmptyBaseline",
        false,
        FeatureStage.Experimental,
    ),

    LEGACY_TRANSFORM_TASK_FORCE_NON_INCREMENTAL(
            "android.experimental.legacyTransform.forceNonIncremental",
            false,
            FeatureStage.Experimental
    ),

    PRIVACY_SANDBOX_SDK_SUPPORT("android.experimental.privacysandboxsdk.enable", false, FeatureStage.Experimental),
    PRIVACY_SANDBOX_SDK_REQUIRE_SERVICES(
            "android.experimental.privacysandboxsdk.requireServices", true, FeatureStage.Experimental),

    PRINT_LINT_STACK_TRACE("android.lint.printStackTrace", false, FeatureStage.Experimental),

    VERIFY_AAR_CLASSES("android.experimental.verifyLibraryClasses", false, FeatureStage.Experimental),
    DISABLE_COMPILE_SDK_CHECKS("android.experimental.disableCompileSdkChecks", false, FeatureStage.Experimental),
    ADDITIONAL_ARTIFACTS_IN_MODEL("android.experimental.additionalArtifactsInModel", false, FeatureStage.Experimental),

    // Whether to suppress warnings about android:extractNativeLibs set to true in dependencies
    SUPPRESS_EXTRACT_NATIVE_LIBS_WARNINGS(
        "android.experimental.suppressExtractNativeLibsWarnings",
        false,
        FeatureStage.Experimental
    ),

    FUSED_LIBRARY_SUPPORT("android.experimental.fusedLibrarySupport", false, FeatureStage.Experimental),
    SUPPORT_PAST_STUDIO_VERSIONS("android.experimental.support.past.studio.versions", false, FeatureStage.Experimental),

    /**
     * Enables task to add version control info to APKs/Bundle
     */
    ENABLE_VCS_INFO("android.enableVcsInfo", false, FeatureStage.Experimental),

    /**
     * Whether to omit line numbers when writing lint baselines
     */
    LINT_BASELINE_OMIT_LINE_NUMBERS(
        "android.lint.baselineOmitLineNumbers",
        false,
        FeatureStage.Experimental
    ),

    /**
     * Whether to use K2 UAST when running lint
     */
    LINT_USE_K2_UAST(
        "android.lint.useK2Uast",
        false,
        FeatureStage.Experimental
    ),

    /* ------------------------
     * SOFTLY-ENFORCED FEATURES
     */
    ENABLE_RESOURCE_OPTIMIZATIONS(
        "android.enableResourceOptimizations",
        true,
        FeatureStage.SoftlyEnforced(VERSION_9_0)
    ),

    ANDROID_TEST_USES_UNIFIED_TEST_PLATFORM(
        "android.experimental.androidTest.useUnifiedTestPlatform",
        true,
        FeatureStage.SoftlyEnforced(VERSION_9_0)
    ),

    /**
     * Whether to do lint analysis per component (instead of analysing the main variant and the test
     * components in the same lint invocation).
     */
    LINT_ANALYSIS_PER_COMPONENT(
        "android.experimental.lint.analysisPerComponent",
        true,
        FeatureStage.SoftlyEnforced(VERSION_9_0)
    ),

    /* -------------------
     * DEPRECATED FEATURES
     */

    BUILD_FEATURE_AIDL(
        "android.defaults.buildfeatures.aidl",
        false,
        ApiStage.Deprecated(VERSION_9_0)
    ),

    BUILD_FEATURE_RENDERSCRIPT(
        "android.defaults.buildfeatures.renderscript",
        false,
        ApiStage.Deprecated(VERSION_9_0)
    ),

    BUILD_FEATURE_BUILDCONFIG(
        "android.defaults.buildfeatures.buildconfig",
        false,
        ApiStage.Deprecated(BUILD_CONFIG_GLOBAL_PROPERTY))
    ,

    /* -----------------
     * ENFORCED FEATURES
     */
    @Suppress("unused")
    PREFER_CMAKE_FILE_API(
        "android.preferCmakeFileApi",
        true, FeatureStage.Enforced(VERSION_7_0)),

    @Suppress("unused")
    ENABLE_NATIVE_CONFIGURATION_FOLDING(
        "android.enableNativeConfigurationFolding",
        true,
        FeatureStage.Enforced(VERSION_7_0)),

    @Suppress("unused")
    ENABLE_SIDE_BY_SIDE_NDK(
        "android.enableSideBySideNdk",
        true,
        FeatureStage.Enforced(
            VERSION_4_1,
            "The android.enableSideBySideNdk property does not have any effect. " +
                    "Side-by-side NDK is always enabled."
        )
    ),

    @Suppress("unused")
    ENABLE_IMPROVED_DEPENDENCY_RESOLUTION(
        "android.enableImprovedDependenciesResolution",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "The android.enableImprovedDependenciesResolution property does not have any effect. "
                    + "Dependency resolution is only performed during task execution phase."
        )
    ),

    @Suppress("unused")
    ENABLE_NEW_RESOURCE_PROCESSING(
        "android.enableNewResourceProcessing",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "New resource processing is now always enabled."
        )
    ),

    @Suppress("unused")
    DISABLE_RES_MERGE_IN_LIBRARY(
        "android.disable.res.merge",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "Resources from dependencies are never merged in libraries."
        )
    ),

    @Suppress("unused")
    ENABLE_DAEMON_MODE_AAPT2(
        "android.enableAapt2DaemonMode",
        true,
        FeatureStage.Enforced(VERSION_BEFORE_4_0, "AAPT2 daemon mode is now always enabled.")
    ),

    @Suppress("unused")
    ENABLE_INCREMENTAL_DESUGARING(
        "android.enableIncrementalDesugaring",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect, incremental desugaring is always enabled."
        )
    ),

    @Suppress("unused")
    ENABLE_CORE_LAMBDA_STUBS(
        "android.enableCoreLambdaStubs",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect, core-lambda-stubs.jar is always in the bootclasspath."
        )
    ),

    @Suppress("unused")
    ENABLE_DEX_ARCHIVE(
        "android.useDexArchive",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect, incremental dexing is always used."
        )
    ),

    @Suppress("unused")
    ENABLE_AAPT2(
        "android.enableAapt2",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect, AAPT2 is now always used."
        )
    ),

    @Suppress("unused")
    USE_AAPT2_FROM_MAVEN(
        "android.useAapt2FromMaven",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect and AAPT2 from maven.google.com is now always used. "
                    + "If you wish to use a local executable of AAPT2 please use the "
                    + "'android.aapt2FromMavenOverride' option."
        )
    ),

    @Suppress("unused")
    ENABLE_D8_MAIN_DEX_LIST(
        "android.enableD8MainDexList",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "This property has no effect, D8 is always used to compute the main dex list."
        )
    ),

    @Suppress("unused")
    ENABLE_DATA_BINDING_V2(
        "android.databinding.enableV2",
        true,
        FeatureStage.Enforced(VERSION_BEFORE_4_0, "Databinding v1 is removed.")
    ),

    ENABLE_SEPARATE_R_CLASS_COMPILATION(
        "android.enableSeparateRClassCompilation",
        true,
        FeatureStage.Enforced(
            VERSION_BEFORE_4_0,
            "Separate R class compilation has been enabled and can no longer be disabled."
        )
    ),

    @Suppress("unused")
    ENABLE_GRADLE_WORKERS(
        "android.enableGradleWorkers",
        true,
        FeatureStage.Enforced(
            VERSION_4_2,
            "Gradle workers are always used."
        )
    ),

    @Suppress("unused")
    ENABLE_D8(
        "android.enableD8",
        true,
        FeatureStage.Enforced(
            VERSION_7_0,
            "For more details, see https://d.android.com/r/studio-ui/d8-overview.html."
        )
    ),

    @Suppress("unused")
    ENABLE_D8_DESUGARING(
        "android.enableD8.desugaring",
        true,
        FeatureStage.Enforced(
            VERSION_7_0,
            "D8 desugaring is used by default, when applicable."
        )
    ),

    @Suppress("unused")
    ENABLE_R8_DESUGARING(
        "android.enableR8.desugaring",
        true,
        FeatureStage.Enforced(
            VERSION_7_0,
            "R8 desugaring is used by default, when applicable."
        )
    ),

    USE_NEW_LINT_MODEL("android.experimental.useNewLintModel", true, FeatureStage.Enforced(VERSION_7_0)),

    /** Whether Jetifier will skip libraries that already support AndroidX. */
    JETIFIER_SKIP_IF_POSSIBLE("android.jetifier.skipIfPossible", true, FeatureStage.Enforced(VERSION_7_0)),

    @Suppress("unused")
    NON_TRANSITIVE_APP_R_CLASS(
            "android.experimental.nonTransitiveAppRClass",
            true,
            FeatureStage.Enforced(
                    VERSION_7_0,
                    "Non-transitive R classes are now enabled in app modules with the " +
                            "${NON_TRANSITIVE_R_CLASS.propertyName} option.")),

    /** Incremental dexing task using D8's new API for desugaring graph computation. */
    ENABLE_INCREMENTAL_DEXING_TASK_V2("android.enableIncrementalDexingTaskV2", true, FeatureStage.Enforced(VERSION_7_0)),

    /** Incremental dexing transform. */
    ENABLE_INCREMENTAL_DEXING_TRANSFORM("android.enableIncrementalDexingTransform", true, FeatureStage.Enforced(VERSION_7_0)),

    ENABLE_R8_LIBRARIES("android.enableR8.libraries", true, FeatureStage.Enforced(VERSION_7_0)),

    ENABLE_SYMBOL_TABLE_CACHING("android.enableSymbolTableCaching", true, FeatureStage.Enforced(VERSION_7_0)),

    ENABLE_JVM_RESOURCE_COMPILER("android.enableJvmResourceCompiler", true, FeatureStage.Enforced(VERSION_7_0)),

    /** Whether to use lint's partial analysis functionality. */
    USE_LINT_PARTIAL_ANALYSIS("android.enableParallelLint", true, FeatureStage.Enforced(VERSION_7_2)),

    ENABLE_AAPT2_WORKER_ACTIONS(
        "android.enableAapt2WorkerActions",
        true,
        FeatureStage.Enforced(
            VERSION_7_3,
            "AAPT2 worker actions have been used unconditionally since Android Gradle Plugin 3.3"
        )
    ),
    ENABLE_JACOCO_TRANSFORM_INSTRUMENTATION(
        "android.enableJacocoTransformInstrumentation",
        true,
        FeatureStage.Enforced(VERSION_7_3)
    ),

    @Suppress("unused")
    ENABLE_SOURCE_SET_PATHS_MAP(
            "android.enableSourceSetPathsMap",
            true,
            FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    @Suppress("unused")
    RELATIVE_COMPILE_LIB_RESOURCES(
            "android.cacheCompileLibResources",
            true,
            FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    @Suppress("unused")
    R8_FAIL_ON_MISSING_CLASSES(
        "android.r8.failOnMissingClasses",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    USE_NEW_JAR_CREATOR(
        "android.useNewJarCreator",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    USE_NEW_APK_CREATOR(
        "android.useNewApkCreator",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    WARN_ABOUT_DEPENDENCY_RESOLUTION_AT_CONFIGURATION(
        "android.dependencyResolutionAtConfigurationTime.warn",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    ENABLE_ART_PROFILES(
        "android.enableArtProfiles",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    USE_RELATIVE_PATH_IN_TEST_CONFIG(
        "android.testConfig.useRelativePath",
        true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    INCLUDE_REPOSITORIES_IN_DEPENDENCY_REPORT(
    "android.bundletool.includeRepositoriesInDependencyReport",
    true,
        FeatureStage.Enforced(Version.VERSION_8_0)
    ),

    ENABLE_INCREMENTAL_DATA_BINDING(
        "android.databinding.incremental",
        true,
        FeatureStage.Enforced(Version.VERSION_8_1)
    ),

    ENABLE_NEW_RESOURCE_SHRINKER("android.enableNewResourceShrinker",
            true,
            FeatureStage.Enforced(Version.VERSION_8_0)),

    @Suppress("unused")
    ENABLE_R_TXT_RESOURCE_SHRINKING(
            "android.enableRTxtResourceShrinking",
            true,
            FeatureStage.Enforced(Version.VERSION_8_1)
    ),

    @Suppress("unused")
    COMPILE_CLASSPATH_LIBRARY_R_CLASSES(
            "android.useCompileClasspathLibraryRClasses",
            true,
            FeatureStage.Enforced(Version.VERSION_8_1)
    ),

    ENABLE_UNCOMPRESSED_NATIVE_LIBS_IN_BUNDLE(
        "android.bundle.enableUncompressedNativeLibs",
        true,
        FeatureStage.Enforced(Version.VERSION_8_1)
    ),

    ENABLE_GLOBAL_SYNTHETICS(
        "android.enableGlobalSyntheticsGeneration",
        true,
        FeatureStage.Enforced(Version.VERSION_8_1)
    ),

    ENABLE_DEXING_DESUGARING_ARTIFACT_TRANSFORM(
        "android.enableDexingArtifactTransform.desugaring",
        true,
        FeatureStage.Enforced(
            Version.VERSION_8_2,
            "If you run into issues with dexing transforms, try setting `${USE_FULL_CLASSPATH_FOR_DEXING_TRANSFORM.propertyName} = true` instead."
        )
    ),

    ENABLE_DEXING_ARTIFACT_TRANSFORM_FOR_EXTERNAL_LIBS(
        "android.enableDexingArtifactTransformForExternalLibs",
        true,
        FeatureStage.Enforced(Version.VERSION_8_2)
    ),

    ENABLE_DEXING_ARTIFACT_TRANSFORM(
        "android.enableDexingArtifactTransform",
        true,
        FeatureStage.Enforced(
            Version.VERSION_8_3,
            "If you run into issues with dexing transforms, try setting `${USE_FULL_CLASSPATH_FOR_DEXING_TRANSFORM.propertyName} = true` instead."
        )
    ),

    /* ----------------
     * REMOVED FEATURES
     */

    @Suppress("unused")
    ENABLE_IN_PROCESS_AAPT2(
        "android.enableAapt2jni",
        false,
        FeatureStage.Removed(VERSION_BEFORE_4_0, "AAPT2 JNI has been removed.")
    ),

    @Suppress("unused")
    ENABLE_DEPRECATED_NDK(
        "android.useDeprecatedNdk",
        false,
        FeatureStage.Removed(VERSION_BEFORE_4_0, "NdkCompile is no longer supported")
    ),

    @Suppress("unused")
    INJECT_SDK_MAVEN_REPOS(
        "android.injectSdkMavenRepos",
        false,
        FeatureStage.Removed(
            VERSION_3_5,
            "The ability to inject the Android SDK maven repos is removed in AGP 3.5"
        )
    ),

    @Suppress("unused")
    ENABLE_UNIT_TEST_BINARY_RESOURCES(
        "android.enableUnitTestBinaryResources",
        false,
        FeatureStage.Removed(
            VERSION_BEFORE_4_0,
            "The raw resource for unit test functionality is removed."
        )
    ),

    @Suppress("unused")
    ENABLE_EXPERIMENTAL_FEATURE_DATABINDING(
        "android.enableExperimentalFeatureDatabinding",
        false,
        FeatureStage.Removed(
            VERSION_4_0,
            "This property has no effect. The features plugin was removed in AGP 4.0.")
    ),

    @Suppress("unused")
    ENABLE_SEPARATE_APK_RESOURCES(
        "android.enableSeparateApkRes",
        false,
        FeatureStage.Removed(VERSION_BEFORE_4_0, "Instant run is replaced by apply changes.")
    ),

    @Suppress("unused")
    KEEP_TIMESTAMPS_IN_APK(
        "android.keepTimestampsInApk",
        false,
        FeatureStage.Removed(
            VERSION_3_6,
            "The ability to keep timestamps in the APK is removed in AGP 3.6"
        )
    ),

    @Suppress("unused")
    ENABLE_SEPARATE_ANNOTATION_PROCESSING(
        "android.enableSeparateAnnotationProcessing",
        false,
        FeatureStage.Removed(VERSION_4_0, "This feature was removed in AGP 4.0")
    ),

    @Suppress("unused")
    GENERATE_R_JAVA(
        "android.generateRJava",
        false,
        FeatureStage.Removed(VERSION_4_1, "This feature was removed in AGP 4.1")),

    @Suppress("unused")
    ENABLE_BUILD_CACHE(
        "android.enableBuildCache",
        false,
        FeatureStage.Removed(VERSION_7_0, "The Android-specific build caches were superseded by the Gradle build cache (https://docs.gradle.org/current/userguide/build_cache.html).")
    ),

    @Suppress("unused")
    ENABLE_INTERMEDIATE_ARTIFACTS_CACHE(
        "android.enableIntermediateArtifactsCache",
        false,
        FeatureStage.Removed(VERSION_7_0, "The Android-specific build caches were superseded by the Gradle build cache (https://docs.gradle.org/current/userguide/build_cache.html).")
    ),

    @Suppress("unused")
    ENABLE_DESUGAR(
            "android.enableDesugar",
            false,
            FeatureStage.Removed(VERSION_7_0, "Desugar tool has been removed from AGP.")
    ),

    @Suppress("unused")
    ENABLE_TEST_SHARDING("android.androidTest.shardBetweenDevices", false, FeatureStage.Removed(Version.VERSION_8_2, "Cross device sharding is no longer supported.")),
    ; // end of enums

    override val status = stage.status

    override fun parse(value: Any): Boolean {
        return parseBoolean(propertyName, value)
    }
}
