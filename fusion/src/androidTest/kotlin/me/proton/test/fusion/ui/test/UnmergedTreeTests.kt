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