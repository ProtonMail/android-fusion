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
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.core.view.descendants
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.platform.app.InstrumentationRegistry
import me.proton.fusion.waits.ConditionWatcher
import org.junit.Assert
import org.hamcrest.Matcher

/**
 * Set of custom [ViewAction]s.
 */
object Actions : ConditionWatcher {

    fun setNumberPickerValue(num: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                (view as NumberPicker).value = num
            }

            override fun getDescription(): String = "Set the passed number into the NumberPicker"

            override fun getConstraints(): Matcher<View> =
                isAssignableFrom(NumberPicker::class.java)
        }
    }

    /**
     * Can be used to click a child with id in [RecyclerView.ViewHolder].
     */
    fun clickOnChildWithId(@IdRes id: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                view.findViewById<View>(id).performClick()
            }

            override fun getDescription(): String = "Click child view with id."

            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
        }
    }

    /**
     * Iterates through [RecyclerView.ViewHolder] views and clicks one that matches provided matcher.
     * @param matcher - matcher that matcher a view inside a holder item view.
     */
    fun clickOnMatchedDescendant(matcher: Matcher<View>): ViewAction {
        return object : ViewAction {

            override fun getDescription(): String = "Click child view that matches: \"$matcher\""

            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

            override fun perform(uiController: UiController, view: View) {
                if (view is ViewGroup) {
                    view.children.forEach { childView ->
                        if (matcher.matches(childView)) {
                            childView.performClick()
                            return
                        } else {
                            // Recursively call perform() function till we reach the last view.
                            perform(uiController, childView)
                        }
                    }
                }
            }
        }
    }

    fun descendantsWithIdInvisible(@IdRes id: Int) = DescendantsWithIdInvisibleViewAssertion(id)

    class DescendantsWithIdInvisibleViewAssertion(@IdRes id: Int) : ViewAssertion {
        private val descendantId = id
        private val resourceName = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getResourceEntryName(descendantId)

        override fun check(view: View?, noView: NoMatchingViewException?) {
            Assert.assertTrue(
                "Ancestor view should be displayed in the hierarchy but it wasn't",
                ViewMatchers.isDisplayed().matches(view)
            )
            var visibleViewExist = false
            (view as ViewGroup).descendants.forEach { descendantView ->
                val matches =
                    ViewMatchers.withId(descendantId).matches(descendantView)
                        .and(
                            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                                .matches(descendantView)
                        )
                if (matches) {
                    visibleViewExist = true
                }
            }
            Assert.assertFalse(
                "View with id: $resourceName was displayed but it shouldn't.",
                visibleViewExist
            )
        }
    }
}
