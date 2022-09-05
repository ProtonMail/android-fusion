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

package me.proton.fusion.waits

import android.util.Log
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.commandTimeout
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.utils.Shell
import java.util.concurrent.TimeoutException

interface ConditionWatcher {

    /**
     * Waits until [conditionBlock] does not throw any exceptions
     * @throws Exception which was last caught during condition check after given [watchTimeout] ms
     */
    fun waitForCondition(
        watchTimeout: Long = commandTimeout,
        watchInterval: Long = 250L,
        conditionBlock: () -> Unit
    ) {
        var throwable: Throwable =
            TimeoutException("Condition was not met in $watchTimeout ms. No exceptions caught.")
        var currentTimestamp = System.currentTimeMillis()
        val timeoutTimestamp = currentTimestamp + watchTimeout

        while (currentTimestamp < timeoutTimestamp) {
            currentTimestamp = System.currentTimeMillis()
            try {
                return conditionBlock()
            } catch (e: Throwable) {
                val firstLine = e.message?.split("\n")?.get(0)
                Log.v(
                    fusionTag,
                    "Waiting for condition. ${timeoutTimestamp - currentTimestamp}ms remaining. Status: $firstLine"
                )
                throwable = e
            }
            Thread.sleep(watchInterval)
        }
        //Shell.takeScreenshot()
        throw throwable
    }

    fun waitForBoolCondition(
        watchTimeout: Long = commandTimeout,
        watchInterval: Long = 250L,
        conditionBlock: () -> Boolean
    ) {
        var timeInterval = 0L
        var throwable: Throwable =
            TimeoutException("Condition was not met in $watchTimeout ms. No exceptions caught.")

        while (timeInterval < watchTimeout) {
            try {
                if (conditionBlock()) return
            } catch (e: Throwable) {
                val firstLine = e.message?.split("\n")?.get(0)
                Log.v(
                    FusionConfig.fusionTag,
                    "Waiting for condition. ${watchTimeout - timeInterval}ms remaining. Status: $firstLine"
                )
                throwable = e
            }
            timeInterval += watchInterval
            Thread.sleep(watchInterval)
        }
        //Log.d(FusionConfig.fusionTag, "Test \"${testName.methodName}\" failed. Saving screenshot")
        //Shell.takeScreenshot()
        throw throwable
    }

}
