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

package me.proton.test.fusion.ui.espresso.wrappers

import androidx.annotation.StringRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.test.fusion.FusionConfig.Espresso
import me.proton.test.fusion.ui.FusionWaiter
import me.proton.test.fusion.ui.espresso.extensions.Matchers
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertFalse
import kotlin.time.Duration

interface EspressoAssertions : FusionWaiter {
    val interaction: ViewInteraction

    fun await(
        timeout: Duration = Espresso.waitTimeout.get(),
        interval: Duration = Espresso.watchInterval.get(),
        assertion: EspressoAssertions.() -> EspressoAssertions
    ) = waitFor(
        timeout = timeout,
        interval = interval
    ) { assertion() }

    /** Assertion wrappers **/
    fun checkContainsText(text: String) = apply {
        interaction.check(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
    }

    fun checkContainsText(@StringRes textId: Int) = apply {
        interaction.check(
            matches(
                ViewMatchers.withText(
                    Espresso.targetContext.resources.getString(
                        textId
                    )
                )
            )
        )
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
