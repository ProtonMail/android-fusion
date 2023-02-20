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

package me.proton.test.fusion.ui.espresso.builders

import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers
import me.proton.test.fusion.utils.ActivityProvider
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/**
 * Simplifies syntax for applying multiple [RootMatchers] to a root view.
 */
class OnRootView : OnView() {
    fun isPlatformPopUp() = apply {
        rootMatchers.add(RootMatchers.isPlatformPopup())
    }

    fun isDialog() = apply {
        rootMatchers.add(RootMatchers.isDialog())
    }

    fun rootIsFocusable() = apply {
        rootMatchers.add(RootMatchers.isFocusable())
    }

    fun isSystemAlertWindow() = apply {
        rootMatchers.add(RootMatchers.isSystemAlertWindow())
    }

    fun isTouchable() = apply {
        rootMatchers.add(RootMatchers.isTouchable())
    }

    fun withDecorView(view: OnView) = apply {
        rootMatchers.add(RootMatchers.withDecorView(view.finalMatcher))
    }

    fun withCustomMatcher(matcher: Matcher<Root>) = apply {
        rootMatchers.add(matcher)
    }

    fun withNotCurrentActivityDecorView() = apply {
        val notCurrentActivityWindow =
            CoreMatchers.not(ActivityProvider.currentActivity!!.window.decorView)
        rootMatchers.add(RootMatchers.withDecorView(notCurrentActivityWindow))
    }
}
