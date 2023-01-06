package me.proton.fusion.ui.common

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.proton.fusion.FusionConfig
import java.util.concurrent.atomic.AtomicBoolean

object ActionHandler {
    private val exceptionCaughtFlag: AtomicBoolean = AtomicBoolean(false)
    private val delayInterval = FusionConfig.Compose.watchInterval.get()

    fun <T> handle(block: () -> T): Result<T> = try {
        Result
            .success(block())
            .apply {
                exceptionCaughtFlag.set(false)
            }
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
                Log.d(FusionConfig.fusionTag, "Condition was not met immediately: $it")
            }
    }
}