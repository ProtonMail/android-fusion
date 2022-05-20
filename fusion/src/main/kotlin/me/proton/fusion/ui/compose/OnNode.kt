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

package me.proton.fusion.ui.compose

import android.util.Log
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.*
import me.proton.fusion.FusionConfig.fusionTag
import me.proton.fusion.FusionConfig

/**
 * Contains identifiers, actions, and checks to find a single semantics node
 * that matches the given condition i.e. [SemanticsNodeInteraction].
 */
open class OnNode(
    private val interaction: SemanticsNodeInteraction? = null,
) : NodeBuilder<OnNode>() {

    internal fun nodeInteraction(shouldNotExist: Boolean = false): SemanticsNodeInteraction {
        return if (interaction != null) {
            // If interaction is not null then the node was given in constructor.
            interaction
        } else {
            val newInteraction = FusionConfig.compose.testRule.onNode(
                semanticMatcher(),
                shouldUseUnmergedTree
            )
            if (shouldNotExist) {
                // Return interaction if shouldNotExist == true.
                newInteraction
            } else {
                // Wait for element existence before acting on it.
                FusionConfig.compose.testRule.waitUntil(timeoutMillis) { newInteraction.exists() }
                newInteraction
            }
        }
    }

    private fun toNode(action: () -> SemanticsNodeInteraction) =
        handlePrint {
            if (FusionConfig.compose.shouldPrintToLog) action().printToLog(FusionConfig.fusionTag) else action()
        }

    /** Node actions **/
    fun click() = apply { toNode { nodeInteraction().performClick() } }

    fun performCustomAction(action: SemanticsPropertyKey<AccessibilityAction<() -> Boolean>>) =
        apply {
            toNode { nodeInteraction().apply { performSemanticsAction(action) } }
        }

    fun scrollTo() = apply { toNode { nodeInteraction().performScrollTo() } }

    fun swipeDown() = apply { toNode { nodeInteraction().performTouchInput { swipeDown() } } }

    fun swipeLeft() = apply { toNode { nodeInteraction().performTouchInput { swipeLeft() } } }

    fun swipeRight() = apply { toNode { nodeInteraction().performTouchInput { swipeRight() } } }

    fun swipeUp() = apply { toNode { nodeInteraction().performTouchInput { swipeUp() } } }

    fun sendGesture(block: TouchInjectionScope.() -> Unit) =
        apply { toNode { nodeInteraction().performTouchInput(block) } }

    fun clearText() = apply { toNode { nodeInteraction().apply { performTextClearance() } } }

    fun typeText(text: String) =
        apply { toNode { nodeInteraction().apply { performTextInput(text) } } }

    fun replaceText(text: String) =
        apply { toNode { nodeInteraction().apply { performTextReplacement(text) } } }

    fun pressKey(keyEvent: KeyEvent) =
        apply { toNode { nodeInteraction().apply { performKeyPress(keyEvent) } } }

    fun performImeAction() = apply { toNode { nodeInteraction().apply { performImeAction() } } }

    /** Node checks **/
    fun checkExists() = apply { toNode { nodeInteraction().assertExists() } }

    fun checkContainsText(text: String) =
        apply {
            toNode {
                nodeInteraction().assertTextContains(
                    text,
                    substring = true,
                    ignoreCase = false
                )
            }
        }

    fun checkDoesNotExist() = apply { nodeInteraction(shouldNotExist = true).assertDoesNotExist() }

    fun checkIsDisplayed() = apply { toNode { nodeInteraction().assertIsDisplayed() } }

    fun checkIsNotDisplayed() = apply { toNode { nodeInteraction().assertIsNotDisplayed() } }

    fun checkEnabled() = apply { toNode { nodeInteraction().assertIsEnabled() } }

    fun checkDisabled() = apply { toNode { nodeInteraction().assertIsNotEnabled() } }

    fun checkIsChecked() = apply { toNode { nodeInteraction().assertIsOn() } }

    fun checkIsNotChecked() = apply { toNode { nodeInteraction().assertIsOff() } }

    fun checkIsSelected() = apply { toNode { nodeInteraction().assertIsSelected() } }

    fun checkIsNotSelected() = apply { toNode { nodeInteraction().assertIsNotSelected() } }

    fun checkIsCheckable() = apply { toNode { nodeInteraction().assertIsToggleable() } }

    fun checkSelectable() = apply { toNode { nodeInteraction().assertIsSelectable() } }

    fun checkIsFocused() = apply { toNode { nodeInteraction().assertIsFocused() } }

    fun checkIsNotFocused() = apply { toNode { nodeInteraction().assertIsNotFocused() } }

    fun checkContentDescEquals(value: String) =
        apply { toNode { nodeInteraction().assertContentDescriptionEquals(value) } }

    fun checkContentDescContains(text: String) =
        apply {
            toNode {
                nodeInteraction().assertContentDescriptionContains(
                    text,
                    substring = false,
                    ignoreCase = false
                )
            }
        }

    fun checkContentDescContainsIgnoringCase(text: String) =
        apply {
            toNode {
                nodeInteraction().assertContentDescriptionContains(
                    text,
                    substring = false,
                    ignoreCase = true
                )
            }
        }

    fun checkTextEquals(value: String) = apply {
        toNode { nodeInteraction().assertTextEquals(value) }
    }

    fun checkProgressBar(range: ProgressBarRangeInfo) = apply {
        toNode { nodeInteraction().assertRangeInfoEquals(range) }
    }

    fun checkClickable() = apply { toNode { nodeInteraction().assertHasClickAction() } }

    fun checkIsNotClickable() = apply { toNode { nodeInteraction().assertHasNoClickAction() } }

    fun checkMatches(matcher: SemanticsMatcher, messagePrefixOnError: (() -> String)?) = apply {
        toNode { nodeInteraction().assert(matcher, messagePrefixOnError) }
    }

    /** Node selectors **/
    fun onChildAt(position: Int) = OnNode(nodeInteraction().onChildAt(position))

    fun onChild() = OnNode(nodeInteraction().onChild())

    fun onChild(node: OnNode) =
        OnNode(nodeInteraction().onChildren().filterToOne(node.semanticMatcher()))

    fun onParent() = OnNode(nodeInteraction().onParent())

    fun onSibling() = OnNode(nodeInteraction().onSibling())

    fun onSibling(node: OnNode) =
        OnNode(nodeInteraction().onSiblings().filterToOne(node.semanticMatcher()))

    fun onChildren() = OnAllNodes(nodeInteraction().onChildren())

    fun onSiblings() = OnAllNodes(nodeInteraction().onSiblings())

    fun onAncestors() = OnAllNodes(nodeInteraction().onAncestors())

    fun onAncestor(node: OnNode) =
        OnNode(nodeInteraction().onAncestors().filterToOne(node.semanticMatcher()))

    fun waitForDisplayed() = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().isDisplayed()
        }
    }

    fun waitForEnabled() = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().isEnabled()
        }
    }

    fun waitForDisabled() = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().isEnabled()
        }
    }

    fun waitForContainsText(text: String) = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().containsText(text)
        }
    }

    fun waitForSelected() = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().isSelected()
        }
    }

    fun waitForNotSelected() = apply {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction().isNotSelected()
        }
    }

    fun waitUntilGone() {
        FusionConfig.compose.testRule.waitUntil(timeoutMillis) {
            nodeInteraction(true).doesNotExist()
        }
    }

    /** Helpers **/
    private fun SemanticsNodeInteraction.doesNotExist(): Boolean {
        try {
            this.assertDoesNotExist()
        } catch (e: Exception) {
            val firstLine = e.message?.split("\n")?.get(0)
            Log.v(fusionTag, "Waiting for condition. Status: $firstLine")
            return false
        }
        return true
    }

    private fun SemanticsNodeInteraction.exists(): Boolean {
        return checkAssertion { this.assertExists() }
    }

    private fun SemanticsNodeInteraction.isDisplayed(): Boolean {
        return checkAssertion { this.assertIsDisplayed() }
    }

    private fun SemanticsNodeInteraction.isEnabled(): Boolean {
        return checkAssertion { this.assertIsEnabled() }
    }

    private fun SemanticsNodeInteraction.isSelected(): Boolean {
        return checkAssertion { this.assertIsSelected() }
    }

    private fun SemanticsNodeInteraction.isNotSelected(): Boolean {
        return checkAssertion { this.assertIsNotSelected() }
    }

    private fun SemanticsNodeInteraction.isDisabled(): Boolean {
        return checkAssertion { this.assertIsNotEnabled() }
    }

    private fun SemanticsNodeInteraction.containsText(text: String): Boolean {
        return checkAssertion { this.assertTextContains(text) }
    }

    private fun checkAssertion(assertion: () -> SemanticsNodeInteraction): Boolean {
        try {
            assertion()
        } catch (e: AssertionError) {
            val firstLine = e.message?.split("\n")?.get(0)
            Log.v(fusionTag, "Waiting for condition. Status: $firstLine")
            return false
        }
        return true
    }
}
