package me.proton.fusion.ui.api

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsMatcher
import me.proton.fusion.ui.api.enums.SwipeDirection

interface FusionActionable<T> {
    val interaction: T

    /** actions **/
    fun click(): FusionActionable<T>
    fun scrollTo(): FusionActionable<T>
    fun swipe(direction: SwipeDirection): FusionActionable<T>
    fun clearText(): FusionActionable<T>
    fun typeText(text: String): FusionActionable<T>
    fun replaceText(text: String): FusionActionable<T>
    fun performImeAction(): FusionActionable<T>

    /** assertions **/
    fun assertExists(): FusionActionable<T>
    fun assertContainsText(text: String): FusionActionable<T>
    fun assertDoesNotExist(): FusionActionable<T>
    fun assertIsDisplayed(): FusionActionable<T>
    fun assertIsNotDisplayed(): FusionActionable<T>
    fun assertEnabled(): FusionActionable<T>
    fun assertDisabled(): FusionActionable<T>
    fun assertIsChecked(): FusionActionable<T>
    fun assertIsNotChecked(): FusionActionable<T>
    fun assertIsSelected(): FusionActionable<T>
    fun assertIsNotSelected(): FusionActionable<T>
    fun assertIsCheckable(): FusionActionable<T>
    fun assertSelectable(): FusionActionable<T>
    fun assertIsFocused(): FusionActionable<T>
    fun assertIsNotFocused(): FusionActionable<T>
    fun assertContentDescEquals(value: String): FusionActionable<T>
    fun assertContentDescContains(text: String): FusionActionable<T>
    fun assertContentDescContainsIgnoringCase(text: String): FusionActionable<T>
    fun assertTextEquals(value: String): FusionActionable<T>
    fun assertProgressBar(range: ProgressBarRangeInfo): FusionActionable<T>
    fun assertClickable(): FusionActionable<T>
    fun assertIsNotClickable(): FusionActionable<T>
    fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String): FusionActionable<T>
}