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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class ToolingApiImplTest {

    private val log = ILogger.newInstance(javaClass.simpleName)

    @Test
    fun testLaunch() {
        val client = TestClient()
        val server = launchServer(client)

        server.initialize(InitializeProjectParams(getTestProject())).get()

        val isInitialized = server.isInitialized().get()

        log.debug("Is server initialized: $isInitialized")
        assertThat(isInitialized).isTrue()
    }

    private fun launchServer(client: IToolingApiClient): IToolingApiServer {
        val builder = ProcessBuilder("java", "-jar", "./build/libs/tooling-api-all.jar")
        val proc = builder.start()

        Thread(Reader(proc.errorStream)).start()
        val launcher =
            Launcher.createLauncher(
                client, IToolingApiServer::class.java, proc.inputStream, proc.outputStream)

        launcher.startListening()

        return launcher.remoteProxy
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
}
