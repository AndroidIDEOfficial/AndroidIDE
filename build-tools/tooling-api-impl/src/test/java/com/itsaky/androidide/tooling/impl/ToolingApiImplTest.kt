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

import com.android.builder.model.v2.ide.ProjectType
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
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
        log.debug(result.syncIssues.asJson())
        assertThat(project).isNotNull()
        assertThat(project!!).isInstanceOf(IdeGradleProject::class.java)

        val isInitialized = server.isInitialized().get()
        assertThat(isInitialized).isTrue()

        val app = project.findByPath(":app")
        assertThat(app).isNotNull()
        assertThat(app).isInstanceOf(IdeAndroidModule::class.java)
        assertAndroidModule(app as IdeAndroidModule)

        val javaLibrary = project.findByPath(":java-library")
        assertThat(javaLibrary).isNotNull()
        assertThat(javaLibrary).isInstanceOf(IdeGradleProject::class.java)

        assertThat(project.findByPath(":does-not-exist")).isNull()
    }

    private fun assertAndroidModule(android: IdeAndroidModule) {
        assertThat(android.projectType).isEqualTo(ProjectType.APPLICATION)
        assertThat(android.viewBindingOptions).isNotNull()
        assertThat(android.viewBindingOptions!!.isEnabled).isFalse()
    }

    private fun launchServer(client: IToolingApiClient): IToolingApiServer {
        val builder = ProcessBuilder("java", "-jar", "./build/libs/tooling-api-all.jar")
        log.debug(System.getenv())
        builder.environment().put("ANDROID_SDK_ROOT", findAndroidHome())
        val proc = builder.start()

        Thread(Reader(proc.errorStream)).start()
        val launcher =
            ToolingApiLauncher.createClientLauncher(client, proc.inputStream, proc.outputStream)

        launcher.startListening()

        return launcher.remoteProxy
    }

    private fun findAndroidHome(): String? {
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
