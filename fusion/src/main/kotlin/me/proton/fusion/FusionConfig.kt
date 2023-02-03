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

package me.proton.fusion

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.StaleObjectException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object FusionConfig {
    const val fusionTag: String = "FUSION"
    val commandTimeout: Duration = 10000.milliseconds
    val watchInterval: Duration = 250.milliseconds
    val uiAutomator = UiAutomator
    val targetContext: Context get() = InstrumentationRegistry.getInstrumentation().targetContext

    object Compose {
        val useUnmergedTree: AtomicBoolean = AtomicBoolean(false)
        val testRule: AtomicReference<ComposeContentTestRule> = AtomicReference(null)
        val shouldPrintHierarchyOnFailure: AtomicBoolean = AtomicBoolean(false)
        val waitTimeout: AtomicReference<Duration> = AtomicReference(10000.milliseconds)
        val watchInterval: AtomicReference<Duration> = AtomicReference(0.milliseconds)
        val shouldPrintToLog: AtomicBoolean = AtomicBoolean(false)

        /** Hooks **/
        var before: () -> Any = { }
        var after: () -> Any = { }
        var onFailure: () -> Any = { }
        var onSuccess: () -> Any = { }
    }

    object UiAutomator {
        private val config: Configurator = Configurator.getInstance()

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

        val defaultTimeout: Long = 10_000L

        fun boost() {
            config.waitForIdleTimeout = 0
            config.waitForSelectorTimeout = 0
            config.actionAcknowledgmentTimeout = 50
        }
    }
}
