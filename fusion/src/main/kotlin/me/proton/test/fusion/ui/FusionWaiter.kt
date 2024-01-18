/*
 * Copyright (c) 2022 Proton Technologies AG
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

package me.proton.test.fusion.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import me.proton.test.fusion.Config
import me.proton.test.fusion.FusionConfig.Espresso
import kotlin.time.Duration

interface FusionWaiter {
    fun <T> T.waitFor(
        config: Config = Espresso,
        timeout: Duration = config.waitTimeout.get(),
        interval: Duration = config.watchInterval.get(),
        conditionBlock: () -> Any,
    ): T = apply {
        config.before()

        var throwableToThrow: Throwable = IllegalStateException("FusionWaiter timed out")

        runBlocking {
            val result = withTimeoutOrNull(timeout) {
                while (isActive) {
                    runCatching(conditionBlock)
                        .onFailure {
                            throwableToThrow = it
                            delay(interval)
                        }
                        .onSuccess {
                            config.onSuccess()
                            return@withTimeoutOrNull
                        }
                }
            }

            config.after()

            if (result == null) {
                config.onFailure()
                throw throwableToThrow.fillInStackTrace()
            }
        }
    }
}
