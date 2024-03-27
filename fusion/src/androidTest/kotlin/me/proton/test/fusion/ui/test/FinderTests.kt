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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import me.proton.test.fusion.Fusion.allNodes
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.ui.compose.FusionComposeTest
import org.junit.Test

class FinderTests : FusionComposeTest() {
    @Test
    fun nodeFinderTest() {
        withContent {
            Row(
                modifier = Modifier.testTag("tag")
            ) {
                Text("txt1")
                Text("txt4")
            }

            Column {
                Row(
                    modifier = Modifier.testTag("tag2")
                ) {
                    Text("txt3")
                    Text("txt4")
                    Text("txt5")
                }
            }
        }

        node.withText("txt1").onSiblings().assertCountEquals(1)
        node.withText("txt1").onSibling().assertIsDisplayed()
        node.withText("txt3").onSiblings().assertCountEquals(2)

        node.withTag("tag").onChildren().assertCountEquals(2)
        node.withTag("tag2").onChildren().assertCountEquals(3)

        node.withTag("tag").onChildAt(0).assertContainsText("txt1")

        allNodes
            .withText("txt4")
            .assertCountEquals(2)
            .filterToOne(node.hasAncestor(node.withTag("tag2")))
            .assertIsDisplayed()

        node
            .withText("txt1")
            .onAncestor(
                node.withTag("tag")
            )
            .onChildAt(1)
            .assertContainsText("txt4")
    }
}
