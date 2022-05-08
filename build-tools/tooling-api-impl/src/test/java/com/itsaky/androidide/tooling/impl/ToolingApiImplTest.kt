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

package com.itsaky.androidide.tooling.impl

import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.android.builder.model.v2.ide.ProjectType
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeJavaModule
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class ToolingApiImplTest {

    private val log = ILogger.newInstance(javaClass.simpleName)

    @Test
    fun testProjectInit() {
        val client = TestClient()
        val server = launchServer(client)

        val result =
            server.initialize(InitializeProjectMessage(getTestProject().absolutePath)).get()
        val project = result.project
        assertThat(project).isNotNull()
        assertThat(project!!).isInstanceOf(IdeGradleProject::class.java)

        val isInitialized = server.isInitialized().get()
        assertThat(isInitialized).isTrue()

        val app = project.findByPath(":app")
        assertThat(app).isNotNull()
        assertThat(app).isInstanceOf(IdeAndroidModule::class.java)

        assertThat((app as IdeAndroidModule).javaCompileOptions).isNotNull()
        assertThat(app.javaCompileOptions.sourceCompatibility).isEqualTo("11")
        assertThat(app.javaCompileOptions.targetCompatibility).isEqualTo("11")
        assertThat(app.javaCompileOptions.isCoreLibraryDesugaringEnabled).isFalse()

        assertThat(app.projectType).isEqualTo(ProjectType.APPLICATION)

        assertThat(app.viewBindingOptions).isNotNull()
        assertThat(app.viewBindingOptions!!.isEnabled).isTrue()

        // There are always more than 100 tasks in an android module
        // Also, the tasks must contain the user defined tasks
        assertThat(app.tasks.size).isAtLeast(100)
        assertThat(app.tasks.first { it.path == "${app.projectPath}:thisIsATestTask" }).isNotNull()

        assertThat(app.variantDependencies).hasSize(2)
        assertThat(app.variantDependencies).containsKey("debug")
        assertThat(app.variantDependencies["debug"]).isNotNull()
        assertThat(app.variantDependencies).containsKey("release")
        assertThat(app.variantDependencies["release"]).isNotNull()

        // Assert that there is at least one dependency on another module in both of the variants
        assertThat(
                app.variantDependencies["debug"]!!.libraries.values.filter { it.type == PROJECT })
            .isNotEmpty()
        assertThat(
                app.variantDependencies["release"]!!.libraries.values.filter { it.type == PROJECT })
            .isNotEmpty()

        val javaLibrary = project.findByPath(":java-library")
        assertThat(javaLibrary).isNotNull()
        assertThat(javaLibrary).isInstanceOf(IdeJavaModule::class.java)

        assertThat(project.findByPath(":does-not-exist")).isNull()
    }

    private fun launchServer(client: IToolingApiClient): IToolingApiServer {
        val builder = ProcessBuilder("java", "-jar", "./build/libs/tooling-api-all.jar")
        log.debug(System.getenv())
        builder.environment()["ANDROID_SDK_ROOT"] = findAndroidHome()
        val proc = builder.start()

        Thread(Reader(proc.errorStream)).start()
        val launcher =
            ToolingApiLauncher.createClientLauncher(client, proc.inputStream, proc.outputStream)

        launcher.startListening()

        return launcher.remoteProxy
    }

    private fun findAndroidHome(): String {
        var fromEnv = System.getenv("ANDROID_SDK_ROOT")
        if (fromEnv != null) {
            return fromEnv
        }

        fromEnv = System.getenv("ANDROID_HOME")
        if (fromEnv != null) {
            return fromEnv
        }

        val home = System.getProperty("user.home")
        if (System.getProperty("os.name").contains("Windows")) {
            return "$home/AppData/Local/Android/Sdk"
        }

        return "$home/android-sdk"
    }

    private class TestClient : IToolingApiClient {
        private val log = ILogger.newInstance(javaClass.simpleName)
        override fun logMessage(line: LogLine) {
            log.log(ILogger.priority(line.priorityChar), line.formattedTagAndMessage())
        }

        override fun logOutput(line: String) {
            log.debug(line.trim())
        }

        override fun prepareBuild() {}
        override fun onBuildSuccessful(result: BuildResult) {}
        override fun onBuildFailed(result: BuildResult) {}

        override fun onProgressEvent(event: ProgressEvent) {}

        override fun getBuildArguments(): CompletableFuture<List<String>> {
            return CompletableFuture.completedFuture(emptyList())
        }

        override fun checkGradleWrapperAvailability(): CompletableFuture<GradleWrapperCheckResult> =
            CompletableFuture.completedFuture(GradleWrapperCheckResult(true))
    }

    private class Reader(val input: InputStream) : Runnable {
        private val log = ILogger.newInstance(javaClass.simpleName)
        override fun run() {
            try {
                val reader = BufferedReader(InputStreamReader(input))
                var line = reader.readLine()
                while (line != null) {
                    log.debug(line)
                    line = reader.readLine()
                }
            } catch (error: Throwable) {
                log.error(error)
            }
        }
    }

    fun Any.getTestProject(): File = File("./src/test/test-project")

    fun Any.asJson(): String {
        val builder = GsonBuilder()
        ToolingApiLauncher.configureGson(builder)
        return builder.create().toJson(this)
    }
}
