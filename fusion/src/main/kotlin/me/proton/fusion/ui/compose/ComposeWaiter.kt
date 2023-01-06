package me.proton.fusion.ui.compose

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.Compose.testRule
import me.proton.fusion.FusionConfig.Compose.useUnmergedTree
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.ui.common.ActionHandler.handle
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

    private fun <T> T.handleLog(shouldPrint: Boolean = FusionConfig.Compose.shouldPrintToLog.get()) = apply {
        if (!shouldPrint)
            return@apply
        when (this) {
            is SemanticsNodeInteraction -> printToLog(FusionConfig.fusionTag)
            is SemanticsNodeInteractionCollection -> printToLog(FusionConfig.fusionTag)
        }
    }
}
