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
import androidx.test.espresso.Espresso
import androidx.test.espresso.Root
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import me.proton.fusion.ui.espresso.EspressoWaiter
import me.proton.fusion.ui.espresso.wrappers.EspressoActions
import me.proton.fusion.ui.espresso.wrappers.EspressoMatchers
import org.hamcrest.Matcher

/**
 * Builder like class that allows to write [ViewActions] and view assertions for ListView items.
 */
open class OnView : EspressoMatchers<OnView>(), EspressoActions, EspressoWaiter {
    override val interaction: ViewInteraction get() = Espresso.onView(finalMatcher).inRoot(finalRootMatcher)
    override fun addViewMatcher(viewMatcher: Matcher<View>) = apply { matchers.add(viewMatcher) }
    override fun addRootMatcher(rootMatcher: Matcher<Root>) = apply { rootMatchers.add(rootMatcher) }
    override fun perform(viewAction: ViewAction) = waitFor { interaction.perform(viewAction) }
}