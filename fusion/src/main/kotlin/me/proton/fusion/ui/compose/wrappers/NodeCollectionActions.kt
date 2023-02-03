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

package me.proton.fusion.ui.compose.wrappers

import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import me.proton.fusion.ui.compose.ComposeWaiter.waitFor
import me.proton.fusion.ui.compose.builders.OnNode
import me.proton.fusion.ui.compose.builders.OnNodes

interface NodeCollectionActions {
    val interaction: SemanticsNodeInteractionCollection

    /** return node at [position] **/
    fun atPosition(position: Int) = OnNode(interaction[position])

    /** return first matched node **/
    fun onFirst() = OnNode(interaction.onFirst())

    /** return last matched node **/
    fun onLast() = OnNode(interaction.onLast())

    /** filter all nodes to one by [node] **/
    fun filterToOne(node: OnNode) = OnNode(interaction.filterToOne(node.finalMatcher))

    /** filter all nodes by [node] **/
    fun filter(node: OnNode) = OnNodes(interaction.filter(node.finalMatcher))

    /** Node assertions **/
    /** check all nodes match [node] matcher **/
    fun assertEach(node: OnNode) =
        waitFor {
            interaction.assertAll(node.finalMatcher)
        }

    /** check any node matches [node] matcher **/
    fun assertAny(node: OnNode) =
        waitFor {
            interaction.assertAny(node.finalMatcher)
        }

    /** check not collection [count] **/
    fun assertCountEquals(count: Int) =
        waitFor {
            interaction.assertCountEquals(count)
        }
}