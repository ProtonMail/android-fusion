package me.proton.fusion.ui.compose

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.TouchInjectionScope
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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import me.proton.fusion.FusionConfig.Compose
import me.proton.fusion.ui.api.FusionActionable
import me.proton.fusion.ui.api.enums.SwipeDirection

open class OnNode(
    private val useUnmergedTree: Boolean = true,
    private val overrideInteraction: SemanticsNodeInteraction? = null,
    override val shouldPrintToLog: Boolean = Compose.shouldPrintToLog.get(),
    override val shouldPrintHierarchyOnFailure: Boolean = Compose.shouldPrintHierarchyOnFailure.get(),
) : ComposeSelectable<OnNode>,
    FusionActionable<SemanticsNodeInteraction> {
    override val interaction
        get() = overrideInteraction ?: composeTestRule.onNode(
            matchers.final,
            useUnmergedTree
        )

    override val matchers: ArrayList<SemanticsMatcher> = arrayListOf()

    /** Actions **/
    override fun click() =
        waitFor {
            interaction.performClick()
        }

    override fun scrollTo() =
        waitFor {
            interaction.performScrollTo()
        }

    override fun swipe(direction: SwipeDirection) =
        waitFor {
            interaction.performTouchInput {
                when (direction) {
                    SwipeDirection.Up -> swipeUp()
                    SwipeDirection.Down -> swipeDown()
                    SwipeDirection.Right -> swipeRight()
                    SwipeDirection.Left -> swipeLeft()
                }
            }
        }

    override fun clearText() =
        waitFor {
            interaction.performTextClearance()
        }

    override fun typeText(text: String) =
        waitFor {
            interaction.performTextInput(text)
        }

    override fun replaceText(text: String) =
        waitFor {
            interaction.performTextReplacement(text)
        }

    override fun performImeAction() =
        waitFor {
            interaction.performImeAction()
        }

    /** Compose specific actions **/
    fun sendGesture(block: TouchInjectionScope.() -> Unit) =
        waitFor {
            interaction.performTouchInput(block)
        }

    fun pressKey(keyEvent: KeyEvent) =
        waitFor {
            interaction.performKeyPress(keyEvent)
        }

    fun performCustomAction(action: SemanticsPropertyKey<AccessibilityAction<() -> Boolean>>) =
        waitFor {
            interaction.performSemanticsAction(action)
        }

    /** Node assertions **/
    override fun assertExists() =
        waitFor {
            interaction.assertExists()
        }

    override fun assertContainsText(text: String) =
        waitFor {
            interaction.assertTextContains(
                text,
                substring = true,
                ignoreCase = false
            )
        }

    override fun assertDoesNotExist() =
        waitFor {
            interaction.assertDoesNotExist()
        }

    override fun assertIsDisplayed() =
        waitFor {
            interaction.assertIsDisplayed()
        }

    override fun assertIsNotDisplayed() =
        waitFor {
            interaction.assertIsNotDisplayed()
        }

    override fun assertEnabled() =
        waitFor {
            interaction.assertIsEnabled()
        }

    override fun assertDisabled() =
        waitFor {
            interaction.assertIsNotEnabled()
        }

    override fun assertIsChecked() =
        waitFor {
            interaction.assertIsOn()
        }

    override fun assertIsNotChecked() =
        waitFor {
            interaction.assertIsOff()
        }

    override fun assertIsSelected() =
        waitFor {
            interaction.assertIsSelected()
        }

    override fun assertIsNotSelected() =
        waitFor {
            interaction.assertIsNotSelected()
        }

    override fun assertIsCheckable() =
        waitFor {
            interaction.assertIsToggleable()
        }

    override fun assertSelectable() =
        waitFor {
            interaction.assertIsSelectable()
        }

    override fun assertIsFocused() =
        waitFor {
            interaction.assertIsFocused()
        }

    override fun assertIsNotFocused() =
        waitFor {
            interaction.assertIsNotFocused()
        }

    override fun assertContentDescEquals(value: String) =
        waitFor {
            interaction.assertContentDescriptionEquals(value)
        }

    override fun assertContentDescContains(text: String) =
        waitFor {
            interaction.assertContentDescriptionContains(
                text,
                substring = false,
                ignoreCase = false
            )
        }

    override fun assertContentDescContainsIgnoringCase(text: String) =
        waitFor {
            interaction.assertContentDescriptionContains(
                text,
                substring = false,
                ignoreCase = true
            )
        }

    override fun assertTextEquals(value: String) =
        waitFor {
            interaction.assertTextEquals(value)
        }

    override fun assertProgressBar(range: ProgressBarRangeInfo) =
        waitFor {
            interaction.assertRangeInfoEquals(range)
        }

    override fun assertClickable() =
        waitFor {
            interaction.assertHasClickAction()
        }

    override fun assertIsNotClickable() =
        waitFor {
            interaction.assertHasNoClickAction()
        }

    override fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String) =
        waitFor {
            interaction.assert(matcher) { messagePrefixOnError }
        }
}
