/*
 * Copyright (c) 2020 Proton Technologies AG
 *
 * This file is part of ProtonMail.
 *
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail. If not, see https://www.gnu.org/licenses/.
 */

package me.proton.fusion

import me.proton.fusion.ui.compose.builders.OnNode
import me.proton.fusion.ui.compose.builders.OnNodes
import me.proton.fusion.ui.compose.wrappers.NodeMatchers
import me.proton.fusion.ui.device.OnDevice
import me.proton.fusion.ui.espresso.builders.OnIntent
import me.proton.fusion.ui.espresso.builders.OnListView
import me.proton.fusion.ui.espresso.builders.OnRecyclerView
import me.proton.fusion.ui.espresso.builders.OnRootView
import me.proton.fusion.ui.espresso.builders.OnView
import me.proton.fusion.ui.espresso.wrappers.EspressoMatchers
import me.proton.fusion.ui.uiautomator.ByObject
import me.proton.fusion.ui.uiautomator.ByObjects
import me.proton.fusion.ui.uiautomator.UiSelectorObject

/**
 * An entry point to Fusion API.
 */
object Fusion {

    val fusionConfig: FusionConfig
        get() = FusionConfig

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
