package me.proton.fusion.ui.compose

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import me.proton.fusion.ui.api.enums.SwipeDirection
import kotlin.time.ExperimentalTime

typealias ComposeAction = SemanticsPropertyKey<AccessibilityAction<() -> Boolean>>
typealias ComposeTouch = TouchInjectionScope.() -> Unit

/** A collection of Compose action wrappers **/
@OptIn(ExperimentalTime::class)
interface NodeActions : NodeAsserts {

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

    /** swipes to [direction] **/
    fun swipe(direction: SwipeDirection) =
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

    /** performs [ComposeTouch] [touchInput] **/
    fun sendGesture(touchInput: ComposeTouch) =
        waitFor {
            interaction.performTouchInput(touchInput)
        }

    /** performs custom [action] **/
    fun performCustomAction(action: ComposeAction) =
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
}
