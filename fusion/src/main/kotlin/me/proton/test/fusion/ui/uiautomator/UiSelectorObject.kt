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

package me.proton.test.fusion.ui.uiautomator

import android.graphics.Point
import android.view.MotionEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import junit.framework.TestCase.fail
import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.FusionConfig.UiAutomator.shouldSearchUiObjectEachAction
import org.hamcrest.MatcherAssert.assertThat
import kotlin.time.Duration

/**
 * Class that wraps interactions for [UiObject] element.
 */
@Suppress("UNUSED_EXPRESSION")
class UiSelectorObject {

    private var objectSelector: UiSelector = UiSelector()
    private fun enabledState() = uiObject().isEnabled
    private fun clickableState() = uiObject().isClickable
    private fun selectedState() = uiObject().isSelected
    private var defaultTimeout: Duration = FusionConfig.UiAutomator.waitTimeout.get()

    /**
     * Selectors that can be applied to [UiObject].
     */
    fun withText(text: String) =
        apply { objectSelector = objectSelector.text(text) }

    fun containsText(text: String) =
        apply { objectSelector = objectSelector.textContains(text) }

    fun withTextMatches(regex: String) =
        apply { objectSelector = objectSelector.textMatches(regex) }

    fun startsWith(text: String) =
        apply { objectSelector = objectSelector.textStartsWith(text) }

    fun instanceOf(clazz: Class<*>?) =
        apply { objectSelector = objectSelector.className(clazz) }

    fun withClassName(text: String) =
        apply { objectSelector = objectSelector.className(text) }

    fun withClassNameMatches(regex: String) =
        apply { objectSelector = objectSelector.classNameMatches(regex) }

    fun withContentDesc(text: String) =
        apply { objectSelector = objectSelector.description(text) }

    fun withContentDescContains(text: String) =
        apply { objectSelector = objectSelector.descriptionContains(text) }

    fun withContentDescMatches(regex: String) =
        apply { objectSelector = objectSelector.descriptionMatches(regex) }

    fun withContentDescStartsWith(text: String) =
        apply { objectSelector = objectSelector.descriptionStartsWith(text) }

    /**
     * Matches the resource ID by a regular expression.
     * @param regex a regular expression
     */
    fun withResIdMatches(regex: String) =
        apply { objectSelector = objectSelector.resourceIdMatches(regex) }

    fun withResId(id: String) = apply { objectSelector = objectSelector.resourceId(id) }

    fun byIndex(index: Int) = apply { objectSelector = objectSelector.index(index) }

    fun onInstance(instance: Int) = apply { objectSelector = objectSelector.instance(instance) }

    fun isDisabled() = apply { objectSelector = objectSelector.enabled(false) }

    fun isEnabled() = apply { objectSelector = objectSelector.enabled(true) }

    fun isFocusable() = apply { objectSelector = objectSelector.focusable(true) }

    fun isNotFocusable() = apply { objectSelector = objectSelector.focusable(false) }

    fun isFocused() = apply { objectSelector = objectSelector.focused(true) }

    fun isNotFocused() = apply { objectSelector = objectSelector.focused(false) }

    fun isSelected() = apply { objectSelector = objectSelector.selected(true) }

    fun isNotSelected() = apply { objectSelector = objectSelector.selected(false) }

    fun isScrollable() = apply { objectSelector = objectSelector.scrollable(true) }

    fun isNotScrollable() = apply { objectSelector = objectSelector.scrollable(false) }

    fun isClickable() = apply { objectSelector = objectSelector.clickable(true) }

    fun isNotClickable() = apply { objectSelector = objectSelector.clickable(false) }

    fun isCheckable() = apply { objectSelector = objectSelector.checkable(true) }

    fun isNotCheckable() = apply { objectSelector = objectSelector.checkable(false) }

    fun isChecked() = apply { objectSelector = objectSelector.checked(true) }

    fun isNotChecked() = apply { objectSelector = objectSelector.checked(false) }

    fun isLongClickable() = apply { objectSelector = objectSelector.longClickable(true) }

    fun isNotLongClickable() = apply { objectSelector = objectSelector.longClickable(false) }

    fun withPkg(name: String) = apply { objectSelector = objectSelector.packageName(name) }

    fun withPkgMatches(regex: String) =
        apply { objectSelector = objectSelector.packageNameMatches(regex) }


    /**
     * Child and sibling selectors.
     */
    fun onSibling(uiObject: UiSelectorObject) =
        apply { objectSelector.apply { fromParent(uiObject.objectSelector) } }

    fun onChild(uiObject: UiSelectorObject) =
        apply { objectSelector.apply { childSelector(uiObject.objectSelector) } }

    /**
     * Actions.
     */
    fun click() = apply { uiObject().click() }

