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

package me.proton.test.fusion.ui.uiautomator

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import me.proton.test.fusion.FusionConfig
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.fail
import kotlin.time.Duration

/**
 * Class that wraps interactions for [UiObject2] elements.
 */
class ByObjects : BySelectorGenerator<ByObjects>() {

    private var exceptionSelectorText: String = ""
    private var selected: Boolean? = null
    private var scrollable: Boolean? = null
    private var shouldWaitForObject: Boolean = true
    private var objectSelectorHash: Int? = null
    private var defaultTimeout: Duration = FusionConfig.commandTimeout
    private var locatedObjects: List<UiObject2>? = null

    fun withTimeout(milliseconds: Duration) = apply { defaultTimeout = milliseconds }

    /** [UiObject] properties. **/
    fun size(): Int = uiObjects2().size

    fun atPosition(position: Int): ByObject {
        lateinit var objectAtPosition: UiObject2
        try {
            objectAtPosition = uiObjects2()[position]
        } catch (e: IndexOutOfBoundsException) {
            fail(
                "Object at position $position does not exist. " +
                        "In total \"${locatedObjects!!.size}\" objects were found."
            )
        }
        return ByObject(objectAtPosition, objectSelector!!)
    }

    private fun uiObjects2(): List<UiObject2> {
        if (shouldWaitForObject || FusionConfig.uiAutomator.shouldSearchByObjectEachAction) {
            exceptionSelectorText = "object: $objectSelector"
            locatedObjects = waitForObjects(objectSelector)
            shouldWaitForObject = false
        } else {
            return locatedObjects!!
        }

        return locatedObjects!!
    }

    private fun waitForObjects(
        bySelector: BySelector?,
        timeout: Duration = FusionConfig.commandTimeout
    ): List<UiObject2> {
        MatcherAssert.assertThat(
            "Given selector: \"$bySelector\" is empty or null",
            bySelector,
            CoreMatchers.notNullValue()
        )
        val byObjects = uiDevice.wait(Until.findObjects(bySelector), timeout.inWholeMilliseconds)
        MatcherAssert.assertThat(
            "Expected at least one object with selector: $bySelector to exist but it doesn't.",
            byObjects,
            CoreMatchers.notNullValue()
        )
        return byObjects
    }

    companion object {
        private val uiDevice: UiDevice =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }
}
