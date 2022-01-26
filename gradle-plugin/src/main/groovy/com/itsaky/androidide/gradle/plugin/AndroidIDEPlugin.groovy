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
package com.itsaky.androidide.gradle.plugin

import groovy.json.JsonBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidIDEPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def manager = project.getPluginManager()
        def isApp = manager.hasPlugin("com.android.application")
        def isLibrary = manager.hasPlugin("com.android.library")

        if (!(isApp || isLibrary)) {
            println "Project " + project.displayName + " does not apply Android's application or library plugin"
            println "AndroidIDE's Gradle Plugin will not be applied to this project"
            return
        }

        if (isApp) {
            project.android.sourceSets {
                main.java.srcDirs += "${System.getenv("HOME")}/logsender"
            }
        }

        project.tasks.create(
                name: "initializeIDEProject",
                group: "AndroidIDE",
                description: "Initializes the project in AndroidIDE. AndroidIDE manages the proper execution of this task." +
                        " You're not supposed to execute this task manually!"
        )
                {
                    outputs.upToDateWhen { false }

                    doLast {
                        def root = project.getRootProject()

                        IDEProject ideProject = new IDEProject()
                        ideProject.name = root.getName()
                        ideProject.displayName = root.getDisplayName()
                        ideProject.description = root.getDescription()
                        ideProject.path = root.getPath()
                        ideProject.projectDir = root.getProjectDir().getAbsolutePath()

                        def tasks = root.tasks
                        if(tasks != null && tasks.size() > 0) {
                            addTasks(ideProject, root)
                        }

                        root.subprojects.forEach { sub ->
                            addIDEProject(ideProject, sub)
                        }

                        File out = new File("${System.getenv( 'TMPDIR')}/ide_project")
                        if(out.exists()) {
                            out.delete()
                        }
                        out << new JsonBuilder( ideProject ).toPrettyString()
                        println ">>> PROJECT INITIALIZED <<<"
                    }
                }

        project.afterEvaluate {
            project.tasks.getByName('compileDebugJavaWithJavac').finalizedBy('initializeIDEProject')
            project.tasks.getByName('compileReleaseJavaWithJavac').finalizedBy('initializeIDEProject')
        }
    }

    def addIDEProject (IDEProject parent, Project sub) {
        if(parent == null || sub == null) {
            return
        }

        def isApp = sub.getPluginManager().hasPlugin("com.android.application")
        def isLibrary = sub.getPluginManager().hasPlugin("com.android.library")

        if(!(isApp || isLibrary)) {
            return
        }

        IDEModule module = new IDEModule()
        if(isApp) {
            module = new IDEAppModule()
        }

        def android = sub.android
        def config = android.defaultConfig

        module.name = sub.getName()
        module.displayName = sub.getDisplayName()
        module.description = sub.getDescription()
        module.path = sub.getPath()
        module.buildToolsVersion = android.buildToolsVersion
        module.compileSdkVersion = android.compileSdkVersion
        module.minSdk = config.minSdkVersion
        module.targetSdk = config.targetSdkVersion
        module.versionCode = config.versionCode
        module.versionName = config.versionName
        module.projectDir = sub.getProjectDir().getAbsolutePath()
        module.viewBindingEnabled = android.buildFeatures.viewBinding

        if(isApp) {
            module.applicationId = config.applicationId
        }

        def tasks = sub.tasks
        if(tasks != null && tasks.size() > 0) {
            addTasks(module, sub)
        }

        def variants = null
        if(isApp) {
            variants = sub.android.applicationVariants
        } else if (isLibrary) {
            variants = sub.android.libraryVariants
        } else {
            println "AndroidIDE: Cannot find build variants of project: ${module.name}(${module.path})"
        }

        if(variants != null) {
            variants.all { variant ->
                addDependencies(module, variant)
            }
        }

        def subs = sub.getSubprojects()
        if(subs != null && subs.size() > 0) {
            subs.forEach { subOfSub ->
                addIDEProject(module, subOfSub)
            }
        }
        parent.modules.add(module)
    }

    static def addDependencies(def module, def variant) {
        variant.getCompileClasspath().each { dependency ->
            def path = dependency.absolutePath
            if(!module.dependencies.contains(path)) {
                module.dependencies.add(dependency.absolutePath)
            }
        }
    }

    static def addTasks (IDEProject project, Project gradleProject) {
        if(project == null || gradleProject == null) {
            return
        }

        gradleProject.tasks.forEach { gradleTask ->
            IDETask task = new IDETask()
            task.name = gradleTask.getName()
            task.description = gradleTask.getDescription()
            task.group = gradleTask.getGroup()
            task.path = gradleTask.getPath()

            project.tasks.add(task)
        }
    }
}