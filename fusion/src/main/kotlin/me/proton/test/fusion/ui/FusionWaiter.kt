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
