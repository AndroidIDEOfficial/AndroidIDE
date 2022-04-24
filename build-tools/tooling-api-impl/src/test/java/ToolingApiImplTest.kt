import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.tooling.api.messages.ProjectInitializedParams
import com.itsaky.androidide.utils.ILogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
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
        val streams = launchServer()
        val client = TestClient()
        val launcher =
            Launcher.createLauncher(
                client, IToolingApiServer::class.java, streams.first, streams.second)

        launcher.startListening()

        val server = launcher.remoteProxy

        server.initialize(InitializeProjectParams(File("."))).get()

        val isInitialized = server.isInitialized().get()

        log.debug("Is server initialized: $isInitialized")
        assertThat(isInitialized).isTrue()
    }

    private fun launchServer(): Pair<InputStream, OutputStream> {
        val builder = ProcessBuilder("java", "-jar", "./build/libs/tooling-api-all.jar")
        val proc = builder.start()

        Thread(Reader(proc.errorStream)).start()

        return Pair(proc.inputStream, proc.outputStream)
    }

    private class TestClient : IToolingApiClient {
        private val log = ILogger.newInstance(javaClass.simpleName)
        override fun onProjectInitialized(params: ProjectInitializedParams) {
            log.logThis()
        }
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
}
