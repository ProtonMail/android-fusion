/*
 * Copyright (c) 2021 Proton Technologies AG
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

package me.proton.fusion.ui.espresso

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.ui.espresso.Assertions.exists
import me.proton.fusion.utils.StringUtils.stringFromResource
import me.proton.fusion.waits.ConditionWatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Contains [ViewActions] and [ViewAssertion] Fusion API for a single [View].
 */
class OnView : ConditionWatcher, OnViewMatchers<OnView>() {
    private var viewInteraction: ViewInteraction? = null

    // Used to tag the assertion to prevent unnecessary assertions on the same view.
    private var assertionHash = 0

    /** [ViewInteraction] wait. **/
    private fun viewInteraction(
        viewAssertion: ViewAssertion = matches(ViewMatchers.isDisplayed())
    ): ViewInteraction {
        return onView(viewMatcher()).inRoot(rootMatcher()).check(viewAssertion)
    }

    private fun waitForViewInteraction(
        viewAssertion: ViewAssertion = matches(ViewMatchers.isDisplayed())
    ): ViewInteraction {
        return if (viewInteraction == null || assertionHash != viewAssertion.hashCode()) {
            assertionHash = viewAssertion.hashCode()
            viewInteraction = onView(viewMatcher()).inRoot(rootMatcher())
            waitForCondition(defaultTimeout) { viewInteraction!!.check(viewAssertion) }
            viewInteraction!!
        } else {
            viewInteraction!!
        }
    }

    /** Action wrappers. **/
    fun click() = apply {
        waitForViewInteraction().perform(ViewActions.click())
    }

    fun pressKey(key: Int) = apply {
        waitForViewInteraction().perform(ViewActions.pressKey(key))
    }

    fun clearText() = apply {
        waitForViewInteraction().perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())
    }

    fun performCustomAction(vararg customViewActions: ViewAction) = apply {
        waitForViewInteraction().perform(*customViewActions)
    }

    fun replaceText(text: String) = apply {
        waitForViewInteraction().perform(
            ViewActions.replaceText(text),
            ViewActions.closeSoftKeyboard()
        )
    }

    fun swipeDown() = apply {
        waitForViewInteraction().perform(ViewActions.swipeDown())
    }

    fun swipeLeft() = apply {
        waitForViewInteraction().perform(ViewActions.swipeLeft())
    }

    fun swipeRight() = apply {
        waitForViewInteraction().perform(ViewActions.swipeRight())
    }

    fun swipeUp() = apply {
        waitForViewInteraction().perform(ViewActions.swipeUp())
    }

    fun typeText(text: String) = apply {
        waitForViewInteraction().perform(
            ViewActions.typeText(text),
            ViewActions.closeSoftKeyboard()
        )
    }

    fun closeKeyboard() = apply {
        waitForViewInteraction().perform(ViewActions.closeSoftKeyboard())
    }

    fun closeDrawer() = apply {
        waitForViewInteraction().perform(DrawerActions.close())
    }

    fun doubleClick() = apply {
        waitForViewInteraction().perform(ViewActions.doubleClick())
    }

    fun longClick() = apply {
        waitForViewInteraction().perform(ViewActions.longClick())
    }

    fun openDrawer() = apply {
        waitForViewInteraction().perform(DrawerActions.open())
    }

    fun pressBack() = apply {
        waitForViewInteraction().perform(ViewActions.pressBack())
    }

    fun performImeAction() = apply {
        waitForViewInteraction().perform(ViewActions.pressImeActionButton())
    }

    fun scrollTo() = apply {
        waitForViewInteraction(exists(viewMatcher())).perform(ViewActions.scrollTo())
    }

    /** Assertion wrappers **/
    fun checkContainsText(text: String) = apply {
        viewInteraction(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
    }

    fun checkContainsText(@StringRes textId: Int) = apply {
        viewInteraction(matches(ViewMatchers.withText(stringFromResource(textId))))
    }

    fun checkDoesNotExist() = apply {
        viewInteraction(doesNotExist())
    }

    fun checkExists() = apply {
        viewInteraction(exists(viewMatcher()))
    }

    fun checkIsChecked() = apply {
        viewInteraction(matches(ViewMatchers.isChecked()))
    }

    fun checkIsNotChecked() = apply {
        viewInteraction(matches(CoreMatchers.not(ViewMatchers.isChecked())))
    }

    fun checkIsFocused() = apply {
        viewInteraction(matches(ViewMatchers.isFocused()))
    }

    fun checkIsDisplayed() = apply {
        viewInteraction(matches(ViewMatchers.isDisplayed()))
    }

    fun checkIsNotDisplayed() = apply {
        viewInteraction(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun checkIsDisabled() = apply {
        viewInteraction(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    fun checkIsEnabled() = apply {
        viewInteraction(matches(ViewMatchers.isEnabled()))
    }

    fun checkIsSelected() = apply {
        viewInteraction(matches(ViewMatchers.isSelected()))
    }

    fun checkIsNotSelected() = apply {
        viewInteraction(matches(ViewMatchers.isNotSelected()))
    }

    fun checkLengthEquals(length: Int) = apply {
        viewInteraction(
            matches(
                ViewMatchers.withText(object : TypeSafeMatcher<String>(String::class.java) {
                    override fun describeTo(description: Description) {
                        description.appendText("String length equals $length")
                    }

                    override fun matchesSafely(item: String?): Boolean = item?.length == length
                })
            )
        )
    }

    fun waitUntilGone() = apply { waitForViewInteraction(doesNotExist()) }

    fun waitForDisplayed() = apply {
        waitForViewInteraction(matches(ViewMatchers.isDisplayed()))
    }

    fun waitForNotDisplayed() = apply {
        waitForViewInteraction(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun waitForDisabled() = apply {
        waitForViewInteraction(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    fun waitForEnabled() = apply {
        waitForViewInteraction(matches(ViewMatchers.isEnabled()))
    }

    fun waitForSelected() = apply {
        waitForViewInteraction(matches(ViewMatchers.isSelected()))
    }

    fun waitForNotSelected() = apply {
        waitForViewInteraction(matches(ViewMatchers.isNotSelected()))
    }

    fun waitForContainsText(text: String) = apply {
        waitForViewInteraction(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
    }

    fun waitForContainsText(@StringRes textId: Int) = apply {
        waitForViewInteraction(matches(ViewMatchers.withText(stringFromResource(textId))))
    }
}
