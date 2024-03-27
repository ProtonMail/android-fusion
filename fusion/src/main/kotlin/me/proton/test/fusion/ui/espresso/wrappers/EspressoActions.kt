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
