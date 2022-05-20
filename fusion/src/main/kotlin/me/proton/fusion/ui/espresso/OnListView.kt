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

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.FusionConfig
import me.proton.fusion.ui.uiautomator.UiSelectorObject
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import me.proton.fusion.waits.ConditionWatcher
import org.hamcrest.CoreMatchers.anything

/**
 * Builder like class that allows to write [ViewActions] and view assertions for ListView items.
 */
class OnListView {

    private var dataMatcher: Matcher<out Any?>? = null

    fun onListItem(dataMatcher: Matcher<out Any?>): Builder {
        return Builder(dataMatcher)
    }

    fun onListItem(): Builder {
        return Builder(anything())
    }

    class Builder(private val dataMatcher: Matcher<out Any?>) : ConditionWatcher {
        private var defaultTimeout: Long = FusionConfig.commandTimeout

        fun withTimeout(milliseconds: Long) = apply { defaultTimeout = milliseconds }

        /** [DataInteraction] matcher functions. **/
        fun atPosition(position: Int) = apply {
            dataInteraction().atPosition(position)
        }

        fun inAdapterView(adapterView: OnView) = apply {
            dataInteraction().inAdapterView(adapterView.viewMatcher())
        }

        fun inRoot(rootView: OnRootView) = apply {
            dataInteraction().inRoot(rootView.matcher())
        }

        fun onChild(childView: OnView) = apply {
            dataInteraction().onChildView(childView.viewMatcher())
        }

        /** [DataInteraction] actions wrappers. **/
        fun click() = apply {
            dataInteraction().perform(ViewActions.click())
        }

        fun longClick() = apply {
            dataInteraction().perform(ViewActions.longClick())
        }

        fun replaceText(text: String) = apply {
            dataInteraction().perform(
                ViewActions.replaceText(text),
                ViewActions.closeSoftKeyboard()
            )
        }

        fun swipeRight() = apply {
            dataInteraction().perform(ViewActions.swipeRight())
        }

        fun swipeLeft() = apply {
            dataInteraction().perform(ViewActions.swipeLeft())
        }

        fun swipeDown() = apply {
            dataInteraction().perform(ViewActions.swipeDown())
        }

        fun swipeUp() = apply {
            dataInteraction().perform(ViewActions.swipeUp())
        }

        fun scrollTo() = apply {
            dataInteraction().perform(ViewActions.scrollTo())
        }

        fun typeText(text: String) = apply {
            dataInteraction().perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard())
        }

        /** [DataInteraction] assertions wrappers. **/
        fun checkContains(text: String) = apply {
            dataInteraction(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
        }

        fun checkDoesNotExist() = apply {
            dataInteraction(ViewAssertions.doesNotExist())
        }

        fun checkDisabled() = apply {
            dataInteraction(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
        }

        fun checkDisplayed() = apply {
            dataInteraction(matches(ViewMatchers.isDisplayed()))
        }

        fun checkNotDisplayed() = apply {
            dataInteraction(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }

        /** Builds [DataInteraction] based on parameters provided to [OnListView.Builder]. **/
        private fun dataInteraction(viewAssertion: ViewAssertion = matches(ViewMatchers.isDisplayed())): DataInteraction {
            waitForCondition(defaultTimeout) { onData(dataMatcher).check(viewAssertion) }
            return onData(dataMatcher)
        }
    }
}
