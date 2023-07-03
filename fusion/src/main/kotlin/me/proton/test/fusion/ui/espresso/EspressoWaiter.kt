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
import me.proton.test.fusion.FusionConfig.Espresso
import me.proton.test.fusion.FusionConfig.fusionTag
import me.proton.test.fusion.ui.common.ActionHandler
import java.util.concurrent.TimeoutException
import kotlin.time.Duration

interface EspressoWaiter {
    fun <T> T.waitFor(
        watchTimeout: Duration = Espresso.waitTimeout.get(),
        watchInterval: Duration = Espresso.watchInterval.get(),
        conditionBlock: () -> Any,
    ): T = apply {
        runBlocking {
            Espresso.before()

            val timeoutMessage = "Code block did not succeed in ${watchTimeout.inWholeMilliseconds}ms"
            var throwableToThrow: Throwable = TimeoutException(timeoutMessage)

            try {
                withTimeout(watchTimeout) {
                    while (true) {
                        ActionHandler
                            .handle(conditionBlock)
                            .onFailure {
                                throwableToThrow = it
                                delay(watchInterval)
                            }
                            .onSuccess {
                                Espresso.onSuccess()
                                return@withTimeout
                            }
                    }
                }
            } catch (ex: TimeoutCancellationException) {
                Log.e(fusionTag, timeoutMessage, ex)
                Espresso.onFailure()
                throw throwableToThrow
            } finally {
                Espresso.after()
            }
        }
    }
}