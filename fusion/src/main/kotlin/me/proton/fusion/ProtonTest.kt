/*
 * Copyright (c) 2020 Proton Technologies AG
 *
 * This file is part of ProtonMail.
 *
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail. If not, see https://www.gnu.org/licenses/.
 */

package me.proton.fusion

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import me.proton.fusion.FusionConfig.commandTimeout
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.rules.RetryRule
import me.proton.fusion.utils.Shell
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Class that holds common setUp() and tearDown() functions.
 *
 * @property activity an ActivityScenarioRule for the RuleChain
 */
open class FusionTest(
    private val activity: Class<out Activity>,
    tries: Int = 1,
    testWatcher: TestWatcher = TestExecutionWatcher(),
    activityScenarioRule: ActivityScenarioRule<out Activity> = ActivityScenarioRule(activity),
) : Fusion {

    class TestExecutionWatcher : TestWatcher() {
        override fun failed(e: Throwable?, description: Description?) =
            Shell.saveToFile(description)
    }

    private val retryRule = RetryRule(activity, tries)

    @Rule
    @JvmField
    val ruleChain = RuleChain
        .outerRule(testName)
        .around(testWatcher)
        .around(retryRule)
        .around(activityScenarioRule)!!


    @Before
    open fun setUp() {
        Log.d(fusionTag, "Starting test execution: ${testName.methodName}")
        intent.init()
        // Stub all external intents
        intent
            .isInternal()
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    open fun tearDown() {
        intent.release()
        Log.d(fusionTag, "Finished test execution: ${testName.methodName}")
    }

    companion object {
        val testName = TestName()
    }
}
