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


import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.fusionTag
import org.junit.runner.Description
import java.io.File

/**
 * Controls device system parameters, and helper functions where system shell is used.
 */
object Shell {

    private val automation = InstrumentationRegistry.getInstrumentation().uiAutomation!!
    private val screenshotLocation =
        InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(null)
            ?.resolve("Screenshots")
    private val artifactsLocation =
        InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(null)
            ?.resolve(FusionConfig.targetContext().packageName)
            ?.resolve("artifacts")

    /**
     * Sets up device in ready for automation mode.
     * Animations turned off, long press timeout is set to 2 seconds, notifications popups are disabled.
     */
    fun setupDeviceForAutomation(shouldDisableNotifications: Boolean) {
        automation.executeShellCommand("settings put secure long_press_timeout 2000")
        automation.executeShellCommand("settings put global animator_duration_scale 0.0")
        automation.executeShellCommand("settings put global transition_animation_scale 0.0")
        automation.executeShellCommand("settings put global window_animation_scale 0.0")
        automation.executeShellCommand("settings put global heads_up_notifications_enabled $shouldDisableNotifications")
    }

    /**
     * Can be used to test file sharing from outside of the app.
     * @param mimeType - file mime type
     * @param fileName - name of the file to share
     */
    fun sendShareFileIntent(mimeType: String, fileName: String) {
        automation
            .executeShellCommand(
                "am start -a android.intent.action.SEND -t $mimeType " +
                        "--eu android.intent.extra.STREAM " +
                        "file:///data/data/${FusionConfig.targetContext().packageName}/files/$fileName " +
                        " --grant-read-uri-permission"
            )
    }

    /**
     * Clears logcat.
     */
    fun clearLogcat() {
        automation.executeShellCommand("logcat -c")
    }

    /**
     * Saves file with given [Description].
     */
    fun saveToFile(description: Description?) {
        val logcatFile = File(artifactsLocation, "${description?.methodName}-logcat.txt")
        automation.executeShellCommand(
            "run-as ${FusionConfig.targetContext().packageName} -d -f $logcatFile"
        )
    }

    /**
     * Takes screenshot and saves to /sdcard/screenshots.
     */
    fun takeScreenshot(fileName: String) {
        val screenshotFileName = "$screenshotLocation/$fileName.png"
        Log.d(
            fusionTag,
            "Test \"$fileName\" failed. Saving screenshot to $screenshotFileName"
        )
        automation.executeShellCommand("screencap -p $screenshotFileName")
    }
}
