package me.proton.fusion.ui.compose.wrappers

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
import me.proton.fusion.FusionConfig
import me.proton.fusion.ui.compose.ComposeWaiter.waitFor
import kotlin.time.Duration

/** A collection of Compose assertion wrappers **/
interface NodeAssertions {
    val interaction: SemanticsNodeInteraction

    /** Waits for node [assertion] with given [timeout] **/
    fun await(
        timeout: Duration = FusionConfig.commandTimeout,
        assertion: NodeAssertions.() -> NodeAssertions
    ) = waitFor(timeout) { assertion() }

    /** Assert node exists **/
    fun assertExists() = apply {
        interaction.assertExists()
    }

    /** Assert node contains [text] **/
    fun assertContainsText(text: String) = apply {
        interaction.assertTextContains(
            text,
            substring = true,
            ignoreCase = false
        )
    }

    /** Assert node does not exist **/
    fun assertDoesNotExist() = apply {
        interaction.apply { assertDoesNotExist() }
    }

    /** Assert node is displayed **/
    fun assertIsDisplayed() = apply {
        interaction.assertIsDisplayed()
    }

    /** Assert node is not displayed **/
    fun assertIsNotDisplayed() = apply {
        interaction.assertIsNotDisplayed()
    }

    /** Assert node is enabled **/
    fun assertEnabled() = apply {
        interaction.assertIsEnabled()
    }

    /** Assert node is disabled **/
    fun assertDisabled() = apply {
        interaction.assertIsNotEnabled()
    }

    /** Assert node is checked **/
    fun assertIsAsserted() = apply {
        interaction.assertIsOn()
    }

    /** Assert node is not checked **/
    fun assertIsNotAsserted() = apply {
        interaction.assertIsOff()
    }

    /** Assert node is selected **/
    fun assertIsSelected() = apply {
        interaction.assertIsSelected()
    }

    /** Assert node is not selected **/
    fun assertIsNotSelected() = apply {
        interaction.assertIsNotSelected()
    }

    /** Assert node is checkable **/
    fun assertIsAssertable() = apply {
        interaction.assertIsToggleable()
    }

    /** Assert node is selectable **/
    fun assertSelectable() = apply {
        interaction.assertIsSelectable()
    }

    /** Assert node is focused **/
    fun assertIsFocused() = apply {
        interaction.assertIsFocused()
    }

    /** Assert node is not focused **/
    fun assertIsNotFocused() = apply {
        interaction.assertIsNotFocused()
    }

    /** Assert node content description [contentDescription] **/
    fun assertContentDescContains(contentDescription: String, exactly: Boolean = false) = apply {
        interaction.assertContentDescriptionContains(
            contentDescription,
            substring = exactly,
            ignoreCase = !exactly
        )
    }

    /** Assert progress bar [range] **/
    fun assertProgressBar(range: ProgressBarRangeInfo) = apply {
        interaction.assertRangeInfoEquals(range)
    }

    /** Assert node is clickable **/
    fun assertClickable() = apply {
        interaction.assertHasClickAction()
    }

    /** Assert node is not clickable **/
    fun assertIsNotClickable() = apply {
        interaction.assertHasNoClickAction()
    }

    /** Assert custom [matcher] with [messagePrefixOnError] **/
    fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String) = apply {
        interaction.assert(matcher) { messagePrefixOnError }
    }
}