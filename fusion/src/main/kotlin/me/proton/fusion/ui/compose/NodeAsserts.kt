package me.proton.fusion.ui.compose

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertContentDescriptionEquals
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
import androidx.compose.ui.test.assertTextEquals
import kotlin.time.ExperimentalTime

/** A collection of Compose assertion wrappers **/
@OptIn(ExperimentalTime::class)
interface NodeAsserts : ComposeWaiter {
    val interaction: SemanticsNodeInteraction

    /** Assert node exists **/
    fun assertExists() =
        waitFor {
            interaction.assertExists()
        }

    /** Assert node contains [text] **/
    fun assertContainsText(text: String) =
        waitFor {
            interaction.assertTextContains(
                text,
                substring = true,
                ignoreCase = false
            )
        }

    /** Assert node does not exist **/
    fun assertDoesNotExist() =
        waitFor {
            interaction.apply { assertDoesNotExist() }
        }

    /** Assert node is displayed **/
    fun assertIsDisplayed() =
        waitFor {
            interaction.assertIsDisplayed()
        }

    /** Assert node is not displayed **/
    fun assertIsNotDisplayed() =
        waitFor {
            interaction.assertIsNotDisplayed()
        }

    /** Assert node is enabled **/
    fun assertEnabled() =
        waitFor {
            interaction.assertIsEnabled()
        }

    /** Assert node is disabled **/
    fun assertDisabled() =
        waitFor {
            interaction.assertIsNotEnabled()
        }

    /** Assert node is checked **/
    fun assertIsAsserted() =
        waitFor {
            interaction.assertIsOn()
        }

    /** Assert node is not checked **/
    fun assertIsNotAsserted() =
        waitFor {
            interaction.assertIsOff()
        }

    /** Assert node is selected **/
    fun assertIsSelected() =
        waitFor {
            interaction.assertIsSelected()
        }

    /** Assert node is not selected **/
    fun assertIsNotSelected() =
        waitFor {
            interaction.assertIsNotSelected()
        }

    /** Assert node is checkable **/
    fun assertIsAssertable() =
        waitFor {
            interaction.assertIsToggleable()
        }

    /** Assert node is selectable **/
    fun assertSelectable() =
        waitFor {
            interaction.assertIsSelectable()
        }

    /** Assert node is focused **/
    fun assertIsFocused() =
        waitFor {
            interaction.assertIsFocused()
        }

    /** Assert node is not focused **/
    fun assertIsNotFocused() =
        waitFor {
            interaction.assertIsNotFocused()
        }

    /** Assert node content description [contentDescription] **/
    fun assertContentDescContains(contentDescription: String, exactly: Boolean = false) =
        waitFor {
            interaction.assertContentDescriptionContains(
                contentDescription,
                substring = exactly,
                ignoreCase = !exactly
            )
        }

    /** Assert progress bar [range] **/
    fun assertProgressBar(range: ProgressBarRangeInfo) =
        waitFor {
            interaction.assertRangeInfoEquals(range)
        }

    /** Assert node is clickable **/
    fun assertClickable() =
        waitFor {
            interaction.assertHasClickAction()
        }

    /** Assert node is not clickable **/
    fun assertIsNotClickable() =
        waitFor {
            interaction.assertHasNoClickAction()
        }

    /** Assert custom [matcher] with [messagePrefixOnError] **/
    fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String) =
        waitFor {
            interaction.assert(matcher) { messagePrefixOnError }
        }
}
