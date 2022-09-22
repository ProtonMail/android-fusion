package me.proton.fusion.ui.espresso

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object Assertions {

    class Exists(viewMatcher: Matcher<View>) : ViewAssertion {

        val matcher = viewMatcher

        override fun check(view: View?, noView: NoMatchingViewException?) {
            if (view == null) {
                ViewMatchers.assertThat(
                    "View with a given matcher: $matcher is not present in the hierarchy",
                    false,
                    Matchers.`is`(true)
                )
            }
        }
    }

    fun exists(viewMatcher: Matcher<View>): ViewAssertion {
        return Exists(viewMatcher)
    }
}
