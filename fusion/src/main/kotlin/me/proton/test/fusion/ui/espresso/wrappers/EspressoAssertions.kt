/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
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