    fun clearText() = apply { uiObject().clearTextField() }

    fun longClick() = apply { uiObject().longClick() }

    fun clickAndWaitForNewWindow() = apply { uiObject().clickAndWaitForNewWindow() }

    fun clickBottomRight() = apply { uiObject().clickBottomRight() }

    fun clickTopLeft() = apply { uiObject().clickTopLeft() }

    fun performMultiPointerGesture(vararg touches: MotionEvent.PointerCoords?) =
        apply { uiObject().performMultiPointerGesture(touches) }

    fun performTwoPointerGesture(
        startPoint1: Point,
        startPoint2: Point,
        endPoint1: Point,
        endPoint2: Point,
        steps: Int = 100
    ) = apply {
        uiObject().performTwoPointerGesture(
            startPoint1,
            startPoint2,
            endPoint1,
            endPoint2,
            steps
        )
    }

    fun pinchIn(percent: Int = 30, steps: Int = 100) = apply { uiObject().pinchIn(percent, steps) }

    fun pinchOut(percent: Int = 30, steps: Int = 100) =
        apply { uiObject().pinchOut(percent, steps) }

    fun typeText(text: String) = apply { uiObject().text = text }

    fun swipeDown(steps: Int = 100) = apply { uiObject().swipeDown(steps) }

    fun swipeRight(steps: Int = 100) = apply { uiObject().swipeRight(steps) }

    fun swipeLeft(steps: Int = 100) = apply { uiObject().swipeLeft(steps) }

    fun swipeUp(steps: Int = 100) = apply { uiObject().swipeUp(steps) }

    /**
     * Checkers.
     */
    fun checkContainsText(text: String) = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} to contain text \"$text\".",
            uiObject().text.contains(text)
        )
    }

    // TODO update error reason
    fun checkDoesNotContainText(text: String) = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} to not contain text \"$text\".",
            !uiObject().text.contains(text)
        )
    }

    fun checkIsChecked() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} checked value to be true but got false",
            uiObject().isChecked
        )
    }

    fun checkIsNotChecked() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} checked value to be false but got true",
            uiObject().isChecked
        )
    }

    fun checkExists() = apply { uiObject() }

    fun checkDoesNotExist() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} to exist but it doesn't.",
            !uiObject().exists()
        )
    }

    fun checkEnabled() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} to exist but it doesn't.",
            uiObject().isEnabled
        )
    }

    fun checkDisabled() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} isEnabled value to be false but got true.",
            !uiObject().isEnabled
        )
    }

    fun checkIsSelected() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} isSelected value to be true but got false.",
            uiObject().isSelected
        )
    }

    fun checkIsNotSelected() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} isSelected value to be false but got true.",
            !uiObject().isSelected
        )
    }

    fun checkIsFocused() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} isSelected value to be true but got false.",
            uiObject().isFocused
        )
    }

    fun checkIsNotFocused() = apply {
        assertThat(
            "Expected object with selector: ${uiObject().selector} isSelected value to be false but got true.",
            !uiObject().isFocused
        )
    }

    /**
     * Object waits.
     */
    fun waitForExists() =
        apply { uiObject() }

    fun waitForEnabled() =
        apply { enabledState() }

    fun waitForDisabled() =
        apply { !enabledState() }

    fun waitForClickable() =
        apply { clickableState() }

    fun waitForSelected() =
        apply { selectedState() }

    fun waitUntilGone() =
        apply { uiObject(shouldExist = false).waitUntilGone(defaultTimeout.inWholeMilliseconds) }

    /** Timeout **/
    fun withTimeout(timeout: Duration = defaultTimeout) = apply { defaultTimeout = timeout }

    /** The core function where object selector is defined. **/
    private fun uiObject(shouldExist: Boolean = true): UiObject {
        if (objectSelector.toString() == "UiSelector[]") {
            fail("Object selectors were not provided.")
        }
        val locatedObject = uiDevice.findObject(objectSelector)
        return if (!locatedObject.exists() || shouldSearchUiObjectEachAction && shouldExist) {
            assertThat(
                "Expected object with selector: $objectSelector to exist but it doesn't ",
                locatedObject.exists()
            )
            locatedObject
        } else {
            locatedObject
        }
    }

    companion object {
        private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        private val config: Configurator = Configurator.getInstance()

        fun setObjectSelectorTimeout(timeout: Long = 500): Configurator =
            config.setWaitForSelectorTimeout(timeout)

        fun setObjectIdleTimeout(timeout: Long = 500): Configurator =
            config.setWaitForIdleTimeout(timeout)

        fun setActionAcknowledgmentTimeout(value: Long) {
            config.actionAcknowledgmentTimeout = value
        }
    }
}
