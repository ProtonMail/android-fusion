package me.proton.fusion.ui.espresso

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import me.proton.fusion.FusionConfig
import me.proton.fusion.utils.StringUtils
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import java.util.ArrayList

@Suppress("UNCHECKED_CAST")
open class OnViewMatchers<T> {

    protected var timeout = FusionConfig.commandTimeout
    protected val matchers: ArrayList<Matcher<View>>
        get() = arrayListOf()
    private val rootMatchers: ArrayList<Matcher<Root>>
        get() = arrayListOf()

    fun withTimeout(milliseconds: Long) = apply {
        timeout = milliseconds
    }

    /** Final [Matcher] for the view. **/
    fun viewMatcher(): Matcher<View> = AllOf.allOf(matchers)

    /** Final [Matcher] for the root. **/
    fun rootMatcher(): Matcher<Root> =
        if (rootMatchers.isEmpty()) RootMatchers.DEFAULT else AllOf.allOf(rootMatchers)

    fun instanceOf(clazz: Class<*>?): T {
        matchers.add(CoreMatchers.instanceOf(clazz))
        return this as T
    }

    fun isEnabled(): T {
        matchers.add(ViewMatchers.isEnabled())
        return this as T
    }

    fun hasSibling(siblingView: OnView): T {
        matchers.add(ViewMatchers.hasSibling(siblingView.viewMatcher()))
        return this as T
    }

    fun withId(@IdRes id: Int): T {
        matchers.add(ViewMatchers.withId(id))
        return this as T
    }

    fun hasParent(parentView: OnView): T {
        matchers.add(ViewMatchers.withParent(parentView.viewMatcher()))
        return this as T
    }

    fun withText(@StringRes textId: Int): T {
        matchers.add(ViewMatchers.withText(StringUtils.stringFromResource(textId)))
        return this as T
    }

    fun withText(text: String): T {
        matchers.add(ViewMatchers.withText(text))
        return this as T
    }

    fun containsTextIgnoringCase(text: String): T {
        matchers.add(ViewMatchers.withText(CoreMatchers.containsStringIgnoringCase(text)))
        return this as T
    }

    fun startsWith(text: String): T {
        matchers.add(ViewMatchers.withText(CoreMatchers.startsWith(text)))
        return this as T
    }

    fun startsWithIgnoringCase(text: String): T {
        matchers.add(ViewMatchers.withText(CoreMatchers.startsWithIgnoringCase(text)))
        return this as T
    }

    fun endsWith(text: String): T {
        matchers.add(ViewMatchers.withText(CoreMatchers.endsWith(text)))
        return this as T
    }

    fun endsWithIgnoringCase(text: String): T {
        matchers.add(ViewMatchers.withText(CoreMatchers.endsWithIgnoringCase(text)))
        return this as T
    }

    fun isClickable(): T {
        matchers.add(ViewMatchers.isClickable())
        return this as T
    }

    fun isNotClickable(): T {
        matchers.add(ViewMatchers.isNotClickable())
        return this as T
    }

    fun isChecked(): T {
        matchers.add(ViewMatchers.isChecked())
        return this as T
    }

    fun isCompletelyDisplayed(): T {
        matchers.add(ViewMatchers.isCompletelyDisplayed())
        return this as T
    }

    fun isDescendantOf(ancestorView: OnView): T {
        matchers.add(ViewMatchers.isDescendantOfA(ancestorView.viewMatcher()))
        return this as T
    }

    fun isDisplayingAtLeast(displayedPercentage: Int): T {
        matchers.add(ViewMatchers.isDisplayingAtLeast(displayedPercentage))
        return this as T
    }

    fun isDisabled(): T {
        matchers.add(CoreMatchers.not(ViewMatchers.isEnabled()))
        return this as T
    }

    fun isFocusable(): T {
        matchers.add(ViewMatchers.isFocusable())
        return this as T
    }

    fun isFocused(): T {
        matchers.add(ViewMatchers.isFocused())
        return this as T
    }

    fun isNotFocusable(): T {
        matchers.add(ViewMatchers.isNotFocusable())
        return this as T
    }

    fun isNotFocused(): T {
        matchers.add(ViewMatchers.isNotFocused())
        return this as T
    }

