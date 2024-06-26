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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.ui.common.enums.SwipeDirection
import me.proton.test.fusion.ui.compose.FusionComposeTest
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.text.input.ImeAction
import me.proton.test.fusion.FusionConfig
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ComposeActionsTests : FusionComposeTest() {

    @Before
    fun configure() {
        FusionConfig.Compose.waitTimeout.set(1.seconds)
        FusionConfig.Compose.shouldPrintHierarchyOnFailure.set(true)
    }

    @Test
    fun clickButton() {

        var clicked = false

        withContent {
            Row {
                Button(onClick = { clicked = !clicked }) { Text("btn1") }
            }
        }

        node
            .withText("btn1")
            .click()
            .also { assert(clicked) }

        node
            .withText("btn1")
            .performSemanticsAction(SemanticsActions.OnClick)
            .also { assert(!clicked) }
    }

    @Test
    fun scrollToElement() {
        withContent {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .testTag("scroll")
            ) {
                Btns(count = 50)
            }
        }

        val btn1 = node.withText("btn1")
        val btn0 = node.withText("btn0")
        val scroll = node.withTag("scroll")

        btn0.assertIsDisplayed()
        btn1.assertIsNotDisplayed()
        scroll.scrollTo(btn1)
        btn1.assertIsDisplayed()
        btn0.scrollTo().assertIsDisplayed()
    }

    @Test
    fun swipeVertical() {
        withContent {
            Row {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .testTag("scroll")
                ) {
                    Btns(50)
                }
            }
        }

        node.withTag("scroll").swipeUp()
        node.withText("btn1").assertIsDisplayed()
        node.withTag("scroll").swipeDown()
        node.withText("btn0").assertIsDisplayed()
        node.withTag("scroll").swipe(SwipeDirection.Up)
        node.withText("btn1").assertIsDisplayed()
        node.withTag("scroll").swipe(SwipeDirection.Down)
        node.withText("btn0").assertIsDisplayed()
    }

    @Test
    fun swipeHorizontal() {
        withContent {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .testTag("scroll")
            ) {
                Btns(10)
            }
        }

        node.withTag("scroll").swipeLeft()
        node.withText("btn1").assertIsDisplayed()
        node.withTag("scroll").swipeRight()
        node.withText("btn0").assertIsDisplayed()
        node.withTag("scroll").swipe(SwipeDirection.Left)
        node.withText("btn1").assertIsDisplayed()
        node.withTag("scroll").swipe(SwipeDirection.Right)
        node.withText("btn0").assertIsDisplayed()
    }

    @Test
    fun keyEvent() {
        withContent {
            var text by remember { mutableStateOf("Hello") }
            Column {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .testTag("txt")
                        .onKeyEvent {
                            (it.key == Key.Escape).also { text = "KEY_DOWN" }
                        }
                )
            }
        }

        node
            .withTag("txt")
            .pressKey(Key.Escape)
            .assertContainsText("KEY_DOWN")
    }

    @Test
    fun sendGesture() {
        withContent {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .testTag("scroll")
            ) {
                Btns(10)
            }
        }

        node
            .withTag("scroll")
            .click()
            .sendGesture {
                swipeLeft()
            }

        node.withText("btn1").assertIsDisplayed()

        node
            .withTag("scroll")
            .click()
            .sendGesture {
                swipeRight()
            }
        node.withText("btn0").assertIsDisplayed()
    }

    @Test
    fun imeAction() {
        withContent {
            var text by remember { mutableStateOf("Hello") }
            Column {
                val keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                )

                val keyboardActions = KeyboardActions(
                    onNext = { text = "NEXT" }
                )

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    modifier = Modifier
                        .testTag("txt")
                        .onKeyEvent {
                            (it.type == KeyEventType.KeyDown).also { text = "KEY_DOWN" }
                        }
                )
            }
        }

        node
            .withTag("txt")
            .performImeAction()
            .assertContainsText("NEXT")

    }

    @Test
    fun textActions() {
        withContent {
            var text by remember { mutableStateOf("Hello") }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Label") },
                modifier = Modifier.testTag("txt")
            )
        }

        node
            .withTag("txt")
            .typeText("text")
            .apply {
                assertContainsText("text")
            }
            .typeText("-test")
            .apply {
                assertContainsText("text-test")
            }
            .replaceText("test")
            .apply {
                assertContainsText("test")
            }

        node
            .withTextSubstring("te")
            .withTextSubstringIgnoringCase("st")
            .withTextSubstring("es")
            .assertIsDisplayed()

        node.withText("test").clearText()
        node.withTag("txt").assertContainsText("")
    }
}


@Composable
fun Btns(count: Int) {
    Text( "btn0")
    for (x in 0..count) {
        Text("btn")
    }
    Text("btn1")
}
