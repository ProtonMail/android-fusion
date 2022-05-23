package me.proton.fusion.waits

import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingResourceTimeoutException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.commandTimeout
import me.proton.fusion.FusionTest
import me.proton.fusion.utils.ActivityProvider.currentActivity
import me.proton.fusion.utils.Shell
import org.hamcrest.Matcher

/**
 * Contains wait functions and retry actions.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object Waits : ConditionWatcher {

    /**
     * Tries to perform an action and retries within provided time period if action fails.
     * @param interaction - [ViewInteraction] parameter.
     * @param action - [ViewAction] that should be performed.
     * @param timeout - optional timeout parameter to wait for the assertion fulfillment.
     */
    fun performActionWithRetry(
        interaction: ViewInteraction,
        action: ViewAction,
        timeout: Long = commandTimeout
    ): ViewInteraction {
        waitForCondition { interaction.perform(action) }
        return interaction
    }

    /**
     * Tries to perform an action until [ViewAssertion] fulfilled.
     * @param interaction - [ViewInteraction] parameter.
     * @param assertion - [ViewAssertion] that should be fulfilled.
     * @param matcher - [Matcher] object that should be matched.
     * @param action - [ViewAction] that should be performed.
     * @param timeout - optional timeout parameter to wait for the assertion fulfillment.
     */
    fun performActionUntilMatcherFulfilled(
        interaction: ViewInteraction,
        assertion: ViewAssertion,
        matcher: Matcher<View>,
        action: ViewAction,
        timeout: Long = commandTimeout
    ): ViewInteraction {
        waitForCondition {
            interaction.perform(action)
            Espresso.onView(matcher).check(assertion)
        }

        return interaction
    }

    fun waitUntilRecyclerViewPopulated(@IdRes id: Int, timeout: Long = commandTimeout) {
        val timedOutResources = ArrayList<String>()

        waitForCondition {
            try {
                val rv = currentActivity!!.findViewById<RecyclerView>(id)
                if (rv != null) {
                    waitUntilLoaded { rv }
                    rv.adapter!!.itemCount > 0
                }
            } catch (e: Throwable) {
                timedOutResources.add(e.stackTrace.toString())
                throw IdlingResourceTimeoutException(timedOutResources)
            }
        }
    }

    /**
     * Stop the test until RecyclerView's data gets loaded.
     * Passed [recyclerProvider] will be activated in UI thread, allowing you to retrieve the View.
     * Workaround for https://issuetracker.google.com/issues/123653014.
     */
    inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
        Espresso.onIdle()
        lateinit var recycler: RecyclerView

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recycler = recyclerProvider()
        }

        while (recycler.hasPendingAdapterUpdates()) {
            Thread.sleep(10)
        }
    }

    /**
     * Waits until [conditionBlock] is true.
     * @throws Exception which was last caught during condition check after given [watchTimeout] ms.
     */
    fun waitUntil(
        watchTimeout: Long = commandTimeout,
        watchInterval: Long = 250L,
        conditionBlock: () -> Boolean,
    ) = runBlocking {
        runCatching {
            withTimeoutOrNull(watchTimeout) {
                while (true) {
                    if (!conditionBlock()) delay(watchInterval) else break
                }
            }
        }.onFailure {
            Log.d(
                FusionConfig.fusionTag,
                "Test \"${FusionTest.testName.methodName}\" failed. Saving screenshot"
            )
            Shell.takeScreenshot()
            throw it
        }
    }
}
