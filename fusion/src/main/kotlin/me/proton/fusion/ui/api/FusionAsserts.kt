package me.proton.fusion.ui.api

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsMatcher

interface FusionAsserts<T> {
    val interaction: T

    /** assertions **/
    fun assertExists(): FusionAsserts<T>
    fun assertContainsText(text: String): FusionAsserts<T>
    fun assertDoesNotExist(): FusionAsserts<T>
    fun assertIsDisplayed(): FusionAsserts<T>
    fun assertIsNotDisplayed(): FusionAsserts<T>
    fun assertEnabled(): FusionAsserts<T>
    fun assertDisabled(): FusionAsserts<T>
    fun assertIsChecked(): FusionAsserts<T>
    fun assertIsNotChecked(): FusionAsserts<T>
    fun assertIsSelected(): FusionAsserts<T>
    fun assertIsNotSelected(): FusionAsserts<T>
    fun assertIsCheckable(): FusionAsserts<T>
    fun assertSelectable(): FusionAsserts<T>
    fun assertIsFocused(): FusionAsserts<T>
    fun assertIsNotFocused(): FusionAsserts<T>
    fun assertContentDescEquals(value: String): FusionAsserts<T>
    fun assertContentDescContains(text: String): FusionAsserts<T>
    fun assertContentDescContainsIgnoringCase(text: String): FusionAsserts<T>
    fun assertTextEquals(value: String): FusionAsserts<T>
    fun assertProgressBar(range: ProgressBarRangeInfo): FusionAsserts<T>
    fun assertClickable(): FusionAsserts<T>
    fun assertIsNotClickable(): FusionAsserts<T>
    fun assertMatches(matcher: SemanticsMatcher, messagePrefixOnError: String): FusionAsserts<T>
}
