package me.proton.fusion.ui.espresso.wrappers

import androidx.annotation.StringRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.FusionConfig
import me.proton.fusion.ui.espresso.EspressoWaiter
import me.proton.fusion.ui.espresso.extensions.Matchers
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertFalse
import kotlin.time.Duration

interface EspressoAssertions : EspressoWaiter {
    val interaction: ViewInteraction

    fun await(
        timeout: Duration = FusionConfig.commandTimeout,
        interval: Duration = FusionConfig.watchInterval,
        assertion: EspressoAssertions.() -> EspressoAssertions
    ) = waitFor(timeout, interval) { assertion() }

    /** Assertion wrappers **/
    fun checkContainsText(text: String) = apply {
        interaction.check(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
    }

    fun checkContainsText(@StringRes textId: Int) = apply {
        interaction.check(matches(ViewMatchers.withText(FusionConfig.targetContext.resources.getString(textId))))
    }

    fun checkContainsAny(vararg text: String) = apply {
        val matchers = text.map { CoreMatchers.containsString(it) }
        interaction.check(matches(ViewMatchers.withText(CoreMatchers.anyOf(matchers))))
    }

    fun checkDoesNotExist() = apply {
        interaction.check(doesNotExist())
    }

    fun checkExists() = apply {
        interaction.check { view, _ ->
            assertFalse("View is not present in the hierarchy", view == null)
        }
    }

    fun checkIsChecked() = apply {
        interaction.check(matches(ViewMatchers.isChecked()))
    }

    fun checkIsNotChecked() = apply {
        interaction.check(matches(CoreMatchers.not(ViewMatchers.isChecked())))
    }

    fun checkIsFocused() = apply {
        interaction.check(matches(ViewMatchers.isFocused()))
    }

    fun checkIsDisplayed() = apply {
        interaction.check(matches(ViewMatchers.isDisplayed()))
    }

    fun checkIsNotDisplayed() = apply {
        interaction.check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun checkIsDisabled() = apply {
        interaction.check(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    fun checkIsEnabled() = apply {
        interaction.check(matches(ViewMatchers.isEnabled()))
    }

    fun checkIsSelected() = apply {
        interaction.check(matches(ViewMatchers.isSelected()))
    }

    fun checkIsNotSelected() = apply {
        interaction.check(matches(ViewMatchers.isNotSelected()))
    }

    fun checkLengthEquals(length: Int) = apply {
        interaction.check(matches(ViewMatchers.withText(Matchers.stringLengthMatcher(length))))
    }
}