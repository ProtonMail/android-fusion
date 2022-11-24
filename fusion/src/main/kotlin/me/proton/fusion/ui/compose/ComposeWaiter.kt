package me.proton.fusion.ui.compose

import android.util.Log
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.Compose.shouldPrintToLog
import me.proton.fusion.FusionConfig.commandTimeout
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.FusionConfig.watchInterval
import java.util.concurrent.TimeoutException
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface ComposeWaiter {
    val composeTestRule: ComposeContentTestRule get() = FusionConfig.Compose.testRule.get()
    val before: () -> Unit get() = { }
    val after: () -> Unit get() = { }
    val onInteraction: () -> Unit get() = { }

    fun <T> T.waitFor(
        timeout: Duration = commandTimeout,
        interval: Duration = watchInterval,
        before: () -> Unit = {  },
        after: () -> Unit = {  },
        block: () -> Any,
    ): T = apply {
        composeTestRule.waitForIdle()
        before.invoke()
        composeTestRule.waitForNoExceptions(timeout) { block() }
        after.invoke()
    }

    private fun ComposeTestRule.waitForNoExceptions(timeout: Duration, condition: () -> Any) {
        var error: Throwable = TimeoutException("Condition not met in ${timeout.inWholeMilliseconds}ms")
        try {
            waitUntil(timeout.inWholeMilliseconds) {
                val result = isSuccessful { condition() }
                error = result.second ?: error
                result.first
            }
        } catch (e: ComposeTimeoutException) {
            Log.e(fusionTag, e.message!!)
            e.handleComposeLog(true)
            throw error
        }
    }

    private fun <T> isSuccessful(block: () -> T): Pair<Boolean, Throwable?> =
        try {
            block().handleComposeLog(shouldPrintToLog.get())
            true to null
        } catch (throwable: Throwable) {
            val messageLine = when (throwable) {
                /** Thrown on Compose failed actions and assertions **/
                is AssertionError -> 1
                /** Thrown when Compose view is not ready **/
                is IllegalStateException -> 0
                else -> throw throwable
            }
            val line = throwable.message?.lines()?.get(messageLine)
            Log.d(fusionTag, "Condition not yet met: $line")
            runBlocking { delay(watchInterval) }
            false to throwable
        }

    private fun <T> T.handleComposeLog(shouldPrint: Boolean) = apply {
        if (!shouldPrint)
            return@apply
        when (this) {
            is SemanticsNodeInteraction -> printToLog(fusionTag)
            is SemanticsNodeInteractionCollection -> printToLog(fusionTag)
            is ComposeTimeoutException -> composeTestRule.onRoot().printToLog(fusionTag)
        }
    }
}
