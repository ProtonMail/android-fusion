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

package me.proton.test.fusion.data

import me.proton.test.fusion.FusionConfig
import me.proton.test.fusion.ui.FusionWaiter
import kotlin.time.Duration

interface Robot: FusionWaiter {

    fun robotDisplayed()

    infix fun <T : Robot> into(robot: T): T = robot

    infix fun <T> into(robot: T): T = robot

    infix fun <T : Robot> FusionActions.into(robot: T) = robot

    fun <T : Robot> T.verify(
        waitTimeout: Duration? = null,
        watchInterval: Duration? = null,
        block: T.() -> Unit,
    ): T = waitFor(
        config = FusionConfig.Robot,
        timeout = waitTimeout ?: FusionConfig.Robot.waitTimeout.get(),
        interval = watchInterval ?: FusionConfig.Robot.watchInterval.get(),
        conditionBlock = { block() }
    )
}
