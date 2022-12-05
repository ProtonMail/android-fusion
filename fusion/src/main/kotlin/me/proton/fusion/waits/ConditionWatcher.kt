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

import me.proton.fusion.FusionConfig.commandTimeout
import kotlin.time.Duration
import java.util.concurrent.TimeoutException
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface ConditionWatcher {

    /**
     * Waits until [conditionBlock] does not throw any exceptions
     * @throws Exception which was last caught during condition check after given [watchTimeout] ms
     */
    fun waitForCondition(
        watchTimeout: Duration = commandTimeout,
        watchInterval: Long = 250L,
        conditionBlock: () -> Unit
    ) {
        var throwable: Throwable =
            TimeoutException("Condition was not met in $watchTimeout ms. No exceptions caught.")
        var currentTimestamp =  System.currentTimeMillis()

        //Shell.takeScreenshot()
        throw throwable
    }
}
