package me.proton.fusion.ui.espresso

import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.ui.common.ActionHandler
import java.util.concurrent.TimeoutException
import kotlin.time.Duration

interface EspressoWaiter {
    fun <T> T.waitFor(
        watchTimeout: Duration = FusionConfig.commandTimeout,
        watchInterval: Duration = FusionConfig.watchInterval,
        conditionBlock: () -> Any,
    ): T = apply {
        runBlocking {
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
                                return@withTimeout
                            }
                    }
                }
            } catch (ex: TimeoutCancellationException) {
                Log.e(fusionTag, timeoutMessage, ex)
                throw throwableToThrow
            }
        }
    }
}