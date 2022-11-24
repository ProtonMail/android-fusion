/*
 * Copyright (c) 2021 Proton Technologies AG
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
import android.graphics.Rect
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.hamcrest.MatcherAssert
import androidx.test.uiautomator.UiObject2
import me.proton.fusion.FusionConfig
import org.hamcrest.CoreMatchers.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Class that wraps interactions for [UiObject2] element.
 */
@OptIn(ExperimentalTime::class)
open class ByObject() : BySelectorGenerator<ByObject>() {

    constructor(newObject: UiObject2, newSelector: BySelector) : this() {
        locatedObject = newObject
        objectSelector = newSelector
    }

    private var onDescendantObject: ByObject? = null
    private var descendantWithDepth: BySelector? = null
    private var depth: Int? = null
    private var exceptionSelectorText: String = ""
    private var givenObject: UiObject2? = null
    private var givenObjectSelector: BySelector? = null
    private var locatedObject: UiObject2? = null
    private var objectSelectorHash: Int? = null
    private var defaultTimeout: Duration = FusionConfig.commandTimeout
    protected var locatedObjects: List<UiObject2>? = null
    protected var objectPosition: Int? = null

    fun withTimeout(milliseconds: Duration) = apply { defaultTimeout = milliseconds }

    /** [UiObject] properties. **/
    fun childCount(): Int = uiObject2().childCount
    fun checkable(): Boolean = uiObject2().isCheckable
    fun packageName(): String = uiObject2().applicationPackage
    fun text(): String = uiObject2().text
    fun selected(): Boolean = uiObject2().isSelected
    fun scrollable(): Boolean = uiObject2().isScrollable
    fun focusable(): Boolean = uiObject2().isFocusable
    fun focused(): Boolean = uiObject2().isFocused
    fun longClickable(): Boolean = uiObject2().isLongClickable
    fun contentDesc(): String = uiObject2().contentDescription
    fun className(): String = uiObject2().className
    fun visibleBounds(): Rect = uiObject2().visibleBounds
    fun clickable(): Boolean = uiObject2().isClickable
    fun resourceName(): String = uiObject2().resourceName
    fun visibleCenter(): Point = uiObject2().visibleCenter
    fun enabled() = locatedObject!!.isEnabled

    /** Descendant functions **/
    fun onDescendant(descendant: ByObject) = apply { this.onDescendantObject = descendant }

    fun onDescendant(descendant: ByObject, depth: Int) = apply {
        this.descendantWithDepth = descendant.objectSelector
        this.depth = depth
    }

    /**
     * Actions.
     */
    fun click() = apply { uiObject2().click() }

    fun drag(point: Point, speed: Int) = apply { uiObject2().drag(point, speed) }

    fun drag(point: Point) = apply { uiObject2().drag(point) }

    fun fling(direction: Direction) = apply { uiObject2().fling(direction) }

    fun fling(direction: Direction, speed: Int) = apply { uiObject2().fling(direction, speed) }

    fun swipeUp() = apply { uiObject2().fling(Direction.UP) }

    fun swipeDown() = apply { uiObject2().fling(Direction.DOWN) }

    fun swipeLeft() = apply { uiObject2().fling(Direction.LEFT) }

    fun swipeRight() = apply { uiObject2().fling(Direction.RIGHT) }

    fun scroll(direction: Direction, percent: Float, speed: Int) =
        apply { uiObject2().scroll(direction, percent, speed) }

    fun scroll(direction: Direction, percent: Float) =
        apply { uiObject2().scroll(direction, percent) }

    fun clearText() = apply { uiObject2().clear() }

    fun longClick() = apply { uiObject2().longClick() }

    fun pinchOut(percent: Float, speed: Int) = apply { uiObject2().pinchOpen(percent, speed) }

    fun pinchIn(percent: Float) = apply { uiObject2().pinchClose(percent) }

    fun typeText(text: String) = apply { uiObject2().text = text }

