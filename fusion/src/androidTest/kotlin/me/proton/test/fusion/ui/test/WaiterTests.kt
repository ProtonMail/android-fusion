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

package me.proton.test.fusion.ui.test

import android.view.View
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.Fusion.view
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.ui.compose.FusionComposeTest
import me.proton.test.fusion.ui.espresso.wrappers.EspressoAssertions
import me.proton.test.fusion.ui.test.RobotTests.BlueScreenRobot.waitFor
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class WaiterTests : FusionComposeTest() {
    @Test
    fun composeWaiterTest() {
        FusionConfig.Compose.waitTimeout.set(3.seconds)
        withContent {
            val showTextState = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = Unit) {
                withContext(Dispatchers.IO) {
                    Thread.sleep(2000)
                }
                showTextState.value = true
            }

            if (showTextState.value) {
                Text("txt1")
            }
        }

        val element = node.withText("txt1")

        Assert.assertThrows(AssertionError::class.java) {
            // Should fail immediately, since not awaited
            element.assertIsDisplayed()
        }

        element.await(9.seconds) { assertIsDisplayed() }
    }

    @Test
    fun composeActionAwaited() {
        var clicked = false
        FusionConfig.Compose.waitTimeout.set(5.seconds)

        withContent {
            val showTextState = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = Unit) {
                withContext(Dispatchers.IO) {
                    Thread.sleep(2000)
                }
                showTextState.value = true
            }

            if (showTextState.value) {
                Button(onClick = { clicked = !clicked }) {
                    Text(text = "Button")
                }
            }
        }

        node
            .withText("Button")
            .click()

        Assert.assertTrue(clicked)

    }

    @Test
    fun fusionWaiterTest() {
        val currentTime = System.currentTimeMillis()
        fun test(): Boolean = System.currentTimeMillis() > currentTime + 2.seconds.inWholeMilliseconds
        view.waitFor(timeout = 3.seconds) { assert(test()) }
    }

    @Test
    fun espressoActionsAwaited() {
        var clicked = false
        val currentTime = System.currentTimeMillis()

        val matcher = object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) {
                // do nothing
            }

            override fun matches(actual: Any?): Boolean =
                System.currentTimeMillis() > currentTime + 2.seconds.inWholeMilliseconds
        }

        val testAction: ViewAction = object : ViewAction {
            override fun getDescription(): String = ""

            override fun getConstraints(): Matcher<View> = matcher

            override fun perform(uiController: UiController?, view: View?) {
                clicked = true
            }
        }

        // This is a default Android element. Assuming it exists
        val element = view.withResourceName("decor_content_parent")

        fun EspressoAssertions.testAssertion() =
            interaction.check(matches(matcher))

        Assert.assertThrows(AssertionError::class.java) {
            // Should fail immediately, since not awaited
            element.testAssertion()
        }

        element.perform(testAction)

        assert(clicked)
    }
}
