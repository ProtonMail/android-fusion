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

package me.proton.fusion.ui.espresso.builders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.Root
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import me.proton.fusion.ui.espresso.EspressoWaiter
import me.proton.fusion.ui.espresso.wrappers.EspressoActions
import me.proton.fusion.ui.espresso.wrappers.EspressoActions.EspressoRecyclerViewActions
import me.proton.fusion.ui.espresso.wrappers.EspressoMatchers
import org.hamcrest.Matcher
import java.util.concurrent.atomic.AtomicReference

class OnRecyclerView : EspressoMatchers<OnRecyclerView>(), EspressoRecyclerViewActions, EspressoWaiter {
    private val position get() = AtomicReference<Int>(null)
    private val viewHolderMatcher get() = AtomicReference<Matcher<ViewHolder>>(null)
    override val interaction: ViewInteraction get() = Espresso.onView(finalMatcher).inRoot(finalRootMatcher)

    override fun perform(viewAction: ViewAction): EspressoActions =
        when (true) {
            (position.get() != null) -> actionOnItemAtPosition<ViewHolder>(position.get(), viewAction)
            (viewHolderMatcher.get() != null) -> actionOnHolderItem(viewHolderMatcher.get(), viewAction)
            else -> viewAction
        }.let {
            waitFor { interaction.perform(it) }
        }

    fun onHolderItem(viewHolderMatcher: Matcher<ViewHolder>): EspressoRecyclerViewActions = apply {
        this.viewHolderMatcher.set(viewHolderMatcher)
    }

    fun onItemAtPosition(position: Int): EspressoRecyclerViewActions = apply {
        this.position.set(position)
    }

    override fun addViewMatcher(viewMatcher: Matcher<View>): OnRecyclerView = apply { matchers.add(viewMatcher) }
    override fun addRootMatcher(rootMatcher: Matcher<Root>): OnRecyclerView = apply { rootMatchers.add(rootMatcher) }
}
