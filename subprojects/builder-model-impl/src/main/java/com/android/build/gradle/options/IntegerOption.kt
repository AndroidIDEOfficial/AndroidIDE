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

import com.android.build.gradle.options.Version.VERSION_8_2
import com.android.builder.model.AndroidProject
import com.android.build.gradle.options.Version.VERSION_BEFORE_4_0
import com.android.builder.model.PROPERTY_BUILD_API

enum class IntegerOption(
    override val propertyName: String,
    stage: ApiStage
) : Option<Int> {
    ANDROID_TEST_SHARD_COUNT("android.androidTest.numShards", ApiStage.Removed(VERSION_8_2, "Cross device sharding is no longer supported.")),
    ANDROID_SDK_CHANNEL("android.sdk.channel", ApiStage.Experimental),

    /**
     * Returns the level of model-only mode.
     *
     * The model-only mode is triggered when the IDE does a sync, and therefore we do things a
     * bit differently (don't throw exceptions for instance). Things evolved a bit over versions and
     * the behavior changes. This reflects the mode to use.
     *
     * @see AndroidProject#MODEL_LEVEL_0_ORIGINAL
     * @see AndroidProject#MODEL_LEVEL_1_SYNC_ISSUE
     * @see AndroidProject#MODEL_LEVEL_3_VARIANT_OUTPUT_POST_BUILD
     * @see AndroidProject#MODEL_LEVEL_4_NEW_DEP_MODEL
     */
    IDE_BUILD_MODEL_ONLY_VERSION(AndroidProject.PROPERTY_BUILD_MODEL_ONLY_VERSIONED, ApiStage.Stable),

    /**
     * The api level for the target device.
     *
     * <p>For preview versions that is the last stable version, and the {@link
     * StringOption#IDE_TARGET_DEVICE_CODENAME} will also be set.
     */
    IDE_TARGET_DEVICE_API(PROPERTY_BUILD_API, ApiStage.Stable),

    /**
     * Size of the buffers in kilobytes used to read .class files and storage for writing .dex files
     * translations into.
     */
    DEXING_READ_BUFFER_SIZE("android.dexingReadBuffer.size", ApiStage.Experimental),
    DEXING_WRITE_BUFFER_SIZE("android.dexingWriteBuffer.size", ApiStage.Experimental),

    /** Number of buckets used by `DexArchiveBuilderTask` and `DexMergingTask`. */
    DEXING_NUMBER_OF_BUCKETS("android.dexingNumberOfBuckets", ApiStage.Experimental),

    /**
     * Maximum number of dynamic features that can be allocated before Oreo platforms.
     */
    PRE_O_MAX_NUMBER_OF_FEATURES("android.maxNumberOfFeaturesBeforeOreo", ApiStage.Experimental),

    /**
     * Override the thread pool size dedicated to AAPT2 work units when not running with WorkerAPI.
     */
    AAPT2_THREAD_POOL_SIZE("android.aapt2ThreadPoolSize", ApiStage.Experimental),

    /**
     * Max number of R8 workers to run at once
     */
    R8_MAX_WORKERS("android.r8.maxWorkers", ApiStage.Experimental),

    /**
     * Flags for Android Test Retention
     * >=1: enable Android Test Retention and set it as maximum number of snapshots.
     * 0: disable Android Test Retention.
     * <0: enable Android Test Retention with unlimited number of snapshots.
     */
    TEST_FAILURE_RETENTION("android.experimental.testOptions.emulatorSnapshots.maxSnapshotsForTestFailures", ApiStage.Experimental),

    /**
     * The number of shards ran on Managed Devices during testing.
     */
    MANAGED_DEVICE_SHARD_POOL_SIZE("android.experimental.androidTest.numManagedDeviceShards", ApiStage.Experimental),

    /**
     * The timeout duration in minute for Gradle Managed Device setup steps (booting AVD and creating snapshot image)
     * If the value is 0 or negative value, it will never time out.
     */
    GRADLE_MANAGED_DEVICE_SETUP_TIMEOUT_MINUTES("android.experimental.testOptions.managedDevices.setupTimeoutMinutes", ApiStage.Experimental),

    /**
     * Maximum number of concurrent Gradle Managed Devices (AVDs) to be active at any one point in time.
     *
     * If the value is 0 or negative value, there is no maximum devices.
     */
    GRADLE_MANAGED_DEVICE_MAX_CONCURRENT_DEVICES("android.experimental.testOptions.managedDevices.maxConcurrentDevices", ApiStage.Experimental),

    /**
     * The timeout duration in seconds for install apk
     * If the value is 0 or negative value, it will be set to a default value by UTP.
     */
    INSTALL_APK_TIMEOUT("android.experimental.testOptions.installApkTimeout", ApiStage.Experimental),
    /* ------------
     * REMOVED APIS
     */

    THREAD_POOL_SIZE(
        "android.threadPoolSize",
        ApiStage.Removed(
            VERSION_BEFORE_4_0,
            "The android.threadPoolSize property has no effect"
        )
    ),

    ;

    override val status = stage.status

    override fun parse(value: Any): Int {
        if (value is CharSequence) {
            try {
                return Integer.parseInt(value.toString())
            } catch (ignored: NumberFormatException) {
                // Throws below.
            }

        }
        if (value is Number) {
            return value.toInt()
        }
        throw IllegalArgumentException(
            "Cannot parse project property "
                    + this.propertyName
                    + "='"
                    + value
                    + "' of type '"
                    + value.javaClass
                    + "' as integer."
        )
    }
}
