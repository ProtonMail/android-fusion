/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
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
