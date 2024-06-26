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

package me.proton.test.fusion.ui.espresso.wrappers

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.test.fusion.FusionConfig.targetContext
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

/**
 * Provides the API for [ViewMatchers] generation.
 */
abstract class EspressoMatchers<T> {
    val matchers: ArrayList<Matcher<View>> = arrayListOf()
    val rootMatchers: ArrayList<Matcher<Root>> = arrayListOf()

    abstract fun addViewMatcher(viewMatcher: Matcher<View>): T
    abstract fun addRootMatcher(rootMatcher: Matcher<Root>): T

    /** Final [Matcher] for the view. **/
    val finalMatcher: Matcher<View> get() = AllOf.allOf(matchers)

    /** Final [Matcher] for the root. **/
    val finalRootMatcher: Matcher<Root>
        get() =
            if (rootMatchers.isEmpty()) RootMatchers.DEFAULT else AllOf.allOf(rootMatchers)

    /** 'With' matchers **/
    fun withId(@IdRes id: Int) = addViewMatcher(ViewMatchers.withId(id))

    fun withText(@StringRes textId: Int) =
        addViewMatcher(ViewMatchers.withText(targetContext.resources.getString(textId)))

    fun withText(text: String) = addViewMatcher(ViewMatchers.withText(text))

    fun withClassName(className: String) =
        addViewMatcher(ViewMatchers.withClassName(CoreMatchers.equalTo(className)))

    fun withContentDesc(contentDescText: String) =
        addViewMatcher(ViewMatchers.withContentDescription(contentDescText))

    fun withContentDesc(@StringRes contentDescTextId: Int) =
        addViewMatcher(
            ViewMatchers.withContentDescription(
                targetContext.resources.getString(
                    contentDescTextId
                )
            )
        )

    fun withContentDescMatches(contentDescMatcher: Matcher<out CharSequence?>?) =
        addViewMatcher(ViewMatchers.withContentDescription(contentDescMatcher))

    fun withContentDescContains(contentDescText: String) =
        addViewMatcher(
            ViewMatchers.withContentDescription(
                CoreMatchers.containsString(
                    contentDescText
                )
            )
        )

    fun withHint(hint: String) = addViewMatcher(ViewMatchers.withHint(hint))

    fun withHint(@StringRes hintId: Int) = addViewMatcher(ViewMatchers.withHint(hintId))

    fun withInputType(inputType: Int) = addViewMatcher(ViewMatchers.withInputType(inputType))

    fun withParentIndex(indexInParent: Int) =
        addViewMatcher(ViewMatchers.withParentIndex(indexInParent))

    fun withResourceName(resourceName: String) =
        addViewMatcher(ViewMatchers.withResourceName(resourceName))

    fun withSpinnerText(spinnerText: String) =
        addViewMatcher(ViewMatchers.withSpinnerText(spinnerText))

    fun withTag(tag: Any) = addViewMatcher(ViewMatchers.withTagValue(CoreMatchers.`is`(tag)))

    fun withTagKey(tagKey: Int) = addViewMatcher(ViewMatchers.withTagKey(tagKey))

    fun withVisibility(visibility: ViewMatchers.Visibility) =
        addViewMatcher(ViewMatchers.withEffectiveVisibility(visibility))

    fun withDisplayingAtLeast(displayedPercentage: Int) =
        addViewMatcher(ViewMatchers.isDisplayingAtLeast(displayedPercentage))

    fun withErrorText(errorText: String) = addViewMatcher(ViewMatchers.hasErrorText(errorText))

    fun withImeAction(imeAction: Int) = addViewMatcher(ViewMatchers.hasImeAction(imeAction))

    fun withRootMatcher(matcher: Matcher<Root>) = addRootMatcher(matcher)

    fun withCustomMatcher(matcher: Matcher<View>) = addViewMatcher(matcher)

    /** 'Is' matchers **/
    fun isClickable() = addViewMatcher(ViewMatchers.isClickable())

    fun isNotClickable() = addViewMatcher(ViewMatchers.isNotClickable())

    fun isChecked() = addViewMatcher(ViewMatchers.isChecked())

    fun isNotChecked() = addViewMatcher(ViewMatchers.isNotChecked())

    fun isCompletelyDisplayed() = addViewMatcher(ViewMatchers.isCompletelyDisplayed())

    fun isEnabled() = addViewMatcher(ViewMatchers.isEnabled())

    fun isDisabled() = addViewMatcher(CoreMatchers.not(ViewMatchers.isEnabled()))

    fun isFocusable() = addViewMatcher(ViewMatchers.isFocusable())

    fun isNotFocusable() = addViewMatcher(ViewMatchers.isNotFocusable())

    fun isFocused() = addViewMatcher(ViewMatchers.isFocused())

    fun isNotFocused() = addViewMatcher(ViewMatchers.isNotFocused())

    fun isSelected() = addViewMatcher(ViewMatchers.isSelected())

    /** 'Has' matchers **/
    fun hasSibling(siblingView: EspressoMatchers<T>) =
        addViewMatcher(ViewMatchers.hasSibling(siblingView.finalMatcher))

    fun hasParent(parentView: EspressoMatchers<T>) =
        addViewMatcher(ViewMatchers.withParent(parentView.finalMatcher))

    fun hasChild(childMatcher: EspressoMatchers<T>) =
        addViewMatcher(ViewMatchers.withChild(childMatcher.finalMatcher))

    fun hasDescendant(descendantView: EspressoMatchers<T>) =
        addViewMatcher(ViewMatchers.hasDescendant(descendantView.finalMatcher))

    fun hasAncestor(ancestorView: EspressoMatchers<T>) =
        addViewMatcher(ViewMatchers.isDescendantOfA(ancestorView.finalMatcher))

    fun hasChildCount(childCount: Int) = addViewMatcher(ViewMatchers.hasChildCount(childCount))

    fun hasContentDescription() = addViewMatcher(ViewMatchers.hasContentDescription())

    fun hasFocus() = addViewMatcher(ViewMatchers.hasFocus())

    fun hasLinks() = addViewMatcher(ViewMatchers.hasLinks())

    /** String matchers **/

    fun startsWith(text: String) =
        addViewMatcher(ViewMatchers.withText(CoreMatchers.startsWith(text)))

    fun startsWithIgnoringCase(text: String) =
        addViewMatcher(ViewMatchers.withText(CoreMatchers.startsWithIgnoringCase(text)))

    fun endsWith(text: String) = addViewMatcher(ViewMatchers.withText(CoreMatchers.endsWith(text)))

    fun endsWithIgnoringCase(text: String) =
        addViewMatcher(ViewMatchers.withText(CoreMatchers.endsWithIgnoringCase(text)))

    fun containsText(substring: String) = addViewMatcher(ViewMatchers.withSubstring(substring))

    fun containsTextIgnoringCase(text: String) =
        addViewMatcher(ViewMatchers.withText(CoreMatchers.containsStringIgnoringCase(text)))

    fun containsText(@StringRes text: Int, vararg formatArgs: Any) =
        addViewMatcher(ViewMatchers.withSubstring(targetContext.getString(text, formatArgs)))

    /** Other matchers **/
    fun supportsInputMethods() = addViewMatcher(ViewMatchers.supportsInputMethods())

    fun instanceOf(clazz: Class<*>?) = addViewMatcher(CoreMatchers.instanceOf(clazz))
}
