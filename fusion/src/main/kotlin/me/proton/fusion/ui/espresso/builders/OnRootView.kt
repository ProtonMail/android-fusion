package me.proton.fusion.ui.espresso.builders

import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers
import me.proton.fusion.utils.ActivityProvider
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
