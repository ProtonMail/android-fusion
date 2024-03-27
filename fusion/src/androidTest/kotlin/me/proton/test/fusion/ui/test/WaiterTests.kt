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
