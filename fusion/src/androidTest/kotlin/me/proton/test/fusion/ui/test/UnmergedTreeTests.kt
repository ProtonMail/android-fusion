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

package me.proton.test.fusion.ui.test

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import me.proton.test.fusion.Fusion.allNodes
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.ui.compose.FusionComposeTest
import org.junit.Test

class UnmergedTreeTests: FusionComposeTest() {
    @Test
    fun unmergedTreeSingleNode() {
        withContent {
            Row {
                Button(onClick = { }) {
                    Text("Hello")
                    Text("World")
                }
            }
        }

        node
            .withText("Hello")
            .withText("World")
            .useUnmergedTree(true)
            .assertDoesNotExist()

        node
            .withText("Hello")
            .withText("World")
            .useUnmergedTree(false)
            .assertIsDisplayed()
            .assertIsDisplayed()
    }

    @Test
    fun useUnmergedTreeMultipleNodes() {
        withContent {
            Row {
                Button(onClick = { }) {
                    Text("Hello")
                    Text("World")
                }
            }
            Row {
                Button(onClick = { }) {
                    Text("Hello")
                    Text("World")
                }
            }
        }

        allNodes
            .withText("Hello")
            .withText("World")
            .useUnmergedTree(true)
            .atPosition(0)
            .assertDoesNotExist()

        allNodes
            .withText("Hello")
            .withText("World")
            .useUnmergedTree(false)
            .atPosition(0)
            .useUnmergedTree(true)
            .assertIsDisplayed()
    }
}
