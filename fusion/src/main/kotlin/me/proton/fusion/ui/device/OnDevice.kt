package me.proton.fusion.ui.device

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

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