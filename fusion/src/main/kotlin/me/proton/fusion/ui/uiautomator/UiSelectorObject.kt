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

package me.proton.fusion.ui.uiautomator

import android.graphics.Point
import android.view.MotionEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import junit.framework.TestCase.fail
import me.proton.fusion.FusionConfig
import me.proton.fusion.FusionConfig.UiAutomator.shouldSearchUiObjectEachAction
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
    private var defaultTimeout: Duration = FusionConfig.commandTimeout

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
