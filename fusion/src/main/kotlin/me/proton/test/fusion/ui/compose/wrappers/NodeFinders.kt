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

package me.proton.test.fusion.ui.compose.wrappers

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onAncestors
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.onSiblings
import me.proton.test.fusion.ui.compose.builders.OnNode
import me.proton.test.fusion.ui.compose.builders.OnNodes

interface NodeFinders : NodeAssertions {
    /** Single node filters **/
    /** returns node at child [position] **/
    fun onChildAt(position: Int): NodeActions = OnNode(interaction.onChildAt(position))

    /** returns a child node (only if node has one child) **/
    fun onChild(): NodeActions = OnNode(interaction.onChild())

    /** returns a parent node (only if node has one parent) **/
    fun onParent(): NodeActions = OnNode(interaction.onParent())

    /** returns a sibling node (only if node has one sibling) **/
    fun onSibling(): NodeActions = OnNode(interaction.onSibling())

    /** returns a node from children with matcher from given [node] **/
    fun onChild(node: OnNode): NodeActions = OnNode(interaction.onChildren().filterToOne(node.matcher))

    /** returns a node from children with matcher from given [ancestor] **/
    fun onAncestor(ancestor: OnNode): NodeActions = OnNode(interaction.onAncestors().filterToOne(ancestor.matcher))

    /** returns a node from siblings with matcher from given [sibling] **/
    fun onSibling(sibling: OnNode): NodeActions = OnNode(interaction.onSiblings().filterToOne(sibling.matcher))

    /** Node collection filters **/
    /** returns children node collection **/
    fun onChildren(): NodeCollectionActions = OnNodes(interaction.onChildren())

    /** returns sibling node collection **/
    fun onSiblings(): NodeCollectionActions = OnNodes(interaction.onSiblings())

    /** returns ancestor node collection **/
    fun onAncestors(): NodeCollectionActions = OnNodes(interaction.onAncestors())
}
