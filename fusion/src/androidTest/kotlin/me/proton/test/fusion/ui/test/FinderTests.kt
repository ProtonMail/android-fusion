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
