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

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.FusionConfig.Compose.testRule
import me.proton.test.fusion.FusionConfig.Compose.useUnmergedTree
import me.proton.test.fusion.FusionConfig.fusionTag
import me.proton.test.fusion.ui.common.ActionHandler.handle
import kotlin.time.Duration

object ComposeWaiter {
    fun <T> T.waitFor(
        timeout: Duration = FusionConfig.Compose.waitTimeout.get(),
        block: () -> Any,
    ): T = apply {
        testRule.get().let { testRule ->
            var throwable: Throwable = ComposeTimeoutException("Condition timed out")
            testRule.waitForIdle()
            try {
                FusionConfig.Compose.before()
                testRule.waitUntil(timeout.inWholeMilliseconds) {
                    handle {
                        block()
                    }.onSuccess {
                        it.handleLog()
                        FusionConfig.Compose.onSuccess()
                    }.onFailure {
                        throwable = it
                    }.isSuccess
                }
                FusionConfig.Compose.after()
            } catch (ex: ComposeTimeoutException) {
                FusionConfig.Compose.onFailure()
                if (FusionConfig.Compose.shouldPrintToLog.get()) {
                    testRule.onRoot(useUnmergedTree = useUnmergedTree.get()).printToLog(fusionTag)
                }
                throw throwable
            }
        }
    }

    private fun <T> T.handleLog(shouldPrint: Boolean = FusionConfig.Compose.shouldPrintToLog.get()) =
        apply {
            if (!shouldPrint)
                return@apply
            when (this) {
                is SemanticsNodeInteraction -> printToLog(fusionTag)
                is SemanticsNodeInteractionCollection -> printToLog(fusionTag)
            }
        }
}
