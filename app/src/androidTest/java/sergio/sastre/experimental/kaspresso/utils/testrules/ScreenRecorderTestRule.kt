package sergio.sastre.experimental.kaspresso.utils.testrules

import com.kaspersky.adbserver.device.AdbTerminal
import com.kaspersky.kaspresso.annotations.RequiresAdbServer
import com.kaspersky.kaspresso.device.server.AdbServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import java.util.concurrent.Executors

/**
 * ScreenRecorderTest rule that enables screen recording for each test annotated with [RecordScreenTest]
 *
 * CHECK LIST
 * 1) What happens when recording more than 180 seconds (screenrecord times out)?
 * 2) Sleep 1 sufficient if recording long (e.g. 180 seconds) due to bigger file ?
 * 3) How it behaves on different APIS from API 21 to API 30
 *     API 21 ?
 *     API 22 ✗ - Recordings created but not playable. Same result if trying with AS screenrecord button
 *     API 23 ?
 *     API 25 ?
 *     API 26 ?
 *     API 26 ?
 *     API 27 ?
 *     API 28 ?
 *     API 29 ✓
 * 4) mkdir command would not run on WINDOWS
 * 5) Does it work with test sharding -> is "adb shell -e/-s" required to screenrecord on the device
 *    running the test if several connected?)
 */
@RequiresAdbServer
class ScreenRecorderTestRule internal constructor(
    private val adbServer: AdbServer,
    private val recordingsPath: String
) : TestWatcher() {

    companion object {
        const val FAILURE_DIRECTORY = "/failed"
        const val SUCCESS_DIRECTORY = "/successful"
    }

    private val failurePath = recordingsPath + FAILURE_DIRECTORY
    private val successPath = recordingsPath + SUCCESS_DIRECTORY

    private val executor = Executors.newCachedThreadPool()

    init {
        AdbTerminal.apply {
            connect()
            createDirectoryIfMissing(failurePath)
            createDirectoryIfMissing(successPath)
        }
    }

    private fun AdbTerminal.createDirectoryIfMissing(path: String) {
        val file = File(recordingsPath)
        if (!file.exists() || !file.isDirectory) {
            executeCmd("mkdir -p $path")
        }
    }

    private fun Description?.markedAsRecordTest(): Boolean =
        !this?.annotations?.filterIsInstance(RecordScreenTest::class.java).isNullOrEmpty()

    private fun Description?.recordTestAnnotation() : RecordScreenTest? =
        this?.annotations
            ?.filterIsInstance(RecordScreenTest::class.java)
            ?.firstOrNull()

    private fun startFilming(recordName: String) {
        // Do not bloc the main thread, otherwise no UI interaction is possible
        // till the screenrecord process is killed
        executor.execute {
            AdbTerminal.apply {
                executeAdb("shell screenrecord /sdcard/${recordName}.mp4")
            }
        }
    }

    private fun stopFilming(recordName: String, savingPath: String, pullFile: Boolean) {
        // Execute on main thread to block other tests start till the recording is erased
        adbServer.apply {
            performCmd("sleep 1")
            performAdb("shell pkill -SIGINT screenrecord")
            if (pullFile) {
                performCmd("sleep 1")
                performAdb("pull /sdcard/${recordName}.mp4 $savingPath")
            }
            performAdb("shell rm /sdcard/${recordName}.mp4")
        }
    }

    override fun starting(description: Description?) {
        val needsRecord = description.markedAsRecordTest()
        if (needsRecord) {
            val recordName =
                "${description!!.className}_${description.methodName}".replace(".", "_")
            startFilming(recordName)
        }
    }

    override fun failed(throwable: Throwable?, description: Description?) {
        val needsRecord = description.markedAsRecordTest()
        if (needsRecord) {
            val recordName =
                "${description!!.className}_${description.methodName}".replace(".", "_")
            stopFilming(recordName, failurePath, true)
        }
    }

    override fun succeeded(description: Description?) {
        val needsRecord = description.markedAsRecordTest()
        if (needsRecord) {
            val recordName =
                "${description!!.className}_${description.methodName}".replace(".", "_")
            val pullFile = description.recordTestAnnotation()?.alsoOnSuccess == true

            stopFilming(recordName, successPath, pullFile)
        }
    }
}