    /**
     * Checks.
     */
    fun checkContainsText(text: String) = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText to contain text \"$text\".",
            uiObject2().text,
            containsString(text)
        )
    }

    fun checkDoesNotContainText(text: String) = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText to not contain text \"$text\".",
            uiObject2().text,
            not(containsString(text))
        )
    }

    fun checkIsChecked() = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText \"isChecked\" value to be true but got false.",
            uiObject2().isChecked,
            `is`(true)
        )
    }

    fun checkIsNotChecked() = apply {
        if (onDescendantObject == null) {
            exceptionSelectorText = "$exceptionSelectorText"
        }
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText \"isChecked\" value to be false but got true.",
            uiObject2().isChecked,
            not(true)
        )
    }

    fun checkExists() = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText to exist but it isn't.",
            device.findObject(objectSelector),
            not(nullValue())
        )
    }

    fun checkDoesNotExist() = apply {
        MatcherAssert.assertThat(
            "Object with selector: $exceptionSelectorText exists.",
            uiObject2(),
            nullValue()
        )
    }

    fun checkEnabled() = apply {
        MatcherAssert.assertThat(
            "Expected object with selector: $exceptionSelectorText to exist but it doesn't.",
            uiObject2().isEnabled,
            `is`(true)
        )
    }

    fun checkDisabled() = apply {
        MatcherAssert.assertThat(
            "Expected object with selector: $exceptionSelectorText isEnabled value to be false but got true.",
            !uiObject2().isEnabled,
            `is`(false)
        )
    }

    fun checkIsSelected() = apply {
        MatcherAssert.assertThat(
            "Expected object with selector: $exceptionSelectorText isSelected value to be true but got false.",
            uiObject2().isSelected,
            `is`(true)
        )
    }

    fun checkIsNotSelected() = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText isSelected value to be false but got true.",
            uiObject2().isSelected,
            `is`(false)
        )
    }

    fun checkIsFocused() = apply {
        MatcherAssert.assertThat(
            "Expected object with selector: $exceptionSelectorText isSelected value to be true but got false.",
            uiObject2().isFocused,
            `is`(true)
        )
    }

    fun checkIsNotFocused() = apply {
        MatcherAssert.assertThat(
            "Expected $exceptionSelectorText isSelected value to be false but got true.",
            uiObject2().isFocused,
            `is`(false)
        )
    }

    /**
     * Waits.
     */
    fun waitForExists(timeout: Duration = defaultTimeout) = apply {
        val selector = objectSelector
        val byObject = device.wait(Until.findObject(selector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected object with selector: $selector to EXIST but it DOES NOT.",
            byObject,
            notNullValue()
        )
    }

    fun waitUntilGone(timeout: Duration = defaultTimeout) = apply {
        val selector = objectSelector
        /** Fail if selector NULL. **/
        MatcherAssert.assertThat(
            "The object selector was not provided. You should specify it.",
            selector,
            notNullValue()
        )
        /** [Until.gone] returns true if object is gone and false when it exists **/
        val byObject: Boolean = device.wait(Until.gone(selector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected object with selector: $selector to GONE but it EXISTS.",
            byObject,
            `is`(true)
        )
    }

    protected open fun waitForObject(
        bySelector: BySelector?,
        timeout: Duration = defaultTimeout
    ): UiObject2 {
        /** Fail if selector NULL. **/
        MatcherAssert.assertThat(
            "The object selector was not provided. Please specify it.",
            bySelector,
            notNullValue()
        )
        val byObject = device.wait(Until.findObject(bySelector), timeout.inWholeMilliseconds)
        /** Fail if object was not found, i.e. it is NULL. **/
        MatcherAssert.assertThat(
            "Expected object with selector: $bySelector to exist but it doesn't.",
            byObject,
            notNullValue()
        )
        return byObject
    }

    protected open fun waitForDescendant(
        bySelector: BySelector?,
        descendantSelector: BySelector?,
        timeout: Duration = defaultTimeout
    ): UiObject2 {
        MatcherAssert.assertThat(
            "The ancestor selector was not provided. Please specify it.",
            bySelector,
            notNullValue()
        )
        val byObject = device.wait(Until.findObject(bySelector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected object with selector: $bySelector to exist but it doesn't.",
            byObject,
            notNullValue()
        )
        MatcherAssert.assertThat(
            "The descendant selector was not provided. Please specify it.",
            descendantSelector,
            notNullValue()
        )
        val descendantObject = byObject.wait(Until.findObject(descendantSelector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected object with selector: $descendantSelector to EXIST but it DOES NOT.",
            descendantObject,
            notNullValue()
        )
        return descendantObject
    }

    protected open fun waitForDescendant(
        descendantSelector: BySelector?,
        timeout: Duration = defaultTimeout
    ): UiObject2 {
        MatcherAssert.assertThat(
            "The descendant selector was not provided. Please specify it.",
            descendantSelector,
            notNullValue()
        )
        val descendantObject = givenObject!!.wait(Until.findObject(descendantSelector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected object with selector: $descendantSelector to EXIST but it DOES NOT.",
            descendantObject,
            notNullValue()
        )
        return descendantObject
    }

    fun waitForChecked(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.checked(true), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be CHECKED but it IS NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForUnchecked(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.checked(false), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be NOT CHECKED but it IS.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForNotFocused(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.focused(false), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be NOT FOCUSED but it IS.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForEnabled(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.enabled(true), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be ENABLED but it IS NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForDisabled(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.enabled(false), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be DISABLED but it IS NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForClickable(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.clickable(true), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be DISABLED but it IS NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForSelected(timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.selected(true), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to be SELECTED but it IS NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitUntilHasObject(byObject: ByObject, timeout: Duration = defaultTimeout) =
        apply {
            val locatedObject = uiObject2().wait(Until.hasObject(byObject.objectSelector), timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText to HAVE OBJECT but it DOES NOT.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForCustomObjectCondition(
        objectCondition: UiObject2Condition<Boolean>,
        timeout: Duration = defaultTimeout
    ) =
        apply {
            val locatedObject = uiObject2().wait(objectCondition, timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $objectCondition result to be TRUE but it FALSE.",
                locatedObject,
                `is`(true)
            )
        }

    fun waitForCustomSearchCondition(
        searchCondition: SearchCondition<UiObject2>,
        timeout: Duration = defaultTimeout
    ) =
        apply {
            val locatedObject = uiObject2().wait(searchCondition, timeout.inWholeMilliseconds)
            MatcherAssert.assertThat(
                "Expected $exceptionSelectorText with search condition $searchCondition to EXIST but it DOES NOT.",
                locatedObject,
                notNullValue()
            )
        }

    /** The core function where object selector is defined. **/
    protected open fun uiObject2(): UiObject2 {
        /**
         * If object was already located and selector hash is the same return located object.
         * Otherwise let UiAutomator locate it.
         * Locate object again if shouldSearchByObjectEachAction was set to true.
         */
        if (
            locatedObject == null || FusionConfig.uiAutomator.shouldSearchByObjectEachAction
        ) {
            // Update object selector hash as it may changed.
            when {
                /**
                 * The case when [ByObject] was created with the given [UiObject2] instance.
                 */
                givenObject != null -> {
                    return if (onDescendantObject != null) {
                        locatedObject = waitForDescendant(onDescendantObject!!.objectSelector)
                        locatedObject!!
                    } else {
                        locatedObject = givenObject
                        locatedObject!!
                    }
                }
                onDescendantObject != null -> {
                    exceptionSelectorText =
                        "descendant object: ${onDescendantObject!!.objectSelector} and ancestor: $objectSelector"
                    locatedObject =
                        waitForDescendant(
                            objectSelector,
                            onDescendantObject!!.objectSelector
                        )
                    return locatedObject!!
                }
                else -> {
                    exceptionSelectorText = "object: $objectSelector"
                    locatedObject = waitForObject(objectSelector)
                    return locatedObject!!
                }
            }
        } else {
            return locatedObject!!
        }
    }

    companion object {
        private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }
}
