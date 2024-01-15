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

package me.proton.test.fusion.ui.compose

import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.proton.test.fusion.FusionConfig.Compose
import me.proton.test.fusion.FusionConfig.fusionTag
import me.proton.test.fusion.ui.compose.wrappers.ComposeInteraction
import kotlin.time.Duration

object ComposeWaiter {
    fun <T> ComposeInteraction<T>.waitFor(
        timeout: Duration = Compose.waitTimeout.get(),
        interval: Duration = Compose.watchInterval.get(),
        block: () -> Any,
    ): ComposeInteraction<T> = apply {
        Compose.testRule.get().let { testRule ->
            var throwableToThrow: Throwable = IllegalStateException("ComposeWaiter timed out")
            var callSuccessful = false

            Compose.before()

            testRule.waitForIdle()

            runCatching {
                testRule.waitUntil(timeout.inWholeMilliseconds) {
                    runCatching(block)
                        .onSuccess {
                            Compose.onSuccess()
                        }.onFailure {
                            throwableToThrow = it
                            runBlocking { delay(interval) }
                        }
                        .isSuccess
                        .also { callSuccessful = it }
                }
            }.onFailure {
                testRule
                    .onRoot(shouldUseUnmergedTree)
                    .takeIf { Compose.shouldPrintHierarchyOnFailure.get() }
                    ?.printToLog(fusionTag)
                Compose.onFailure()
            }

            Compose.after()

            if (!callSuccessful) {
                throwableToThrow.let { throw it }
            }
        }
    }
}
