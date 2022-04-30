/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.android.builder.model.v2.ide

import com.android.builder.model.v2.AndroidModel

/**
 * Class representing a sync issue. The goal is to make these issues not fail the sync but instead
 * report them at the end of a successful sync.
 *
 * @since 4.2
 */
interface SyncIssue: AndroidModel {
    /** Returns the severity of the issue.  */
    val severity: Int

    /** Returns the type of the issue.  */
    val type: Int

    /**
     * Returns the data of the issue.
     *
     * This is a machine-readable string used by the IDE for known issue types.
     */
    val data: String?

    /**
     * Returns the a user-readable message for the issue.
     *
     * This is used by IDEs that do not recognize the issue type (ie older IDE released before
     * the type was added to the plugin).
     */
    val message: String

    /**
     * Returns the a user-readable nulti-line message for the issue.
     *
     * This is an optional extension of [message]
     */
    val multiLineMessage: List<String?>?

    companion object {
        const val SEVERITY_WARNING = 1
        const val SEVERITY_ERROR = 2

        /** Generic error with no data payload, and no expected quick fix in IDE.  */
        const val TYPE_GENERIC = 0

        /** Data is expiration data.  */
        const val TYPE_PLUGIN_OBSOLETE = 1

        /** Data is dependency coordinate.  */
        const val TYPE_UNRESOLVED_DEPENDENCY = 2

        /** Data is dependency coordinate.  */
        const val TYPE_DEPENDENCY_IS_APK = 3

        /** Data is dependency coordinate.  */
        const val TYPE_DEPENDENCY_IS_APKLIB = 4

        /** Data is local file.  */
        const val TYPE_NON_JAR_LOCAL_DEP = 5

        /** Data is dependency coordinate/path.  */
        const val TYPE_NON_JAR_PACKAGE_DEP = 6

        /** Data is dependency coordinate/path.  */
        const val TYPE_NON_JAR_PROVIDED_DEP = 7

        /** Data is dependency coordinate/path.  */
        const val TYPE_JAR_DEPEND_ON_AAR = 8

        /**
         * Mismatch dependency version between tested and test app. Data is dep coordinate without the
         * version (groupId:artifactId)
         */
        const val TYPE_MISMATCH_DEP = 9

        /** Data is dependency coordinate.  */
        const val TYPE_OPTIONAL_LIB_NOT_FOUND = 10

        /** Data is variant name.  */
        const val TYPE_JACK_IS_NOT_SUPPORTED = 11

        /** Data is the min version of Gradle.  */
        const val TYPE_GRADLE_TOO_OLD = 12

        /** Data is the required min build tools version, parsable by Revision.  */
        const val TYPE_BUILD_TOOLS_TOO_LOW = 13

        /**
         * Found dependency that's the maven published android.jar. Data is the maven artifact
         * coordinates.
         */
        const val TYPE_DEPENDENCY_MAVEN_ANDROID = 14

        /**
         * Found dependency that is known to be inside android.jar. Data is maven artifact coordinates.
         */
        const val TYPE_DEPENDENCY_INTERNAL_CONFLICT = 15

        /** Errors configuring NativeConfigValues for individual individual variants  */
        const val TYPE_EXTERNAL_NATIVE_BUILD_CONFIGURATION = 16

        /**
         * Errors configuring NativeConfigValues. There was a process exception. Data contains STDERR
         * which should be interpreted by Android Studio.
         */
        const val TYPE_EXTERNAL_NATIVE_BUILD_PROCESS_EXCEPTION = 17

        /** Cannot use Java 8 Language features without Jack.  */
        const val TYPE_JACK_REQUIRED_FOR_JAVA_8_LANGUAGE_FEATURES = 18

        /**
         * A wearApp configuration was resolved and found more than one apk. Data is the configuration
         * name.
         */
        const val TYPE_DEPENDENCY_WEAR_APK_TOO_MANY = 19

        /** A wearApp configuration was resolved and found an apk even though unbundled mode is on.  */
        const val TYPE_DEPENDENCY_WEAR_APK_WITH_UNBUNDLED = 20

        /** Data is dependency coordinate/path.  */
        @Deprecated("")
        const val TYPE_JAR_DEPEND_ON_ATOM = 21

        /** Data is dependency coordinate/path.  */
        @Deprecated("")
        const val TYPE_AAR_DEPEND_ON_ATOM = 22

        /** Data is dependency coordinate/path.  */
        @Deprecated("")
        const val TYPE_ATOM_DEPENDENCY_PROVIDED = 23

        /**
         * Indicates that a required SDK package was not installed. The data field contains the sdklib
         * package ID of the missing package that the user should install.
         */
        const val TYPE_MISSING_SDK_PACKAGE = 24

        /**
         * Indicates that the plugin requires a newer version of studio. Minimum version is passed in
         * the data.
         */
        const val TYPE_STUDIO_TOO_OLD = 25

        /**
         * Indicates that the module contains flavors but that no dimensions have been named. data is
         * empty.
         */
        const val TYPE_UNNAMED_FLAVOR_DIMENSION = 26

        /** An incompatible plugin is used.  */
        const val TYPE_INCOMPATIBLE_PLUGIN = 27

        /**
         * Indicates that the project uses a deprecated DSL. The data paylod is dslElement::removeTarget
         * where removal target is the version of the plugin where the dsl element is targeted to be
         * removed.
         */
        const val TYPE_DEPRECATED_DSL = 28

        /**
         * Indicates that the project uses a deprecated configuration.
         *
         *
         * This type is now replaced with TYPE_USING_DEPRECATED_CONFIGURATION (see
         * http://issuetracker.google.com/138278313).
         */
        @Deprecated("")
        const val TYPE_DEPRECATED_CONFIGURATION = 29

        /**
         * Indicates that the project uses a deprecated DSL, the Data payload is a URL giving context to
         * the user on how to remove the deprecated element or value.
         *
         *
         * This type is now replaced with TYPE_USING_DEPRECATED_DSL_VALUE (see
         * http://issuetracker.google.com/138278313).
         */
        @Deprecated("")
        const val TYPE_DEPRECATED_DSL_VALUE = 29

        /** Indicates that the project contains the min sdk in the android manifest file.  */
        const val TYPE_MIN_SDK_VERSION_IN_MANIFEST = 30

        /** Indicates that the project contains the target sdk in the android manifest file.  */
        const val TYPE_TARGET_SDK_VERSION_IN_MANIFEST = 31

        /** Indicated that an experimental gradle project option is used.  */
        const val TYPE_UNSUPPORTED_PROJECT_OPTION_USE = 32

        /**
         * Indicates that building the configuration rules for this project requires parsing the
         * manifest file.
         */
        const val TYPE_MANIFEST_PARSED_DURING_CONFIGURATION = 33

        /**
         * Indicates that the version of a third-party Gradle plugin (not the Android Gradle plugin) is
         * not supported and needs to be updated.
         */
        const val TYPE_THIRD_PARTY_GRADLE_PLUGIN_TOO_OLD = 34

        /**
         * Indicates that the signing configuration is declared in the dynamic-feature gradle file. This
         * should only be declared in the application module, as dynamic-features use the base module's
         * signing configuration, and this will be ignored.
         */
        const val TYPE_SIGNING_CONFIG_DECLARED_IN_DYNAMIC_FEATURE = 35

        /**
         * Indicates that the SDK is missing or invalid, this can either be set in the ANDROID_SDK_ROOT
         * environment variable or the projects local.properties files.
         */
        const val TYPE_SDK_NOT_SET = 36

        /** Indicates that the user has specified multiple default build types  */
        const val TYPE_AMBIGUOUS_BUILD_TYPE_DEFAULT = 37

        /** Indicates that the user has specified multiple default product flavors  */
        const val TYPE_AMBIGUOUS_PRODUCT_FLAVOR_DEFAULT = 38

        /** Indicates that the compileSdkVersion is missing  */
        const val TYPE_COMPILE_SDK_VERSION_NOT_SET = 39

        /**
         * Indicates that the `android.useAndroidX` property must be enabled but is currently disabled.
         */
        const val TYPE_ANDROID_X_PROPERTY_NOT_ENABLED = 40

        /** Indicates that the project uses a deprecated configuration.  */
        const val TYPE_USING_DEPRECATED_CONFIGURATION = 41

        /**
         * Indicates that the project uses a deprecated DSL, the Data payload is a URL giving context to
         * the user on how to remove the deprecated element or value.
         */
        const val TYPE_USING_DEPRECATED_DSL_VALUE = 42

        /** The user or a plugin has tried to mutate a DSL value after it has been locked.  */
        const val TYPE_EDIT_LOCKED_DSL_VALUE = 43

        // NOTE: When adding a new type here, increment the index by 1. This index may not be consistent
        // with the corresponding value in studio_stats.proto (e.g., it could be lower by 1), because of
        // an indexing issue in the past (see http://issuetracker.google.com/138278313).

        /** Indicates that a manifest is missing  */
        const val TYPE_MISSING_ANDROID_MANIFEST = 44
    }
}