    fun isNotChecked(): T {
        matchers.add(ViewMatchers.isNotChecked())
        return this as T
    }

    fun isSelected(): T {
        matchers.add(ViewMatchers.isSelected())
        return this as T
    }

    fun hasChildCount(childCount: Int): T {
        matchers.add(ViewMatchers.hasChildCount(childCount))
        return this as T
    }

    fun hasContentDescription(): T {
        matchers.add(ViewMatchers.hasContentDescription())
        return this as T
    }

    fun hasDescendant(descendantView: OnView): T {
        matchers.add(ViewMatchers.hasDescendant(descendantView.viewMatcher()))
        return this as T
    }

    fun hasErrorText(errorText: String): T {
        matchers.add(ViewMatchers.hasErrorText(errorText))
        return this as T
    }

    fun hasFocus(): T {
        matchers.add(ViewMatchers.hasFocus())
        return this as T
    }

    fun hasImeAction(imeAction: Int): T {
        matchers.add(ViewMatchers.hasImeAction(imeAction))
        return this as T
    }

    fun hasLinks(): T {
        matchers.add(ViewMatchers.hasLinks())
        return this as T
    }

    fun supportsInputMethods(): T {
        matchers.add(ViewMatchers.supportsInputMethods())
        return this as T
    }

    fun hasChild(childMatcher: OnView): T {
        matchers.add(ViewMatchers.withChild(childMatcher.viewMatcher()))
        return this as T
    }

    fun withClassName(className: String): T {
        matchers.add(ViewMatchers.withClassName(CoreMatchers.equalTo(className)))
        return this as T
    }

    fun withContentDesc(contentDescText: String): T {
        matchers.add(ViewMatchers.withContentDescription(contentDescText))
        return this as T
    }

    fun withContentDesc(@StringRes contentDescTextId: Int): T {
        matchers.add(
            ViewMatchers.withContentDescription(
                StringUtils.stringFromResource(contentDescTextId)
            )
        )
        return this as T
    }

    fun withContentDescMatches(contentDescMatcher: Matcher<out CharSequence?>?): T {
        matchers.add(ViewMatchers.withContentDescription(contentDescMatcher))
        return this as T
    }

    fun withContentDescContains(contentDescText: String): T {
        matchers.add(ViewMatchers.withContentDescription(CoreMatchers.containsString(contentDescText)))
        return this as T
    }

    fun withHint(hint: String): T {
        matchers.add(ViewMatchers.withHint(hint))
        return this as T
    }

    fun withHint(@StringRes hintId: Int): T {
        matchers.add(ViewMatchers.withHint(hintId))
        return this as T
    }

    fun withInputType(inputType: Int): T {
        matchers.add(ViewMatchers.withInputType(inputType))
        return this as T
    }

    fun withParentIndex(indexInParent: Int): T {
        matchers.add(ViewMatchers.withParentIndex(indexInParent))
        return this as T
    }

    fun withResName(resourceName: String): T {
        matchers.add(ViewMatchers.withResourceName(resourceName))
        return this as T
    }

    fun containsText(substring: String): T {
        matchers.add(ViewMatchers.withSubstring(substring))
        return this as T
    }

    fun withSpinnerText(spinnerText: String): T {
        matchers.add(ViewMatchers.withSpinnerText(spinnerText))
        return this as T
    }

    fun withTag(tag: Any): T {
        matchers.add(ViewMatchers.withTagValue(CoreMatchers.`is`(tag)))
        return this as T
    }

    fun withTagKey(tagKey: Int): T {
        matchers.add(ViewMatchers.withTagKey(tagKey))
        return this as T
    }

    fun withVisibility(visibility: ViewMatchers.Visibility): T {
        matchers.add(ViewMatchers.withEffectiveVisibility(visibility))
        return this as T
    }

    fun withCustomMatcher(matcher: Matcher<View>): T {
        matchers.add(matcher)
        return this as T
    }

    fun withRootMatcher(matcher: Matcher<Root>): T {
        rootMatchers.add(matcher)
        return this as T
    }

    fun inRoot(root: OnRootView): T {
        rootMatchers.add(root.matcher())
        return this as T
    }
}
