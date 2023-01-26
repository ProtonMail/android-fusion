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

package me.proton.fusion.ui.espresso.builders

import android.view.View
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.DataInteraction.DisplayDataMatcher.displayDataMatcher
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.AdapterViewProtocols
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.util.EspressoOptional
import me.proton.fusion.ui.espresso.EspressoWaiter
import me.proton.fusion.ui.espresso.wrappers.EspressoActions
import me.proton.fusion.ui.espresso.wrappers.EspressoMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import java.util.concurrent.atomic.AtomicReference

/**
 * Builder like class that allows to write [ViewActions] and view assertions for ListView items.
 */
class OnListView : EspressoMatchers<OnListView>(), EspressoActions, EspressoWaiter {
    private val adapterViewMatcher = AtomicReference<Matcher<View>>(null)
    private val childViewMatcher = AtomicReference<Matcher<View>>(null)
    private val position: AtomicReference<Int> = AtomicReference(null)

    override fun perform(viewAction: ViewAction): EspressoActions = waitFor {
        interaction.perform(viewAction)
    }

    override val interaction: ViewInteraction
        get() {
            var targetViewMatcher: Matcher<View> = displayDataMatcher(
                adapterViewMatcher.get(),
                finalMatcher,
                finalRootMatcher,
                EspressoOptional.fromNullable(position.get()),
                AdapterViewProtocols.standardProtocol()
            )
            if (childViewMatcher.get() != null) {
                targetViewMatcher = allOf(childViewMatcher.get(), isDescendantOfA(targetViewMatcher))
            }

            return onView(targetViewMatcher).inRoot(finalRootMatcher)
        }

    /** [DataInteraction] matcher functions. **/
    fun atPosition(position: Int) = apply {
        this.position.set(position)
    }

    fun inAdapterView(adapterView: OnView) = apply {
        adapterViewMatcher.set(adapterView.finalMatcher)
    }

    fun onChild(childView: OnView) = apply {
        childViewMatcher.set(childView.finalMatcher)
    }

    override fun addViewMatcher(viewMatcher: Matcher<View>): OnListView = apply { matchers.add(viewMatcher) }
    override fun addRootMatcher(rootMatcher: Matcher<Root>): OnListView = apply { rootMatchers.add(rootMatcher) }
}
