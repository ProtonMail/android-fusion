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
