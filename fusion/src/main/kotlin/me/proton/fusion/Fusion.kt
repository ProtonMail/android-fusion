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

import me.proton.fusion.ui.compose.OnAllNodes
import me.proton.fusion.ui.compose.OnNode
import me.proton.fusion.ui.device.OnDevice
import me.proton.fusion.ui.espresso.*
import me.proton.fusion.ui.uiautomator.ByObject
import me.proton.fusion.ui.uiautomator.ByObjects
import me.proton.fusion.ui.uiautomator.UiSelectorObject
import me.proton.fusion.utils.Shell

interface Fusion {

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

    val listView: OnListView
        get() = OnListView()

    val recyclerView: OnRecyclerView
        get() = OnRecyclerView()

    val rootView: OnRootView
        get() = OnRootView()

    val view: OnView
        get() = OnView()

    /**
     * Compose UI test.
     */
    val node: OnNode
        get() = OnNode()

    val allNodes: OnAllNodes
        get() = OnAllNodes()

    val fusionConfig: FusionConfig
        get() = FusionConfig

    /**
     * System.
     */
    val device: OnDevice
        get() = OnDevice()

    val shell: Shell
        get() = Shell
}