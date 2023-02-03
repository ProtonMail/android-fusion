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

package me.proton.fusion.ui.device

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

/**
 * The collection of functions to control the test device using the UiDevice API.
 */
class OnDevice {

    private val automation = InstrumentationRegistry.getInstrumentation().uiAutomation!!
    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun openNotification() = apply { uiDevice.openNotification() }

    fun openQuickSettings() = apply { uiDevice.openQuickSettings() }

    fun pressBack() = apply { uiDevice.pressBack() }

    fun pressDelete() = apply { uiDevice.pressDelete() }

    fun pressEnter() = apply { uiDevice.pressEnter() }

    fun pressHome() = apply { uiDevice.pressHome() }

    fun pressKeyCode(keyCode: Int, metaState: Int) =
        apply { uiDevice.pressKeyCode(keyCode, metaState) }

    fun pressKeyCode(keyCode: Int) = apply { uiDevice.pressKeyCode(keyCode) }

    fun pressMenu() = apply { uiDevice.pressMenu() }

    fun pressRecentApps() = apply { uiDevice.pressRecentApps() }

    fun pressSearch() = apply { uiDevice.pressSearch() }
}