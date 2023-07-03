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

package me.proton.test.fusion.ui.espresso

import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.proton.test.fusion.Config
import me.proton.test.fusion.FusionConfig.Espresso
import me.proton.test.fusion.FusionConfig.fusionTag
import me.proton.test.fusion.ui.common.ActionHandler
import java.util.concurrent.TimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

interface EspressoWaiter {
    fun <T> T.waitFor(
        watchTimeout: Duration = Espresso.waitTimeout.get(),
        watchInterval: Duration = Espresso.watchInterval.get(),
        config: Config = Espresso,
        conditionBlock: () -> Any,
    ): T = apply {
        config.before()
        runBlocking {

            val timeoutMessage = "Code block did not succeed in ${watchTimeout.inWholeMilliseconds}ms"
            var throwableToThrow: Throwable = TimeoutException(timeoutMessage)

            try {
                /** set minimum timeout to 1 millisecond, so check does not fail immediately **/
                val timeout = if (watchTimeout == ZERO) 1.milliseconds else watchTimeout

                withTimeout(timeout) {
                    while (true) {
                        ActionHandler
                            .handle(conditionBlock)
                            .onFailure {
                                throwableToThrow = it
                                delay(watchInterval)
                            }
                            .onSuccess {
                                return@withTimeout
                            }
                    }
                }
                config.onSuccess()
            } catch (ex: TimeoutCancellationException) {
                Log.e(fusionTag, timeoutMessage, ex)
                config.onFailure()
                throw throwableToThrow
            } finally {
                config.after()
            }
        }
    }
}