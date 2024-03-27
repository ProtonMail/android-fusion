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
    private var defaultTimeout: Duration = FusionConfig.UiAutomator.waitTimeout.get()
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
        if (shouldWaitForObject || FusionConfig.UiAutomator.shouldSearchByObjectEachAction) {
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
        timeout: Duration = FusionConfig.UiAutomator.waitTimeout.get()
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
