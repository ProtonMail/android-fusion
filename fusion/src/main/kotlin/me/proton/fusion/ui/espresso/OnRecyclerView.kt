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
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.waits.ConditionWatcher
import me.proton.fusion.ui.espresso.Actions.clickOnMatchedDescendant
import org.hamcrest.Matcher
import kotlin.time.ExperimentalTime

/**
 * Builder like class that simplifies syntax for actions and verifications on
 * [RecyclerView.ViewHolder].
 */

@OptIn(ExperimentalTime::class)
class OnRecyclerView : ConditionWatcher, OnViewMatchers<OnRecyclerView>() {
    private var position: Int? = null
    private var viewHolderMatcher: Matcher<RecyclerView.ViewHolder>? = null
    private var itemChildViewMatcher: Matcher<View>? = null

    /** [ViewInteraction] for the [RecyclerView] instance. **/
    private fun viewInteraction(
        viewAssertion: ViewAssertion = ViewAssertions.matches(ViewMatchers.isDisplayed())
    ): ViewInteraction {
        waitForCondition(defaultTimeout) {
            onView(viewMatcher()).inRoot(rootMatcher()).check(viewAssertion)
        }
        return onView(viewMatcher())
    }

    /** [RecyclerViewActions] **/
    fun click() = apply { perform(ViewActions.click()) }

    fun doubleClick() = apply { perform(ViewActions.doubleClick()) }

    fun longClick() = apply { perform(ViewActions.longClick()) }

    fun swipeDown() = apply { perform(ViewActions.swipeDown()) }

    fun swipeLeft() = apply { perform(ViewActions.swipeLeft()) }

    fun swipeRight() = apply { perform(ViewActions.swipeRight()) }

    fun swipeUp() = apply { perform(ViewActions.swipeUp()) }

    fun scrollTo() = apply { perform(ViewActions.scrollTo()) }

    fun waitUntilGone() = apply {
        waitForCondition(defaultTimeout) { viewInteraction().check(ViewAssertions.doesNotExist()) }
    }

    fun scrollToHolder(viewHolderMatcher: Matcher<RecyclerView.ViewHolder>) = apply {
        waitForCondition(defaultTimeout) {
            viewInteraction().perform(
                RecyclerViewActions.scrollToHolder(
                    viewHolderMatcher
                )
            )
        }
    }

    /** Wrapper for [RecyclerViewActions.actionOnHolderItem]. **/
    fun onHolderItem(viewHolderMatcher: Matcher<RecyclerView.ViewHolder>) = apply {
        this.viewHolderMatcher = viewHolderMatcher
    }

    /** Wrapper for [RecyclerViewActions.actionOnItemAtPosition]. **/
    fun onItemAtPosition(position: Int) = apply { this.position = position }

    /** Points to perform an action on [RecyclerView.ViewHolder] child or descendant. **/
    fun onItemChildView(view: OnView) = apply { itemChildViewMatcher = view.viewMatcher() }

    /** Performs action on [RecyclerView] based on action defined by [OnRecyclerView]. **/
    private fun perform(viewAction: ViewAction): Any = when (true) {
        (viewHolderMatcher != null) -> {
            waitForCondition { viewInteraction().perform(viewHolderAction(viewAction)) }
        }
        (position != null) -> {
            waitForCondition { viewInteraction().perform(positionedAction(viewAction)) }
        }
        else -> waitForCondition { viewInteraction().perform(viewHolderAction(viewAction)) }
    }

    private fun viewHolderAction(viewAction: ViewAction): ViewAction {
        return if (itemChildViewMatcher != null) {
            actionOnHolderItem(viewHolderMatcher, clickOnMatchedDescendant(itemChildViewMatcher!!))
        } else {
            actionOnHolderItem(viewHolderMatcher, viewAction)
        }
    }

    private fun positionedAction(viewAction: ViewAction): ViewAction {
        return if (itemChildViewMatcher != null) {
            actionOnHolderItem(viewHolderMatcher, clickOnMatchedDescendant(itemChildViewMatcher!!))
        } else {
            actionOnItemAtPosition<RecyclerView.ViewHolder?>(position!!, viewAction)
        }
    }
}
