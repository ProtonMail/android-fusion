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

package me.proton.test.fusion.extension

import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.Fusion.view
import me.proton.test.fusion.data.Robot
import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.wrappers.NodeAssertions
import me.proton.test.fusion.ui.compose.wrappers.NodeMatchers
import me.proton.test.fusion.ui.espresso.builders.OnView
import me.proton.test.fusion.ui.espresso.wrappers.EspressoAssertions
import me.proton.test.fusion.ui.espresso.wrappers.EspressoMatchers
import java.lang.AssertionError

fun Robot.nodeDisplayed(matcher: NodeMatchers<OnNode>.() -> NodeAssertions): NodeAssertions =
    node.matcher().assertIsDisplayed()

fun Robot.viewDisplayed(matcher: EspressoMatchers<OnView>.() -> EspressoAssertions): EspressoAssertions =
    view.matcher().checkIsDisplayed()

fun Robot.nodeNotDisplayed(
    mustExist: Boolean = false,
    matcher: NodeMatchers<OnNode>.() -> NodeAssertions
): NodeAssertions =
    try {
        node.matcher().assertIsNotDisplayed()
    } catch (assertionError: AssertionError) {
        if (mustExist) throw assertionError
        node.matcher().assertDoesNotExist()
    }

fun Robot.viewNotDisplayed(
    mustExist: Boolean = false,
    matcher: EspressoMatchers<OnView>.() -> EspressoAssertions
): EspressoAssertions =
    try {
        view.matcher().checkIsDisplayed()
    } catch (assertionError: AssertionError) {
        if (mustExist) throw assertionError
        view.matcher().checkDoesNotExist()
    }
