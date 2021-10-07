/*
 * Copyright (C) 2017, Megatron King
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.megatronking.stringfog.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.github.megatronking.stringfog.plugin.utils.Log
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
/**
 * The plugin defines some tasks.
 *
 * @author Megatron King
 * @since 2017/3/6 19:43
 */

class StringFogPlugin implements Plugin<Project> {

    private static final String PLUGIN_NAME = 'stringfog'

    @Override
    void apply(Project project) {
        project.extensions.create(PLUGIN_NAME, StringFogExtension)

        def android = project.extensions.android
        if (android instanceof AppExtension) {
            applyApplication(project, android)
        }
        if (android instanceof LibraryExtension) {
            applyLibrary(project, android)
        }

        project.afterEvaluate {
            Log.setDebug(project.stringfog.debug)
        }
    }

    void applyApplication(Project project, def android) {
        android.registerTransform(new StringFogTransformForApplication(project, android.applicationVariants))
        // throw an exception in instant run mode
        android.applicationVariants.all { variant ->
            def variantName = variant.name.capitalize()
            try {
                def instantRunTask = project.tasks.getByName("transformClassesWithInstantRunFor${variantName}")
                if (instantRunTask) {
                    throw new GradleException(
                            "StringFog does not support instant run mode, please trigger build"
                                    + " by assemble${variantName} or disable instant run"
                                    + " in 'File->Settings...'."
                    )
                }
            } catch (UnknownTaskException e) {
                // Not in instant run mode, continue.
            }
        }
    }

    void applyLibrary(Project project, def android) {
        android.registerTransform(new StringFogTransformForLibrary(project, android.libraryVariants))
    }

}
