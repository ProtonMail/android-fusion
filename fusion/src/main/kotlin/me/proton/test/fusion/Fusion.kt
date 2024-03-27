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

package me.proton.test.fusion

import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.builders.OnNodes
import me.proton.test.fusion.ui.compose.wrappers.NodeMatchers
import me.proton.test.fusion.ui.device.OnDevice
import me.proton.test.fusion.ui.espresso.builders.OnIntent
import me.proton.test.fusion.ui.espresso.builders.OnListView
import me.proton.test.fusion.ui.espresso.builders.OnRecyclerView
import me.proton.test.fusion.ui.espresso.builders.OnRootView
import me.proton.test.fusion.ui.espresso.builders.OnView
import me.proton.test.fusion.ui.espresso.wrappers.EspressoMatchers
import me.proton.test.fusion.ui.uiautomator.ByObject
import me.proton.test.fusion.ui.uiautomator.ByObjects
import me.proton.test.fusion.ui.uiautomator.UiSelectorObject

/**
 * An entry point to Fusion API.
 */
object Fusion {

    /**
     *  UiAutomator with UiSelector.
     */
    val uiObject: UiSelectorObject
        get() = UiSelectorObject()

    /**
     *  UiAutomator with BySelector.
     */
    val byObject: ByObject
        get() = ByObject()

    val byObjects: ByObjects
        get() = ByObjects()

    /**
     * Espresso.
     */
    val intent: OnIntent
        get() = OnIntent()

    val listView: EspressoMatchers<OnListView>
        get() = OnListView()

    val recyclerView: EspressoMatchers<OnRecyclerView>
        get() = OnRecyclerView()

    val view: EspressoMatchers<OnView>
        get() = OnView()

    val rootView: OnRootView
        get() = OnRootView()

    /**
     * ComposeUiTest.
     */

    val node: NodeMatchers<OnNode>
        get() = OnNode()

    val allNodes: NodeMatchers<OnNodes>
        get() = OnNodes()

    /**
     * System.
     */
    val device: OnDevice
        get() = OnDevice()
}
