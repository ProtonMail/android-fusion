/*
 * Copyright (c) 2021 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.fusion.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.fusionTag
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Helper class to deal with files and folders used during testing.
 */
object FileUtils {

    private val automation = InstrumentationRegistry.getInstrumentation().uiAutomation!!

    /**
     * Deletes artifacts folder from /sdcard/Download.
     */
    fun deleteDownloadArtifactsFolder() {
        val downloadArtifactsPath =
            FusionConfig.targetContext.getExternalFilesDir(null)!!.absolutePath
        automation.executeShellCommand("rm -rf $downloadArtifactsPath")
    }

    /**
     * Creates or clears directory with provided [path].
     */
    fun prepareArtifactsDir(path: String) {
        val dir = File(path)
        if (!dir.exists()) {
            Log.v(fusionTag, "Path $path does not exist. Creating directories.")
            dir.mkdirs()
            return
        }

        dir.list()?.forEach {
            val message = if (File(it).delete()) "Deleted $it" else "Could not delete $it"
            Log.v(fusionTag, message)
        }
    }

    /**
     * Copies files from test project assets into main app files internal storage.
     * @param fileName - name of the file which exists in androidTests/assets folder.
     */
    fun copyAssetFileToInternalFilesStorage(fileName: String) {
        val testContext = InstrumentationRegistry.getInstrumentation().context
        val file = File("${FusionConfig.targetContext.filesDir.path}/$fileName")

        if (!file.exists()) {
            try {
                testContext.assets.open(fileName).use { it.copyTo(FileOutputStream(file)) }
            } catch (e: Exception) {
                throw IOException(e)
            }
        }
    }


    /**
     * Creates new activity result for a file in test app assets.
     * @param fileName - name of the file that will be copied from test app assets to the main app files.
     */
    fun createFileResultFromAssets(fileName: String): Instrumentation.ActivityResult {
        val resultIntent = Intent()

        // Declare variables for test and application context.
        val testContext = InstrumentationRegistry.getInstrumentation().context
        val file = File("${FusionConfig.targetContext.cacheDir}/$fileName")

        if (!file.exists()) {
            try {
                testContext.assets.open(fileName).use { it.copyTo(FileOutputStream(file)) }
            } catch (e: Exception) {
                throw IOException(e)
            }
        }

        // Build a stubbed result from temp file.
        resultIntent.data = Uri.fromFile(file)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)
    }
}
