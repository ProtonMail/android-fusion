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

package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import me.proton.test.fusion.data.FusionActions
import me.proton.test.fusion.ui.common.enums.SwipeDirection
import me.proton.test.fusion.ui.compose.builders.OnNode
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import me.proton.test.fusion.ui.compose.ComposeWaiter.waitFor as wait

interface NodeActions : NodeAssertions, FusionActions, NodeFinders {
    /** Node Actions **/
    /** performs click **/
    fun click() = waitFor(interaction::performClick)

    /** scrolls to element **/
    fun scrollTo() = waitFor(interaction::performScrollTo)

    /** Scrolls to the item with the given [index] **/
    fun scrollToIndex(index: Int) = waitFor { interaction.performScrollToIndex(index) }

    /** Scrolls to the item with the given [key] **/
    fun scrollToKey(key: Any) = waitFor { interaction.performScrollToKey(key) }

    /** performs ime action **/
    fun performImeAction() = waitFor(interaction::performImeAction)

    /** scrolls to given [node] **/
    fun scrollTo(node: OnNode) =
        waitFor {
            interaction.performScrollToNode(node.matcher)
        }

    /** swipe up on element **/
    fun swipeUp() =
        waitFor {
            interaction.performTouchInput {
                swipeUp()
            }
        }

    /** swipe down on element **/
    fun swipeDown() =
        waitFor {
            interaction.performTouchInput {
                swipeDown()
            }
        }

    /** swipe right on element **/
    fun swipeRight() =
        waitFor {
            interaction.performTouchInput {
                swipeRight()
            }
        }

    /** swipe left on element **/
    fun swipeLeft() =
        waitFor {
            interaction.performTouchInput {
                swipeLeft()
            }
        }

    /** swipes to [direction] with [duration] **/
    fun swipe(
        direction: SwipeDirection,
        duration: Duration = 200.milliseconds
    ) =
        waitFor {
            interaction.performTouchInput {
                when (direction) {
                    SwipeDirection.Up -> swipeUp(durationMillis = duration.inWholeMilliseconds)
                    SwipeDirection.Down -> swipeDown(durationMillis = duration.inWholeMilliseconds)
                    SwipeDirection.Right -> swipeRight(durationMillis = duration.inWholeMilliseconds)
                    SwipeDirection.Left -> swipeLeft(durationMillis = duration.inWholeMilliseconds)
                }
            }
        }

    /** performs [touchInput] **/
    fun sendGesture(touchInput: TouchInjectionScope.() -> Unit) =
        waitFor {
            interaction.performTouchInput(touchInput)
        }

    /** performs custom [action] **/
    fun performSemanticsAction(action: SemanticsPropertyKey<AccessibilityAction<() -> Boolean>>) =
        waitFor {
            interaction.performSemanticsAction(action)
        }

    /** Text actions **/
    /** clears text **/
    fun clearText() = waitFor(interaction::performTextClearance)

    /** types [text] **/
    fun typeText(text: String) =
        waitFor {
            interaction.apply { performTextInput(text) }
        }

    /** replaces [text] **/
    fun replaceText(text: String) =
        waitFor {
            interaction.apply { performTextReplacement(text) }
        }

    /** simulates an [eventType] for a given [key] **/
    fun pressKey(
        key: Key,
        eventType: KeyEventType = KeyEventType.KeyUp
    ) =
        waitFor {
            interaction.apply {
                @Suppress("DEPRECATION")
                val type = when (eventType) {
                    KeyEventType.KeyUp -> NativeKeyEvent.ACTION_UP
                    KeyEventType.KeyDown -> NativeKeyEvent.ACTION_DOWN
                    else -> NativeKeyEvent.ACTION_MULTIPLE
                }
                val nativeKeyEvent = NativeKeyEvent(
                    type,
                    key.nativeKeyCode
                )
                performSemanticsAction(SemanticsActions.RequestFocus)
                performKeyPress(KeyEvent(nativeKeyEvent))
            }
        }

    private fun waitFor(block: () -> Any) = wait(block = block) as NodeActions
}
