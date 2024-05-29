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
 * Copyright (C) 2019 The Android Open Source Project
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

/**
 * The stage of an API or feature in its life cycle.
 *
 * An API or feature has an associated [Option] to allow the users to change the behavior of the
 * Android Gradle plugin. The difference between them is that:
 *   - The [Option] associated with an API is intended to be used in the long term. For example, if
 *     it is a [BooleanOption] (or [OptionalBooleanOption]), one value of the option may be
 *     experimental at first, but eventually both values of the option will be supported.
 *   - The [Option] associated with a feature is intended to be used only in the short term. It can
 *     only be a [BooleanOption] (or [OptionalBooleanOption]). One value of the [BooleanOption] may
 *     be experimental at first to enable the new behavior, but eventually the option will be
 *     removed, either because the feature is fully supported and now enforced, or because the
 *     feature was not / is no longer useful and now removed.
 */
open class Stage(

    /**
     * Status of the [Option] which represents an API or feature. It is related but not the same as
     * the [Stage] of the API of feature.
     */
    val status: Option.Status
)

/**
 * The stage of an API in its life cycle.
 *
 * See [Stage] for the difference between an API and a feature.
 */
sealed class ApiStage(status: Option.Status) : Stage(status) {

    /**
     * Indicates that the API is experimental.
     *
     * It may become stable or may be removed in a future release (see stage [Stable] and
     * [Removed]).
     */
    object Experimental : ApiStage(Option.Status.EXPERIMENTAL)

    /**
     * Indicates that the API is stable.
     */
    object Stable : ApiStage(Option.Status.STABLE)

    /**
     * Indicates that the API will be removed soon because it was not / is no longer useful (see
     * stage [Removed]).
     *
     * @param removalTarget a target when the API and the corresponding [Option] will be removed
     */
    class Deprecated(removalTarget: DeprecationTarget) :
        ApiStage(Option.Status.Deprecated(removalTarget))

    /**
     * Indicates that the API and the corresponding [Option] have been removed.
     *
     * @param removedVersion the version when the API and the corresponding [Option] were removed
     * @param additionalMessage the additional message to be shown if the [Option] is used
     */
    class Removed(removedVersion: Version, additionalMessage: String? = null) :
        ApiStage(Option.Status.Removed(removedVersion, additionalMessage))
}

/**
 * The stage of a feature in its life cycle.
 *
 * See [Stage] for the difference between an API and a feature.
 */
sealed class FeatureStage(status: Option.Status) : Stage(status) {

    /**
     * Indicates that the feature is experimental.
     *
     * It may be enforced or removed in a future release (see stage [Enforced] and [Removed]).
     */
    object Experimental : FeatureStage(Option.Status.EXPERIMENTAL)

    /**
     * Indicates that the feature is fully supported.
     *
     * It may or may not be enabled by default. If it is not yet enabled by default, it will likely
     * be enabled by default in a future release.
     *
     * Eventually, the feature will likely be enforced (see stage [SoftlyEnforced] and [Enforced]).
     * In some cases, it may be removed (see stage [Deprecated] and [Removed]).
     *
     * DISCOURAGED USAGE: The use of this stage is actually discouraged as it doesn't specify a
     * clear timeline and features may stay in this stage for too long, thus increasing maintenance
     * cost to AGP and users. Consider using [SoftlyEnforced] or [Deprecated] instead.
     */
    object Supported : FeatureStage(Option.Status.STABLE)

    /**
     * Indicates that the feature will be enforced soon (see stage [Enforced]).
     *
     * @param enforcementTarget a target when the feature will be enforced, at which point the
     *     corresponding [Option] will be removed (hence this parameter has type
     *     `DeprecationTarget`)
     */
    class SoftlyEnforced(enforcementTarget: DeprecationTarget) :
        FeatureStage(Option.Status.Deprecated(enforcementTarget))

    /**
     * Indicates that the feature is enforced (always enabled), and the corresponding [Option] has
     * been removed.
     *
     * @param enforcedVersion the version when the feature is enforced and the corresponding
     *     [Option] was removed
     * @param additionalMessage the additional message to be shown if the [Option] is used
     */
    class Enforced(enforcedVersion: Version, additionalMessage: String? = null) :
        FeatureStage(Option.Status.Removed(enforcedVersion, additionalMessage))

    /**
     * Indicates that the feature will be removed soon because it was not / is no longer useful (see
     * stage [Removed]).
     *
     * @param removalTarget a target when the feature and the corresponding [Option] will be removed
     */
    class Deprecated(removalTarget: DeprecationTarget) :
        FeatureStage(Option.Status.Deprecated(removalTarget))

    /**
     * Indicates that the feature has been removed (always disabled), and the corresponding [Option]
     * has been removed.
     *
     * @param removedVersion the version when the feature and the corresponding [Option] were
     *     removed
     * @param additionalMessage the additional message to be shown if the [Option] is used
     */
    class Removed(removedVersion: Version, additionalMessage: String? = null) :
        FeatureStage(Option.Status.Removed(removedVersion, additionalMessage))
}
