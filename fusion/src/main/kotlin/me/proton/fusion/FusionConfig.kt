/*
 * Copyright (c) 2021 Proton Technologies AG
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

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Configurator
import androidx.test.uiautomator.StaleObjectException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

typealias AndroidComposeRule = AndroidComposeTestRule<ActivityScenarioRule<*>, *>

object FusionConfig {

    val compose = Compose
    val uiAutomator = UiAutomator
    val testRule = TestRule
    var fusionTag: String = "FUSION"
    var commandTimeout: Long = 10_000L
    val targetContext get() = InstrumentationRegistry.getInstrumentation().targetContext!!

    object TestRule {
        var retriesCount: Int = 1
    }

    object Compose {
        val testRule: AtomicReference<AndroidComposeRule> = AtomicReference<AndroidComposeRule>()
        val shouldPrintHierarchyOnFailure: AtomicBoolean = AtomicBoolean(false)
        val shouldPrintToLog: AtomicBoolean = AtomicBoolean(false)
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
