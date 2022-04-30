/*
 * Copyright (C) 2021 The Android Open Source Project
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

package com.android.builder.model.v2

import java.io.File

/**
 * Represents a model sync file.
 *
 * A model sync file is produced by the Android Gradle Plugin during the execution phase and
 * contains information that cannot be determined at configuration time.
 */
interface ModelSyncFile {
    /**
     * Enum of all sync types supported by this plugin.
     */
    enum class ModelSyncType {

        /**
         * Sync that will contain elements of type com.android.ide.model.sync.AppIdListSync
         */
        APP_ID_LIST,

        /**
         * Basic sync type will contain elements of type com.android.ide.model.sync.Variant.
         *
         */
        BASIC,
    }

    /**
     * [ModelSyncType] for this file.
     */
    val modelSyncType: ModelSyncType

    /**
     * Name of the task that can produce the model sync file. The task must have executed
     * successfully for the [syncFile] file to be available.
     */
    val taskName: String

    /**
     * Sync file currently in the proto format.
     *
     * The content depends on the [modelSyncType]
     */
    val syncFile: File
}
