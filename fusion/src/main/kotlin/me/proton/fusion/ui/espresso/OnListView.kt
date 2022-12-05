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
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import me.proton.fusion.waits.ConditionWatcher
import org.hamcrest.CoreMatchers.anything
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Builder like class that allows to write [ViewActions] and view assertions for ListView items.
 */
@OptIn(ExperimentalTime::class)
class OnListView {

    fun onListItem(dataMatcher: Matcher<out Any?>): Builder {
        return Builder(dataMatcher)
    }

    fun onListItem(): Builder {
        return Builder(anything())
    }

    class Builder(private val dataMatcher: Matcher<out Any?>) : ConditionWatcher {

        private var dataInteraction: DataInteraction = onData(dataMatcher)
        private var defaultTimeout: Duration = FusionConfig.commandTimeout
        private var adapterView: OnView? = null
        private var rootView: OnRootView? = null
        private var childView: OnView? = null
        private var position: Int? = null

        fun withTimeout(milliseconds: Duration) = apply { defaultTimeout = milliseconds }

        /** [DataInteraction] matcher functions. **/
        fun atPosition(position: Int) = apply {
            this.position = position
        }

        fun inAdapterView(adapterView: OnView) = apply {
            this.adapterView = adapterView
        }

        fun inRoot(rootView: OnRootView) = apply {
            this.rootView = rootView
        }

        fun onChild(childView: OnView) = apply {
            this.childView = childView
        }

        /** [DataInteraction] actions wrappers. **/
        fun click() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.click()) }
        }

        fun longClick() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.longClick()) }
        }

        fun replaceText(text: String) = apply {
            waitForCondition(defaultTimeout) {
                interaction().perform(
                    ViewActions.replaceText(text),
                    ViewActions.closeSoftKeyboard()
                )
            }
        }

        fun swipeRight() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.swipeRight()) }
        }

        fun swipeLeft() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.swipeLeft()) }
        }

        fun swipeDown() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.swipeDown()) }
        }

        fun swipeUp() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.swipeUp()) }
        }

        fun scrollTo() = apply {
            waitForCondition(defaultTimeout) { interaction().perform(ViewActions.scrollTo()) }
        }

        fun typeText(text: String) = apply {
            waitForCondition(defaultTimeout) {
                interaction().perform(
                    ViewActions.typeText(text),
                    ViewActions.closeSoftKeyboard()
                )
            }
        }

        /** [DataInteraction] assertions wrappers. **/
        fun checkContains(text: String) = apply {
            check(matches(ViewMatchers.withText(CoreMatchers.containsString(text))))
        }

        fun checkDoesNotExist() = apply {
            check(ViewAssertions.doesNotExist())
        }

        fun checkIsDisabled() = apply {
            check(matches(CoreMatchers.not(ViewMatchers.isEnabled())))
        }

        fun checkIsDisplayed() = apply {
            check(matches(ViewMatchers.isDisplayed()))
        }

        fun checkIsNotDisplayed() = apply {
            check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }

        /** Builds [DataInteraction] based on parameters provided to [OnListView.Builder]. **/
        private fun check(viewAssertion: ViewAssertion) {
            waitForCondition(defaultTimeout) { interaction().check(viewAssertion) }
        }

        private fun interaction(): DataInteraction {
            if (adapterView != null) {
                dataInteraction = dataInteraction.inAdapterView(adapterView!!.viewMatcher())
            }
            if (rootView != null) {
                dataInteraction = dataInteraction.inRoot(rootView!!.finalMatcher)
            }
            if (position != null) {
                dataInteraction = dataInteraction.atPosition(position)
            }
            if (childView != null) {
                dataInteraction = dataInteraction.onChildView(childView!!.viewMatcher())
            }
            return dataInteraction
        }
    }
}
