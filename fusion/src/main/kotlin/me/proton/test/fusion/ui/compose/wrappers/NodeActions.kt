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
