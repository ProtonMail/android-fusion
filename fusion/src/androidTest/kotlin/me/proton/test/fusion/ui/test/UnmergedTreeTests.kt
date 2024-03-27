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
