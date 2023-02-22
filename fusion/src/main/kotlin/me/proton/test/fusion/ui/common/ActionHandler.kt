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

package me.proton.test.fusion.ui.common

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.FusionConfig.fusionTag
import java.util.concurrent.atomic.AtomicBoolean

object ActionHandler {
    private val exceptionCaughtFlag: AtomicBoolean = AtomicBoolean(false)
    private val delayInterval = FusionConfig.Compose.watchInterval.get()

    fun <T> handle(block: () -> T): Result<T> = try {
        Result
            .success(block())
            .apply { exceptionCaughtFlag.set(false) }
    } catch (throwable: Throwable) {
        throwable.takeIf { !exceptionCaughtFlag.get() }?.handleErrorLog()
        exceptionCaughtFlag.set(true)
        runBlocking { delay(delayInterval) }
        Result.failure(throwable)
    }

    private fun Throwable.handleErrorLog() {
        // Relevant information is usually on the second line of AssertionError message
        // In other cases it's the first line
        val messageLine = if (this is AssertionError && message?.lines()?.count()!! > 1) 1 else 0
        message
            ?.lines()
            ?.get(messageLine)
            .let {
                Log.d(fusionTag, "Condition was not met immediately: $it")
            }
    }
}
