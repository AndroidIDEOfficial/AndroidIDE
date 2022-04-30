/*
 * Copyright (C) 2013 The Android Open Source Project
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
import java.io.File

/**
 * Options for lint.
*/
interface LintOptions: AndroidModel {
    /**
     * The set of issue id's to suppress.
     */
    val disable: Set<String>

    /**
     * The set of issue id's to enable.
     */
    val enable: Set<String>

    /** Issues that have severity overridden to 'informational' */
    val informational: Set<String>

    /** Issues that have severity overridden to 'warning' */
    val warning: Set<String>

    /** Issues that have severity overridden to 'error' */
    val error: Set<String>

    /** Issues that have severity overridden to 'fatal' */
    val fatal: Set<String>

    /**
     * The exact set of issues to check. If empty lint will detect the issues that are enabled
     * by default plus any issues enabled via [enable] and without issues disabled via [disable].
     */
    val checkOnly: Set<String>

    /** Whether lint should abort the build if errors are found  */
    val abortOnError: Boolean

    /**
     * Whether lint should display full paths in the error output. By default the paths
     * are relative to the path lint was invoked from.
     */
    val absolutePaths: Boolean

    /**
     * Whether lint should include the source lines in the output where errors occurred
     * (true by default)
     */
    val noLines: Boolean

    /**
     * Returns whether lint should be quiet (for example, not write informational messages
     * such as paths to report files written)
     */
    val quiet: Boolean

    /** Returns whether lint should check all warnings, including those off by default  */
    val checkAllWarnings: Boolean

    /** Returns whether lint will only check for errors (ignoring warnings)  */
    val ignoreWarnings: Boolean

    /** Returns whether lint should treat all warnings as errors  */
    val warningsAsErrors: Boolean

    /**
     * Returns whether lint should run all checks on test sources, instead of just the
     * lint checks that have been specifically written to include tests (e.g. checks
     * looking for specific test errors, or checks that need to consider testing code
     * such as the unused resource detector)
     */
    val checkTestSources: Boolean

    /**
     * Like [.isCheckTestSources], but always skips analyzing tests -- meaning that it also
     * ignores checks that have explicitly asked to look at test sources, such as the unused
     * resource check.
     */
    val ignoreTestSources: Boolean

    /**
     * Whether lint should ignore all testFixtures sources.
     */
    val ignoreTestFixturesSources: Boolean

    /**
     * Returns whether lint should run checks on generated sources.
     */
    val checkGeneratedSources: Boolean

    /** Returns whether lint should include explanations for issue errors. (Note that
     * HTML and XML reports intentionally do this unconditionally, ignoring this setting.)  */
    val explainIssues: Boolean

    /**
     * Returns whether lint should include all output (e.g. include all alternate
     * locations, not truncating long messages, etc.)
     */
    val showAll: Boolean

    /**
     * Returns an optional path to a lint.xml configuration file
     */
    val lintConfig: File?

    /** Whether we should write an text report. Default false. The location can be
     * controlled by [.getTextOutput].  */
    val textReport: Boolean

    /**
     * The optional path to where a text report should be written. The special value
     * "stdout" can be used to point to standard output.
     */
    val textOutput: File?

    /** Whether we should write an HTML report. Default true. The location can be
     * controlled by [.getHtmlOutput].  */
    val htmlReport: Boolean

    /** The optional path to where an HTML report should be written  */
    val htmlOutput: File?

    /** Whether we should write an XML report. Default true. The location can be
     * controlled by [.getXmlOutput].  */
    val xmlReport: Boolean

    /** The optional path to where an XML report should be written  */
    val xmlOutput: File?

    /**
     * Whether we should write a SARIF (OASIS Static Analysis Results Interchange Format) report.
     * Default is false. The location can be controlled by [sarifOutput].
     */
    val sarifReport: Boolean

    /**
     * The optional path to where a SARIF report (OASIS Static
     * Analysis Results Interchange Format) should be written.
     * Setting this property will also turn on [sarifOutput].
     */
    val sarifOutput: File?

    /**
     * Returns whether lint should check for fatal errors during release builds. Default is true.
     * If issues with severity "fatal" are found, the release build is aborted.
     */
    val checkReleaseBuilds: Boolean

    /**
     * Returns whether lint should check all dependencies too as part of its analysis. Default is
     * false.
     */
    val checkDependencies: Boolean

    /**
     * Returns the baseline file to use, if any. The baseline file is
     * an XML report previously created by lint, and any warnings and
     * errors listed in that report will be ignored from analysis.
     *
     *
     * If you have a project with a large number of existing warnings,
     * this lets you set a baseline and only see newly introduced warnings
     * until you get a chance to go back and address the "technical debt"
     * of the earlier warnings.
     */
    val baseline: File?
}
