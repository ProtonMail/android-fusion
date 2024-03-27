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

package me.proton.test.fusion

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.StaleObjectException
import me.proton.test.fusion.ui.uiautomator.ByObject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object FusionConfig : Config() {
    /** Logcat tag **/
    const val fusionTag: String = "FUSION"

    object Compose : Config() {
        val useUnmergedTree: AtomicBoolean = AtomicBoolean(false)
        val testRule: AtomicReference<ComposeTestRule> = AtomicReference(null)
        val shouldPrintHierarchyOnFailure: AtomicBoolean = AtomicBoolean(false)
    }

    object Espresso : Config()

    object Intents : Config()

    object Robot : Config()

    object UiAutomator : Config() {
        /**
         * Set it to true if you would like to declare once [ByObject] and use it multiple times.
         * In this case it will not trigger [StaleObjectException].
         * Example:
         *      val fabButton = byObject.withRes("com.example.app:id/fab_add_task")
         *      fabButton.click()
         *      ...
         *      fabButton.click()
         * As a drawback every object will be searched on each and every action, check and wait functions.
         * This will slow down the test execution but not significantly.
         */
        var shouldSearchByObjectEachAction: Boolean = false
        var shouldSearchUiObjectEachAction: Boolean = false

        /**
         * By calling this function you can speed up UIAutomator execution time significantly.
         */
        fun boost(idleTimeout: Long = 0L, selectorTimeout: Long = 0L, actionTimeout: Long = 50L) {
            Configurator
                .getInstance()
                .apply {
                    waitForIdleTimeout = idleTimeout
                    waitForSelectorTimeout = selectorTimeout
                    actionAcknowledgmentTimeout = actionTimeout
                }
        }
    }
}

open class Config {
    val targetContext: Context get() = InstrumentationRegistry.getInstrumentation().targetContext

    /** Set of default timeouts **/
    val waitTimeout: AtomicReference<Duration> = AtomicReference(10000.milliseconds)
    val assertTimeout: AtomicReference<Duration> = AtomicReference(25.milliseconds)
    val watchInterval: AtomicReference<Duration> = AtomicReference(25.milliseconds)

    /** Test execution hooks **/
    var before: () -> Unit = { }
        set(value) { field += value }
    var after: () -> Unit = { }
        set(value) { field += value }
    var onFailure: () -> Unit = { }
        set(value) { field += value }
    var onSuccess: () -> Unit = { }
        set(value) { field += value }
}

private operator fun (() -> Any).plus(other: () -> Any): () -> Unit = { this().also { other() } }
