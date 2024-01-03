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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import me.proton.test.fusion.data.FusionActions
import me.proton.test.fusion.ui.common.enums.SwipeDirection
import me.proton.test.fusion.ui.espresso.extensions.Actions
import me.proton.test.fusion.ui.espresso.extensions.asDayOfMonth
import me.proton.test.fusion.ui.espresso.extensions.asMonth
import me.proton.test.fusion.ui.espresso.extensions.asYear
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import java.util.Date

/**
 * Contains [ViewActions] and [ViewAssertion] Fusion API for a single [View].
 */
interface EspressoActions : EspressoAssertions, FusionActions {
    fun perform(viewAction: ViewAction): EspressoActions

    /** Action wrappers. **/
    fun click() = perform(ViewActions.click())

    fun pressKey(key: Int) = perform(ViewActions.pressKey(key))

    fun clearText() =
        perform(ViewActions.clearText())
            .perform(ViewActions.closeSoftKeyboard())

    fun performCustomAction(vararg customViewActions: ViewAction) =
        customViewActions.forEach {
            perform(it)
        }

    fun replaceText(text: String) =
        perform(ViewActions.replaceText(text))
            .perform(ViewActions.closeSoftKeyboard())

    fun swipe(swipeDirection: SwipeDirection): EspressoActions =
        when (swipeDirection) {
            SwipeDirection.Up -> ViewActions.swipeUp()
            SwipeDirection.Down -> ViewActions.swipeDown()
            SwipeDirection.Right -> ViewActions.swipeRight()
            SwipeDirection.Left -> ViewActions.swipeLeft()
        }
            .let {
                perform(it)
            }

    fun typeText(text: String) =
        perform(ViewActions.typeText(text))
            .perform(ViewActions.closeSoftKeyboard())

    fun closeKeyboard() = perform(ViewActions.closeSoftKeyboard())

    fun doubleClick() = perform(ViewActions.doubleClick())

    fun longClick() = perform(ViewActions.longClick())

    fun pressBack() = perform(ViewActions.pressBack())

    fun performImeAction() = perform(ViewActions.pressImeActionButton())

    fun scrollTo() = perform(ViewActions.scrollTo())

    fun closeDrawer() = perform(DrawerActions.close())

    fun openDrawer() = perform(DrawerActions.open())

    fun scrollToNestedScrollView() = apply {
        // TODO: Check with standard impl
        interaction.check(ViewAssertions.matches(CoreMatchers.anything()))
            .perform(Actions.NestedScrollViewExtension())
    }

    fun setDate(date: Date, normalizeMonthOfYear: Boolean = true) =
        perform(
            PickerActions.setDate(
                date.asYear,
                date.asMonth + (!normalizeMonthOfYear).toInt(),
                date.asDayOfMonth
            )
        )

    interface EspressoRecyclerViewActions : EspressoActions {
        fun scrollToHolder(viewHolderMatcher: Matcher<RecyclerView.ViewHolder>): EspressoActions =
            perform(RecyclerViewActions.scrollToHolder(viewHolderMatcher))
    }

    private fun Boolean.toInt() = when (this) {
        true -> 1
        false -> 0
    }
}
