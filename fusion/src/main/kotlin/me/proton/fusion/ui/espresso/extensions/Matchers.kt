package me.proton.fusion.ui.espresso.extensions

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object Matchers {
    fun stringLengthMatcher(length: Int) = object : TypeSafeMatcher<String>(String::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("String length equals $length")
        }

        override fun matchesSafely(item: String?): Boolean = item?.length == length
    }
}
