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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.data.Robot
import me.proton.test.fusion.extension.nodeDisplayed
import me.proton.test.fusion.extension.nodeNotDisplayed
import me.proton.test.fusion.ui.compose.FusionComposeTest
import me.proton.test.fusion.ui.test.RobotTests.BlueScreenRobot.BlueScreen
import me.proton.test.fusion.ui.test.RobotTests.BlueScreenRobot.verify
import me.proton.test.fusion.ui.test.RobotTests.GreenScreenRobot.GreenScreen
import org.junit.Before
import org.junit.Test

class RobotTests : FusionComposeTest() {

    object Texts {
        const val GOTO_GREEN_SCREEN_TEXT = "Go to Green Screen"
        const val GOTO_BLUE_SCREEN_TEXT = "Back to Blue Screen"
        const val BLUE_SCREEN = "blueScreen"
        const val GREEN_SCREEN = "greenScreen"
    }

    object BlueScreenRobot : Robot {
        @Composable
        fun BlueScreen(navController: androidx.navigation.NavHostController) {
            var text by remember { mutableStateOf(Texts.BLUE_SCREEN) }

            Column(
                modifier = Modifier.background(Color.Blue)
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter Text") }
                )
                Button(onClick = { navController.navigate(Texts.GREEN_SCREEN) }) {
                    Text(Texts.GOTO_GREEN_SCREEN_TEXT)
                }
            }
        }

        private val buttonOne = node.withText(Texts.GOTO_GREEN_SCREEN_TEXT)

        fun clickButtonOnBlueScreen() = buttonOne.click() into GreenScreenRobot

        override fun robotDisplayed() {
            buttonOne.assertIsDisplayed()
        }
    }

    object GreenScreenRobot : Robot {
        @Composable
        fun GreenScreen(navController: androidx.navigation.NavHostController) {
            var text by remember { mutableStateOf(Texts.GREEN_SCREEN) }

            Column(
                modifier = Modifier.background(Color.Green)
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter Text") }
                )
                Button(onClick = { navController.popBackStack() }) {
                    Text(Texts.GOTO_BLUE_SCREEN_TEXT)
                }
            }
        }

        private val buttonTwo = node.withText(Texts.GOTO_BLUE_SCREEN_TEXT)

        fun clickButtonOnGreenScreen() = buttonTwo.click() into BlueScreenRobot

        override fun robotDisplayed() {
            buttonTwo.assertIsDisplayed()
        }
    }

    @Before
    fun setContent() {
        withContent {

            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Texts.BLUE_SCREEN) {
                composable(Texts.BLUE_SCREEN) {
                    BlueScreen(navController)
                }
                composable(Texts.GREEN_SCREEN) {
                    GreenScreen(navController)
                }
            }
        }
    }

    @Test
    fun testInfixFunctions() {
        BlueScreenRobot
            .clickButtonOnBlueScreen()
            .verify {
                robotDisplayed()
                nodeDisplayed { withText(Texts.GREEN_SCREEN) }
            }
            .clickButtonOnGreenScreen()
            .verify {
                robotDisplayed()
                nodeDisplayed { withText(Texts.BLUE_SCREEN) }
            } into GreenScreenRobot
            .verify {
                nodeNotDisplayed { withText(Texts.GREEN_SCREEN) }
            }
    }
}
