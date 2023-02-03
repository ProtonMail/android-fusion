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

package me.proton.fusion.ui.compose.wrappers

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
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
import me.proton.fusion.ui.common.enums.SwipeDirection
import me.proton.fusion.ui.compose.ComposeWaiter.waitFor
import me.proton.fusion.ui.compose.builders.OnNode
import me.proton.fusion.ui.compose.builders.OnNodes
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** A collection of Compose action wrappers **/
interface NodeActions : NodeAssertions {

    /** Node Actions **/
    /** performs click **/
    fun click() =
        waitFor {
            interaction.performClick()
        }

    /** scrolls to element **/
    fun scrollTo() =
        waitFor {
            interaction.performScrollTo()
        }

    /** scrolls to given [node] **/
    fun scrollTo(node: OnNode) =
        waitFor {
            interaction.performScrollToNode(node.finalMatcher)
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
    fun performCustomAction(action: SemanticsPropertyKey<AccessibilityAction<() -> Boolean>>) =
        waitFor {
            interaction.performSemanticsAction(action)
        }

    /** Text actions **/
    /** clears text **/
    fun clearText() =
        waitFor {
            interaction.apply { performTextClearance() }
        }

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

    /** performs ime action **/
    fun performImeAction() =
        waitFor {
            interaction.apply { performImeAction() }
        }

    /** performs [keyEvent] **/
    fun pressKey(keyEvent: KeyEvent) =
        waitFor {
            interaction.apply { performKeyPress(keyEvent) }
        }

    /** returns node at child [position] **/
    fun onChildAt(position: Int): NodeActions =
        waitFor {
            OnNode(interaction.onChildAt(position))
        }

    /** returns a child node (only if node has one child) **/
    fun onChild() =
        waitFor {
            OnNode(interaction.onChild())
        }

    /** returns a parent node (only if node has one parent) **/
    fun onParent() =
        waitFor { OnNode(interaction.onParent()) }

    /** returns a sibling node (only if node has one sibling) **/
    fun onSibling() =
        waitFor {
            OnNode(interaction.onSibling())
        }

    /** returns a node from children with matcher from given [node] **/
    fun onChild(node: OnNode) =
        waitFor {
            OnNode(interaction.onChildren().filterToOne(node.finalMatcher))
        }

    /** returns a node from children with matcher from given [ancestor] **/
    fun onAncestor(ancestor: OnNode) =
        waitFor {
            OnNode(interaction.onAncestors().filterToOne(ancestor.finalMatcher))
        }

    /** returns a node from siblings with matcher from given [sibling] **/
    fun onSibling(sibling: OnNode) =
        waitFor {
            OnNode(interaction.onSiblings().filterToOne(sibling.finalMatcher))
        }

    /** returns children node collection **/
    fun onChildren() =
        waitFor {
            OnNodes(interaction.onChildren())
        }

    /** returns sibling node collection **/
    fun onSiblings() =
        waitFor {
            OnNodes(interaction.onSiblings())
        }

    /** returns ancestor node collection **/
    fun onAncestors() =
        waitFor {
            OnNodes(interaction.onAncestors())
        }
}
