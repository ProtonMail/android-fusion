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

package me.proton.test.fusion.ui.espresso.builders

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Root
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import me.proton.test.fusion.ui.FusionWaiter
import me.proton.test.fusion.ui.espresso.wrappers.EspressoActions
import me.proton.test.fusion.ui.espresso.wrappers.EspressoMatchers
import org.hamcrest.Matcher

/**
 * Builder like class that allows to write [ViewActions] and view assertions for ListView items.
 */
open class OnView : EspressoMatchers<OnView>(), EspressoActions, FusionWaiter {
    override val interaction: ViewInteraction
        get() = Espresso.onView(finalMatcher).inRoot(finalRootMatcher)

    override fun addViewMatcher(viewMatcher: Matcher<View>) = apply { matchers.add(viewMatcher) }
    override fun addRootMatcher(rootMatcher: Matcher<Root>) =
        apply { rootMatchers.add(rootMatcher) }

    override fun perform(viewAction: ViewAction) = waitFor { interaction.perform(viewAction) }
}
