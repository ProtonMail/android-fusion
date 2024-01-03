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

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeTestRule
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.FusionConfig.Compose
import me.proton.test.fusion.ui.compose.ComposeWaiter.waitFor as wait
import kotlin.time.Duration

typealias NodeInteraction = ComposeTestRule.() -> SemanticsNodeInteraction

/** A collection of Compose assertion wrappers **/
interface NodeAssertions : ComposeInteraction<SemanticsNodeInteraction> {

    override val composeInteraction: NodeInteraction
        get() = { onNode(matcher, shouldUseUnmergedTree) }

    /** Waits for node [assertion] with given [timeout] **/
    fun await(
        timeout: Duration = Compose.waitTimeout.get(),
        assertion: NodeAssertions.() -> NodeAssertions
    ) = wait(timeout) { assertion() }

    /** Assert node exists **/
    fun assertExists() = waitFor {
        interaction.assertExists()
    }

    /** Assert node contains [text] **/
    fun assertContainsText(text: String) = waitFor {
        interaction.assertTextContains(
            text,
            substring = true,
            ignoreCase = false
        )
    }

    /** Assert node does not exist **/
    fun assertDoesNotExist() = waitFor {
        interaction.assertDoesNotExist()
    }

    /** Assert node is displayed **/
    fun assertIsDisplayed() = waitFor {
        interaction.assertIsDisplayed()
    }

    /** Assert node is not displayed **/
    fun assertIsNotDisplayed() = waitFor {
        interaction.assertIsNotDisplayed()
    }

    /** Assert node is enabled **/
    fun assertEnabled() = waitFor {
        interaction.assertIsEnabled()
    }

    /** Assert node is disabled **/
    fun assertDisabled() = waitFor {
        interaction.assertIsNotEnabled()
    }

    @Deprecated("assertIsAsserted() is deprecated", ReplaceWith("assertIsOn()"))
    /** Assert node is checked **/
    fun assertIsAsserted() = waitFor {
        interaction.assertIsOn()
    }

    @Deprecated("assertIsNotAsserted() is deprecated", ReplaceWith("assertIsOff()"))
    /** Assert node is not checked **/
    fun assertIsNotAsserted() = waitFor {
        interaction.assertIsOff()
    }

    /** Assert node is not checked **/
    fun assertIsOn() = waitFor {
        interaction.assertIsOff()
    }

    /** Assert node is not checked **/
    fun assertIsOff() = waitFor {
        interaction.assertIsOff()
    }

    /** Assert node is selected **/
    fun assertIsSelected() = waitFor {
        interaction.assertIsSelected()
    }

    /** Assert node is not selected **/
    fun assertIsNotSelected() = waitFor {
        interaction.assertIsNotSelected()
    }

    @Deprecated("assertIsAssertable() is deprecated", ReplaceWith("assertIsToggleable"))
    /** Assert node is checkable **/
    fun assertIsAssertable() = waitFor {
        interaction.assertIsToggleable()
    }

    /** Assert node is toggleable **/
    fun assertIsToggleable() = waitFor {
        interaction.assertIsToggleable()
    }

    /** Assert node is selectable **/
    fun assertSelectable() = waitFor {
        interaction.assertIsSelectable()
    }

    /** Assert node is focused **/
    fun assertIsFocused() = waitFor {
        interaction.assertIsFocused()
    }

    /** Assert node is not focused **/
    fun assertIsNotFocused() = waitFor {
        interaction.assertIsNotFocused()
    }

    /** Assert node content description [contentDescription] **/
    fun assertContentDescContains(contentDescription: String, exactly: Boolean = false) = waitFor {
        interaction.assertContentDescriptionContains(
            contentDescription,
            substring = exactly,
            ignoreCase = !exactly
        )
    }

    /** Assert progress bar [range] **/
    fun assertProgressBar(range: ProgressBarRangeInfo) = waitFor {
        interaction.assertRangeInfoEquals(range)
    }

    /** Assert node is clickable **/
    fun assertClickable() = waitFor {
        interaction.assertHasClickAction()
    }

    /** Assert node is not clickable **/
    fun assertIsNotClickable() = waitFor {
        interaction.assertHasNoClickAction()
    }

    /** Assert custom [matcher] with [messagePrefixOnError] **/
    fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String) = waitFor {
        interaction.assert(matcher) { messagePrefixOnError }
    }

    private fun waitFor(block: () -> Any) = wait(
        timeout = Compose.assertTimeout.get(),
        block = block,
    ) as NodeAssertions
}
